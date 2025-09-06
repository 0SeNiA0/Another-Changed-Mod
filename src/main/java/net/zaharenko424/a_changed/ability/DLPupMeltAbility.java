package net.zaharenko424.a_changed.ability;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.ability.api.Ability;
import net.zaharenko424.a_changed.ability.api.ActivationType;
import net.zaharenko424.a_changed.attachment.DLPupMeltData;
import net.zaharenko424.a_changed.registry.AttachmentRegistry;
import net.zaharenko424.a_changed.registry.MobEffectRegistry;
import net.zaharenko424.a_changed.registry.SoundRegistry;
import net.zaharenko424.a_changed.registry.TransfurRegistry;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurType.TransfurType;
import net.zaharenko424.a_changed.util.AbilityUtils;
import org.jetbrains.annotations.NotNull;

public class DLPupMeltAbility implements Ability {

    private static final ResourceLocation tex = AChanged.textureLoc("mob_effect/latex_solvent");

    @Override
    public void drawIcon(@NotNull Player player, @NotNull GuiGraphics graphics, int x, int y, boolean overlay) {
        graphics.blit(tex, x, y, 32, 32, 0, 0, 64, 64, 64, 64);
        DLPupMeltData data = getAbilityData(player);
        if(data.isActivated()){
            graphics.blit(HypnosisAbility.activated, x - 16, y - 16, 0, 0, 0, 64, 64, 64, 64);
        }
    }

    @Override
    public boolean canUse(@NotNull LivingEntity holder) {
        TransfurType<?> tf = TransfurManager.getTransfurType(holder);
        return tf != null && tf.is(TransfurRegistry.DARK_LATEX_PUP_TF);
    }

    @Override
    public ActivationType activationType() {
        return ActivationType.SWITCH;
    }

    @Override
    public boolean isActivated(LivingEntity holder) {
        return getAbilityData(holder).isActivated();
    }

    @Override
    public void activate(@NotNull LivingEntity holder) {
        playSound(holder);
        holder.forceAddEffect(new MobEffectInstance(MobEffectRegistry.INVISIBLE_SLOWDOWN, -1, 6, false, false, false), null);
        getAbilityData(holder).setMolten(true);
        holder.refreshDimensions();
    }

    @Override
    public void deactivate(@NotNull LivingEntity holder) {
        DLPupMeltData data = getAbilityData(holder);
        if(!data.isActivated()) return;

        playSound(holder);
        holder.removeEffect(MobEffectRegistry.INVISIBLE_SLOWDOWN);
        data.setMolten(false);
        holder.refreshDimensions();
    }

    protected void playSound(LivingEntity holder){
        holder.level().playSound(null, holder, SoundRegistry.TRANSFUR.get(), SoundSource.NEUTRAL, 1, 1);
    }

    @Override
    public void serverTick(@NotNull LivingEntity holder) {
        if(holder.level().getGameTime() % 10 != 0) return;//run every 10th tick
        DLPupMeltData data = getAbilityData(holder);
        if(!data.isActivated()) return;

        holder.level().getEntitiesOfClass(LivingEntity.class, holder.getBoundingBox(), entity ->
                entity != holder && (!AbilityUtils.hasAbility(this, entity) || !getAbilityData(entity).isActivated())
        ).forEach(entity ->
                entity.forceAddEffect(new MobEffectInstance(MobEffectRegistry.INVISIBLE_SLOWDOWN, 15, 3, false, false, false), holder));
    }

    @Override
    public void remove(@NotNull LivingEntity holder) {
        deactivate(holder);
        holder.removeData(AttachmentRegistry.DL_PUP_MELT_DATA);
    }

    @Override
    public DLPupMeltData getAbilityData(@NotNull LivingEntity holder) {
        return DLPupMeltData.dataOf(holder);
    }
}
