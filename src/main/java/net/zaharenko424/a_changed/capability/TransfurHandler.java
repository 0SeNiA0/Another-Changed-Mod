package net.zaharenko424.a_changed.capability;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.IAttachmentCopyHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.ability.Ability;
import net.zaharenko424.a_changed.ability.AbilityHolder;
import net.zaharenko424.a_changed.event.custom.AddTransfurProgressEvent;
import net.zaharenko424.a_changed.event.custom.TransfurredEvent;
import net.zaharenko424.a_changed.event.custom.UnTransfurredEvent;
import net.zaharenko424.a_changed.network.packets.ability.ServerboundSelectAbilityPacket;
import net.zaharenko424.a_changed.network.packets.transfur.ClientboundOpenTransfurScreenPacket;
import net.zaharenko424.a_changed.network.packets.transfur.ClientboundTransfurSyncPacket;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.registry.ArmorMaterialRegistry;
import net.zaharenko424.a_changed.registry.AttachmentRegistry;
import net.zaharenko424.a_changed.transfurSystem.*;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import net.zaharenko424.a_changed.util.AbilityUtils;
import net.zaharenko424.a_changed.util.TransfurUtils;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static net.zaharenko424.a_changed.AChanged.*;
import static net.zaharenko424.a_changed.transfurSystem.TransfurManager.*;

public class TransfurHandler implements AbilityHolder {

    public static final Supplier<RuntimeException> NO_CAPABILITY_EXC = ()-> new RuntimeException("Transfur capability was expected but not found!");

    public static final Serializer SERIALIZER = new Serializer();
    public static final IAttachmentCopyHandler<TransfurHandler> COPY_HANDLER = (attachment, holder, lookup) -> {
        if(((LivingEntity)holder).level().getGameRules().getBoolean(AChanged.KEEP_TRANSFUR) && attachment.isTransfurred()) {
            CompoundTag tag = SERIALIZER.write(attachment, lookup);
            return tag != null ? SERIALIZER.read(holder, tag, lookup) : null;
        }
        return null;
    };

    public static @Nullable TransfurHandler of(@NotNull LivingEntity entity){
        if(!entity.getType().is(TRANSFURRABLE_TAG)) return null;
        return entity.getData(AttachmentRegistry.TRANSFUR_HANDLER);
    }

    public static @NotNull TransfurHandler nonNullOf(@NotNull LivingEntity entity){
        return Utils.nonNullOrThrow(of(entity), NO_CAPABILITY_EXC.get());
    }


    static final int ticksUntilTFProgressDecrease = 200;
    static final int ticksBetweenTFProgressDecrease = 20;

    private final LivingEntity holder;

    //Synced data
    private Ability selectedAbility;
    private float transfurProgress = 0;
    private TransfurType transfurType = null;
    private boolean isTransfurred = false;

    //Client only data
    ResourceLocation lastTFModelId = null;

    //Server only data
    boolean isBeingTransfurred = false;
    static final int ticksUntilDeathTF = 400;
    int beingTransfurredTimer;
    int i0 = 0;

    @ApiStatus.Internal
    public TransfurHandler(IAttachmentHolder holder){
        if(!(holder instanceof LivingEntity living) || !living.getType().is(AChanged.TRANSFURRABLE_TAG))
            throw new IllegalStateException("Tried to create TransfurHandler for unsupported holder: " + holder);
        this.holder = living;
        if(isTransfurred()) return;
        if(living instanceof Player && !living.level().isClientSide) selectedAbility = AbilityRegistry.GRAB_ABILITY.get();//make sure that players have access to (don't)wantToBeGrabbed screen
    }

    @Override
    public Ability getSelectedAbility(){
        return selectedAbility;
    }

    @Override
    public @NotNull List<? extends Ability> getAllowedAbilities() {
        return isTransfurred() ? transfurType.abilities : selectedAbility != null ? List.of(selectedAbility) : List.of();
    }

    @Override
    public void selectAbility(@NotNull Ability ability) {
        if(!isTransfurred() || !transfurType.abilities.contains(ability) || ability == selectedAbility) return;

        if(holder.level().isClientSide){
            PacketDistributor.sendToServer(new ServerboundSelectAbilityPacket(AbilityUtils.abilityIdOf(ability)));
            return;
        }

        if(selectedAbility != null) selectedAbility.unselect(holder);
        selectedAbility = ability;
        selectedAbility.select(holder);
        syncClients();
    }

    /**Client only*/
    public ResourceLocation getLastTFModelId(){
        return lastTFModelId;
    }

    /**Client only*/
    public void setLastTFModelId(ResourceLocation lastTFModelId){
        this.lastTFModelId = lastTFModelId;
    }

    public float getTransfurProgress() {
        return transfurProgress;
    }

