package net.zaharenko424.a_changed.ability;

import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.IAttachmentCopyHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.ability.api.*;
import net.zaharenko424.a_changed.ability.event.CopyAbilitiesOnDeathEvent;
import net.zaharenko424.a_changed.ability.network.packets.ServerboundSelectAbilityPacket;
import net.zaharenko424.a_changed.client.Keybindings;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.registry.AttachmentRegistry;
import net.zaharenko424.a_changed.util.AbilityUtils;
import net.zaharenko424.cmrs.util.StreamCodecUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
public class AbilityHolderAttachment implements AbilityHolder {

    public static final Serializer SERIALIZER = new Serializer();
    public static final Sync SYNC = new Sync();
    public static final IAttachmentCopyHandler<AbilityHolderAttachment> COPY_HANDLER = ((attachment, holder, provider) -> {
        if(!(holder instanceof Player player)) return null;

        if(!attachment.holder.isDeadOrDying()) return new AbilityHolderAttachment(holder, attachment.abilities, attachment.selected);

        Set<ResourceLocation> providersToCopy = new HashSet<>();
        Map<Ability, Set<ResourceLocation>> toCopy = new HashMap<>();
        Player dead = attachment.holder;
        NeoForge.EVENT_BUS.post(new CopyAbilitiesOnDeathEvent(player, dead, providersToCopy, toCopy));

        LinkedHashMap<Ability, Set<ResourceLocation>> abilities = attachment.abilities;
        Iterator<Map.Entry<Ability, Set<ResourceLocation>>> mapIt = abilities.entrySet().iterator();
        Map.Entry<Ability, Set<ResourceLocation>> entry;
        Set<ResourceLocation> keys, keysToCopy;
        Iterator<ResourceLocation> keyIt;

        ResourceLocation key;
        while (mapIt.hasNext()){
            entry = mapIt.next();
            keys = entry.getValue();

            keysToCopy = toCopy.get(entry.getKey());
            if(keysToCopy == null) keysToCopy = Set.of();

            keyIt = keys.iterator();
            while(keyIt.hasNext()){
                key = keyIt.next();
                if(providersToCopy.contains(key) || keysToCopy.contains(key)) continue;
                keyIt.remove();
            }

            if(keys.isEmpty()) {
                entry.getKey().remove(dead);
                mapIt.remove();
            }
        }

        AbilityHolderAttachment ret = new AbilityHolderAttachment(holder, abilities, abilities.containsKey(attachment.selected) ? attachment.selected : null);
        ret.maybeReselectAbility();
        return ret;
    });

    public static AbilityHolderAttachment of(Player player){
        return player.getData(AttachmentRegistry.ABILITY_HOLDER);
    }

    private final Player holder;
    private Ability loadedSelected;

    private final LinkedHashMap<Ability, Set<ResourceLocation>> abilities;
    private SequencedSet<Ability> abilityView;
    private Ability selected;
    private final InputController controller;

    @ApiStatus.Internal
    public AbilityHolderAttachment(IAttachmentHolder holder){
        this(holder, new LinkedHashMap<>(), null);
    }

    AbilityHolderAttachment(IAttachmentHolder holder, LinkedHashMap<Ability, Set<ResourceLocation>> abilities, @Nullable Ability selected){
        if(!(holder instanceof Player player))
            throw new IllegalStateException("Tried to create AbilityHolder for unsupported holder: " + holder);
        this.holder = player;
        this.abilities = abilities;
        this.selected = selected;
        this.controller = new InputController(this.holder,
                (player.level().isClientSide ? Keybindings.ABILITY_KEY::isDown : () -> false),
                (controller) -> {
                        if(this.selected != null) this.selected.activationType().onInput(this, controller, this.selected);
                }
        );
    }

    @ApiStatus.Internal
    public void trySelectLoaded(){
        if(loadedSelected == null || !abilities.containsKey(loadedSelected)) return;
        selectAbility(loadedSelected);
        loadedSelected = null;
    }

    @Override
    public Player asEntity() {
        return holder;
    }

    @Override
    public InputController getInputController() {
        return controller;
    }

    @Override
    public Ability getSelectedAbility() {
        return selected;
    }

    @Override
    public @NotNull SequencedSet<? extends Ability> getAbilities() {
        if(abilityView == null) abilityView = Collections.unmodifiableSequencedSet(abilities.sequencedKeySet());
        return abilityView;
    }

    @Override
    public boolean hasAbility(Ability ability, ResourceLocation key) {
        Set<ResourceLocation> keys = abilities.get(ability);
        return keys != null && keys.contains(key);
    }

//---------------------------------- Add
    @Override
    public boolean addAbility(Ability ability, ResourceLocation key){
        if(holder.level().isClientSide) return false;

        boolean addedNew = addAbilityNoSync(ability, key);

        if(maybeReselectAbility() || addedNew) holder.syncData(AttachmentRegistry.ABILITY_HOLDER);
        return addedNew;
    }

