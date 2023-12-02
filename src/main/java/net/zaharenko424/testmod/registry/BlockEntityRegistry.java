package net.zaharenko424.testmod.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.testmod.TestMod;
import net.zaharenko424.testmod.block.blockEntity.BookStackEntity;

public class BlockEntityRegistry {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, TestMod.MODID);

    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BookStackEntity>> BOOK_STACK_ENTITY = BLOCK_ENTITIES
            .register("book_stack", ()-> BlockEntityType.Builder.of(BookStackEntity::new,BlockRegistry.BOOK_STACK.get()).build(null));
}