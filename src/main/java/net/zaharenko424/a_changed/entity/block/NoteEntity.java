package net.zaharenko424.a_changed.entity.block;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import net.zaharenko424.a_changed.util.NBTUtils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class NoteEntity extends BlockEntity {

    private List<String> text = new ArrayList<>();
    private boolean finalized = false;

    public NoteEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.NOTE_ENTITY.get(), pos, state);
    }

    public boolean isFinalized(){
        return finalized;
    }

    public ImmutableList<String> getText(){
        return ImmutableList.copyOf(text);
    }

    public void setText(List<String> text, boolean finalize){
        if(finalized) return;
        this.text = text;
        finalized = finalize;
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.@NotNull Provider lookup) {
        super.loadAdditional(tag, lookup);
        CompoundTag modTag = NBTUtils.modTag(tag);
        NBTUtils.readFromTag(modTag,text);
        finalized = modTag.getBoolean("finalized");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.@NotNull Provider lookup) {
        super.saveAdditional(tag, lookup);
        CompoundTag modTag = NBTUtils.modTag(tag);
        NBTUtils.writeToTag(modTag, text);
        modTag.putBoolean("finalized", finalized);
    }
}