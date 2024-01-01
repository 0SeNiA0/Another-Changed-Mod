package net.zaharenko424.a_changed.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.zaharenko424.a_changed.block.blocks.Keypad;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import net.zaharenko424.a_changed.registry.SoundRegistry;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;

@ParametersAreNonnullByDefault
public class KeypadEntity extends BlockEntity {

    private int[] code=new int[0];
    private boolean open=false;
    private int ticksUntilReset=200;

    public KeypadEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(BlockEntityRegistry.KEYPAD_ENTITY.get(), p_155229_, p_155230_);
    }

    public boolean isCodeSet(){
        return code.length>0;
    }

    public int codeLength(){
        return code.length;
    }

    public void tryCode(int[] attempt){
        if(open) return;
        level.playSound(null,worldPosition, SoundRegistry.BUTTON_PRESSED.get(), SoundSource.BLOCKS);
        if(Arrays.equals(code,attempt)){
            level.setBlockAndUpdate(worldPosition,getBlockState().setValue(Keypad.UNLOCKED,true));
            open=true;
            level.playSound(null,worldPosition, SoundRegistry.KEYPAD_UNLOCKED.get(), SoundSource.BLOCKS);
            return;
        }
        level.playSound(null,worldPosition, SoundRegistry.KEYPAD_WRONG_PASSWORD.get(), SoundSource.BLOCKS);
    }

    public void setCode(int[] code){
        if(this.code.length>0) return;
        this.code=code;
        setChanged();
    }

    public void tick(){
        if(!open) return;
        ticksUntilReset--;
        if(ticksUntilReset==0){
            ticksUntilReset=200;
            open=false;
            level.setBlockAndUpdate(worldPosition,getBlockState().setValue(Keypad.UNLOCKED,false));
        }
    }

    @Override
    public void load(CompoundTag p_155245_) {
        super.load(p_155245_);
        CompoundTag tag= TransfurManager.modTag(p_155245_);
        code=tag.getIntArray("code");
        open=tag.getBoolean("open");
        if(tag.contains("ticks")) ticksUntilReset=tag.getInt("ticks");
    }

    @Override
    protected void saveAdditional(CompoundTag p_187471_) {
        super.saveAdditional(p_187471_);
        CompoundTag tag=TransfurManager.modTag(p_187471_);
        tag.putIntArray("code",code);
        tag.putBoolean("open",open);
        if(ticksUntilReset!=200) tag.putInt("ticks",ticksUntilReset);
    }
}