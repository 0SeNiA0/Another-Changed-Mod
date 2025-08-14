package net.zaharenko424.cmrs.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.a_changed.util.Utils;
import net.zaharenko424.cmrs.api.*;
import net.zaharenko424.cmrs.client.ModelPropertyManager;
import net.zaharenko424.cmrs.client.geom.ModelPart;
import net.zaharenko424.cmrs.property.FPArms;
import net.zaharenko424.cmrs.property.ModelPropertyType;
import net.zaharenko424.cmrs.property.StringProperty;
import net.zaharenko424.cmrs.client.renderer.MultiBufferSource;
import net.zaharenko424.cmrs.registry.ModelPropertyRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class UniversalCustomModel<E extends LivingEntity> extends EntityModel<E> implements NoYFlip, CustomModel<E> {

    protected final ModelPart root;
    protected final List<Texture> textures;
    protected final List<Material> materials;
    protected final List<RenderLayer> layers;
    protected final Map<String, ModelProperty> properties;
    protected Map<String, ModelProperty> overrides;
    protected final List<AnimationComponent> animations;
    protected final float shadowRadius;
    protected RenderStack stack;

    public UniversalCustomModel(@NotNull ModelPart root, @NotNull List<Texture> textures, @NotNull List<Material> materials, @NotNull List<RenderLayer> layers, @NotNull Map<String, ModelProperty> properties, @NotNull List<AnimationComponent> animations, float shadowRadius){
        super(RenderType::entityCutoutNoCull);
        this.root = root.getPart("root");
        this.textures = List.copyOf(textures);
        this.materials = List.copyOf(materials);

        for(Material mat : materials){
            mat.verifyTextures(textures);
        }

        this.layers = List.copyOf(layers);
        this.properties = Map.copyOf(properties);
        this.animations = animations;
        this.shadowRadius = shadowRadius;
    }

    public ModelPart root(){
        return root;
    }

    public ModelPart getPart(@NotNull String name){
        return root.getPart(name);
    }

    public boolean hasProperty(@NotNull String key, @NotNull ModelPropertyType<?> type){
        ModelProperty property;
        if(overrides != null && !overrides.isEmpty()) {
            property = overrides.get(key);

            if(property != null && property.type().get() == type) return true;
        }

        property = properties.get(key);

        return property != null && property.type().get() == type;
    }

    public boolean hasProperty(@NotNull String key, @NotNull DeferredHolder<ModelPropertyType<?>, ?> type){
        ModelProperty property;
        if(overrides != null && !overrides.isEmpty()) {
            property = overrides.get(key);

            if(property != null && property.type().equals(type)) return true;
        }

        property = properties.get(key);

        return property != null && property.type().equals(type);
    }

    public <P extends ModelProperty> P getProperty(@NotNull String key, @NotNull ModelPropertyType<P> type){
        ModelProperty property;
        if(overrides != null && !overrides.isEmpty()) {
            property = overrides.get(key);

            if(property != null && property.type().get() == type) return (P) property;
        }

        property = properties.get(key);

        if(property == null || property.type().get() != type) return null;
        return (P) property;
    }

    public <P extends ModelProperty> P getProperty(@NotNull String key, @NotNull DeferredHolder<ModelPropertyType<?>, ModelPropertyType<P>> type){
        ModelProperty property;
        if(overrides != null && !overrides.isEmpty()) {
            property = overrides.get(key);

            if(property != null && property.type().equals(type)) return (P) property;
        }

        property = properties.get(key);

        if(property == null || !property.type().equals(type)) return null;
        return (P) property;
    }

    protected RenderStack getStack(){
        if(stack == null){
            stack = new RenderStack();
            stack.setRemap(hasProperty(ModelPropertyKeys.REMAP_UV, ModelPropertyRegistry.UNIT));
        }

        return stack;
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer consumer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, consumer, packedLight, packedOverlay, color);
    }

    @Override
    public void renderToBuffer(@NotNull E entity, @NotNull PoseStack poseStack, @Nullable Function<ResourceLocation, RenderType> suggestedRenderType, int packedLight, int packedOverlay, int color) {
        if(suggestedRenderType == null) return;

        MultiBufferSource source = MultiBufferSource.getInstance();

        getStack().setRenderTypeFunc(suggestedRenderType);

        overrides = ModelPropertyManager.getPropertiesOrEmpty(entity);
        for(int i = 0; i < materials.size(); i++){
            materials.get(i).setupRenderStack(this, entity, stack.getOrCreate(i), source);
        }
        overrides = null;

        root().render(poseStack, stack, packedLight, packedOverlay, color);

        stack.reset();
    }

    public void renderLayers(@NotNull PoseStack poseStack, int packedLight, @NotNull E entity,
                             float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch){
        net.minecraft.client.renderer.MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        layers.forEach(layer -> layer.render(entity, this, poseStack, buffer, packedLight, limbSwing,
                limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch));
    }

    public void setupAnim(@NotNull E entity, @NotNull PoseStack poseStack, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        root.getAllParts().forEach(ModelPart::resetPose);

        float partialTick = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(entity.level().tickRateManager().isEntityFrozen(entity));
        for(AnimationComponent anim : animations){
            anim.animate(root(), entity, poseStack, partialTick, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        }

        setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    @Override
    public void setupAnim(@NotNull E entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {}

    public void renderHand(@NotNull E entity, @NotNull PoseStack poseStack, int light, @NotNull HumanoidArm arm){
        FPArms fpArms = getProperty(ModelPropertyKeys.FP_ARMS, ModelPropertyRegistry.FP_ARMS);
        if(fpArms == null) return;

        ModelPart part = fpArms.getTransformed(this, arm);
        if(part == null) return;

        MultiBufferSource source = MultiBufferSource.getInstance();
        getStack();

        Material mat;
        for(int i = 0; i < materials.size(); i++){
            mat = materials.get(i);
            if(mat.shouldRenderInFirstPerson()) mat.setupRenderStack(this, entity, stack.getOrCreate(i), source);
        }

        setAllVisible(true, part);
        setDrawAll(true, part);
        part.render(poseStack, getStack(), light, OverlayTexture.NO_OVERLAY, -1);

        stack.reset();
    }

    @Override
    public void prepareMobModel(@NotNull E entity, float limbSwing, float limbSwingAmount, float partialTick) {
        setModelProperties(entity);
    }

    protected void setModelProperties(@NotNull E entity){
        setAllVisible(true);
        if(entity.isSpectator()){
            setDrawAll(false);

            ModelPart head = null;

            StringProperty str = getProperty(ModelPropertyKeys.HEAD, ModelPropertyRegistry.STRING);
            if(str != null) head = getPart(str.str());

            if(head == null) head = getPart("head");
            if(head != null) setDrawAll(true, head);
        } else setDrawAll(true);
    }

    @Override
    public ResourceLocation getTexture() {
        return textures.isEmpty() ? Utils.NULL_LOC : textures.getFirst().getLocation();
    }

    @Override
    public Texture getTexture(int index) {
        return textures.get(index);
    }

    @Override
    public float getShadowRadius(@NotNull E entity) {
        return shadowRadius * entity.getScale() * entity.getAgeScale();
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
}