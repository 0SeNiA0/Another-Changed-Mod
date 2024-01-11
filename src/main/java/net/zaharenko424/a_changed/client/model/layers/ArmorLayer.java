package net.zaharenko424.a_changed.client.model.layers;

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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.zaharenko424.a_changed.client.model.HierarchicalHumanoidModel;
import net.zaharenko424.a_changed.client.renderer.LatexEntityRenderer;
import net.zaharenko424.a_changed.util.Utils;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@ParametersAreNonnullByDefault
public class ArmorLayer<E extends LivingEntity,M extends HierarchicalHumanoidModel<E>> extends RenderLayer<E,M> {
    private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
    private final TextureAtlas armorTrimAtlas;
    private M model;

    public ArmorLayer(LatexEntityRenderer<E> renderer, TextureAtlas armorTrimAtlas) {
        super((RenderLayerParent<E, M>) renderer);
        this.armorTrimAtlas=armorTrimAtlas;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light, E entity, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
        model = renderer.getModel();
        renderArmorPiece(poseStack,buffer,entity,EquipmentSlot.HEAD,light);
        renderArmorPiece(poseStack,buffer,entity,EquipmentSlot.CHEST,light);
        renderArmorPiece(poseStack,buffer,entity,EquipmentSlot.LEGS,light);
        renderArmorPiece(poseStack,buffer,entity,EquipmentSlot.FEET,light);
    }

    private void renderArmorPiece(PoseStack poseStack, MultiBufferSource buffer, E entity, EquipmentSlot slot, int light) {
        ItemStack itemstack = entity.getItemBySlot(slot);
        if(!(itemstack.getItem() instanceof ArmorItem armoritem) || armoritem.getEquipmentSlot() != slot) return;
        renderer.getModel().setupArmorPart(slot);
        if(armoritem instanceof net.minecraft.world.item.DyeableLeatherItem) {
            int i = ((net.minecraft.world.item.DyeableLeatherItem)armoritem).getColor(itemstack);
            float f = (float)(i >> 16 & 0xFF) / 255.0F;
            float f1 = (float)(i >> 8 & 0xFF) / 255.0F;
            float f2 = (float)(i & 0xFF) / 255.0F;
            renderModel(poseStack, buffer, light, f, f1, f2, getArmorResource(entity, itemstack, slot, null));
            renderModel(poseStack, buffer, light, 1.0F, 1.0F, 1.0F, getArmorResource(entity, itemstack, slot, "overlay"));
        } else {
            renderModel(poseStack, buffer, light, 1.0F, 1.0F, 1.0F, getArmorResource(entity, itemstack, slot, null));
        }
        ArmorTrim.getTrim(entity.level().registryAccess(), itemstack, true).ifPresent(p_289638_ ->
                renderTrim(armoritem.getMaterial(), poseStack, buffer, light, p_289638_, slot==EquipmentSlot.LEGS)
        );
        if(itemstack.hasFoil()) renderGlint(poseStack, buffer, light);
    }

    protected void renderModel(PoseStack poseStack, MultiBufferSource buffer, int light, float p_289678_, float p_289674_, float p_289693_, ResourceLocation armorResource) {
        VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.armorCutoutNoCull(armorResource));
        model.renderToBuffer(poseStack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, p_289678_, p_289674_, p_289693_, 1.0F);
    }

    protected void renderTrim(ArmorMaterial material, PoseStack poseStack, MultiBufferSource buffer, int light, ArmorTrim trim, boolean p_289651_) {
        TextureAtlasSprite textureatlassprite = this.armorTrimAtlas.getSprite(p_289651_ ? trim.innerTexture(material) : trim.outerTexture(material));
        VertexConsumer vertexconsumer = textureatlassprite.wrap(buffer.getBuffer(Sheets.armorTrimsSheet(trim.pattern().value().decal())));
        model.renderToBuffer(poseStack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    protected void renderGlint(PoseStack poseStack, MultiBufferSource buffer, int light) {
        model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.armorEntityGlint()), light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public ResourceLocation getArmorResource(E entity, ItemStack stack, EquipmentSlot slot, @Nullable String type) {
        String s1 = Utils.getArmorTexture(stack, slot, type);

        s1 = net.neoforged.neoforge.client.ClientHooks.getArmorTexture(entity, stack, s1, slot, type);
        ResourceLocation resourcelocation = ARMOR_LOCATION_CACHE.get(s1);

        if (resourcelocation == null) {
            resourcelocation = new ResourceLocation(s1);
            ARMOR_LOCATION_CACHE.put(s1, resourcelocation);
        }

        return resourcelocation;
    }
}