package net.zaharenko424.testmod.network.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.NetworkEvent;
import net.neoforged.neoforge.network.simple.SimpleMessage;
import net.zaharenko424.testmod.TestMod;
import net.zaharenko424.testmod.block.blockEntity.KeypadEntity;
import org.jetbrains.annotations.NotNull;

public class ServerboundTryPasswordPacket implements SimpleMessage {

    private final int[] attempt;
    private final BlockPos pos;

    public ServerboundTryPasswordPacket(int[] attempt,@NotNull BlockPos pos){
        this.attempt=attempt;
        this.pos=pos;
    }

    public ServerboundTryPasswordPacket(@NotNull FriendlyByteBuf buffer){
        attempt=buffer.readNbt().getIntArray("code");
        pos=buffer.readBlockPos();
    }

    @Override
    public void encode(@NotNull FriendlyByteBuf buffer) {
        CompoundTag tag=new CompoundTag();
        tag.putIntArray("code",attempt);
        buffer.writeNbt(tag);
        buffer.writeBlockPos(pos);
    }

    @Override
    public void handleMainThread(NetworkEvent.@NotNull Context context) {
        ServerPlayer sender=context.getSender();
        if(sender==null) {
            TestMod.LOGGER.warn("Received a packet from player which is not on the server!");
            return;
        }
        BlockEntity entity=sender.level().getBlockEntity(pos);
        if(entity instanceof KeypadEntity keypad){
            if(keypad.isCodeSet()){
                keypad.tryCode(attempt);
                return;
            }
            keypad.setCode(attempt);
            return;
        }
        TestMod.LOGGER.warn("Block position does not contain KeypadEntity! ("+pos+")");
    }
}