package net.zaharenko424.a_changed.attachment;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.IAttachmentCopyHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.AChangedTags;
import net.zaharenko424.a_changed.ability.api.AbilityHolder;
import net.zaharenko424.a_changed.event.custom.AddTransfurProgressEvent;
import net.zaharenko424.a_changed.event.custom.TransfurredEvent;
import net.zaharenko424.a_changed.event.custom.UnTransfurredEvent;
import net.zaharenko424.a_changed.network.packets.transfur.ClientboundOpenTransfurScreenPacket;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.registry.ArmorMaterialRegistry;
import net.zaharenko424.a_changed.registry.AttachmentRegistry;
import net.zaharenko424.a_changed.transfurSystem.*;
import net.zaharenko424.a_changed.transfurSystem.transfurType.TransfurType;
import net.zaharenko424.a_changed.util.AbilityUtils;
import net.zaharenko424.a_changed.util.TransfurUtils;
import net.zaharenko424.a_changed.util.TransfurUtilsClient;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.zaharenko424.a_changed.AChanged.*;
import static net.zaharenko424.a_changed.transfurSystem.TransfurManager.TRANSFUR_TOLERANCE;

public class TransfurHandler {

    public static final Serializer SERIALIZER = new Serializer();
    public static final Sync SYNC = new Sync();
    public static final IAttachmentCopyHandler<TransfurHandler> COPY_HANDLER = (attachment, holder, lookup) -> {
        if(((LivingEntity)holder).level().getGameRules().getBoolean(AChanged.KEEP_TRANSFUR) && attachment.isTransfurred()) {
            CompoundTag tag = SERIALIZER.write(attachment, lookup);
            return tag != null ? SERIALIZER.read(holder, tag, lookup) : null;
        }
        return null;
    };

    public static @Nullable TransfurHandler of(@NotNull LivingEntity entity){
        if(!entity.getType().is(AChangedTags.Entity.TRANSFURRABLE_TAG)) return null;
        return entity.getData(AttachmentRegistry.TRANSFUR_HANDLER);
    }

    public static @NotNull TransfurHandler nonNullOf(@NotNull LivingEntity entity){
        TransfurHandler handler = of(entity);
        if(handler == null) throw new RuntimeException("Transfur attachment was expected but not found!");
        return handler;
    }

    @ApiStatus.Internal
    public static final ResourceLocation ABILITY_PROVIDER = AChanged.resourceLoc("transfur");

    static final int ticksUntilTFProgressDecrease = 200;
    static final int ticksBetweenTFProgressDecrease = 20;

    private final LivingEntity holder;

