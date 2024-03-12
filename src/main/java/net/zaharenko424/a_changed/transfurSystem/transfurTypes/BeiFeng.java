package net.zaharenko424.a_changed.transfurSystem.transfurTypes;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.zaharenko424.a_changed.client.cmrs.model.CustomEntityModel;
import net.zaharenko424.a_changed.client.model.BeiFengModel;
import org.jetbrains.annotations.NotNull;

public class BeiFeng extends AbstractTransfurType{
    public BeiFeng(@NotNull Properties properties) {
        super(properties.eyeHeight(1.75f,1.5f));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public <E extends LivingEntity> CustomEntityModel<E> getModel(int modelVariant) {
        return new BeiFengModel<>();
    }
}