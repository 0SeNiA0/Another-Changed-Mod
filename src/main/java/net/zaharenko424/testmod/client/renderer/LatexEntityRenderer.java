package net.zaharenko424.testmod.client.renderer;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.registries.RegistryObject;
import net.zaharenko424.testmod.TestMod;
import net.zaharenko424.testmod.client.model.DummyModel;
import net.zaharenko424.testmod.entity.transfurTypes.AbstractTransfurType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class LatexEntityRenderer<T extends LivingEntity, M extends HierarchicalModel<T>> extends LivingEntityRenderer<T, M> {

    protected AbstractTransfurType transfurType=null;

    protected final EntityRendererProvider.Context context;
    /**
    Only use for creating latex renderer for players!
     */
    public LatexEntityRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, (M) new DummyModel<>(p_174304_.bakeLayer(new ModelLayerLocation(new ResourceLocation(TestMod.MODID,"dummy"),"main"))), 1);
        context=p_174304_;
    }

    public LatexEntityRenderer(EntityRendererProvider.Context context, @NotNull RegistryObject<? extends AbstractTransfurType> transfurType){
        this(context);
        this.transfurType=transfurType.get();
        this.model= (M) this.transfurType.getModel(context);
    }

    public boolean isTransfurTypeNull(){
        return transfurType==null;
    }

    public void updateTransfurType(@Nullable AbstractTransfurType transfurType){
        if(transfurType==this.transfurType) return;
        this.transfurType=transfurType;
        if(transfurType!=null) this.model= (M) transfurType.getModel(context);
    }

    protected boolean shouldShowName(@NotNull T p_115506_) {
        return super.shouldShowName(p_115506_)
                && (p_115506_.shouldShowName() || p_115506_.hasCustomName() && p_115506_ == this.entityRenderDispatcher.crosshairPickEntity);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull LivingEntity p_114482_) {
        return transfurType!=null?transfurType.resourceLocation.withPrefix("textures/entity/").withSuffix(".png"):new ResourceLocation(TestMod.MODID,"textures/entity/dummy.png");
    }
}