package net.zaharenko424.a_changed.event;

import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.SpawnPlacementRegisterEvent;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.entity.AbstractLatexBeast;
import net.zaharenko424.a_changed.entity.LatexBeast;
import net.zaharenko424.a_changed.network.PacketHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.zaharenko424.a_changed.AChanged.*;
import static net.zaharenko424.a_changed.registry.EntityRegistry.*;
@ParametersAreNonnullByDefault
@Mod.EventBusSubscriber(modid = AChanged.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
class CommonMod {

    @SubscribeEvent
    public static void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");

        PacketHandler.init();
    }

    @SubscribeEvent
    public static void onAttributeModify(EntityAttributeModificationEvent event){
        event.getTypes().forEach((type)-> {
            if(!event.has(type,AIR_DECREASE_SPEED.get())) event.add(type, AIR_DECREASE_SPEED.get());
            if(!event.has(type,LATEX_RESISTANCE.get())) event.add(type, LATEX_RESISTANCE.get());
        });
    }

    @SubscribeEvent
    public static void onEntityAttributes(@NotNull EntityAttributeCreationEvent event){
        event.put(BEI_FENG.get(), LatexBeast.createAttributes().build());
        event.put(DARK_LATEX_WOLF_MALE.get(), LatexBeast.createAttributes().build());
        event.put(DARK_LATEX_WOLF_FEMALE.get(), LatexBeast.createAttributes().build());
        event.put(GAS_WOLF.get(), LatexBeast.createAttributes().build());
        event.put(WHITE_LATEX_WOLF_MALE.get(), LatexBeast.createAttributes().build());
        event.put(WHITE_LATEX_WOLF_FEMALE.get(), LatexBeast.createAttributes().build());
    }

    @SubscribeEvent
    public static void onSpawnPlacementRegister(SpawnPlacementRegisterEvent event){
        event.register(BEI_FENG.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, AbstractLatexBeast::checkLatexBeastSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(DARK_LATEX_WOLF_FEMALE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, AbstractLatexBeast::checkLatexBeastSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(DARK_LATEX_WOLF_MALE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, AbstractLatexBeast::checkLatexBeastSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(GAS_WOLF.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, AbstractLatexBeast::checkLatexBeastSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(WHITE_LATEX_WOLF_FEMALE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, AbstractLatexBeast::checkLatexBeastSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(WHITE_LATEX_WOLF_MALE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, AbstractLatexBeast::checkLatexBeastSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
    }
}