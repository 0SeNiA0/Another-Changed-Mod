package net.zaharenko424.a_changed.transfurTypes;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.client.model.AbstractLatexEntityModel;
import net.zaharenko424.a_changed.client.model.BeiFengModel;
import org.jetbrains.annotations.NotNull;

public class BeiFeng extends AbstractTransfurType{
    public BeiFeng(@NotNull Properties properties) {
        super(properties);
    }

    @Override
    public <T extends LivingEntity> AbstractLatexEntityModel<T> getModel(@NotNull EntityRendererProvider.Context context) {
        return new BeiFengModel<>(context,true);
    }

    @Override
    public <T extends LivingEntity> AbstractLatexEntityModel<T> getArmorModel(@NotNull EntityRendererProvider.Context context) {
        return new BeiFengModel<>(context,false);
    }
}