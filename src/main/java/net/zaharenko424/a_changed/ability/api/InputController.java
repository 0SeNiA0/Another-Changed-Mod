package net.zaharenko424.a_changed.ability.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.ability.network.packets.BidirectionalInputPacket;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
public class InputController {

    protected final LivingEntity holder;
    protected final BooleanSupplier keyChecker;
    protected final Consumer<InputController> onInput;

    protected boolean lastInput;
    protected int holdingFor;
    protected boolean consumed;

    public InputController(LivingEntity holder, BooleanSupplier keyChecker, Consumer<InputController> onInput){
        this.holder = holder;
        this.keyChecker = keyChecker;
        this.onInput = onInput;
    }

    public boolean isDown(){
        return lastInput;
    }

    public int getHoldingFor(){
        return holdingFor;
    }

    public boolean consumeInput(){
        if(consumed || !lastInput) return false;

        consumed = true;
        return true;
    }

    public void inputTick(){
        boolean input = keyChecker.getAsBoolean();

        if(input == lastInput) return;
        sendData(buf -> buf.writeBoolean(input), 1);
    }

    public void simulateInput(boolean input){
        if(lastInput == input) return;

        if(lastInput) {
            holdingFor = 0;
            consumed = false;
        }

        lastInput = input;
        onInput.accept(this);
        sendData(this::send, 4);
    }

    public void tick(){
        if(lastInput) {
            holdingFor++;
            sendData(this::send, 4);
        }
    }

    public void send(FriendlyByteBuf buf){
        buf.writeBoolean(lastInput).writeVarInt(holdingFor);
    }

    public void handleData(FriendlyByteBuf data) {
        if(holder.level().isClientSide) {
            lastInput = data.readBoolean();
            holdingFor = data.readVarInt();
            return;
        }

        simulateInput(data.readBoolean());
    }

    public void save(CompoundTag tag){
        if(lastInput){
            tag.putBoolean("lastInput", true);
            tag.putInt("holdingFor", holdingFor);
        }
    }

    public void load(CompoundTag tag){
        if(tag.contains("lastInput")) {
            lastInput = true;
            holdingFor = tag.getInt("holdingFor");
            return;
        }

        lastInput = false;
        holdingFor = 0;
    }

    protected void sendData(Consumer<FriendlyByteBuf> dataWriter){
        sendData(dataWriter, 256);
    }

    protected void sendData(Consumer<FriendlyByteBuf> dataWriter, int expectedSize){
        if(!(holder instanceof Player player)) return;

        CustomPacketPayload payload = new BidirectionalInputPacket(dataWriter, expectedSize);
        if(player.level().isClientSide){
            PacketDistributor.sendToServer(payload);
        } else
            PacketDistributor.sendToPlayer((ServerPlayer) player, payload);
    }
}
