package net.zaharenko424.a_changed.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.entity.block.*;

public class BlockEntityRegistry {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, AChanged.MODID);

    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<AirConditionerEntity>> AIR_CONDITIONER_ENTITY = BLOCK_ENTITIES
            .register("air_conditioner", () -> BlockEntityType.Builder.of(AirConditionerEntity::new, BlockRegistry.AIR_CONDITIONER.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BookStackEntity>> BOOK_STACK_ENTITY = BLOCK_ENTITIES
            .register("book_stack", () -> BlockEntityType.Builder.of(BookStackEntity::new, BlockRegistry.BOOK_STACK.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BoxPileEntity>> BOX_PILE_ENTITY = BLOCK_ENTITIES
            .register("box_pile", () -> BlockEntityType.Builder.of(BoxPileEntity::new, BlockRegistry.SMALL_CARDBOARD_BOX.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<GasTankEntity>> GAS_TANK_ENTITY = BLOCK_ENTITIES
            .register("gas_canister", () -> BlockEntityType.Builder.of(GasTankEntity::new, BlockRegistry.GAS_TANK.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<KeypadEntity>> KEYPAD_ENTITY = BLOCK_ENTITIES
            .register("keypad", () -> BlockEntityType.Builder.of(KeypadEntity::new,BlockRegistry.KEYPAD.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<LaserEmitterEntity>> LASER_EMITTER_ENTITY = BLOCK_ENTITIES
            .register("laser_emitter", () -> BlockEntityType.Builder.of(LaserEmitterEntity::new, BlockRegistry.LASER_EMITTER.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<LatexContainerEntity>> LATEX_CONTAINER_ENTITY = BLOCK_ENTITIES
            .register("latex_container", () -> BlockEntityType.Builder.of(LatexContainerEntity::new, BlockRegistry.LATEX_CONTAINER.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<NoteEntity>> NOTE_ENTITY = BLOCK_ENTITIES
            .register("note", () -> BlockEntityType.Builder.of(NoteEntity::new, BlockRegistry.NOTE.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<SmartSewageEntity>> SMART_SEWAGE_ENTITY = BLOCK_ENTITIES
            .register("smart_sewage", () -> BlockEntityType.Builder.of(SmartSewageEntity::new, BlockRegistry.SMART_SEWAGE_SYSTEM.get()).build(null));
}