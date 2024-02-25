package net.zaharenko424.a_changed.event;

import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.SpawnPlacementRegisterEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.entity.AbstractLatexBeast;
import net.zaharenko424.a_changed.entity.LatexBeast;
import net.zaharenko424.a_changed.network.ClientPacketHandler;
import net.zaharenko424.a_changed.network.ServerPacketHandler;
import net.zaharenko424.a_changed.network.packets.*;
import net.zaharenko424.a_changed.network.packets.grab.*;
import net.zaharenko424.a_changed.network.packets.transfur.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.zaharenko424.a_changed.AChanged.*;
import static net.zaharenko424.a_changed.registry.EntityRegistry.*;
@ParametersAreNonnullByDefault
@Mod.EventBusSubscriber(modid = AChanged.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonMod {

    @SubscribeEvent
    public static void onRegisterPayload(RegisterPayloadHandlerEvent event){
        IPayloadRegistrar registrar = event.registrar(MODID);

        //Transfur tolerance update
        registrar.common(ClientboundTransfurToleranceSyncPacket.ID, ClientboundTransfurToleranceSyncPacket::new, handler ->
                handler.client((packet, context) -> ClientPacketHandler.INSTANCE.handleTransfurToleranceSync(packet)));

        //Grab
        registrar.common(ClientboundGrabSyncPacket.ID, ClientboundGrabSyncPacket::new, handler ->
                handler.client((packet, context) -> ClientPacketHandler.INSTANCE.handleGrabSyncPacket(packet)));
        registrar.play(ClientboundRemoteGrabSyncPacket.ID, ClientboundRemoteGrabSyncPacket::new, handler ->
                handler.client((packet, context) -> ClientPacketHandler.INSTANCE.handleRemoteGrabSyncPacket(packet)));
        registrar.play(ServerboundGrabPacket.ID, ServerboundGrabPacket::new, handler ->
                handler.server(ServerPacketHandler.INSTANCE::handleGrabPacket));

        //Grab modes
        registrar.play(ServerboundGrabModePacket.ID, ServerboundGrabModePacket::new, handler ->
                handler.server(ServerPacketHandler.INSTANCE::handleGrabModePacket));
        registrar.play(ServerboundWantToBeGrabbedPacket.ID, ServerboundWantToBeGrabbedPacket::new, handler ->
                handler.server(ServerPacketHandler.INSTANCE::handleWantToBeGrabbedPacket));

        //Transfur data
        registrar.play(ClientboundPlayerTransfurSyncPacket.ID, ClientboundPlayerTransfurSyncPacket::new, handler ->
                handler.client((packet, context) -> ClientPacketHandler.INSTANCE.handlePlayerTransfurSync(packet)));
        registrar.play(ClientboundRemotePlayerTransfurSyncPacket.ID, ClientboundRemotePlayerTransfurSyncPacket::new, handler ->
                handler.client((packet, context) -> ClientPacketHandler.INSTANCE.handleRemotePlayerTransfurSync(packet)));

        //Transfur screen
        registrar.play(ClientboundOpenTransfurScreenPacket.ID, a -> new ClientboundOpenTransfurScreenPacket(), handler ->
                handler.client((packet, context) -> ClientPacketHandler.INSTANCE.handleOpenTransfurScreen()));
        registrar.play(ServerboundTransfurChoicePacket.ID, ServerboundTransfurChoicePacket::new, handler ->
                handler.server(ServerPacketHandler.INSTANCE::handleTransfurChoicePacket));

        //Latex encoder
        registrar.play(ServerboundLatexEncoderScreenPacket.ID, ServerboundLatexEncoderScreenPacket::new, handler ->
                handler.server(ServerPacketHandler.INSTANCE::handleLatexEncoderScreenPacket));

        //Note
        registrar.play(ClientboundOpenNotePacket.ID, ClientboundOpenNotePacket::new, handler ->
                handler.client((packet, context) -> ClientPacketHandler.INSTANCE.handleOpenNotePacket(packet)));
        registrar.play(ServerboundEditNotePacket.ID, ServerboundEditNotePacket::new, handler ->
                handler.server(ServerPacketHandler.INSTANCE::handleEditNotePacket));

        //Keypad
        registrar.play(ClientboundOpenKeypadPacket.ID, ClientboundOpenKeypadPacket::new, handler ->
                handler.client((packet, context) -> ClientPacketHandler.INSTANCE.handleOpenKeypadPacket(packet)));
        registrar.play(ServerboundTryPasswordPacket.ID, ServerboundTryPasswordPacket::new, handler ->
                handler.server(ServerPacketHandler.INSTANCE::handleTryPasswordPacket));
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
        event.put(BENIGN.get(), LatexBeast.createAttributes().build());
        event.put(DARK_LATEX_WOLF_MALE.get(), LatexBeast.createAttributes().build());
        event.put(DARK_LATEX_WOLF_FEMALE.get(), LatexBeast.createAttributes().build());
        event.put(GAS_WOLF.get(), LatexBeast.createAttributes().build());
        event.put(PURE_WHITE_LATEX_WOLF.get(), LatexBeast.createAttributes().build());
        event.put(WHITE_LATEX_WOLF_MALE.get(), LatexBeast.createAttributes().build());
        event.put(WHITE_LATEX_WOLF_FEMALE.get(), LatexBeast.createAttributes().build());
    }

    @SubscribeEvent
    public static void onSpawnPlacementRegister(SpawnPlacementRegisterEvent event){
        event.register(BEI_FENG.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, AbstractLatexBeast::checkLatexBeastSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(DARK_LATEX_WOLF_FEMALE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, AbstractLatexBeast::checkLatexBeastSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(DARK_LATEX_WOLF_MALE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, AbstractLatexBeast::checkLatexBeastSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(GAS_WOLF.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, AbstractLatexBeast::checkLatexBeastSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(PURE_WHITE_LATEX_WOLF.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, AbstractLatexBeast::checkLatexBeastSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(WHITE_LATEX_WOLF_FEMALE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, AbstractLatexBeast::checkLatexBeastSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(WHITE_LATEX_WOLF_MALE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, AbstractLatexBeast::checkLatexBeastSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
    }
}