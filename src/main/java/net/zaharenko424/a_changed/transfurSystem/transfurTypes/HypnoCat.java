package net.zaharenko424.a_changed.transfurSystem.transfurTypes;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.zaharenko424.a_changed.client.cmrs.model.CustomHumanoidModel;
import net.zaharenko424.a_changed.client.model.HypnoCatModel;
import org.jetbrains.annotations.NotNull;

public class HypnoCat extends AbstractLatexCat {

    public HypnoCat(@NotNull Properties properties) {
        super(properties.maxHealthModifier(4));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public <E extends LivingEntity> CustomHumanoidModel<E> getModel(int modelVariant) {
        return new HypnoCatModel<>();
    }
}