package net.zaharenko424.a_changed.transfurSystem.transfurTypes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.client.cmrs.model.UniversalCustomModel;
import net.zaharenko424.a_changed.client.model.LatexWolfFemaleModel;
import net.zaharenko424.a_changed.client.model.LatexWolfMaleModel;
import net.zaharenko424.a_changed.transfurSystem.Gender;

import java.util.function.Supplier;

/**
 * Class for scamming server to not crash
 */
final class ClientOnly {

    static Supplier<UniversalCustomModel<LivingEntity>> latexWolfModel(Gender gender, ResourceLocation loc){
        ResourceLocation texture = loc.withPrefix("textures/entity/").withSuffix(".png");
        return gender == Gender.FEMALE ?() -> new LatexWolfFemaleModel<>(texture) : () -> new LatexWolfMaleModel<>(texture);
    }
}