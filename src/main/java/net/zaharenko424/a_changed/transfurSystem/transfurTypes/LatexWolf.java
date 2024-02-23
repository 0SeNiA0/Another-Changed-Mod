package net.zaharenko424.a_changed.transfurSystem.transfurTypes;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.zaharenko424.a_changed.client.model.AbstractLatexEntityModel;
import net.zaharenko424.a_changed.client.model.LatexWolfFemaleModel;
import net.zaharenko424.a_changed.client.model.LatexWolfMaleModel;
import net.zaharenko424.a_changed.transfurSystem.Gender;
import org.jetbrains.annotations.NotNull;

public class LatexWolf extends AbstractTransfurType {

    public LatexWolf(@NotNull Properties properties){
        super(properties.eyeHeight(1.75f,1.5f));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public <E extends LivingEntity> AbstractLatexEntityModel<E> getModel(int modelVariant) {
        return gender != Gender.FEMALE ? new LatexWolfMaleModel<>() : new LatexWolfFemaleModel<>();
    }
}