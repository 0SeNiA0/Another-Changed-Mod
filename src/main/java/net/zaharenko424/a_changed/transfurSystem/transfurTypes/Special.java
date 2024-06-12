package net.zaharenko424.a_changed.transfurSystem.transfurTypes;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.zaharenko424.a_changed.client.cmrs.model.CustomHumanoidModel;
import org.jetbrains.annotations.NotNull;

public class Special extends AbstractTransfurType {

    public Special(@NotNull Properties properties) {
        super(properties.maxHealthModifier(4).organic(true));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public CustomHumanoidModel<LivingEntity> getModel_() {
        return null;
    }
}