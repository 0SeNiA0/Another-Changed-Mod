package net.zaharenko424.a_changed.entity.block;

import net.minecraft.core.BlockPos;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PileOfOrangesEntity extends BlockEntity {

    private static final float OFFSET = 2 / 16f;
    private final List<Pair<Orange, AABB>> oranges = new ArrayList<>();
    private boolean changed = false;
    private VoxelShape cache;

    public PileOfOrangesEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.PILE_OF_ORANGES_ENTITY.get(), pPos, pBlockState);
    }

    public boolean isEmpty(){
        return oranges.isEmpty();
    }

    public List<Pair<Orange, AABB>> getOranges() {
        return oranges;
    }

    /**
     * Only use on server!
     */
    public boolean addOrange(@NotNull Vec3 pos, int headRot){
        Vec3 relative = relative(pos);
        VoxelShape shape = makeShape(relative);
        AABB test = shape.bounds();
        if(cache != null && oranges.stream().anyMatch(pair -> pair.getValue().intersects(test))) return false;
        Orange orange = new Orange(relative, Mth.DEG_TO_RAD * (-headRot + 180), shape);
        oranges.add(Pair.of(orange, test));
        changed = true;
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        setChanged();
        return true;
    }

    private static @NotNull VoxelShape makeShape(@NotNull Vec3 vec){
        return Shapes.box(vec.x - OFFSET, vec.y, vec.z - OFFSET, vec.x + OFFSET, vec.y + OFFSET + OFFSET, vec.z + OFFSET);
    }

    private Vec3 relative(Vec3 abs){
        return new Vec3(abs.x - worldPosition.getX(), abs.y - worldPosition.getY(), abs.z - worldPosition.getZ());
    }

    /**
     * Only use on server!
     */
    public ItemStack removeOrange(Vec3 pos){
        Vec3 relative = relative(pos);
        Pair<Orange, AABB> target = null;
        for(Pair<Orange, AABB> pair : oranges){
            if(!pair.getValue().inflate(.01).contains(relative)) continue;
            target = pair;
            break;
        }
        if(target == null) return ItemStack.EMPTY;
        oranges.remove(target);
        changed = true;
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        setChanged();
        return ItemRegistry.ORANGE_ITEM.get().getDefaultInstance();
    }

    public VoxelShape getFinalShape(){
        if(oranges.isEmpty()) return Shapes.empty();
        if(!changed) return cache;
        changed = false;
        cache = oranges.get(0).getKey().shape;
        for(int i = 1; i < oranges.size(); i++){
            cache = Shapes.or(cache, oranges.get(i).getKey().shape);
        }
        return cache;
    }

    public void onRemove(){
        if(oranges.isEmpty()) return;
        Block.popResource(level, worldPosition, ItemRegistry.ORANGE_ITEM.toStack(oranges.size()));
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        save(tag);
        return tag;
    }

    @Override
    public void onDataPacket(@NotNull Connection net, @NotNull ClientboundBlockEntityDataPacket pkt) {
        handleUpdateTag(pkt.getTag());
    }

    @Override
    public void handleUpdateTag(@NotNull CompoundTag tag) {
        int size = tag.getInt("Size");
        oranges.clear();
        for(int i = 0; i < size; i++){
            oranges.add(Pair.of(new Orange(tag.getCompound("i" + i)), null));
        }
        changed = true;
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        int size = tag.getInt("Size");
        oranges.clear();
        for(int i = 0; i < size; i++){
            Orange orange = new Orange(tag.getCompound("i" + i));
            oranges.add(Pair.of(orange, orange.shape.bounds()));
        }
        changed = true;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        save(pTag);
    }

    void save(@NotNull CompoundTag tag){
        int size = oranges.size();
        tag.putInt("Size", size);
        int i = 0;
        CompoundTag tag1;
        for(Pair<Orange, AABB> pair : oranges){
            tag1 = new CompoundTag();
            pair.getKey().save(tag1);
            tag.put("i" + i, tag1);
            i++;
        }
    }

    public record Orange(Vec3 pos, float rotRad, VoxelShape shape){

        Orange(Vec3 pos, float rotRad){
            this(pos, rotRad, makeShape(pos));
        }

        Orange(@NotNull CompoundTag tag){
            this(new Vec3(tag.getFloat("x"), tag.getFloat("y"), tag.getFloat("z")), tag.getFloat("rot"));
        }

        void save(@NotNull CompoundTag tag){
            tag.putFloat("x", (float) pos.x);
            tag.putFloat("y", (float) pos.y);
            tag.putFloat("z", (float) pos.z);
            tag.putFloat("rot", rotRad);
        }
    }
}