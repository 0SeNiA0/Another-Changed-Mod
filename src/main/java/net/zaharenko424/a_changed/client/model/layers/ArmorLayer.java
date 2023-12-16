package net.zaharenko424.a_changed.client.model.layers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.zaharenko424.a_changed.client.model.AbstractLatexEntityModel;
import net.zaharenko424.a_changed.client.renderer.LatexEntityRenderer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Map;

public class ArmorLayer<E extends LivingEntity,M extends AbstractLatexEntityModel<E>> extends RenderLayer<E,M> {
    private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
    private final TextureAtlas armorTrimAtlas;
    private final LatexEntityRenderer<E> renderer;

    public ArmorLayer(LatexEntityRenderer<E> p_117346_,TextureAtlas armorTrimAtlas) {
        super((RenderLayerParent<E, M>) p_117346_);
        this.armorTrimAtlas=armorTrimAtlas;
        renderer=p_117346_;
    }

    @Override
    public void render(@NotNull PoseStack p_117349_, @NotNull MultiBufferSource p_117350_, int p_117351_, @NotNull E p_117352_, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
        M model= (M) renderer.getArmorModel();
        renderArmorPiece(p_117349_,p_117350_,p_117352_,EquipmentSlot.HEAD,p_117351_,model);
        renderArmorPiece(p_117349_,p_117350_,p_117352_,EquipmentSlot.CHEST,p_117351_,model);
        renderArmorPiece(p_117349_,p_117350_,p_117352_,EquipmentSlot.LEGS,p_117351_,model);
        renderArmorPiece(p_117349_,p_117350_,p_117352_,EquipmentSlot.FEET,p_117351_,model);
    }

    private void renderArmorPiece(PoseStack p_117119_, MultiBufferSource p_117120_, @NotNull E p_117121_, EquipmentSlot p_117122_, int p_117123_, M p_117124_) {
        ItemStack itemstack = p_117121_.getItemBySlot(p_117122_);
        Item $$9 = itemstack.getItem();
        if ($$9 instanceof ArmorItem armoritem) {
            if (armoritem.getEquipmentSlot() == p_117122_) {
                getParentModel().copyPropertiesTo(p_117124_);
                getFoot(p_117124_,true).copyFrom(getParentModel().rightLeg);
                getFoot(p_117124_,false).copyFrom(getParentModel().leftLeg);
                this.setPartVisibility(p_117124_, p_117122_);
                if (armoritem instanceof net.minecraft.world.item.DyeableLeatherItem) {
                    int i = ((net.minecraft.world.item.DyeableLeatherItem)armoritem).getColor(itemstack);
                    float f = (float)(i >> 16 & 0xFF) / 255.0F;
                    float f1 = (float)(i >> 8 & 0xFF) / 255.0F;
                    float f2 = (float)(i & 0xFF) / 255.0F;
                    this.renderModel(p_117119_, p_117120_, p_117123_, p_117124_, f, f1, f2, this.getArmorResource(p_117121_, itemstack, p_117122_, null));
                    this.renderModel(p_117119_, p_117120_, p_117123_, p_117124_, 1.0F, 1.0F, 1.0F, this.getArmorResource(p_117121_, itemstack, p_117122_, "overlay"));
                } else {
                    this.renderModel(p_117119_, p_117120_, p_117123_, p_117124_, 1.0F, 1.0F, 1.0F, this.getArmorResource(p_117121_, itemstack, p_117122_, null));
                }

                ArmorTrim.getTrim(p_117121_.level().registryAccess(), itemstack, true).ifPresent(p_289638_ ->
                        this.renderTrim(armoritem.getMaterial(), p_117119_, p_117120_, p_117123_, p_289638_, p_117124_, p_117122_==EquipmentSlot.LEGS)
                );

                if (itemstack.hasFoil()) {
                    this.renderGlint(p_117119_, p_117120_, p_117123_, p_117124_);
                }
            }
        }
    }

    protected void setPartVisibility(@NotNull M p_117126_, @NotNull EquipmentSlot p_117127_) {
        p_117126_.setAllVisible(false);
        switch(p_117127_) {
            case HEAD:
                p_117126_.getHead().visible = true;
                break;
            case CHEST:
                p_117126_.setAllVisible(p_117126_.body,true);
                p_117126_.rightArm.visible = true;
                p_117126_.leftArm.visible = true;
                break;
            case LEGS:
                p_117126_.body.visible = true;
                p_117126_.setAllVisible(p_117126_.rightLeg,true);
                p_117126_.setAllVisible(p_117126_.leftLeg,true);
                break;
            case FEET:
                p_117126_.setAllVisible(getFoot(p_117126_,true),true);
                p_117126_.setAllVisible(getFoot(p_117126_,false),true);
        }
    }

    private void renderModel(PoseStack p_289664_, @NotNull MultiBufferSource p_289689_, int p_289681_, @NotNull M p_289658_, float p_289678_, float p_289674_, float p_289693_, ResourceLocation armorResource) {
        VertexConsumer vertexconsumer = p_289689_.getBuffer(RenderType.armorCutoutNoCull(armorResource));
        p_289658_.renderToBuffer(p_289664_, vertexconsumer, p_289681_, OverlayTexture.NO_OVERLAY, p_289678_, p_289674_, p_289693_, 1.0F);
    }

    private void renderTrim(ArmorMaterial p_289690_, PoseStack p_289687_, @NotNull MultiBufferSource p_289643_, int p_289683_, @NotNull ArmorTrim p_289692_, net.minecraft.client.model.@NotNull Model p_289663_, boolean p_289651_) {
        TextureAtlasSprite textureatlassprite = this.armorTrimAtlas.getSprite(p_289651_ ? p_289692_.innerTexture(p_289690_) : p_289692_.outerTexture(p_289690_));
        VertexConsumer vertexconsumer = textureatlassprite.wrap(p_289643_.getBuffer(Sheets.armorTrimsSheet(p_289692_.pattern().value().decal())));
        p_289663_.renderToBuffer(p_289687_, vertexconsumer, p_289683_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderGlint(PoseStack p_289673_, @NotNull MultiBufferSource p_289654_, int p_289649_, net.minecraft.client.model.@NotNull Model p_289659_) {
        p_289659_.renderToBuffer(p_289673_, p_289654_.getBuffer(RenderType.armorEntityGlint()), p_289649_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public ResourceLocation getArmorResource(net.minecraft.world.entity.Entity entity, @NotNull ItemStack stack, EquipmentSlot slot, @Nullable String type) {
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

    private @NotNull ModelPart getFoot(@NotNull M model, boolean right){
        return model.root().getChild((right?"right":"left")+"_foot");
    }
}
