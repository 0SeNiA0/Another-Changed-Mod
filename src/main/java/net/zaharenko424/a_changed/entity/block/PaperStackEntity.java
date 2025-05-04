package net.zaharenko424.a_changed.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import org.jetbrains.annotations.NotNull;

public class PaperStackEntity extends AbstractStackEntity {

    public PaperStackEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.PAPER_STACK_ENTITY.get(), pos, blockState);
    }

    @Override
    public boolean hasSpace() {
        return size() < 32;
    }

    @Override
    protected boolean isItemValid(ItemStack stack) {
        return stack.is(Items.PAPER);
    }

    @Override
    protected Entry makeEntry(@NotNull ItemStack stack, float headRotDeg) {
        return new Entry(Mth.DEG_TO_RAD * (-headRotDeg), 0);
    }

    public void write(){
        if(isEmpty()) return;

        Entry last = entries.get(size() - 1);
        if(last.modelId() == 3) return;
        entries.set(size() - 1, new Entry(last.rotation(), last.modelId() + 1));
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        setChanged();
    }

    public void erase(){
        if(isEmpty()) return;

        Entry last = entries.get(size() - 1);
        if(last.modelId() == 0) return;
        entries.set(size() - 1, new Entry(last.rotation(), 0));
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        setChanged();
    }
}
