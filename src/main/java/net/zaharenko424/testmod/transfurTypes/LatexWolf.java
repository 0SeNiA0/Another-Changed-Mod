package net.zaharenko424.testmod.transfurTypes;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.zaharenko424.testmod.client.model.AbstractLatexEntityModel;
import net.zaharenko424.testmod.client.model.LatexWolfFemaleModel;
import net.zaharenko424.testmod.client.model.LatexWolfMaleModel;
import org.jetbrains.annotations.NotNull;

public class LatexWolf extends AbstractGenderedTransfurType {

    public LatexWolf(@NotNull Properties properties){
        super(properties.eyeHeight(1.75f,1.5f));

    }

    @Override @OnlyIn(Dist.CLIENT)
    public <T extends LivingEntity> AbstractLatexEntityModel<T> getModel(@NotNull EntityRendererProvider.Context context) {
        return isMale()?new LatexWolfMaleModel<>(context,true):new LatexWolfFemaleModel<>(context,true);
    }

    @Override @OnlyIn(Dist.CLIENT)
    public <T extends LivingEntity> AbstractLatexEntityModel<T> getArmorModel(@NotNull EntityRendererProvider.Context context) {
        return isMale()?new LatexWolfMaleModel<>(context,false):new LatexWolfFemaleModel<>(context,false);
    }
}