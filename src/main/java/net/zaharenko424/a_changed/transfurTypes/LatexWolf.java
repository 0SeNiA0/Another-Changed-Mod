package net.zaharenko424.a_changed.transfurTypes;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.zaharenko424.a_changed.client.model.LatexWolfFemaleModel;
import net.zaharenko424.a_changed.client.model.LatexWolfMaleModel;
import org.jetbrains.annotations.NotNull;

public class LatexWolf extends AbstractGenderedTransfurType {

    public LatexWolf(@NotNull Properties properties){
        super(properties.eyeHeight(1.75f,1.5f));

    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public <E extends LivingEntity> net.zaharenko424.a_changed.client.model.AbstractLatexEntityModel<E> getModel() {
        return isMale()?new LatexWolfMaleModel<>(true):new LatexWolfFemaleModel<>(true);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public <E extends LivingEntity> net.zaharenko424.a_changed.client.model.AbstractLatexEntityModel<E> getArmorModel() {
        return isMale()?new LatexWolfMaleModel<>(false):new LatexWolfFemaleModel<>(false);
    }
}