package net.zaharenko424.a_changed.client.cmrs.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.objects.Reference2ObjectLinkedOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.client.cmrs.api.*;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelPart;
import net.zaharenko424.a_changed.client.cmrs.properties.FPArms;
import net.zaharenko424.a_changed.client.cmrs.properties.Glow;
import net.zaharenko424.a_changed.client.cmrs.properties.ModelPropertyMapImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class UniversalCustomModel<E extends LivingEntity> extends EntityModel<E> implements NoYFlip, CustomModel<E> {

    protected final ModelPart root;
    protected final ModelPropertyMap propertyMap;
    protected final List<AnimationComponent> animations;
    protected RenderStack stack;

    public UniversalCustomModel(@NotNull ModelPart root, @NotNull Reference2ObjectLinkedOpenHashMap<ModelPropertyType<?>, Object> properties){
        this(root, properties, new ArrayList<>(1));
    }

    public UniversalCustomModel(@NotNull ModelPart root, @NotNull Reference2ObjectLinkedOpenHashMap<ModelPropertyType<?>, Object> properties, @NotNull List<AnimationComponent> animations){
        this(root, new ModelPropertyMapImpl(properties), animations);
    }

    public UniversalCustomModel(@NotNull ModelPart root, @NotNull ModelPropertyMap properties, @NotNull List<AnimationComponent> animations){
        super(RenderType::entityCutoutNoCull);
        this.root = root.getPart("root");
        verifyProperties(properties);
        this.propertyMap = properties;
        this.animations = animations;
    }

    protected void verifyProperties(@NotNull ModelPropertyMap properties){
        if(!properties.hasProperty(ModelPropertyRegistry.TEXTURES.get())) throw new IllegalStateException("Model has to have a texture property");

        IntOpenHashSet set = new IntOpenHashSet();
        properties.forEachModelLayer(layer ->
            layer.renderIds().forEach(id -> {
                if(!set.add(id)) throw new IllegalStateException("Repeated renderId: " + id);
            })
        );
    }

    public ModelPart root(){
        return root;
    }

    public ModelPart getPart(@NotNull String name){
        return root.getPart(name);
    }

    public boolean hasProperty(@NotNull ModelPropertyType<?> propertyType){
        return propertyMap.hasProperty(propertyType);
    }

    public <P> P getProperty(@NotNull ModelPropertyType<P> type){
        return propertyMap.getProperty(type);
    }

    protected RenderStack getStack(){
        if(stack == null){
            stack = new RenderStack();
            stack.setRemap(hasProperty(ModelPropertyRegistry.REMAP_UV.get()));
        }

        return stack;
    }

    /**
     * Call on render thread after modifying model in editor(TODO). In fact do all modifications to the model on render thread(end of frame or start of frame) just in case. or even better: just stop rendering while applying changes
     */
    public void refreshModel(boolean properties){
        if(properties) {
            getStack().setRemap(hasProperty(ModelPropertyRegistry.REMAP_UV.get()));
            stack.clear();
        }
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer consumer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, consumer, packedLight, packedOverlay, color);
    }

    @Override
    public void renderToBuffer(@NotNull E entity, @NotNull PoseStack poseStack, @Nullable Function<ResourceLocation, RenderType> suggestedRenderType, int packedLight, int packedOverlay, int color) {
        if(suggestedRenderType == null) return;

        BufferSourceAccess access = BufferSourceAccess.get();
        access.cmrs$finishBatched();
        prepareRenderStack(entity, suggestedRenderType, access);

        root().render(poseStack, stack, packedLight, packedOverlay, color);

        access.cmrs$finishBatched();
    }

    protected void prepareRenderStack(E entity, Function<ResourceLocation, RenderType> suggestedRenderType, BufferSourceAccess access){
        getStack().reset();
        stack.setRenderTypeFunc(suggestedRenderType);
        propertyMap.forEachModelLayer(layer -> layer.setupRenderStack(this, entity, stack, access));
    }

    public void renderLayers(@NotNull PoseStack poseStack, int packedLight, @NotNull E entity,
                             float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch){
        BufferSourceAccess access = BufferSourceAccess.get();
        propertyMap.forEachRenderLayer(layer -> layer.render(entity, this, poseStack, access, packedLight, limbSwing,
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
        FPArms fpArms = getProperty(ModelPropertyRegistry.FP_ARMS.get());
        if(fpArms == null) return;

        ModelPart part = fpArms.getTransformed(this, arm);
        if(part == null) return;

        BufferSourceAccess access = BufferSourceAccess.get();
        access.cmrs$finishBatched();

        getStack().reset();
        stack.setRenderTypeFunc(RenderType.ENTITY_CUTOUT);
        //Hardcoded Textures + Glow for first person rendering
        getProperty(ModelPropertyRegistry.TEXTURES.get()).setupRenderStack(this, entity, stack, access);
        Glow glow = getProperty(ModelPropertyRegistry.GLOW.get());
        if(glow != null) glow.setupRenderStack(this, entity, stack, access);
        setAllVisible(true, part);
        setDrawAll(true, part);
        part.render(poseStack, stack, light, OverlayTexture.NO_OVERLAY, -1);
        access.cmrs$finishBatched();
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
            if(hasProperty(ModelPropertyRegistry.HEAD.get())) head = getPart(getProperty(ModelPropertyRegistry.HEAD.get()));
            if(head == null) head = getPart("head");
            if(head != null) setDrawAll(true, head);
        } else setDrawAll(true);
    }

    @Override
    public ResourceLocation getTexture() {
        return getProperty(ModelPropertyRegistry.TEXTURES.get()).firstTexture();
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