    @Override
    public boolean addAbilities(SequencedSet<Ability> abilities, ResourceLocation key) {
        if(holder.level().isClientSide) return false;

        boolean addedNew = addAbilitiesNoSync(abilities, key);

        if(maybeReselectAbility() || addedNew) holder.syncData(AttachmentRegistry.ABILITY_HOLDER);
        return addedNew;
    }

    protected boolean addAbilityNoSync(Ability ability, ResourceLocation key){
        boolean addedNew = !abilities.containsKey(ability);
        abilities.computeIfAbsent(ability, ab -> {
            ab.add(holder);
            return new ObjectArraySet<>();
        }).add(key);
        return addedNew;
    }

    protected boolean addAbilitiesNoSync(SequencedSet<Ability> abilities, ResourceLocation key){
        boolean addedNew = !this.abilities.sequencedKeySet().containsAll(abilities);
        for(Ability ability : abilities){
            this.abilities.computeIfAbsent(ability, ab -> {
                ab.add(holder);
                return new ObjectArraySet<>();
            }).add(key);
        }

        return addedNew;
    }

//---------------------------------- Replace

    @Override
    public void replaceAbilities(Ability add, ResourceLocation key) {
        if(holder.level().isClientSide) return;

        boolean[] alreadyAdded = {false};
        boolean changed = abilities.entrySet().removeIf(entry -> {
            if(entry.getKey() == add) {
                entry.getValue().add(key);
                alreadyAdded[0] = true;
                return false;
            }

            entry.getValue().remove(key);
            if(!entry.getValue().isEmpty()) return false;

            entry.getKey().remove(holder);
            if(selected == entry.getKey()) selected = null;
            return true;
        });
        if(!alreadyAdded[0]) changed |= addAbilityNoSync(add, key);

        if(maybeReselectAbility() || changed) holder.syncData(AttachmentRegistry.ABILITY_HOLDER);
    }

    @Override
    public void replaceAbilities(SequencedSet<Ability> add, ResourceLocation key){
        if(holder.level().isClientSide) return;

        boolean changed = abilities.entrySet().removeIf(entry -> {
            entry.getValue().remove(key);
            if(!entry.getValue().isEmpty()) return false;

            if(!add.contains(entry.getKey())){
                entry.getKey().remove(holder);
                if (selected == entry.getKey()) selected = null;
            }
            return true;
        });
        changed |= addAbilitiesNoSync(add, key);


        if(maybeReselectAbility() || changed) holder.syncData(AttachmentRegistry.ABILITY_HOLDER);
    }

    @Override
    public void replaceAbilities(SequencedSet<Ability> remove, SequencedSet<Ability> add, ResourceLocation key) {
        if(holder.level().isClientSide) return;

        boolean changed = removeAbilitiesNoSync(remove, key);
        changed |= addAbilitiesNoSync(add, key);
        if(maybeReselectAbility() || changed) holder.syncData(AttachmentRegistry.ABILITY_HOLDER);
    }

//---------------------------------- Remove
    @Override
    public boolean removeAbility(Ability ability, ResourceLocation key){
        if(holder.level().isClientSide) return false;

        Set<ResourceLocation> keys = abilities.get(ability);
        if(keys == null) return false;

        keys.remove(key);
        if(!keys.isEmpty()) return false;

        abilities.remove(ability);
        ability.remove(holder);
        if(selected == ability) selected = null;
        maybeReselectAbility();

        holder.syncData(AttachmentRegistry.ABILITY_HOLDER);
        return true;
    }

    @Override
    public boolean removeAbilities(SequencedSet<Ability> abilities, ResourceLocation key){
        if(holder.level().isClientSide) return false;

        boolean anyRemoved = removeAbilitiesNoSync(abilities, key);

        if(maybeReselectAbility() || anyRemoved) holder.syncData(AttachmentRegistry.ABILITY_HOLDER);
        return anyRemoved;
    }

    protected boolean removeAbilitiesNoSync(SequencedSet<Ability> abilities, ResourceLocation key){
        boolean anyRemoved = false;
        Set<ResourceLocation> keys;
        for(Ability ability : abilities){
            keys = this.abilities.get(ability);
            if(keys == null) continue;

            keys.remove(key);
            if(!keys.isEmpty()) continue;

            this.abilities.remove(ability);
            ability.remove(holder);
            if(selected == ability) selected = null;
            anyRemoved = true;
        }
        return anyRemoved;
    }

    @Override
    public boolean removeAbilities(ResourceLocation key){
        if(holder.level().isClientSide) return false;

        boolean anyRemoved = abilities.entrySet().removeIf(entry -> {
            entry.getValue().remove(key);
            if(!entry.getValue().isEmpty()) return false;

            entry.getKey().remove(holder);
            if(selected == entry.getKey()) selected = null;
            return true;
        });

        if(maybeReselectAbility() || anyRemoved) holder.syncData(AttachmentRegistry.ABILITY_HOLDER);
        return anyRemoved;
    }

