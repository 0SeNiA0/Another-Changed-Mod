package net.zaharenko424.a_changed.client.cmrs.layers;

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
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.neoforge.client.ClientHooks;
import net.zaharenko424.a_changed.client.cmrs.CustomHumanoidRenderer;
import net.zaharenko424.a_changed.client.cmrs.model.CustomHumanoidModel;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ArmorLayer<E extends LivingEntity, M extends CustomHumanoidModel<E>> extends RenderLayer<E,M> {

    protected final TextureAtlas armorTrimAtlas;
    protected M model;

    public ArmorLayer(CustomHumanoidRenderer<E> renderer, TextureAtlas armorTrimAtlas) {
        super((RenderLayerParent<E, M>) renderer);
        this.armorTrimAtlas = armorTrimAtlas;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light, E entity, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
        model = renderer.getModel();
        if(!model.hasArmor()) return;
        model.setAllVisible(true);
        renderArmorPiece(poseStack, buffer, entity, EquipmentSlot.HEAD, light);
        renderArmorPiece(poseStack, buffer, entity, EquipmentSlot.CHEST, light);
        renderArmorPiece(poseStack, buffer, entity, EquipmentSlot.LEGS, light);
        renderArmorPiece(poseStack, buffer, entity, EquipmentSlot.FEET, light);
    }

    private void renderArmorPiece(PoseStack poseStack, MultiBufferSource buffer, E entity, EquipmentSlot slot, int light) {
        ItemStack itemstack = entity.getItemBySlot(slot);
        if(!(itemstack.getItem() instanceof ArmorItem armoritem) || armoritem.getEquipmentSlot() != slot) return;

        model.setupArmorPart(slot, false);

        boolean innerModel = slot == EquipmentSlot.LEGS;
        ArmorMaterial armormaterial = armoritem.getMaterial().value();

        //net.neoforged.neoforge.client.extensions.common.IClientItemExtensions extensions = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(itemstack);
        //int fallbackColor = extensions.getDefaultDyeColor(itemstack);
        //for (int layerIdx = 0; layerIdx < armormaterial.layers().size(); layerIdx++) {
        //    ArmorMaterial.Layer armormaterial$layer = armormaterial.layers().get(layerIdx);
        //    int j = extensions.getArmorLayerTintColor(itemstack, entity, armormaterial$layer, layerIdx, fallbackColor);
        //    if (j != 0) {
        //        var texture = net.neoforged.neoforge.client.ClientHooks.getArmorTexture(entity, itemstack, armormaterial$layer, innerModel, slot);
        //        this.renderModel(poseStack, buffer, light, j, texture);
        //    }
        //}

        int color = itemstack.is(ItemTags.DYEABLE) ? FastColor.ARGB32.opaque(DyedItemColor.getOrDefault(itemstack, -6265536)) : -1;
        for (int layerIdx = 0; layerIdx < armormaterial.layers().size(); layerIdx++) {
            ArmorMaterial.Layer armormaterial$layer = armormaterial.layers().get(layerIdx);
            int j = armormaterial$layer.dyeable() ? color : -1;
            if (j != 0) {
                var texture = net.neoforged.neoforge.client.ClientHooks.getArmorTexture(entity, itemstack, armormaterial$layer, innerModel, slot);
                this.renderModel(poseStack, buffer, light, j, texture);
            }
        }

        ArmorTrim trim = itemstack.get(DataComponents.TRIM);
        if(trim != null)
            renderTrim(armoritem.getMaterial(), poseStack, buffer, light, trim, slot == EquipmentSlot.LEGS);

        if(itemstack.hasFoil()) renderGlint(poseStack, buffer, light);

        if(!model.hasGlowingArmor()) return;
        model.setupArmorPart(slot, true);
        model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.eyes(ClientHooks.getArmorTexture(entity, itemstack, armormaterial.layers().get(0), innerModel, slot))), light, OverlayTexture.NO_OVERLAY, 1);
    }

    protected void renderModel(PoseStack poseStack, MultiBufferSource buffer, int light, int color, ResourceLocation armorResource) {
        VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.armorCutoutNoCull(armorResource));
        model.renderToBuffer(poseStack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, color);
    }

    protected void renderTrim(Holder<ArmorMaterial> material, PoseStack poseStack, MultiBufferSource buffer, int light, ArmorTrim trim, boolean innerTexture) {
        TextureAtlasSprite textureatlassprite = this.armorTrimAtlas.getSprite(innerTexture ? trim.innerTexture(material) : trim.outerTexture(material));
        VertexConsumer vertexconsumer = textureatlassprite.wrap(buffer.getBuffer(Sheets.armorTrimsSheet(trim.pattern().value().decal())));
        model.renderToBuffer(poseStack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, 1);
    }

    protected void renderGlint(PoseStack poseStack, MultiBufferSource buffer, int light) {
        model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.armorEntityGlint()), light, OverlayTexture.NO_OVERLAY, 1);
    }
}