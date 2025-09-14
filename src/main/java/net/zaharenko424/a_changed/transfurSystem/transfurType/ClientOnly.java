package net.zaharenko424.a_changed.transfurSystem.transfurType;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.client.model.DarkLatexWolfFemaleModel;
import net.zaharenko424.a_changed.client.model.DarkLatexWolfMaleModel;
import net.zaharenko424.a_changed.client.model.WhiteLatexWolfFemaleModel;
import net.zaharenko424.a_changed.client.model.LatexWolfMaleModel;
import net.zaharenko424.a_changed.transfurSystem.Gender;
import net.zaharenko424.cmrs.client.model.UniversalCustomModel;

import java.util.function.Supplier;

final class ClientOnly {

    static Supplier<UniversalCustomModel<LivingEntity>> latexWolfModel(ResourceLocation loc){
        ResourceLocation texture = loc.withPrefix("textures/entity/").withSuffix(".png");
        return () -> new LatexWolfMaleModel<>(texture);
    }

    static Supplier<UniversalCustomModel<LivingEntity>> whiteLatexWolfModel(Gender gender, ResourceLocation loc){
        ResourceLocation texture = loc.withPrefix("textures/entity/").withSuffix(".png");
        return gender == Gender.FEMALE ? WhiteLatexWolfFemaleModel::new : () -> new LatexWolfMaleModel<>(texture);
    }

    static Supplier<UniversalCustomModel<LivingEntity>> darkLatexWolfModel(Gender gender){
        return gender == Gender.FEMALE ? DarkLatexWolfFemaleModel::new : DarkLatexWolfMaleModel::new;
    }
}