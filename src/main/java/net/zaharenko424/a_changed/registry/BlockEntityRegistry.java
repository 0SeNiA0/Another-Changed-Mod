package net.zaharenko424.a_changed.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.entity.block.*;
import net.zaharenko424.a_changed.entity.block.machines.*;
import net.zaharenko424.a_changed.entity.block.sign.HangingSignEntity;
import net.zaharenko424.a_changed.entity.block.sign.SignEntity;

import static net.zaharenko424.a_changed.registry.BlockRegistry.*;

@SuppressWarnings("ConstantConditions")//suppressing all the .build(null)
public class BlockEntityRegistry {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, AChanged.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PileOfOrangesEntity>> PILE_OF_ORANGES_ENTITY = BLOCK_ENTITIES
            .register("pile_of_oranges", () -> BlockEntityType.Builder.of(PileOfOrangesEntity::new, PILE_OF_ORANGES.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AirConditionerEntity>> AIR_CONDITIONER_ENTITY = BLOCK_ENTITIES
            .register("air_conditioner", () -> BlockEntityType.Builder.of(AirConditionerEntity::new, AIR_CONDITIONER.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BookStackEntity>> BOOK_STACK_ENTITY = BLOCK_ENTITIES
            .register("book_stack", () -> BlockEntityType.Builder.of(BookStackEntity::new, BOOK_STACK.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BoxPileEntity>> BOX_PILE_ENTITY = BLOCK_ENTITIES
            .register("box_pile", () -> BlockEntityType.Builder.of(BoxPileEntity::new, SMALL_CARDBOARD_BOX.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CannedOrangesEntity>> CANNED_ORANGES_ENTITY = BLOCK_ENTITIES
            .register("canned_oranges", ()-> BlockEntityType.Builder.of(CannedOrangesEntity::new, CANNED_ORANGES.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CapacitorEntity>> CAPACITOR_ENTITY = BLOCK_ENTITIES
            .register("capacitor", () -> BlockEntityType.Builder.of(CapacitorEntity::new, CAPACITOR.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CompressorEntity>> COMPRESSOR_ENTITY = BLOCK_ENTITIES
            .register("compressor", () -> BlockEntityType.Builder.of(CompressorEntity::new, COMPRESSOR.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CopperWireEntity>> COPPER_WIRE_ENTITY = BLOCK_ENTITIES
            .register("copper_wire", ()-> BlockEntityType.Builder.of(CopperWireEntity::new, COPPER_WIRE.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CryoChamberEntity>> CRYO_CHAMBER_ENTITY = BLOCK_ENTITIES
            .register("cryo_chamber", ()-> BlockEntityType.Builder.of(CryoChamberEntity::new, CRYO_CHAMBER.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DNAExtractorEntity>> DNA_EXTRACTOR_ENTITY = BLOCK_ENTITIES
            .register("dna_extractor", ()-> BlockEntityType.Builder.of(DNAExtractorEntity::new, DNA_EXTRACTOR.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GasTankEntity>> GAS_TANK_ENTITY = BLOCK_ENTITIES
            .register("gas_canister", () -> BlockEntityType.Builder.of(GasTankEntity::new, GAS_TANK.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GeneratorEntity>> GENERATOR_ENTITY = BLOCK_ENTITIES
            .register("generator", () -> BlockEntityType.Builder.of(GeneratorEntity::new, GENERATOR.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<HangingSignEntity>> HANGING_SIGN_ENTITY = BLOCK_ENTITIES
            .register("hanging_sign", () -> BlockEntityType.Builder.of(HangingSignEntity::new, ORANGE_HANGING_SIGN.get(), ORANGE_WALL_HANGING_SIGN.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<KeypadEntity>> KEYPAD_ENTITY = BLOCK_ENTITIES
            .register("keypad", () -> BlockEntityType.Builder.of(KeypadEntity::new, KEYPAD.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LaserEmitterEntity>> LASER_EMITTER_ENTITY = BLOCK_ENTITIES
            .register("laser_emitter", () -> BlockEntityType.Builder.of(LaserEmitterEntity::new, LASER_EMITTER.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LatexContainerEntity>> LATEX_CONTAINER_ENTITY = BLOCK_ENTITIES
            .register("latex_container", () -> BlockEntityType.Builder.of(LatexContainerEntity::new, LATEX_CONTAINER.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LatexEncoderEntity>> LATEX_ENCODER_ENTITY = BLOCK_ENTITIES
            .register("latex_encoder", ()-> BlockEntityType.Builder.of(LatexEncoderEntity::new, LATEX_ENCODER.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LatexPurifierEntity>> LATEX_PURIFIER_ENTITY = BLOCK_ENTITIES
            .register("latex_purifier", () -> BlockEntityType.Builder.of(LatexPurifierEntity::new, LATEX_PURIFIER.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<NoteEntity>> NOTE_ENTITY = BLOCK_ENTITIES
            .register("note", () -> BlockEntityType.Builder.of(NoteEntity::new, NOTE.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SignEntity>> SIGN_ENTITY = BLOCK_ENTITIES
            .register("sign", ()-> BlockEntityType.Builder.of(SignEntity::new, ORANGE_SIGN.get(), ORANGE_WALL_SIGN.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SmartSewageEntity>> SMART_SEWAGE_ENTITY = BLOCK_ENTITIES
            .register("smart_sewage", () -> BlockEntityType.Builder.of(SmartSewageEntity::new, SMART_SEWAGE_SYSTEM.get()).build(null));
}