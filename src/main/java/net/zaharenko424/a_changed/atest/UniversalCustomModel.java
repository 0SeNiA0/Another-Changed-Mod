package net.zaharenko424.a_changed.atest;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.zaharenko424.a_changed.client.cmrs.NoYFlip;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelPart;
import net.zaharenko424.a_changed.client.cmrs.model.CustomModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UniversalCustomModel<E extends Entity> extends EntityModel<E> implements NoYFlip, CustomModel {

    protected final ModelPart root;
    protected final Reference2ObjectOpenHashMap<ModelPropertyType<?>, Object> properties;
    List<AnimationComponent> animations;

    public UniversalCustomModel(ModelPart root){
        this(root, new Reference2ObjectOpenHashMap<>());
    }

    public UniversalCustomModel(ModelPart root, Reference2ObjectOpenHashMap<ModelPropertyType<?>, Object> properties){
        this.root = root;
        this.properties = properties;
    }

    public ModelPart root(){
        return root;
    }

    public ModelPart getPart(String name){
        return root.getPart(name);
    }

    public boolean hasProperty(ModelPropertyType<?> propertyType){
        return properties.containsKey(propertyType);
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer consumer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, consumer, packedLight, packedOverlay, color);
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @Nullable RenderType suggestedRenderType, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay, int color) {

    }

    @Override
    public void setupAnim(@NotNull E entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {}

    @Override
    public void prepareMobModel(@NotNull E entity, float limbSwing, float limbSwingAmount, float partialTick) {
        setModelProperties(entity);
    }

    protected void setModelProperties(@NotNull E entity){}

    @Override
    public ResourceLocation getTexture() {
        return null;
    }

    /**
     * Sets whether to draw all parts of the model
     */
    public void setDrawAll(boolean draw){
        setDrawAll(draw, root);
    }

    /**
     * Sets whether to draw all parts after provided part (including it)
     * @param part starting ModelPart
     */
    public void setDrawAll(boolean draw, ModelPart part){
        part.draw = draw;
        part.getAllChildParts().forEach(part0 -> part0.draw = draw);
    }

    /**
     * Sets visibility of all parts except for root.
     */
    public void setAllVisible(boolean visibility){
        root.getAllParts().filter(part -> part != root).forEach(child -> child.visible = visibility);
    }

    /**
     * Sets visibility of all children of provided modelPart.
     */
    public void setAllVisible(boolean visible, ModelPart part){
        part.visible = visible;
        part.getAllChildParts().forEach(child -> child.visible = visible);
    }


    @Override
    public boolean hasGlowParts() {//TMP methods
        return false;
    }

    @Override
    public void setupDrawGlow(boolean draw) {

    }

    @Override
    public boolean hasArmor() {
        return false;
    }

    @Override
    public boolean hasGlowingArmor() {
        return false;
    }

    @Override
    public void setupArmorPart(EquipmentSlot slot, boolean glowing) {}
}