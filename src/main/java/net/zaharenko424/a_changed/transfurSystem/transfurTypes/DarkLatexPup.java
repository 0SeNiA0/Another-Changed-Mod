package net.zaharenko424.a_changed.transfurSystem.transfurTypes;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.zaharenko424.a_changed.attachments.DLPupMeltData;
import net.zaharenko424.a_changed.client.cmrs.event.RegisterBuiltInModelsEvent;
import net.zaharenko424.a_changed.client.model.DLPupMoltenModel;
import net.zaharenko424.a_changed.client.model.DarkLatexPupModel;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DarkLatexPup extends TransfurType {

    protected final ResourceLocation molten;

    public DarkLatexPup(@NotNull Properties properties) {
        super(properties.organic(true)
                .maxHealthModifier(-4).colors(-13686230, -14146010)
                .addAbility(AbilityRegistry.DL_PUP_MELT).addAbility(AbilityRegistry.DL_PUP_AGE).addAbility(AbilityRegistry.WOLF_PASSIVE));
        molten = id.withSuffix("_molten");
    }

    @Override
    public void registerModels(@NotNull RegisterBuiltInModelsEvent event) {
        event.registerModelSupplier(id, DarkLatexPupModel::new);
        event.registerModelSupplier(molten, DLPupMoltenModel::new);
    }

    @Override
    public @Nullable ResourceLocation getModelIdFor(@NotNull LivingEntity entity) {
        DLPupMeltData data = AbilityRegistry.DL_PUP_MELT.get().getAbilityData(entity);
        return data.isMolten() ? molten : super.getModelIdFor(entity);
    }

    protected final EntityDimensions dimensions = new EntityDimensions(.6f, .85f, .68f,
            EntityAttachments.builder().attach(EntityAttachment.PASSENGER, new Vec3(0.0, 0.81875, -0.0625)).build(.6f, .85f),
            false);
    protected final EntityDimensions dimensionsMolten = EntityDimensions.scalable(.9f, .45f);

    @Override
    public @Nullable EntityDimensions getPoseDimensions(@NotNull LivingEntity entity, @NotNull Pose pose) {
        if (AbilityRegistry.DL_PUP_MELT.get().getAbilityData(entity).isMolten()) return dimensionsMolten.scale(entity.getAgeScale());
        return dimensions.scale(entity.getAgeScale());
    }

    @Override
    public void onTransfur(@NotNull LivingEntity entity) {//Drop unsupported armor
        super.onTransfur(entity);
        if(!(entity instanceof Player player)) return;

        ItemStack stack = player.getOffhandItem();
        if(!stack.isEmpty()) {
            ItemHandlerHelper.giveItemToPlayer(player, stack);
            player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
        }

        NonNullList<ItemStack> armor = player.getInventory().armor;
        for(int i = 0; i < armor.size(); i++){
            stack = armor.get(i);
            if(!stack.is(Items.WOLF_ARMOR)){
                ItemHandlerHelper.giveItemToPlayer(player, stack);
                armor.set(i, ItemStack.EMPTY);
            }
        }
    }

    @Override
    public void onUnTransfur(@NotNull LivingEntity entity) {//Drop unsupported armor
        super.onUnTransfur(entity);
        if(!(entity instanceof Player player)) return;

        ItemStack stack = player.getInventory().armor.getFirst();
        if(stack.isEmpty()) return;
        ItemHandlerHelper.giveItemToPlayer(player, stack);
        player.getInventory().armor.set(0, ItemStack.EMPTY);
    }
}