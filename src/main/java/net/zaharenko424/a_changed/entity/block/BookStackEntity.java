package net.zaharenko424.a_changed.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.zaharenko424.a_changed.TransfurManager;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BookStackEntity extends BlockEntity {
    private final NonNullList<ItemStack> books=NonNullList.create();
    private final NonNullList<Book> books1=NonNullList.create();

    public BookStackEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(BlockEntityRegistry.BOOK_STACK_ENTITY.get(), p_155229_, p_155230_);
    }

    public boolean hasSpace(){
        return books.size()<8;
    }

    public boolean isEmpty(){
        return books.isEmpty();
    }

    public int bookAmount(){
        return books.size();
    }

    public NonNullList<Book> getBooks(){
        return books1;
    }

    public void addBook(@NotNull ItemStack book, int rotation, boolean shrink){
        books.add(book.copyWithCount(1));
        books1.add(new Book(Mth.DEG_TO_RAD*rotation,level.random.nextInt(0,4)));
        if(shrink) book.shrink(1);
        level.sendBlockUpdated(worldPosition,getBlockState(),getBlockState(), Block.UPDATE_ALL);
    }

    public ItemStack removeBook(){
        int i=books.size()-1;
        books1.remove(i);
        level.sendBlockUpdated(worldPosition,getBlockState(),getBlockState(), Block.UPDATE_ALL);
        return books.remove(i);
    }

    public void dropBooks(){
        books.forEach(book->Utils.dropItem(level,getBlockPos(),book));
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag=new CompoundTag();
        if(books1.isEmpty()) return tag;
        tag.putInt("Size",books1.size());
        Book book;
        for(int i=0;i<books1.size();i++){
            book=books1.get(i);
            tag.putFloat("rotation"+i,book.rotation);
            tag.putInt("modelId"+i,book.modelId);
        }
        return tag;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        handleUpdateTag(pkt.getTag());
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        books1.clear();
        if(!tag.contains("Size")) return;
        int size=tag.getInt("Size");
        float rotation;
        int modelId;
        for(int i=0;i<size;i++){
            rotation=tag.getFloat("rotation"+i);
            modelId=tag.getInt("modelId"+i);
            books1.add(new Book(rotation,modelId));
        }
        super.handleUpdateTag(tag);
    }

    @Override
    public void load(@NotNull CompoundTag p_155245_) {
        super.load(p_155245_);
        CompoundTag tag=TransfurManager.modTag(p_155245_);
        int size=tag.getInt("Size");
        ItemStack book;
        float rotation;
        int modelId;
        for(int i=0;i<size;i++){
            book=ItemStack.of(tag.getCompound("book"+i));
            rotation=tag.getFloat("rotation"+i);
            modelId=tag.getInt("modelId"+i);
            books.add(book);
            books1.add(new Book(rotation,modelId));
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag p_187471_) {
        super.saveAdditional(p_187471_);
        CompoundTag tag=TransfurManager.modTag(p_187471_);
        tag.putInt("Size",books1.size());
        Book book;
        for(int i=0;i<books1.size();i++){
            book=books1.get(i);
            tag.put("book"+i,books.get(i).save(new CompoundTag()));
            tag.putFloat("rotation"+i,book.rotation);
            tag.putInt("modelId"+i,book.modelId);
        }
    }
    @ApiStatus.Internal
    public record Book(float rotation, int modelId) {}
}