    protected boolean maybeReselectAbility(){
        if(selected != null) return false;

        for(Ability ability : abilities.sequencedKeySet()){
            if(ability.isPassive()) continue;
            selected = ability;
            ability.select(holder);
            return true;
        }

        return false;
    }

    @Override
    public void selectAbility(@Nullable Ability ability){
        if(selected == ability || (ability != null && (!hasAbility(ability) || ability.isPassive()))) return;

        if(holder.level().isClientSide){
            PacketDistributor.sendToServer(new ServerboundSelectAbilityPacket(ability));
            return;
        }

        if(selected != null){
            selected.unselect(holder);
        }

        selected = ability;
        if(selected != null) selected.select(holder);

        holder.syncData(AttachmentRegistry.ABILITY_HOLDER);
    }

    @Override
    public void inputTick() {
        Minecraft minecraft = Minecraft.getInstance();
        controller.inputTick();

        if(selected instanceof CustomInputAbility customInput) customInput.inputTick(holder, minecraft);

        getAbilities().forEach(abilityUnselected -> {
            if(abilityUnselected == selected) return;
            if(abilityUnselected instanceof CustomInputAbility customInput) customInput.inputTickUnselected(holder, minecraft);
        });
    }

    @Override
    public void serverTick(){
        if(holder.level().isClientSide) return;

        controller.tick();

        if(selected != null) selected.serverTick(holder);

        getAbilities().forEach(abilityUnselected -> {
            if (abilityUnselected == selected) return;
            abilityUnselected.serverTickUnselected(holder);
        });
    }

    public void syncAbilities(){
        if(holder.level().isClientSide) return;

        AbilityData data;
        for(Ability ability : abilities.keySet()){
            data = ability.getAbilityData(holder);
            if(data != null) data.sync();
        }
    }

    @ParametersAreNonnullByDefault
    public static class Serializer implements IAttachmentSerializer<ByteArrayTag, AbilityHolderAttachment> {

        private static final StreamCodec<FriendlyByteBuf, Ability> ABILITY = StreamCodecUtils.RESOURCE_LOC.map(AbilityUtils::abilityOf, AbilityUtils::abilityIdOf);

        private Serializer(){}

        @Override
        public @NotNull AbilityHolderAttachment read(IAttachmentHolder holder, ByteArrayTag tag, HolderLookup.Provider provider) {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(tag.getAsByteArray()));

            AbilityHolderAttachment attachment = new AbilityHolderAttachment(holder);
            attachment.loadedSelected = StreamCodecUtils.readOptionally(buf, ABILITY);
            return attachment;
        }

        @Override
        public @Nullable ByteArrayTag write(AbilityHolderAttachment attachment, HolderLookup.Provider provider) {
            return new ByteArrayTag(StreamCodecUtils.writeCustomData(buf ->
                StreamCodecUtils.writeOptionally(attachment.selected, buf, ABILITY)
            ));
        }
    }

    @ParametersAreNonnullByDefault
    public static class Sync implements AttachmentSelfSyncHandler<AbilityHolderAttachment> {

        private static final StreamCodec<FriendlyByteBuf, Ability> ABILITY_SYNC = ByteBufCodecs.VAR_INT.map(AbilityRegistry.ABILITY_REGISTRY::byId, AbilityRegistry.ABILITY_REGISTRY::getId).cast();
        private static final StreamCodec<FriendlyByteBuf, SequencedSet<Ability>> MAP_SYNC = ByteBufCodecs.collection(LinkedHashSet::new, ABILITY_SYNC);

        private Sync(){}

        @Override
        public void writeToSelf(RegistryFriendlyByteBuf buf, AbilityHolderAttachment attachment, boolean initialSync) {
            write(buf, attachment, initialSync);
            StreamCodecUtils.writeOptionally(attachment.abilities.sequencedKeySet(), !attachment.abilities.isEmpty(), buf, MAP_SYNC);

            buf.writeBoolean(initialSync);
            if(initialSync) attachment.getInputController().send(buf);
        }

        @Override
        public void write(RegistryFriendlyByteBuf buf, AbilityHolderAttachment attachment, boolean initialSync) {
            StreamCodecUtils.writeOptionally(attachment.selected, buf, ABILITY_SYNC);
        }

        @Override
        public @Nullable AbilityHolderAttachment read(IAttachmentHolder holder, RegistryFriendlyByteBuf buf, @Nullable AbilityHolderAttachment previousValue) {
            if(!(holder instanceof Player)) return null;

            if(previousValue == null) previousValue = new AbilityHolderAttachment(holder);

            previousValue.selected = StreamCodecUtils.readOptionally(buf, ABILITY_SYNC);

            if(holder == Minecraft.getInstance().player){
                SequencedSet<Ability> abilities = StreamCodecUtils.readOptionally(buf, MAP_SYNC, LinkedHashSet::new);
                previousValue.abilities.clear();
                for (Ability ability : abilities) previousValue.abilities.put(ability, Set.of());

                if(buf.readBoolean()) previousValue.getInputController().handleData(buf);
            }

            return previousValue;
        }
    }
}
