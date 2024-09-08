package net.zaharenko424.a_changed.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.a_changed.atest.TestZombie;
import net.zaharenko424.a_changed.entity.*;
import net.zaharenko424.a_changed.entity.projectile.SyringeProjectile;
import org.jetbrains.annotations.NotNull;

import static net.zaharenko424.a_changed.AChanged.MODID;
import static net.zaharenko424.a_changed.registry.TransfurRegistry.*;
import static net.zaharenko424.a_changed.util.Utils.NULL_STR;

public class EntityRegistry {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MODID);
//TMP DON'T FORGET TO REMOVE
    public static final DeferredHolder<EntityType<?>, EntityType<TestZombie>> TEST = ENTITIES.register("test_entity", ()->
            EntityType.Builder.<TestZombie>of((a, b) -> new TestZombie(b), MobCategory.MISC).build(NULL_STR));

    //Entities
    public static final DeferredHolder<EntityType<?>, EntityType<SeatEntity>> SEAT_ENTITY = registerEntity(
            "seat_entity",
            EntityType.Builder.<SeatEntity>of((a, b) -> new SeatEntity(b), MobCategory.MISC).sized(0, 0)
    );
    public static final DeferredHolder<EntityType<?>, EntityType<RotatingChairEntity>> CHAIR_ENTITY = registerEntity(
            "chair_entity",
            EntityType.Builder.<RotatingChairEntity>of((a, b) -> new RotatingChairEntity(b), MobCategory.MISC).sized(0, 0)
    );


    public static final DeferredHolder<EntityType<?>, EntityType<RoombaEntity>> ROOMBA_ENTITY = registerEntity(
            "roomba",
            EntityType.Builder.<RoombaEntity>of((a, b) -> new RoombaEntity(b), MobCategory.CREATURE).sized(.9f, .5f)
    );


    public static final DeferredHolder<EntityType<?>, EntityType<MilkPuddingEntity>> MILK_PUDDING = registerEntity(
            "milk_pudding",
            EntityType.Builder.<MilkPuddingEntity>of((a, b) -> new MilkPuddingEntity(b), MobCategory.MONSTER).sized(.8f, .4f)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<LatexBeast>> BEI_FENG = registerEntity(
            "bei_feng",
            EntityType.Builder.of((a, b) -> new LatexBeast(a,b, BEI_FENG_TF.get()), MobCategory.MONSTER)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<LatexBeast>> BENIGN = registerEntity(
            "benign",
            EntityType.Builder.of((a, b) -> new LatexBeast(a, b, BENIGN_TF.get()), MobCategory.MONSTER)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<LatexBeast>> DARK_LATEX_WOLF_MALE = registerEntity(
            "dark_latex_wolf_male",
            EntityType.Builder.of((a, b) -> new LatexBeast(a,b, DARK_LATEX_WOLF_M_TF.get()), MobCategory.MONSTER)
    );
    public static final DeferredHolder<EntityType<?>, EntityType<LatexBeast>> DARK_LATEX_WOLF_FEMALE = registerEntity(
            "dark_latex_wolf_female",
            EntityType.Builder.of((a, b) -> new LatexBeast(a,b, DARK_LATEX_WOLF_F_TF.get()), MobCategory.MONSTER)
    );

    public static final DeferredHolder<EntityType<?>,EntityType<LatexBeast>> GAS_WOLF = registerEntity(
            "gas_wolf",
            EntityType.Builder.of((a, b) -> new LatexBeast(a,b, GAS_WOLF_TF.get()), MobCategory.MONSTER)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<LatexBeast>> HYPNO_CAT = registerEntity(
            "hypno_cat",
            EntityType.Builder.of((a, b) -> new LatexBeast(a, b, HYPNO_CAT_TF.get()), MobCategory.MONSTER)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<WaterLatexBeast>> LATEX_SHARK_FEMALE = registerEntity(
            "latex_shark_female",
            EntityType.Builder.of((a, b) -> new WaterLatexBeast(a, b, LATEX_SHARK_F_TF.get()), MobCategory.MONSTER)
    );
    public static final DeferredHolder<EntityType<?>, EntityType<WaterLatexBeast>> LATEX_SHARK_MALE = registerEntity(
            "latex_shark_male",
            EntityType.Builder.of((a, b) -> new WaterLatexBeast(a, b, LATEX_SHARK_M_TF.get()), MobCategory.MONSTER)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<LatexBeast>> PURE_WHITE_LATEX_WOLF = registerEntity(
            "pure_white_latex_wolf",
            EntityType.Builder.of((a, b) -> new LatexBeast(a, b, PURE_WHITE_LATEX_WOLF_TF.get()), MobCategory.MONSTER)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<LatexBeast>> SNOW_LEOPARD_FEMALE = registerEntity(
            "snow_leopard_female",
            EntityType.Builder.of((a, b) -> new LatexBeast(a, b, SNOW_LEOPARD_F_TF.get()), MobCategory.MONSTER)
    );
    public static final DeferredHolder<EntityType<?>, EntityType<LatexBeast>> SNOW_LEOPARD_MALE = registerEntity(
            "snow_leopard_male",
            EntityType.Builder.of((a, b) -> new LatexBeast(a, b, SNOW_LEOPARD_M_TF.get()), MobCategory.MONSTER)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<LatexBeast>> WHITE_LATEX_WOLF_MALE = registerEntity(
            "white_latex_wolf_male",
            EntityType.Builder.of((a, b) -> new LatexBeast(a, b, WHITE_LATEX_WOLF_M_TF.get()), MobCategory.MONSTER)
    );
    public static final DeferredHolder<EntityType<?>, EntityType<LatexBeast>> WHITE_LATEX_WOLF_FEMALE = registerEntity(
            "white_latex_wolf_female",
            EntityType.Builder.of((a, b) -> new LatexBeast(a, b, WHITE_LATEX_WOLF_F_TF.get()), MobCategory.MONSTER)
    );

    public static final DeferredHolder<EntityType<?>, EntityType<LatexBeast>> YUFENG_DRAGON = registerEntity(
            "yufeng_dragon",
            EntityType.Builder.of((a, b) -> new LatexBeast(a, b, YUFENG_DRAGON_TF.get()), MobCategory.MONSTER)
    );

    //Projectiles
    public static final DeferredHolder<EntityType<?>, EntityType<SyringeProjectile>> SYRINGE_PROJECTILE = registerEntity(
            "syringe_projectile",
            EntityType.Builder.<SyringeProjectile>of((a, b)-> new SyringeProjectile(b), MobCategory.MISC).sized(.3f, .3f)
    );

    private static <T extends Entity> @NotNull DeferredHolder<EntityType<?>, EntityType<T>> registerEntity(@NotNull String id, @NotNull EntityType.Builder<T> builder){
        return ENTITIES.register(id, ()-> builder.build(NULL_STR));
    }
}