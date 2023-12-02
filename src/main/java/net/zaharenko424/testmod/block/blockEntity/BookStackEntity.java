package net.zaharenko424.testmod.block.blockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.zaharenko424.testmod.TransfurManager;
import net.zaharenko424.testmod.registry.BlockEntityRegistry;
import net.zaharenko424.testmod.util.Utils;
import org.jetbrains.annotations.NotNull;

public class BookStackEntity extends BlockEntity {
    private final NonNullList<ItemStack> books=NonNullList.create();

    public BookStackEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(BlockEntityRegistry.BOOK_STACK_ENTITY.get(), p_155229_, p_155230_);
    }

    public boolean hasSpace(){
        return books.size()<4;
    }

    public boolean isEmpty(){
        return books.isEmpty();
    }

    public int bookAmount(){
        return books.size();
    }

    public void addBook(@NotNull ItemStack book,boolean shrink){
        books.add(book.copyWithCount(1));
        if(shrink) book.shrink(1);
    }

    public ItemStack removeBook(){
        return books.remove(books.size()-1);
    }

    public void dropBooks(){
        books.forEach(book->Utils.dropItem(level,getBlockPos(),book));
    }

    @Override
    public void load(@NotNull CompoundTag p_155245_) {
        super.load(p_155245_);
        Utils.loadAllItems(TransfurManager.modTag(p_155245_),books);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag p_187471_) {
        super.saveAdditional(p_187471_);
        Utils.saveAllItems(TransfurManager.modTag(p_187471_),books);
    }
}