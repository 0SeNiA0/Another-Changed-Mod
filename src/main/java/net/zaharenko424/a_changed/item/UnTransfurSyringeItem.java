package net.zaharenko424.a_changed.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.capability.TransfurCapability;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.jetbrains.annotations.NotNull;

public class UnTransfurSyringeItem extends AbstractSyringe {

    public UnTransfurSyringeItem(){
        this(new Properties());
    }

    protected UnTransfurSyringeItem(@NotNull Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand) {
        if(!TransfurManager.isTransfurred(pPlayer)) return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public @NotNull Rarity getRarity(@NotNull ItemStack pStack) {
        return Rarity.RARE;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack item, @NotNull Level p_41410_, @NotNull LivingEntity p_41411_) {
        Player player = (Player) p_41411_;
        if(!p_41410_.isClientSide){
            if(TransfurManager.isTransfurred(player)){
                use(item, (ServerPlayer) player);
            } else {
                giveDebuffs((ServerPlayer) player, 2);
                giveWither(player, .5f);
            }
        }
        return onUse(item, new ItemStack(ItemRegistry.SYRINGE_ITEM.get()), player);
    }

    protected void use(ItemStack item, ServerPlayer player){
        TransfurCapability.nonNullOf(player).unTransfur(TransfurContext.UNTRANSFUR);
        if(player.getRandom().nextFloat() > .5) giveDebuffs(player, 1);
    }

    protected void giveDebuffs(@NotNull ServerPlayer player, int durationMul){
        int duration = 60 * durationMul;
        player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, duration, 0, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, duration, 0, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration, 1, false, false));
    }

    protected void giveWither(@NotNull Player player, float chance){
        if(player.getRandom().nextFloat() > 1 - chance)
            player.addEffect(new MobEffectInstance(MobEffects.WITHER, 120, 1, false, false));
    }
}