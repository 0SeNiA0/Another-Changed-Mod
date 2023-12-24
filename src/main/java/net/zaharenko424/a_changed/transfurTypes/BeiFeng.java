package net.zaharenko424.a_changed.transfurTypes;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.zaharenko424.a_changed.client.model.BeiFengModel;
import org.jetbrains.annotations.NotNull;

public class BeiFeng extends AbstractTransfurType{
    public BeiFeng(@NotNull Properties properties) {
        super(properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public <E extends LivingEntity> net.zaharenko424.a_changed.client.model.AbstractLatexEntityModel<E> getModel() {
        return new BeiFengModel<>(true);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public <E extends LivingEntity> net.zaharenko424.a_changed.client.model.AbstractLatexEntityModel<E> getArmorModel() {
        return new BeiFengModel<>(false);
    }
}