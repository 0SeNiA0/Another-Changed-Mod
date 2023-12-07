package net.zaharenko424.testmod.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.testmod.TestMod;
import net.zaharenko424.testmod.block.blockEntity.BookStackEntity;
import net.zaharenko424.testmod.block.blockEntity.LatexContainerEntity;
import net.zaharenko424.testmod.block.blockEntity.NoteEntity;

public class BlockEntityRegistry {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, TestMod.MODID);

    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BookStackEntity>> BOOK_STACK_ENTITY = BLOCK_ENTITIES
            .register("book_stack", ()-> BlockEntityType.Builder.of(BookStackEntity::new,BlockRegistry.BOOK_STACK.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<LatexContainerEntity>> LATEX_CONTAINER_ENTITY = BLOCK_ENTITIES
            .register("latex_container", ()-> BlockEntityType.Builder.of(LatexContainerEntity::new,BlockRegistry.LATEX_CONTAINER.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<NoteEntity>> NOTE_ENTITY = BLOCK_ENTITIES
            .register("note", ()-> BlockEntityType.Builder.of(NoteEntity::new,BlockRegistry.NOTE.get()).build(null));
}