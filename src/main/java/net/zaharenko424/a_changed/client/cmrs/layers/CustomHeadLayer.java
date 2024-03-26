package net.zaharenko424.a_changed.client.cmrs.layers;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.WalkAnimationState;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.zaharenko424.a_changed.client.cmrs.model.CustomEntityModel;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class CustomHeadLayer <E extends LivingEntity, M extends CustomEntityModel<E>> extends RenderLayer<E, M> {
    private final float scaleX;
    private final float scaleY;
    private final float scaleZ;
    private final Map<SkullBlock.Type, SkullModelBase> skullModels;
    private final ItemInHandRenderer itemInHandRenderer;

    public CustomHeadLayer(RenderLayerParent<E, M> pRenderer, EntityModelSet pModelSet, ItemInHandRenderer pItemInHandRenderer) {
        this(pRenderer, pModelSet, 1.0F, 1.0F, 1.0F, pItemInHandRenderer);
    }

    public CustomHeadLayer(
            RenderLayerParent<E, M> pRenderer, EntityModelSet pModelSet, float pScaleX, float pScaleY, float pScaleZ, ItemInHandRenderer pItemInHandRenderer
    ) {
        super(pRenderer);
        this.scaleX = pScaleX;
        this.scaleY = pScaleY;
        this.scaleZ = pScaleZ;
        this.skullModels = SkullBlockRenderer.createSkullRenderers(pModelSet);
        this.itemInHandRenderer = pItemInHandRenderer;
    }

    public void render(
            @NotNull PoseStack pPoseStack,
            @NotNull MultiBufferSource pBuffer,
            int pPackedLight,
            @NotNull E pLivingEntity,
            float pLimbSwing,
            float pLimbSwingAmount,
            float pPartialTicks,
            float pAgeInTicks,
            float pNetHeadYaw,
            float pHeadPitch
    ) {
        ItemStack itemstack = pLivingEntity.getItemBySlot(EquipmentSlot.HEAD);
        if (!itemstack.isEmpty()) {
            Item item = itemstack.getItem();
            pPoseStack.pushPose();
            pPoseStack.scale(this.scaleX, this.scaleY, this.scaleZ);
            boolean flag = pLivingEntity instanceof Villager || pLivingEntity instanceof ZombieVillager;
            if (pLivingEntity.isBaby() && !(pLivingEntity instanceof Villager)) {
                pPoseStack.translate(0.0F, 0.03125F, 0.0F);
                pPoseStack.scale(0.7F, 0.7F, 0.7F);
                pPoseStack.translate(0.0F, 1.0F, 0.0F);
            }

            getParentModel().head.translateAndRotate(pPoseStack);
            if (item instanceof BlockItem && ((BlockItem)item).getBlock() instanceof AbstractSkullBlock) {
                //float f2 = 1.1875F; magic number I guess
                pPoseStack.scale(1.1875F, 1.1875F, -1.1875F);
                if (flag) {
                    pPoseStack.translate(0.0F, 0.0625F, 0.0F);
                }

                GameProfile gameprofile = null;
                if (itemstack.hasTag()) {
                    CompoundTag compoundtag = itemstack.getTag();
                    if (compoundtag.contains("SkullOwner", 10)) {
                        gameprofile = NbtUtils.readGameProfile(compoundtag.getCompound("SkullOwner"));
                    }
                }

                pPoseStack.translate(-0.5, 0.0, -0.5);
                SkullBlock.Type skullblock$type = ((AbstractSkullBlock)((BlockItem)item).getBlock()).getType();
                SkullModelBase skullmodelbase = this.skullModels.get(skullblock$type);
                RenderType rendertype = SkullBlockRenderer.getRenderType(skullblock$type, gameprofile);
                Entity entity = pLivingEntity.getVehicle();
                WalkAnimationState walkanimationstate;
                if (entity instanceof LivingEntity livingentity) {
                    walkanimationstate = livingentity.walkAnimation;
                } else {
                    walkanimationstate = pLivingEntity.walkAnimation;
                }

                float f3 = walkanimationstate.position(pPartialTicks);
                SkullBlockRenderer.renderSkull(null, 180.0F, f3, pPoseStack, pBuffer, pPackedLight, skullmodelbase, rendertype);
            } else if (!(item instanceof ArmorItem armoritem) || armoritem.getEquipmentSlot() != EquipmentSlot.HEAD) {
                translateToHead(pPoseStack, flag);
                this.itemInHandRenderer.renderItem(pLivingEntity, itemstack, ItemDisplayContext.HEAD, false, pPoseStack, pBuffer, pPackedLight);
            }

            pPoseStack.popPose();
        }
    }

    public static void translateToHead(@NotNull PoseStack pPoseStack, boolean pIsVillager) {
        pPoseStack.translate(0.0F, 0.25F, 0.0F);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        pPoseStack.scale(0.625F, 0.625F, 0.625F);
        if (pIsVillager) {
            pPoseStack.translate(0.0F, 0.1875F, 0.0F);
        }
    }
}