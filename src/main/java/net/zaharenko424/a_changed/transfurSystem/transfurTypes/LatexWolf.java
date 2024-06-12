package net.zaharenko424.a_changed.transfurSystem.transfurTypes;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.zaharenko424.a_changed.client.cmrs.model.CustomHumanoidModel;
import net.zaharenko424.a_changed.client.model.LatexWolfFemaleModel;
import net.zaharenko424.a_changed.client.model.LatexWolfMaleModel;
import net.zaharenko424.a_changed.transfurSystem.Gender;
import org.jetbrains.annotations.NotNull;

public class LatexWolf extends AbstractTransfurType {

    public LatexWolf(@NotNull Properties properties){
        super(properties.eyeHeight(1.75f,1.5f).maxHealthModifier(4));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected CustomHumanoidModel<LivingEntity> getModel_() {
        return gender != Gender.FEMALE ? new LatexWolfMaleModel<>(id) : new LatexWolfFemaleModel<>(id);
    }
}