package net.zaharenko424.a_changed.client.cmrs.properties;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.WalkAnimationState;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.zaharenko424.a_changed.client.cmrs.api.RenderLayerLike;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelPart;
import net.zaharenko424.a_changed.client.cmrs.api.CustomModel;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class Head implements RenderLayerLike {

    public static final StreamCodec<FriendlyByteBuf, Head> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            head -> head.headName,
            Head::new
    );

    private final String headName;
    private final Minecraft minecraft = Minecraft.getInstance();
    private final Map<SkullBlock.Type, SkullModelBase> skullModels = SkullBlockRenderer.createSkullRenderers(minecraft.getEntityModels());

    public Head(@NotNull String name){
        headName = name;
    }

    public ModelPart getPart(@NotNull CustomModel<?> model){
        return model.root().getPart(headName);
    }

    public void transformToHead(@NotNull CustomModel<?> model, @NotNull PoseStack stack){
        ModelPart head = getPart(model);
        if(head != null) head.translateAndRotate(stack);
    }

    @Override
    public <E extends LivingEntity> void render(@NotNull E livingEntity, @NotNull CustomModel<E> model, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if(getPart(model) == null) return;

        ItemStack itemstack = livingEntity.getItemBySlot(EquipmentSlot.HEAD);
        if (!itemstack.isEmpty()) {
            Item item = itemstack.getItem();
            poseStack.pushPose();
            boolean villager = livingEntity instanceof Villager || livingEntity instanceof ZombieVillager;
            if (livingEntity.isBaby() && !(livingEntity instanceof Villager)) {
                poseStack.translate(0.0F, 0.03125F, 0.0F);
                poseStack.scale(0.7F, 0.7F, 0.7F);
                poseStack.translate(0.0F, 1.0F, 0.0F);
            }

            transformToHead(model, poseStack);
            if (item instanceof BlockItem && ((BlockItem)item).getBlock() instanceof AbstractSkullBlock) {
                poseStack.scale(1.1875F, 1.1875F, -1.1875F);
                if (villager) {
                    poseStack.translate(0.0F, 0.0625F, 0.0F);
                }

                ResolvableProfile resolvableprofile = itemstack.get(DataComponents.PROFILE);
                poseStack.translate(-0.5, 0.0, -0.5);
                SkullBlock.Type skullblock$type = ((AbstractSkullBlock)((BlockItem)item).getBlock()).getType();
                SkullModelBase skullmodelbase = skullModels.get(skullblock$type);
                RenderType rendertype = SkullBlockRenderer.getRenderType(skullblock$type, resolvableprofile);
                WalkAnimationState walkanimationstate;
                if (livingEntity.getVehicle() instanceof LivingEntity livingentity) {
                    walkanimationstate = livingentity.walkAnimation;
                } else {
                    walkanimationstate = livingEntity.walkAnimation;
                }

                float f3 = walkanimationstate.position(partialTicks);
                SkullBlockRenderer.renderSkull(null, 180.0F, f3, poseStack, buffer, packedLight, skullmodelbase, rendertype);
            } else if (!(item instanceof ArmorItem armoritem) || armoritem.getEquipmentSlot() != EquipmentSlot.HEAD) {
                translateToHead(poseStack, villager);
                minecraft.getEntityRenderDispatcher().getItemInHandRenderer().renderItem(livingEntity, itemstack, ItemDisplayContext.HEAD, false, poseStack, buffer, packedLight);
            }

            poseStack.popPose();
        }
    }

    public static void translateToHead(PoseStack poseStack, boolean isVillager) {//TODO test
        poseStack.translate(0.0F, 0.25F, 0.0F);
        poseStack.scale(0.625F, 0.625F, 0.625F);
        if (isVillager) {
            poseStack.translate(0.0F, 0.1875F, 0.0F);
        }
    }
}