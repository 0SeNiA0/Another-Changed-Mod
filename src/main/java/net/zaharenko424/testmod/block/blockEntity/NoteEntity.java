package net.zaharenko424.testmod.block.blockEntity;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.zaharenko424.testmod.TransfurManager;
import net.zaharenko424.testmod.registry.BlockEntityRegistry;
import net.zaharenko424.testmod.util.Utils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class NoteEntity extends BlockEntity {

    private List<String> text=new ArrayList<>();
    private boolean finalized=false;

    public NoteEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(BlockEntityRegistry.NOTE_ENTITY.get(), p_155229_, p_155230_);
    }

    public boolean isFinalized(){
        return finalized;
    }

    public ImmutableList<String> getText(){
        return ImmutableList.copyOf(text);
    }

    public void setText(List<String> text,boolean finalize){
        if(finalized) return;
        this.text=text;
        finalized=finalize;
    }

    @Override
    public void load(CompoundTag p_155245_) {
        super.load(p_155245_);
        CompoundTag modTag=TransfurManager.modTag(p_155245_);
        Utils.readFromTag(modTag,text);
        finalized=modTag.getBoolean("finalized");
    }

    @Override
    protected void saveAdditional(CompoundTag p_187471_) {
        super.saveAdditional(p_187471_);
        CompoundTag modTag=TransfurManager.modTag(p_187471_);
        Utils.writeToTag(modTag,text);
        modTag.putBoolean("finalized",finalized);
    }
}