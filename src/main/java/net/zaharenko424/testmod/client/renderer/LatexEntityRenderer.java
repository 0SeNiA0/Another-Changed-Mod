package net.zaharenko424.testmod.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.registries.RegistryObject;
import net.zaharenko424.testmod.TestMod;
import net.zaharenko424.testmod.TransfurType;
import net.zaharenko424.testmod.client.model.LatexEntityModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LatexEntityRenderer<T extends LivingEntity, M extends HierarchicalModel<T>> extends LivingEntityRenderer<T, M> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(TestMod.MODID,"textures/entity/test_entity.png");

    private TransfurType transfurType=null;

    private final EntityRendererProvider.Context context;

    public LatexEntityRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, (M) new LatexEntityModel<>(p_174304_.bakeLayer(TestMod.WHITE_LATEX_TRANSFUR.get().modelLayerLocation)), 1);//TODO put some default model
        context=p_174304_;
    }

    public LatexEntityRenderer(EntityRendererProvider.Context context, @NotNull RegistryObject<TransfurType> transfurType){
        this(context);
        this.transfurType=transfurType.get();
        this.model= (M) this.transfurType.playerModel(context);
    }

    public boolean isTransfurTypeNull(){
        return transfurType==null;
    }

    public void updateTransfurType(@Nullable TransfurType transfurType){
        this.transfurType=transfurType;
        if(transfurType!=null) this.model= (M) transfurType.playerModel(context);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull LivingEntity p_114482_) {
        return TEXTURE;
    }

    public void render(T p_115308_, float p_115309_, float p_115310_, PoseStack p_115311_, MultiBufferSource p_115312_, int p_115313_, M model) {
        this.model=model;
        super.render(p_115308_, p_115309_, p_115310_, p_115311_, p_115312_, p_115313_);
    }
}