package net.zaharenko424.a_changed.client.test.geom;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.zaharenko424.a_changed.client.model.AbstractLatexEntityModel;
import net.zaharenko424.a_changed.client.renderer.LatexEntityRenderer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Map;

public class PerFaceArmorLayer<E extends LivingEntity,M extends AbstractLatexEntityModel<E>> extends RenderLayer<E,M> {
    private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
    private final TextureAtlas armorTrimAtlas;
    private final LatexEntityRenderer<E> renderer;

    public PerFaceArmorLayer(LatexEntityRenderer<E> p_117346_,TextureAtlas armorTrimAtlas) {
        super((RenderLayerParent<E, M>) p_117346_);
        this.armorTrimAtlas=armorTrimAtlas;
        renderer=p_117346_;
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int light, @NotNull E entity, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
        ArmorModel<E> model= renderer.armorModel();
        renderArmorPiece(poseStack,buffer,entity,EquipmentSlot.HEAD,light,model);
        renderArmorPiece(poseStack,buffer,entity,EquipmentSlot.CHEST,light,model);
        renderArmorPiece(poseStack,buffer,entity,EquipmentSlot.LEGS,light,model);
        renderArmorPiece(poseStack,buffer,entity,EquipmentSlot.FEET,light,model);
    }

    private void renderArmorPiece(PoseStack poseStack, MultiBufferSource buffer, @NotNull E entity, EquipmentSlot slot, int light, ArmorModel<E> model) {
        ItemStack itemstack = entity.getItemBySlot(slot);
        Item $$9 = itemstack.getItem();
        if ($$9 instanceof ArmorItem armoritem) {
            if (armoritem.getEquipmentSlot() == slot) {
                getParentModel().copyPropertiesToArmor(model);
                getFoot(model,true).copyFrom(getParentModel().rightLeg);
                getFoot(model,false).copyFrom(getParentModel().leftLeg);
                this.setPartVisibility(model, slot);
                if (armoritem instanceof DyeableLeatherItem) {
                    int i = ((DyeableLeatherItem)armoritem).getColor(itemstack);
                    float f = (float)(i >> 16 & 0xFF) / 255.0F;
                    float f1 = (float)(i >> 8 & 0xFF) / 255.0F;
                    float f2 = (float)(i & 0xFF) / 255.0F;
                    this.renderModel(poseStack, buffer, light, model, f, f1, f2, getArmorResource(entity, itemstack, slot, null));
                    this.renderModel(poseStack, buffer, light, model, 1.0F, 1.0F, 1.0F, getArmorResource(entity, itemstack, slot, "overlay"));
                } else {
                    this.renderModel(poseStack, buffer, light, model, 1.0F, 1.0F, 1.0F, getArmorResource(entity, itemstack, slot, null));
                }

                ArmorTrim.getTrim(entity.level().registryAccess(), itemstack, true).ifPresent(p_289638_ ->
                        this.renderTrim(armoritem.getMaterial(), poseStack, buffer, light, p_289638_, model, slot==EquipmentSlot.LEGS)
                );

                if (itemstack.hasFoil()) {
                    this.renderGlint(poseStack, buffer, light, model);
                }
            }
        }
    }

    protected void setPartVisibility(@NotNull ArmorModel<E> model, @NotNull EquipmentSlot slot) {
        model.setAllVisible(false);
        switch(slot) {
            case HEAD:
                model.head.visible = true;
                break;
            case CHEST:
                model.setAllVisible(model.body,true);
                model.rightArm.visible = true;
                model.leftArm.visible = true;
                break;
            case LEGS:
                model.body.visible = true;
                model.setAllVisible(model.rightLeg,true);
                model.setAllVisible(model.leftLeg,true);
                break;
            case FEET:
                model.setAllVisible(getFoot(model,true),true);
                model.setAllVisible(getFoot(model,false),true);
        }
    }

    private void renderModel(PoseStack poseStack, @NotNull MultiBufferSource buffer, int light, @NotNull ArmorModel<E> model, float p_289678_, float p_289674_, float p_289693_, ResourceLocation armorResource) {
        VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.armorCutoutNoCull(armorResource));
        model.renderToBuffer(poseStack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, p_289678_, p_289674_, p_289693_, 1.0F);
    }

    private void renderTrim(ArmorMaterial material, PoseStack poseStack, @NotNull MultiBufferSource buffer, int light, @NotNull ArmorTrim p_289692_, net.minecraft.client.model.@NotNull Model p_289663_, boolean p_289651_) {
        TextureAtlasSprite textureatlassprite = this.armorTrimAtlas.getSprite(p_289651_ ? p_289692_.innerTexture(material) : p_289692_.outerTexture(material));
        VertexConsumer vertexconsumer = textureatlassprite.wrap(buffer.getBuffer(Sheets.armorTrimsSheet(p_289692_.pattern().value().decal())));
        p_289663_.renderToBuffer(poseStack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderGlint(PoseStack poseStack, @NotNull MultiBufferSource buffer, int light, net.minecraft.client.model.@NotNull Model p_289659_) {
        p_289659_.renderToBuffer(poseStack, buffer.getBuffer(RenderType.armorEntityGlint()), light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public ResourceLocation getArmorResource(Entity entity, @NotNull ItemStack stack, EquipmentSlot slot, @Nullable String type) {
        ArmorItem item = (ArmorItem)stack.getItem();
        String texture = item.getMaterial().getName();
        String domain = "minecraft";
        int idx = texture.indexOf(':');
        if (idx != -1) {
            domain = texture.substring(0, idx);
            texture = texture.substring(idx + 1);
        }
        String s1 = String.format(java.util.Locale.ROOT, "%s:textures/models/armor/%s_layer_%d%s.png", domain, texture, (slot==EquipmentSlot.LEGS ? 2 : 1), type == null ? "" : String.format(java.util.Locale.ROOT, "_%s", type));

        s1 = net.neoforged.neoforge.client.ClientHooks.getArmorTexture(entity, stack, s1, slot, type);
        ResourceLocation resourcelocation = ARMOR_LOCATION_CACHE.get(s1);

        if (resourcelocation == null) {
            resourcelocation = new ResourceLocation(s1);
            ARMOR_LOCATION_CACHE.put(s1, resourcelocation);
        }

        return resourcelocation;
    }

    private @NotNull ModelPart getFoot(@NotNull ArmorModel<E> model, boolean right){
        return model.root().getChild((right?"right":"left")+"_foot");
    }
}