package net.zaharenko424.a_changed.event;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.items.ComponentItemHandler;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.capability.item.ItemEnergyCapability;
import net.zaharenko424.a_changed.entity.*;
import net.zaharenko424.a_changed.entity.block.machines.AbstractMachineEntity;
import net.zaharenko424.a_changed.network.ClientPacketHandler;
import net.zaharenko424.a_changed.network.ServerPacketHandler;
import net.zaharenko424.a_changed.network.packets.*;
import net.zaharenko424.a_changed.network.packets.ability.*;
import net.zaharenko424.a_changed.network.packets.transfur.ClientboundOpenTransfurScreenPacket;
import net.zaharenko424.a_changed.network.packets.transfur.ClientboundTransfurSyncPacket;
import net.zaharenko424.a_changed.network.packets.transfur.ClientboundTransfurToleranceSyncPacket;
import net.zaharenko424.a_changed.network.packets.transfur.ServerboundTransfurChoicePacket;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import net.zaharenko424.a_changed.registry.BlockRegistry;
import net.zaharenko424.a_changed.registry.ComponentRegistry;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static net.zaharenko424.a_changed.AChanged.*;
import static net.zaharenko424.a_changed.registry.EntityRegistry.*;
@ParametersAreNonnullByDefault
@EventBusSubscriber(modid = AChanged.MODID,bus = EventBusSubscriber.Bus.MOD)
public class CommonMod {

    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event){
        ((FlowerPotBlock)Blocks.FLOWER_POT).addPlant(BlockRegistry.ORANGE_SAPLING.getId(), BlockRegistry.POTTED_ORANGE_SAPLING);
    }

    @SubscribeEvent
    public static void onRegisterPayload(RegisterPayloadHandlersEvent event){
        PayloadRegistrar registrar = event.registrar(MODID);

        //Lambda SHOULDN'T be replaced with method reference on handleClient! -> server will crash

        //Ability
        registrar.playToServer(ServerboundActivateAbilityPacket.TYPE, ServerboundActivateAbilityPacket.CODEC,
                ServerPacketHandler.INSTANCE::handleActivateAbilityPacket);

        registrar.playToServer(ServerboundDeactivateAbilityPacket.TYPE, ServerboundDeactivateAbilityPacket.CODEC,
                ServerPacketHandler.INSTANCE::handleDeactivateAbilityPacket);

        registrar.playToServer(ServerboundSelectAbilityPacket.TYPE, ServerboundSelectAbilityPacket.CODEC,
                ServerPacketHandler.INSTANCE::handleSelectAbilityPacket);

        registrar.playToClient(ClientboundAbilitySyncPacket.TYPE, ClientboundAbilitySyncPacket.CODEC,
                (packet, context) -> ClientPacketHandler.INSTANCE.handleAbilitySyncPacket(packet, context));

        registrar.playToServer(ServerboundAbilityPacket.TYPE, ServerboundAbilityPacket.CODEC,
                ServerPacketHandler.INSTANCE::handleAbilityPacket);

        //Smooth look
        registrar.playToClient(ClientboundSmoothLookPacket.TYPE, ClientboundSmoothLookPacket.CODEC,
                (packet, context) -> ClientPacketHandler.INSTANCE.handleSmoothLookPacket(packet, context));

        //Transfur tolerance update
        registrar.commonToClient(ClientboundTransfurToleranceSyncPacket.TYPE, ClientboundTransfurToleranceSyncPacket.CODEC,
                (packet, context) -> ClientPacketHandler.INSTANCE.handleTransfurToleranceSync(packet));

        //Transfur sync
        registrar.playToClient(ClientboundTransfurSyncPacket.TYPE, ClientboundTransfurSyncPacket.CODEC,
                (packet, context) -> ClientPacketHandler.INSTANCE.handleTransfurSyncPacket(packet, context));

        //Transfur screen
        registrar.playToClient(ClientboundOpenTransfurScreenPacket.TYPE, ClientboundOpenTransfurScreenPacket.CODEC,
                (packet, context) -> ClientPacketHandler.INSTANCE.handleOpenTransfurScreen(context));
        registrar.playToServer(ServerboundTransfurChoicePacket.TYPE, ServerboundTransfurChoicePacket.CODEC,
                ServerPacketHandler.INSTANCE::handleTransfurChoicePacket);

        //Latex coveredness of chunks
        registrar.playToClient(ClientboundLTCDataPacket.TYPE, ClientboundLTCDataPacket.CODEC,
                (packet, context) -> ClientPacketHandler.INSTANCE.handleLTCDataSync(packet, context));

        //Latex encoder
        registrar.playToServer(ServerboundLatexEncoderScreenPacket.TYPE, ServerboundLatexEncoderScreenPacket.CODEC,
                ServerPacketHandler.INSTANCE::handleLatexEncoderScreenPacket);

        //Note
        registrar.playToClient(ClientboundOpenNotePacket.TYPE, ClientboundOpenNotePacket.CODEC,
                (packet, context) -> ClientPacketHandler.INSTANCE.handleOpenNotePacket(packet, context));
        registrar.playToServer(ServerboundEditNotePacket.TYPE, ServerboundEditNotePacket.CODEC,
                ServerPacketHandler.INSTANCE::handleEditNotePacket);

        //Keypad
        registrar.playToClient(ClientboundOpenKeypadPacket.TYPE, ClientboundOpenKeypadPacket.CODEC,
                (packet, context) -> ClientPacketHandler.INSTANCE.handleOpenKeypadPacket(packet, context));
        registrar.playToServer(ServerboundTryPasswordPacket.TYPE, ServerboundTryPasswordPacket.CODEC,
                ServerPacketHandler.INSTANCE::handleTryPasswordPacket);
    }

    private static final List<EntityType<? extends LivingEntity>> transfurrable = List.of(EntityType.PLAYER, EntityType.ZOMBIE,
            EntityType.SKELETON, EntityType.WITCH, EntityType.WITHER_SKELETON, EntityType.DROWNED, EntityType.PIGLIN_BRUTE,
            EntityType.PIGLIN, EntityType.PILLAGER, EntityType.EVOKER, EntityType.HUSK, EntityType.VILLAGER, EntityType.VINDICATOR,
            EntityType.ZOMBIE_VILLAGER, EntityType.STRAY, EntityType.ZOMBIFIED_PIGLIN, EntityType.ILLUSIONER);

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event){
        //Item
        event.registerItem(Capabilities.EnergyStorage.ITEM, (item, context) ->
                ItemEnergyCapability.getCapability(10000, 128, item), ItemRegistry.POWER_CELL);
        event.registerItem(Capabilities.EnergyStorage.ITEM, (item, context) ->
                ItemEnergyCapability.getCapability(25000, 128, item), ItemRegistry.STUN_BATON);
        event.registerItem(Capabilities.EnergyStorage.ITEM, (item, context) ->
                ItemEnergyCapability.getCapability(50000, 256, item), ItemRegistry.SYRINGE_COIL_GUN);

        event.registerItem(Capabilities.ItemHandler.ITEM, (item, context) ->
                new ComponentItemHandler(item, ComponentRegistry.ITEM_INVENTORY.get(), 9), ItemRegistry.PNEUMATIC_SYRINGE_RIFLE);
        event.registerItem(Capabilities.ItemHandler.ITEM, (item, context) ->
                new ComponentItemHandler(item, ComponentRegistry.ITEM_INVENTORY.get(), 4), ItemRegistry.SYRINGE_COIL_GUN);

        //BlockEntity
        registerMachineEntityCaps(event, BlockEntityRegistry.CAPACITOR_ENTITY.get());
        registerMachineEntityCaps(event, BlockEntityRegistry.COMPRESSOR_ENTITY.get());
        registerMachineEntityCaps(event, BlockEntityRegistry.DNA_EXTRACTOR_ENTITY.get());
        registerMachineEntityCaps(event, BlockEntityRegistry.GENERATOR_ENTITY.get());
        registerMachineEntityCaps(event, BlockEntityRegistry.LATEX_ENCODER_ENTITY.get());
        registerMachineEntityCaps(event, BlockEntityRegistry.LATEX_PURIFIER_ENTITY.get());
    }

    private static void registerMachineEntityCaps(RegisterCapabilitiesEvent event, BlockEntityType<? extends AbstractMachineEntity<?,?>> type){
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, type, (machine, side) ->
                machine.getCapability(Capabilities.ItemHandler.BLOCK, side));
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, type, (machine, side) ->
                machine.getCapability(Capabilities.EnergyStorage.BLOCK, side));
    }

    @SubscribeEvent
    public static void onAttributeModify(EntityAttributeModificationEvent event){
        event.getTypes().forEach((type)-> {
            if(!event.has(type, AIR_DECREASE_SPEED)) event.add(type, AIR_DECREASE_SPEED);
            if(!event.has(type, LATEX_RESISTANCE)) event.add(type, LATEX_RESISTANCE);
        });
    }

    @SubscribeEvent
    public static void onEntityAttributes(@NotNull EntityAttributeCreationEvent event){
        event.put(MILK_PUDDING.get(), MilkPuddingEntity.createAttributes().build());
        event.put(ROOMBA_ENTITY.get(), RoombaEntity.createAttributes().build());

        event.put(BEI_FENG.get(), LatexBeast.createAttributes().build());
        event.put(BENIGN.get(), LatexBeast.createAttributes().build());
        event.put(DARK_LATEX_WOLF_MALE.get(), LatexBeast.createAttributes().build());
        event.put(DARK_LATEX_WOLF_FEMALE.get(), LatexBeast.createAttributes().build());
        event.put(GAS_WOLF.get(), LatexBeast.createAttributes().build());

        event.put(HYPNO_CAT.get(), LatexBeast.createAttributes().build());

        event.put(LATEX_SHARK_FEMALE.get(), WaterLatexBeast.createAttributes().build());
        event.put(LATEX_SHARK_MALE.get(), WaterLatexBeast.createAttributes().build());

        event.put(PURE_WHITE_LATEX_WOLF.get(), LatexBeast.createAttributes().build());

        event.put(SNOW_LEOPARD_FEMALE.get(), LatexBeast.createAttributes().build());
        event.put(SNOW_LEOPARD_MALE.get(), LatexBeast.createAttributes().build());

        event.put(WHITE_LATEX_WOLF_MALE.get(), LatexBeast.createAttributes().build());
        event.put(WHITE_LATEX_WOLF_FEMALE.get(), LatexBeast.createAttributes().build());

        event.put(YUFENG_DRAGON.get(), LatexBeast.createAttributes().build());
//TMP DON'T FORGET TO REMOVE
        event.put(TEST.get(), Zombie.createAttributes().build());
    }

    @SubscribeEvent
    public static void onSpawnPlacementRegister(RegisterSpawnPlacementsEvent event){
        event.register(MILK_PUDDING.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MilkPuddingEntity::checkSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);

        event.register(BEI_FENG.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractLatexBeast::checkLatexBeastSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(DARK_LATEX_WOLF_FEMALE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LatexBeast::checkDarkLatexSpawn, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(DARK_LATEX_WOLF_MALE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LatexBeast::checkDarkLatexSpawn, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(GAS_WOLF.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractLatexBeast::checkLatexBeastSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);

        event.register(HYPNO_CAT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractLatexBeast::checkLatexBeastSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);

        event.register(LATEX_SHARK_FEMALE.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterLatexBeast::checkSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(LATEX_SHARK_MALE.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterLatexBeast::checkSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);

        event.register(PURE_WHITE_LATEX_WOLF.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LatexBeast::checkWhiteLatexSpawn, RegisterSpawnPlacementsEvent.Operation.OR);

        event.register(SNOW_LEOPARD_FEMALE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractLatexBeast::checkLatexBeastSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(SNOW_LEOPARD_MALE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractLatexBeast::checkLatexBeastSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);

        event.register(WHITE_LATEX_WOLF_FEMALE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LatexBeast::checkWhiteLatexSpawn, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(WHITE_LATEX_WOLF_MALE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LatexBeast::checkWhiteLatexSpawn, RegisterSpawnPlacementsEvent.Operation.OR);

        event.register(YUFENG_DRAGON.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractLatexBeast::checkLatexBeastSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
    }
}