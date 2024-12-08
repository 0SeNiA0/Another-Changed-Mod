package net.zaharenko424.a_changed.client.cmrs.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.objects.Reference2ObjectLinkedOpenHashMap;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.a_changed.client.cmrs.CustomModelRenderer;
import net.zaharenko424.a_changed.client.cmrs.properties.ModelPropertyType;
import net.zaharenko424.a_changed.client.cmrs.api.AnimationComponent;
import net.zaharenko424.a_changed.client.cmrs.properties.Armed;
import net.zaharenko424.a_changed.client.cmrs.properties.Glow;
import net.zaharenko424.a_changed.client.cmrs.api.ModelLayer;
import net.zaharenko424.a_changed.client.cmrs.api.RenderLayerLike;
import net.zaharenko424.a_changed.client.cmrs.api.BufferSourceAccess;
import net.zaharenko424.a_changed.client.cmrs.api.NoYFlip;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelPart;
import net.zaharenko424.a_changed.client.cmrs.api.CustomModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class UniversalCustomModel<E extends LivingEntity> extends EntityModel<E> implements NoYFlip, CustomModel<E> {

    protected final ModelPart root;
    protected final Reference2ObjectLinkedOpenHashMap<ModelPropertyType<?>, Object> properties;
    protected final List<AnimationComponent> animations;
    protected RenderStack stack;

    public UniversalCustomModel(ModelPart root, Reference2ObjectLinkedOpenHashMap<ModelPropertyType<?>, Object> properties){
        this(root, properties, new ArrayList<>(1));
    }

    public UniversalCustomModel(ModelPart root, Reference2ObjectLinkedOpenHashMap<ModelPropertyType<?>, Object> properties, List<AnimationComponent> animations){
        super(RenderType::entityCutout);
        this.root = root;
        verifyProperties(properties);
        this.properties = properties;
        this.animations = animations;
    }

    protected void verifyProperties(Reference2ObjectLinkedOpenHashMap<ModelPropertyType<?>, Object> properties){
        if(!properties.containsKey(CustomModelRenderer.TEXTURES)) throw new IllegalStateException("Model has to have a texture property");

        IntOpenHashSet set = new IntOpenHashSet();
        for(Object obj : properties.values()){
            if(!(obj instanceof ModelLayer layer)) continue;
            layer.renderIds().forEach(id -> {
                if(!set.add(id)) throw new IllegalStateException("Repeated renderId: " + id);
            });
        }
    }

    public ModelPart root(){
        return root;
    }

    public ModelPart getPart(@NotNull String name){
        return root.getPart(name);
    }

    public boolean hasProperty(ModelPropertyType<?> propertyType){
        return properties.containsKey(propertyType);
    }

    public <P> P getProperty(ModelPropertyType<P> type){
        if(!properties.containsKey(type)) return null;
        return (P) properties.get(type);
    }

    protected RenderStack getStack(){
        if(stack == null){
            stack = new RenderStack();
            stack.setRemap(hasProperty(CustomModelRenderer.REMAP_UV));
        }

        return stack;
    }

    /**
     * Call on render thread after modifying model in editor(TODO). In fact do all modifications to the model on render thread(end of frame or start of frame) just in case. or even better: just stop rendering while applying changes
     */
    public void refreshModel(boolean properties){
        if(properties) {
            getStack().setRemap(hasProperty(CustomModelRenderer.REMAP_UV));
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

        for(Object property : properties.values()){
            if(property instanceof ModelLayer layer) layer.setupRenderStack(this, entity, stack, access);
        }
    }

    public void renderLayers(@NotNull PoseStack poseStack, int packedLight, @NotNull E entity,//Works
                             float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch){
        BufferSourceAccess access = BufferSourceAccess.get();
        for(Object obj : properties.values()){
            if(obj instanceof RenderLayerLike layer) layer.render(entity, this, poseStack, access, packedLight, limbSwing,
                    limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        }
    }

    @Override
    public void setupAnim(@NotNull E entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        root.getAllParts().forEach(ModelPart::resetPose);

        for(AnimationComponent anim : animations){
            anim.animate(root(), entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        }
    }

    public void renderHand(@NotNull E entity, @NotNull PoseStack poseStack, int light, @NotNull HumanoidArm arm){//Works
        Armed armed = getProperty(CustomModelRenderer.ARMED);
        if(armed == null) return;

        ModelPart part = armed.getArm(this, arm);
        if(part == null) return;

        part.resetPose();
        armed.transformFirstPerson(this, arm);

        BufferSourceAccess access = BufferSourceAccess.get();
        access.cmrs$finishBatched();

        getStack().reset();
        stack.setRenderTypeFunc(RenderType.ENTITY_CUTOUT);
        //Hardcoded Textures + Glow for first person rendering
        getProperty(CustomModelRenderer.TEXTURES).setupRenderStack(this, entity, stack, access);
        Glow glow = getProperty(CustomModelRenderer.GLOW);
        if(glow != null) glow.setupRenderStack(this, entity, stack, access);
        setAllVisible(true, part);
        //setDrawAll(true, part);
        part.render(poseStack, stack, light, OverlayTexture.NO_OVERLAY, -1);
        access.cmrs$finishBatched();
    }

    @Override
    public void prepareMobModel(@NotNull E entity, float limbSwing, float limbSwingAmount, float partialTick) {
        setModelProperties(entity);
    }

    protected void setModelProperties(@NotNull E entity){
        if(entity instanceof Player player && player.isSpectator()){
            setAllVisible(false);

            ModelPart head = null;
            if(hasProperty(CustomModelRenderer.HEAD)) head = getProperty(CustomModelRenderer.HEAD).getPart(this);
            if(head == null) head = getPart("head");
            if(head != null) setAllVisible(true, head);
        } else setAllVisible(true);
        setDrawAll(true);
    }

    @Override
    public ResourceLocation getTexture() {
        return getProperty(CustomModelRenderer.TEXTURES).firstTexture();
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