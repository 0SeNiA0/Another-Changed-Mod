package net.zaharenko424.a_changed.transfurSystem.transfurTypes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.client.cmrs.model.UniversalCustomModel;
import net.zaharenko424.a_changed.client.model.*;
import net.zaharenko424.a_changed.transfurSystem.Gender;
import net.zaharenko424.a_changed.util.MemorizingSupplier;

/**
 * Class for scamming server to not crash
 */
final class ClientOnly {

    static MemorizingSupplier<UniversalCustomModel<LivingEntity>> latexWolfModel(Gender gender, ResourceLocation loc){
        ResourceLocation texture = loc.withPrefix("textures/entity/").withSuffix(".png");
        return MemorizingSupplier.of(() -> gender == Gender.FEMALE ? new LatexWolfFemaleModel<>(texture) : new LatexWolfMaleModel<>(texture));
    }

    static MemorizingSupplier<UniversalCustomModel<LivingEntity>> latexSharkModel(Gender gender){
        return MemorizingSupplier.of(() -> gender == Gender.FEMALE ? new LatexSharkFemaleModel<>() : new LatexSharkMaleModel<>());
    }

    static MemorizingSupplier<UniversalCustomModel<LivingEntity>> snowLeopardModel(Gender gender){
        return MemorizingSupplier.of(() -> gender == Gender.FEMALE ? new SnowLeopardFemaleModel<>() : new SnowLeopardMaleModel<>());
    }
}