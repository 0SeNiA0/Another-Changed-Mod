package net.zaharenko424.a_changed.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.util.NBTUtils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BoxPileEntity extends BlockEntity {

    private int boxes = 1;

    public BoxPileEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(BlockEntityRegistry.BOX_PILE_ENTITY.get(), p_155229_, p_155230_);
    }

    public boolean hasSpace(){
        return boxes < 3;
    }

    public boolean isEmpty(){
        return boxes == 0;
    }

    public int boxAmount(){
        return boxes;
    }

    public void addBox(@NotNull ItemStack box, boolean shrink){
        boxes++;
        if(shrink) box.shrink(1);
    }

    public ItemStack removeBox(){
        boxes--;
        return new ItemStack(ItemRegistry.SMALL_CARDBOARD_BOX_ITEM.get());
    }

    public void dropBoxes(){
        if(boxes > 0) Block.popResource(level, worldPosition, new ItemStack(ItemRegistry.SMALL_CARDBOARD_BOX_ITEM.get(), boxes));
    }

    @Override
    public void loadAdditional(CompoundTag p_155245_, HolderLookup.@NotNull Provider lookup) {
        super.loadAdditional(p_155245_, lookup);
        boxes = NBTUtils.modTag(p_155245_).getInt("boxes");
    }

    @Override
    protected void saveAdditional(CompoundTag p_187471_, HolderLookup.@NotNull Provider lookup) {
        super.saveAdditional(p_187471_, lookup);
        NBTUtils.modTag(p_187471_).putInt("boxes", boxes);
    }
}