    public void addTransfurProgress(float amount, @NotNull TransfurType transfurType, @NotNull TransfurContext context) {
        if(holder.level().isClientSide) return;
        if(isBeingTransfurred() || isTransfurred()) return;

        AddTransfurProgressEvent event = new AddTransfurProgressEvent(holder, transfurType, amount, context);
        NeoForge.EVENT_BUS.post(event);
        if(event.isCanceled()) return;
        amount = event.getProgressToAdd();
        context = event.getContext();

        if(context.checkResistance()){
            amount *= 1 - calculateResistance();
        }
        float progress = getTransfurProgress() + amount;

        if(progress >= TRANSFUR_TOLERANCE) {
            transfur(transfurType, context);
            return;
        }

        i0 = ticksUntilTFProgressDecrease;
        transfurProgress = progress;
        this.transfurType = transfurType;

        syncClients();
    }

    protected float calculateResistance(){
        Iterable<ItemStack> iterable = holder.getArmorSlots();
        int armorSlots = 0, armorPieces = 0, armorPoints = 0;

        Holder<ArmorMaterial> material;
        for (ItemStack stack : iterable) {
            armorSlots++;

            if(!(stack.getItem() instanceof ArmorItem armor)) continue;

            material = armor.getMaterial();
            if(material == ArmorMaterials.LEATHER || material == ArmorMaterials.CHAIN || material == ArmorMaterialRegistry.LATEX.getDelegate()) continue;

            armorPieces++;
            armorPoints += armor.getDefense();
        }

        float covered = armorSlots > 0 && armorPieces > 0 ? (armorSlots == armorPieces ? 1 : (float)armorPieces / (float)armorSlots) : 0;
        if(covered == 0) return 0;

        float armorRes = Math.min(armorPoints, 20) / 20f * .2f;//Balanced for vanilla (diamond/netherite full set 20 armor)
        float attributeRes = (float) (holder.getAttributeValue(LATEX_RESISTANCE) * .6f);

        return (armorRes + attributeRes) * covered;//Max .8
    }

    public @Nullable TransfurType getTransfurType() {
        return transfurType;
    }

    public void setTransfurType(@NotNull TransfurType transfurType) {
            this.transfurType = transfurType;
    }

    public boolean isTransfurred() {
            return isTransfurred && transfurType != null;
    }

    public void transfur(@NotNull TransfurType transfurType, @NotNull TransfurContext context) {
        Level level = holder.level();
        if(level.isClientSide) return;

        SoundEvent onTransfurSound = context.onTransfurSound();

        if(!(holder instanceof ServerPlayer player)){
            LatexBeast latexBeast = TransfurUtils.spawnLatex(transfurType, (ServerLevel) level, holder.blockPosition());
            latexBeast.copyEquipment(holder);
            if(onTransfurSound != null) holder.playSound(onTransfurSound);
            holder.discard();

            NeoForge.EVENT_BUS.post(new TransfurredEvent(holder, latexBeast, transfurType, context));
            return;
        }

        TransfurResult result = context.result();

        if(isTransfurred() && !player.isCreative() && result != TransfurResult.TRANSFUR) return;
        if(onTransfurSound != null) level.playSound(null, player, onTransfurSound, SoundSource.PLAYERS,1,1);

        if(player.isCreative() || player.isSpectator() || result == TransfurResult.TRANSFUR){
            actuallyTransfur(transfurType, context);
            return;
        }
        switch (result != null ? result
                : level.getGameRules().getBoolean(TRANSFUR_IS_DEATH) ? TransfurResult.DEATH
                : level.getGameRules().getBoolean(CHOOSE_TF_OR_DIE) ? TransfurResult.PROMPT
                : TransfurResult.TRANSFUR){
            case DEATH -> {
                LatexBeast latexBeast = TransfurUtils.spawnLatex(transfurType, (ServerLevel) level, player.blockPosition());
                latexBeast.copyEquipment(holder);
                player.setInvulnerable(false);
                player.hurt(DamageSources.transfur(null, Objects.requireNonNullElse(player.getLastHurtByMob(), player)), Float.MAX_VALUE);

                NeoForge.EVENT_BUS.post(new TransfurredEvent(player, latexBeast, transfurType, context));
            }
            case PROMPT -> {
                setBeingTransfurred(true);
                this.transfurType = transfurType;
                PacketDistributor.sendToPlayer(player, new ClientboundOpenTransfurScreenPacket());
            }
            case TRANSFUR -> actuallyTransfur(transfurType, context);
        }
    }

    private void actuallyTransfur(TransfurType transfurType, TransfurContext context){
        setBeingTransfurred(false);

        if(isTransfurred()){
            this.transfurType.onUnTransfur(holder);
            TransfurUtils.removeModifiers(holder, this.transfurType);
            this.transfurType.abilities.forEach(ability -> ability.remove(holder));
        }

        loadSyncedData(transfurType.abilities.isEmpty() ? null : transfurType.abilities.get(0),
                TRANSFUR_TOLERANCE, true, transfurType);

        transfurType.abilities.forEach(ability -> ability.add(holder));
        TransfurUtils.addModifiers(holder, transfurType);
        transfurType.onTransfur(holder);

        syncClients();

        NeoForge.EVENT_BUS.post(new TransfurredEvent(holder, null, transfurType, context));
    }

