package net.zaharenko424.a_changed.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.a_changed.entity.AbstractLatexBeast;
import net.zaharenko424.a_changed.entity.LatexBeast;
import net.zaharenko424.a_changed.entity.SeatEntity;
import org.jetbrains.annotations.NotNull;

import static net.zaharenko424.a_changed.AChanged.MODID;
import static net.zaharenko424.a_changed.AChanged.resourceLoc;
import static net.zaharenko424.a_changed.registry.TransfurRegistry.*;

public final class EntityRegistry {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MODID);

    //Entities
    public static final DeferredHolder<EntityType<?>, EntityType<SeatEntity>> CHAIR_ENTITY = ENTITIES.register("chair_entity", () -> EntityType.Builder.<SeatEntity>of((a, b) -> new SeatEntity(b), MobCategory.MISC).sized(.1f, .1f).build(resourceLoc("chair_entity").toString()));

    public static final DeferredHolder<EntityType<?>, EntityType<LatexBeast>> BEI_FENG =
            registerLatex("bei_feng", EntityType.Builder.<LatexBeast>of((a,b) ->
            new LatexBeast(a,b, BEI_FENG_TF.get()), MobCategory.MONSTER).sized(.6f,1.9f));

    public static final DeferredHolder<EntityType<?>, EntityType<LatexBeast>> DARK_LATEX_WOLF_MALE =
            registerLatex("dark_latex_wolf_male", EntityType.Builder.<LatexBeast>of((a, b) ->
            new LatexBeast(a,b, DARK_LATEX_WOLF_M_TF.get()), MobCategory.MONSTER).sized(.6f,1.9f));
    public static final DeferredHolder<EntityType<?>, EntityType<LatexBeast>> DARK_LATEX_WOLF_FEMALE =
            registerLatex("dark_latex_wolf_female", EntityType.Builder.<LatexBeast>of((a, b) ->
            new LatexBeast(a,b, DARK_LATEX_WOLF_F_TF.get()), MobCategory.MONSTER).sized(.6f,1.9f));

    public static final DeferredHolder<EntityType<?>,EntityType<LatexBeast>> GAS_WOLF =
            registerLatex("gas_wolf", EntityType.Builder.<LatexBeast>of((a, b) ->
            new LatexBeast(a,b, GAS_WOLF_TF.get()), MobCategory.MONSTER).sized(.6f,1.85f));

    public static final DeferredHolder<EntityType<?>, EntityType<LatexBeast>> WHITE_LATEX_WOLF_MALE =
            registerLatex("white_latex_wolf_male", EntityType.Builder.<LatexBeast>of((a, b) ->
            new LatexBeast(a,b, WHITE_LATEX_WOLF_M_TF.get()), MobCategory.MONSTER).sized(.6f,1.9f));
    public static final DeferredHolder<EntityType<?>, EntityType<LatexBeast>> WHITE_LATEX_WOLF_FEMALE =
            registerLatex("white_latex_wolf_female", EntityType.Builder.<LatexBeast>of((a, b) ->
            new LatexBeast(a,b, WHITE_LATEX_WOLF_F_TF.get()), MobCategory.MONSTER).sized(.6f,1.9f));


    private static <T extends AbstractLatexBeast> @NotNull DeferredHolder<EntityType<?>,EntityType<T>> registerLatex(@NotNull String id, @NotNull EntityType.Builder<T> builder){
        return ENTITIES.register(id,()-> builder.build(id));
    }
}