package net.zaharenko424.a_changed.datagen.worldgen.template_pool;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

public class LabPools {

    public static final ResourceKey<StructureTemplatePool> START = key("lab/entrance");
    private static final ResourceKey<StructureTemplatePool> STAIRCASE = key("lab/staircase");
    private static final ResourceKey<StructureTemplatePool> STAIRCASE_UP = key("lab/staircase_up");
    private static final ResourceKey<StructureTemplatePool> STAIRCASE_DOWN = key("lab/staircase_down");
    private static final ResourceKey<StructureTemplatePool> ROOMS = key("lab/rooms");
    private static final ResourceKey<StructureTemplatePool> ROOMS_END = key("lab/rooms_end");
    private static final ResourceKey<StructureTemplatePool> ROOMS_UNDERGROUND = key("lab/underground");

    public static void bootstrap(@NotNull BootstrapContext<StructureTemplatePool> context){
        HolderGetter<StructureTemplatePool> poolGetter = context.lookup(Registries.TEMPLATE_POOL);
        Holder<StructureTemplatePool> empty = poolGetter.getOrThrow(Pools.EMPTY);

        List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>> chair = List.of(
                Pair.of(StructurePoolElement.empty(), 3),
                Pair.of(StructurePoolElement.single(loc("lab/generic/chair1")), 3),//regular
                Pair.of(StructurePoolElement.single(loc("lab/generic/chair2")), 3),
                Pair.of(StructurePoolElement.single(loc("lab/generic/chair3")), 3),
                Pair.of(StructurePoolElement.single(loc("lab/generic/chair4")), 2),//rotated
                Pair.of(StructurePoolElement.single(loc("lab/generic/chair5")), 2),
                Pair.of(StructurePoolElement.single(loc("lab/generic/chair6")), 2),
                Pair.of(StructurePoolElement.single(loc("lab/generic/chair7")), 2),
                Pair.of(StructurePoolElement.single(loc("lab/generic/chair8")), 2)
        );

        Holder<StructureTemplatePool> chairFallback = context.register(key("lab/generic/chair_fallback"), new StructureTemplatePool(
                empty, chair, StructureTemplatePool.Projection.RIGID
        ));

        context.register(key("lab/generic/chair"), new StructureTemplatePool(
                chairFallback, chair, StructureTemplatePool.Projection.RIGID
        ));


        List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>> chairFwd = List.of(
                Pair.of(StructurePoolElement.empty(), 2),
                Pair.of(StructurePoolElement.single(loc("lab/generic/chair1")), 6),//regular
                Pair.of(StructurePoolElement.single(loc("lab/generic/chair4")), 2),//rotated
                Pair.of(StructurePoolElement.single(loc("lab/generic/chair5")), 2),
                Pair.of(StructurePoolElement.single(loc("lab/generic/chair6")), 2)
        );

        Holder<StructureTemplatePool> chairFwdFallback = context.register(key("lab/generic/chair_fwd_fallback"), new StructureTemplatePool(
                empty, chairFwd, StructureTemplatePool.Projection.RIGID
        ));

        context.register(key("lab/generic/chair_fwd"), new StructureTemplatePool(
                chairFwdFallback, chairFwd, StructureTemplatePool.Projection.RIGID
        ));


        List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>> workplaceMisc = List.of(
                Pair.of(StructurePoolElement.empty(), 10),
                Pair.of(StructurePoolElement.single(loc("lab/generic/workplace_misc0")), 2),
                Pair.of(StructurePoolElement.single(loc("lab/generic/workplace_misc1")), 2),
                Pair.of(StructurePoolElement.single(loc("lab/generic/workplace_misc2")), 2),
                Pair.of(StructurePoolElement.single(loc("lab/generic/workplace_misc3")), 2),
                Pair.of(StructurePoolElement.single(loc("lab/generic/workplace_misc4")), 2),
                Pair.of(StructurePoolElement.single(loc("lab/generic/workplace_misc5")), 2),
                Pair.of(StructurePoolElement.single(loc("lab/generic/workplace_misc6")), 2),
                Pair.of(StructurePoolElement.single(loc("lab/generic/workplace_misc7")), 2),
                Pair.of(StructurePoolElement.single(loc("lab/generic/workplace_misc8")), 1),//canned oranges
                Pair.of(StructurePoolElement.single(loc("lab/generic/workplace_misc9")), 2)
        );

        Holder<StructureTemplatePool> workplaceMiscFallback = context.register(key("lab/generic/workplace_misc_fallback"), new StructureTemplatePool(
                empty, workplaceMisc, StructureTemplatePool.Projection.RIGID
        ));

        context.register(key("lab/generic/workplace_misc"), new StructureTemplatePool(
                workplaceMiscFallback, workplaceMisc, StructureTemplatePool.Projection.RIGID
        ));


        context.register(START, new StructureTemplatePool(
                empty,
                List.of(Pair.of(StructurePoolElement.single(loc("lab/entrance")), 1)),
                StructureTemplatePool.Projection.RIGID
        ));

        context.register(key("lab/entrance/maybe_books"), new StructureTemplatePool(
                empty, List.of(
                        Pair.of(StructurePoolElement.empty(), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/entrance/books0")), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/entrance/books1")), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/entrance/books2")), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/entrance/books3")), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/entrance/books4")), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/entrance/books5")), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/entrance/books6")), 1)
                ),
                StructureTemplatePool.Projection.RIGID
        ));

        context.register(key("lab/entrance/maybe_chest"), new StructureTemplatePool(
                empty, List.of(
                        Pair.of(StructurePoolElement.empty(), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/entrance/chest0")), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/entrance/chest1")), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/entrance/chest2")), 1)
                ),
                StructureTemplatePool.Projection.RIGID
        ));

        context.register(key("lab/entrance/maybe_decor"), new StructureTemplatePool(
                empty, List.of(
                        Pair.of(StructurePoolElement.empty(), 2),
                        Pair.of(StructurePoolElement.single(loc("lab/entrance/decor0")), 3),
                        Pair.of(StructurePoolElement.single(loc("lab/entrance/decor1")), 3)
                ),
                StructureTemplatePool.Projection.RIGID
        ));

        //pool for stairs(up / up&down / empty -> fallback)
        context.register(STAIRCASE, new StructureTemplatePool(
                empty, List.of(
                        Pair.of(StructurePoolElement.single(loc("lab/staircase/up")), 3),
                        //Pair.of(StructurePoolElement.single(loc("lab/staircase/up_unfinished_up")), 2),//TODO remove?
                        Pair.of(StructurePoolElement.single(loc("lab/staircase/updown")), 3)
                ),
                StructureTemplatePool.Projection.RIGID
        ));

        Holder<StructureTemplatePool> staircaseCap = context.register(key("lab/staircase_cap"), new StructureTemplatePool(
                empty,
                List.of(Pair.of(StructurePoolElement.single(loc("lab/staircase/cap")), 1)),
                StructureTemplatePool.Projection.RIGID
        ));

        //pool for room on top of stairs(more stairs / end stairs)
        context.register(STAIRCASE_UP, new StructureTemplatePool(
                staircaseCap, List.of(
                        Pair.of(StructurePoolElement.single(loc("lab/staircase/down")), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/staircase/updown")), 2)
                ),
                StructureTemplatePool.Projection.RIGID
        ));

        Holder<StructureTemplatePool> fallbackRoom = context.register(key("lab/staircase_fallback_room"), new StructureTemplatePool(
                empty,
                List.of(
                        Pair.of(StructurePoolElement.single(loc("lab/rooms/roof")), 3),
                        Pair.of(StructurePoolElement.single(loc("lab/rooms/roof_alt0")), 2),
                        Pair.of(StructurePoolElement.single(loc("lab/rooms/roof_alt1")), 2)
                ),
                StructureTemplatePool.Projection.RIGID
        ));

        //pool for room after stairs up(corridor with windows? / some other room; Make sure that upper rooms don't hang in the air!)
        context.register(ROOMS, new StructureTemplatePool(
                fallbackRoom, List.of(
                        Pair.of(StructurePoolElement.single(loc("lab/rooms/patient_room")), 2),
                        Pair.of(StructurePoolElement.single(loc("lab/rooms/office")), 2),
                        Pair.of(StructurePoolElement.single(loc("lab/rooms/cabinets")), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/rooms/cabinets_alt")), 1)
                ),
                StructureTemplatePool.Projection.RIGID
        ));

        context.register(ROOMS_END, new StructureTemplatePool(
                fallbackRoom, List.of(
                        Pair.of(StructurePoolElement.single(loc("lab/rooms/patient_room")), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/rooms/office")), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/rooms/cabinets")), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/rooms/roof")), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/rooms/roof_alt0")), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/rooms/roof_alt1")), 1)
        ),
                StructureTemplatePool.Projection.RIGID
        ));

        //Patient room
        List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>> beds = List.of(
                Pair.of(StructurePoolElement.single(loc("lab/rooms/patient_room/beds0")), 1),
                Pair.of(StructurePoolElement.single(loc("lab/rooms/patient_room/beds1")), 1),
                Pair.of(StructurePoolElement.single(loc("lab/rooms/patient_room/beds2")), 1),
                Pair.of(StructurePoolElement.single(loc("lab/rooms/patient_room/beds3")), 1),
                Pair.of(StructurePoolElement.single(loc("lab/rooms/patient_room/beds4")), 1),
                Pair.of(StructurePoolElement.single(loc("lab/rooms/patient_room/beds5")), 1)
        );

        Holder<StructureTemplatePool> patientRoomFallback = context.register(key("lab/rooms/patient_room/fallback"), new StructureTemplatePool(
                empty, beds, StructureTemplatePool.Projection.RIGID
        ));
        context.register(key("lab/rooms/patient_room/beds"), new StructureTemplatePool(
                patientRoomFallback, beds, StructureTemplatePool.Projection.RIGID
        ));

        //Office
        List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>> officeChest = List.of(
                Pair.of(StructurePoolElement.empty(), 1),
                Pair.of(StructurePoolElement.single(loc("lab/rooms/office/chest")), 1)
        );

        Holder<StructureTemplatePool> chestFallback = context.register(key("lab/rooms/office/chest_fallback"), new StructureTemplatePool(
                empty, officeChest, StructureTemplatePool.Projection.RIGID
        ));
        context.register(key("lab/rooms/office/maybe_chest"), new StructureTemplatePool(
                chestFallback, officeChest, StructureTemplatePool.Projection.RIGID
        ));


        List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>> workplaceChair = List.of(
                Pair.of(StructurePoolElement.empty(), 1),
                Pair.of(StructurePoolElement.single(loc("lab/rooms/office/workplace/chair0")), 1),
                Pair.of(StructurePoolElement.single(loc("lab/rooms/office/workplace/chair1")), 1),
                Pair.of(StructurePoolElement.single(loc("lab/rooms/office/workplace/chair2")), 1),
                Pair.of(StructurePoolElement.single(loc("lab/rooms/office/workplace/chair3")), 1),
                Pair.of(StructurePoolElement.single(loc("lab/rooms/office/workplace/chair4")), 1)
        );

        Holder<StructureTemplatePool> workplaceChairFallback = context.register(key("lab/rooms/office/workplace/chair_fallback"), new StructureTemplatePool(
                empty, workplaceChair, StructureTemplatePool.Projection.RIGID
        ));
        context.register(key("lab/rooms/office/workplace/maybe_chair"), new StructureTemplatePool(
                workplaceChairFallback, workplaceChair, StructureTemplatePool.Projection.RIGID
        ));

        //Cabinets
        List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>> cabinetsChest = List.of(
                Pair.of(StructurePoolElement.empty(), 2),
                Pair.of(StructurePoolElement.single(loc("lab/rooms/cabinets/chest0")), 1),
                Pair.of(StructurePoolElement.single(loc("lab/rooms/cabinets/chest1")), 1),
                Pair.of(StructurePoolElement.single(loc("lab/rooms/cabinets/chest2")), 1),
                Pair.of(StructurePoolElement.single(loc("lab/rooms/cabinets/chest3")), 1),
                Pair.of(StructurePoolElement.single(loc("lab/rooms/cabinets/chest4")), 1)
        );

        Holder<StructureTemplatePool> chestFallbackC = context.register(key("lab/rooms/cabinets/chest_fallback"), new StructureTemplatePool(
                empty, cabinetsChest, StructureTemplatePool.Projection.RIGID
        ));
        context.register(key("lab/rooms/cabinets/maybe_chest"), new StructureTemplatePool(
                chestFallbackC, cabinetsChest, StructureTemplatePool.Projection.RIGID
        ));

        //pool for room below stairs(more stairs / end stairs)
        context.register(STAIRCASE_DOWN, new StructureTemplatePool(
                empty, List.of(
                        Pair.of(StructurePoolElement.single(loc("lab/staircase/up")), 3)
                        //Pair.of(StructurePoolElement.single(loc("lab/staircase/up_unfinished"), fossilRot), 1)//TODO remove?
                ),
                StructureTemplatePool.Projection.RIGID
        ));
        //pool for room after stairs down
        context.register(ROOMS_UNDERGROUND, new StructureTemplatePool(
                empty, List.of(
                        Pair.of(StructurePoolElement.single(loc("lab/underground/storage")), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/underground/generator_room")), 1)
                ),
                StructureTemplatePool.Projection.RIGID
        ));

        context.register(key("lab/underground/storage/maybe_boxes"), new StructureTemplatePool(
                empty, List.of(
                        Pair.of(StructurePoolElement.empty(), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/underground/storage/boxes0")), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/underground/storage/boxes1")), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/underground/storage/boxes2")), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/underground/storage/boxes3")), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/underground/storage/boxes4")), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/underground/storage/boxes5")), 1)
                ),
                StructureTemplatePool.Projection.RIGID
        ));

        context.register(key("lab/underground/storage/lock"), new StructureTemplatePool(
                empty, List.of(
                        Pair.of(StructurePoolElement.single(loc("lab/underground/storage/password_lock0")), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/underground/storage/password_lock1")), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/underground/storage/password_lock2")), 1)
                ),
                StructureTemplatePool.Projection.RIGID
        ));

        context.register(key("lab/underground/storage/maybe_misc"), new StructureTemplatePool(
                empty, List.of(
                        Pair.of(StructurePoolElement.empty(), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/underground/storage/misc0")), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/underground/storage/misc1")), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/underground/storage/misc2")), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/underground/storage/misc3")), 1)
                ),
                StructureTemplatePool.Projection.RIGID
        ));

        context.register(key("lab/underground/generator_room/maybe_boxes"), new StructureTemplatePool(
                empty, List.of(
                        Pair.of(StructurePoolElement.empty(), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/underground/generator_room/boxes0")), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/underground/generator_room/boxes1")), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/underground/generator_room/boxes2")), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/underground/generator_room/boxes3")), 1)
                ),
                StructureTemplatePool.Projection.RIGID
        ));

        context.register(key("lab/underground/generator_room/maybe_misc"), new StructureTemplatePool(
                empty, List.of(
                        Pair.of(StructurePoolElement.empty(), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/underground/generator_room/misc0")), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/underground/generator_room/misc1")), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/underground/generator_room/misc2")), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/underground/generator_room/misc3")), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/underground/generator_room/misc4")), 1),
                        Pair.of(StructurePoolElement.single(loc("lab/underground/generator_room/misc5")), 1)
                ),
                StructureTemplatePool.Projection.RIGID
        ));
    }

    private static @NotNull ResourceKey<StructureTemplatePool> key(String str){
        return Utils.resourceKey(Registries.TEMPLATE_POOL, str);
    }

    @Contract(pure = true)
    private static @NotNull String loc(String str){
        return AChanged.MODID + ":" + str;
    }
}