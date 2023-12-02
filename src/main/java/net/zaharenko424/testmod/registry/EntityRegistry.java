package net.zaharenko424.testmod.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.testmod.entity.SeatEntity;
import net.zaharenko424.testmod.entity.LatexBeast;

import static net.zaharenko424.testmod.TestMod.MODID;
import static net.zaharenko424.testmod.TestMod.resourceLoc;
import static net.zaharenko424.testmod.registry.TransfurRegistry.*;

public final class EntityRegistry {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE,MODID);

    //Entities
    public static final DeferredHolder<EntityType<?>,EntityType<SeatEntity>> CHAIR_ENTITY = ENTITIES.register("chair_entity", ()-> EntityType.Builder.<SeatEntity>of((a, b) -> new SeatEntity(b),MobCategory.MISC).sized(.1f,.1f).build(resourceLoc("chair_entity").toString()));

    public static final DeferredHolder<EntityType<?>,EntityType<LatexBeast>> DARK_LATEX_WOLF_MALE = ENTITIES.register("dark_latex_wolf_male",()-> EntityType.Builder.<LatexBeast>of((a, b)->new LatexBeast(a,b, DARK_LATEX_WOLF_M_TF.get()), MobCategory.CREATURE).sized(0.6f,2).build(resourceLoc("dark_latex_wolf_male").toString()));
    public static final DeferredHolder<EntityType<?>,EntityType<LatexBeast>> DARK_LATEX_WOLF_FEMALE = ENTITIES.register("dark_latex_wolf_female",()-> EntityType.Builder.<LatexBeast>of((a, b)->new LatexBeast(a,b, DARK_LATEX_WOLF_F_TF.get()), MobCategory.CREATURE).sized(0.6f,2).build(resourceLoc("dark_latex_wolf_female").toString()));

    public static final DeferredHolder<EntityType<?>,EntityType<LatexBeast>> WHITE_LATEX_WOLF_MALE = ENTITIES.register("white_latex_wolf_male",()-> EntityType.Builder.<LatexBeast>of((a, b)->new LatexBeast(a,b, WHITE_LATEX_WOLF_M_TF.get()), MobCategory.CREATURE).sized(0.6f,2).build(resourceLoc("white_latex_wolf_male").toString()));
    public static final DeferredHolder<EntityType<?>,EntityType<LatexBeast>> WHITE_LATEX_WOLF_FEMALE = ENTITIES.register("white_latex_wolf_female", ()-> EntityType.Builder.<LatexBeast>of((a,b)->new LatexBeast(a,b,WHITE_LATEX_WOLF_F_TF.get()), MobCategory.CREATURE).sized(0.6f,2).build(resourceLoc("white_latex_wolf_female").toString()));
}