    //Synced data
    private float transfurProgress = 0;
    private TransfurType<?> transfurType = null;
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
        if(!(holder instanceof LivingEntity living) || !living.getType().is(AChangedTags.Entity.TRANSFURRABLE_TAG))
            throw new IllegalStateException("Tried to create TransfurHandler for unsupported holder: " + holder);
        this.holder = living;
    }

    @ApiStatus.Internal
    public void addDefAbilities(){
        if(isTransfurred()) return;

        AbilityUtils.of(holder).addAbility(AbilityRegistry.GRAB_ABILITY, ABILITY_PROVIDER);
    }

    @ApiStatus.Internal
    public void addTFAbilitiesOrDef(){
        AbilityHolder holder = AbilityUtils.of(this.holder);
        if(isTransfurred()){
            holder.replaceAbilities(transfurType.abilities, ABILITY_PROVIDER);
        } else holder.replaceAbilities(AbilityRegistry.GRAB_ABILITY, ABILITY_PROVIDER);
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

    public void addTransfurProgress(float amount, @NotNull TransfurType<?> transfurType, @NotNull TransfurContext context) {
        if(holder.level().isClientSide || amount <= 0) return;
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
        transfurProgress = Math.max(0, progress);
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

        float armorRes = Math.min(armorPoints, 20) / 20f * .2f;//Balanced for vanilla (diamond/netherite full set 20 armor) TODO add max armor to config?
        float attributeRes = (float) (holder.getAttributeValue(LATEX_RESISTANCE) * .6f);

        return (armorRes + attributeRes) * covered;//Max .8
    }

    public void subTransfurProgress(float amount){
        if(holder.level().isClientSide || isTransfurred() || transfurProgress == 0 || amount <= 0) return;

        transfurProgress = Math.max(0, transfurProgress - amount);
        if(transfurProgress == 0) transfurType = null;

        syncClients();
    }

    public @Nullable TransfurType<?> getTransfurType() {
        return transfurType;
    }

    public boolean isTransfurred() {
        return isTransfurred && transfurType != null;
    }

    public void transfur(@NotNull TransfurType<?> transfurType, @NotNull TransfurContext context) {
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

                DamageSource source = DamageSources.transfurKill(player.level(), player.getLastHurtByMob());
                player.hurt(source, Float.MAX_VALUE);

                NeoForge.EVENT_BUS.post(new TransfurredEvent(player, latexBeast, null, transfurType, context, source));
            }
            case PROMPT -> {
                setBeingTransfurred(true);
                this.transfurType = transfurType;
                PacketDistributor.sendToPlayer(player, new ClientboundOpenTransfurScreenPacket());
            }
            case TRANSFUR -> actuallyTransfur(transfurType, context);
        }
    }

    private void actuallyTransfur(TransfurType<?> transfurType, TransfurContext context){
        setBeingTransfurred(false);

        TransfurType<?> previous = null;
        if(isTransfurred()){
            previous = this.transfurType;
            this.transfurType.onUnTransfur(holder);
            TransfurUtils.removeModifiers(holder, this.transfurType);
        }

        loadSyncedData(TRANSFUR_TOLERANCE, true, transfurType);

        AbilityUtils.of(holder).replaceAbilities(transfurType.abilities, ABILITY_PROVIDER);
        TransfurUtils.addModifiers(holder, transfurType);
        transfurType.onTransfur(holder);

        syncClients();
        AbilityUtils.syncAbilities(holder);

        NeoForge.EVENT_BUS.post(new TransfurredEvent(holder, null, previous, transfurType, context, null));
    }

    public void unTransfur(@NotNull TransfurContext context) {
        if(holder.level().isClientSide) return;

        if(!(holder instanceof Player player)){//only non players, that are not fully transfurred, should be here so just reset progress
            transfurProgress = 0;
            transfurType = null;
            syncClients();
            if(context.onUntransfurSound() != null)
                holder.level().playSound(null, holder.blockPosition(), context.onUntransfurSound(), SoundSource.NEUTRAL);
            return;
        }

        setBeingTransfurred(false);

        TransfurType<?> transfurTypeO = transfurType;
        if(isTransfurred()) {
            transfurType.onUnTransfur(holder);
            TransfurUtils.removeModifiers(holder, transfurType);
        }

        AbilityUtils.of(player).replaceAbilities(AbilityRegistry.GRAB_ABILITY, ABILITY_PROVIDER);
        loadSyncedData(0, false, null);//assign grab ability to be able to switch (don't)wantToBeGrabbed
        syncClients();

        if(context.onUntransfurSound() != null)
            holder.level().playSound(null, holder.blockPosition(), context.onUntransfurSound(), SoundSource.PLAYERS);

        if(transfurTypeO != null) NeoForge.EVENT_BUS.post(new UnTransfurredEvent(player, transfurTypeO, context));
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
        subTransfurProgress(1);
    }

    void syncClients(){
        holder.refreshDimensions();
        holder.syncData(AttachmentRegistry.TRANSFUR_HANDLER);
    }

    void loadSyncedData(float transfurProgress, boolean isTransfurred, TransfurType<?> transfurType){
        this.transfurProgress = transfurProgress;
        this.isTransfurred = isTransfurred;
        this.transfurType = transfurType;
    }

    public static class Serializer implements IAttachmentSerializer<CompoundTag, TransfurHandler> {

        static final String TRANSFURRED_KEY = "transfurred";
        static final String BEING_TRANSFURRED_KEY = "isBeingTransfurred";
        static final String TRANSFUR_PROGRESS_KEY = "transfur_progress";
        static final String TRANSFUR_TYPE_KEY = "transfur_type";

        private Serializer(){}

        @Override
        public @NotNull TransfurHandler read(@NotNull IAttachmentHolder holder, @NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookup) {
            TransfurHandler handler = new TransfurHandler(holder);

            handler.loadSyncedData(
                    tag.getFloat(TRANSFUR_PROGRESS_KEY), tag.getBoolean(TRANSFURRED_KEY),
                    TransfurManager.getTransfurType(ResourceLocation.parse(tag.getString(TRANSFUR_TYPE_KEY))));

            if(holder instanceof Player) handler.setBeingTransfurred(tag.getBoolean(BEING_TRANSFURRED_KEY));

            if(handler.isTransfurred()) TransfurUtils.addModifiers((LivingEntity) holder, handler.transfurType);
            return handler;
        }

        @Override
        public @Nullable CompoundTag write(@NotNull TransfurHandler attachment, HolderLookup.@NotNull Provider lookup) {
            CompoundTag tag = new CompoundTag();

            tag.putFloat(TRANSFUR_PROGRESS_KEY, attachment.transfurProgress);

            TransfurType<?> transfurType = attachment.transfurType;
            if(transfurType != null) {
                tag.putBoolean(TRANSFURRED_KEY, attachment.isTransfurred);
                tag.putString(TRANSFUR_TYPE_KEY, transfurType.id.toString());
            }

            if(attachment.holder instanceof Player) tag.putBoolean(BEING_TRANSFURRED_KEY, attachment.isBeingTransfurred);
            return tag;
        }
    }

    @ParametersAreNonnullByDefault
    public static class Sync implements AttachmentSyncHandler<TransfurHandler> {

        private Sync(){}

        @Override
        public void write(RegistryFriendlyByteBuf buf, TransfurHandler attachment, boolean initialSync) {
            buf.writeFloat(attachment.transfurProgress);

            TransfurType<?> tf = attachment.transfurType;
            if(tf != null){
                buf.writeBoolean(true);
                buf.writeBoolean(attachment.isTransfurred);
                buf.writeVarInt(TransfurManager.getTransfurId(tf));
            } else buf.writeBoolean(false);
        }

        @Override
        public @Nullable TransfurHandler read(IAttachmentHolder holder, RegistryFriendlyByteBuf buf, @Nullable TransfurHandler previousValue) {
            TransfurHandler handler = previousValue != null ? previousValue : new TransfurHandler(holder);

            float progress = buf.readFloat();
            boolean isTransfurred = false;
            TransfurType<?> tf = null;
            if(buf.readBoolean()){
                isTransfurred = buf.readBoolean();
                tf = TransfurManager.getTransfurType(buf.readVarInt());
            }

            if(holder instanceof AbstractClientPlayer player) {
                handler.setLastTFModelId(TransfurUtilsClient.updateTFModel(player, handler.getLastTFModelId(), isTransfurred ? tf : null));
            }

            handler.loadSyncedData(progress, isTransfurred, tf);
            handler.holder.refreshDimensions();
            return handler;
        }
    }
}