    public void unTransfur(@NotNull TransfurContext context) {
        if(holder.level().isClientSide) return;

        setBeingTransfurred(false);

        TransfurType transfurTypeO = transfurType;
        if(isTransfurred()) {
            transfurType.onUnTransfur(holder);
            TransfurUtils.removeModifiers(holder, transfurType);
            this.transfurType.abilities.forEach(ability -> ability.remove(holder));
        }

        loadSyncedData(AbilityRegistry.GRAB_ABILITY.get(),0, false, null);//assign grab ability to be able to switch (don't)wantToBeGrabbed
        syncClients();

        if(context.onUntransfurSound() != null)
            holder.level().playSound(null, holder.blockPosition(), context.onUntransfurSound(), SoundSource.PLAYERS);

        if(transfurTypeO != null) NeoForge.EVENT_BUS.post(new UnTransfurredEvent((Player) holder, transfurTypeO, context));
    }

    public boolean isBeingTransfurred() {
        return isBeingTransfurred;
    }

    public void setBeingTransfurred(boolean isBeingTransfurred) {
        if(this.isBeingTransfurred != isBeingTransfurred){
            beingTransfurredTimer = isBeingTransfurred ? ticksUntilDeathTF : 0;
        }
        this.isBeingTransfurred = isBeingTransfurred;
    }

    public void tick() {
        if(selectedAbility != null) {
            selectedAbility.serverTick(holder);

            getAllowedAbilities().forEach(abilityUnselected -> {
                if (abilityUnselected == selectedAbility) return;
                abilityUnselected.serverTickUnselected(holder);
            });
        }

        if(isTransfurred() || transfurProgress <= 0) return;

        if(isBeingTransfurred){
            if(beingTransfurredTimer > 0){
                beingTransfurredTimer--;
            } else transfur(transfurType, TransfurContext.TRANSFUR_DEATH);
            return;
        }

        if(i0 > 0) {
            i0--;
            return;
        }
        if(holder.tickCount % ticksBetweenTFProgressDecrease != 0) return;
        transfurProgress = Math.max(0, transfurProgress - 1);

        syncClients();
    }

    public void syncClient(ServerPlayer packetReceiver) {
        PacketDistributor.sendToPlayer(packetReceiver, packet());
    }

    public void syncClients(){
        holder.refreshDimensions();
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(holder, packet());
    }

    ClientboundTransfurSyncPacket packet(){
        return new ClientboundTransfurSyncPacket(holder.getId(),
                selectedAbility != null ? AbilityRegistry.ABILITY_REGISTRY.getKey(selectedAbility) : Utils.NULL_LOC, transfurProgress,
                isTransfurred, transfurType);
    }

    @ApiStatus.Internal
    public void loadSyncedData(@Nullable Ability ability, float transfurProgress, boolean isTransfurred, TransfurType transfurType){
        this.selectedAbility = ability;
        this.transfurProgress = transfurProgress;
        this.isTransfurred = isTransfurred;
        this.transfurType = transfurType;
    }

    public static class Serializer implements IAttachmentSerializer<CompoundTag, TransfurHandler> {

        private Serializer(){}

        @Override
        public @NotNull TransfurHandler read(@NotNull IAttachmentHolder holder, @NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookup) {
            TransfurHandler handler = new TransfurHandler(holder);

            handler.loadSyncedData(tag.contains("ability") ? AbilityRegistry.ABILITY_REGISTRY.get(ResourceLocation.parse(tag.getString("ability"))) : null,
                    tag.getFloat(TRANSFUR_PROGRESS_KEY), tag.getBoolean(TRANSFURRED_KEY),
                    TransfurManager.getTransfurType(ResourceLocation.parse(tag.getString(TRANSFUR_TYPE_KEY))));

            if(holder instanceof Player) handler.setBeingTransfurred(tag.getBoolean(BEING_TRANSFURRED_KEY));

            if(handler.isTransfurred()) TransfurUtils.addModifiers((LivingEntity) holder, handler.transfurType);
            return handler;
        }

        @Override
        public @Nullable CompoundTag write(@NotNull TransfurHandler attachment, HolderLookup.@NotNull Provider lookup) {
            CompoundTag tag = new CompoundTag();
            if(attachment.selectedAbility != null) tag.putString("ability", AbilityRegistry.ABILITY_REGISTRY.getKey(attachment.selectedAbility).toString());

            tag.putFloat(TRANSFUR_PROGRESS_KEY, attachment.transfurProgress);

            TransfurType transfurType = attachment.transfurType;
            if(transfurType != null) {
                tag.putBoolean(TRANSFURRED_KEY, attachment.isTransfurred);
                tag.putString(TRANSFUR_TYPE_KEY, transfurType.id.toString());
            }

            if(attachment.holder instanceof Player) tag.putBoolean(BEING_TRANSFURRED_KEY, attachment.isBeingTransfurred);
            return tag;
        }
    }
}