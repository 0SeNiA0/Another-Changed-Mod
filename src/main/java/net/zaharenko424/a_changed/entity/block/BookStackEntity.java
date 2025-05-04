package net.zaharenko424.a_changed.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import net.zaharenko424.a_changed.util.NBTUtils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BookStackEntity extends AbstractStackEntity {

    public BookStackEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.BOOK_STACK_ENTITY.get(), pos, state);
    }

    public boolean hasSpace(){
        return size() < 8;
    }

    @Override
    protected boolean isItemValid(ItemStack stack) {
        return stack.is(ItemTags.BOOKSHELF_BOOKS);
    }

    @Override
    protected Entry makeEntry(@NotNull ItemStack stack, float headRotDeg) {
        return new Entry(Mth.DEG_TO_RAD * (-headRotDeg), level.random.nextInt(0,4));
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookup) {
        if(tag.contains(NBTUtils.KEY)) dataFix(tag);
        super.loadAdditional(tag, lookup);
    }
//keep for a few updates so that all old book stacks will be updated
    void dataFix(CompoundTag tag){
        CompoundTag modTag = NBTUtils.modTag(tag);
        int size = modTag.getInt("Size");
        tag.putInt("size", size);

        for (int i = 0; i < size; i++) {
            tag.put("item" + i, modTag.getCompound("book" + i));
            tag.putFloat("rotation" + i, modTag.getFloat("rotation" + i) - Mth.PI);
            tag.putInt("modelId" + i, modTag.getInt("modelId" + i));
        }
        tag.remove(NBTUtils.KEY);
    }
}