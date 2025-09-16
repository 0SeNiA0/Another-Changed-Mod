package net.zaharenko424.a_changed.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PaperStackEntity extends AbstractStackEntity implements StructureRandomizable {

    protected ResourceKey<LootTable> lootTable;
    protected long lootTableSeed;

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

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookup) {
        super.saveAdditional(tag, lookup);
        trySaveLootTable(tag);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookup) {
        super.loadAdditional(tag, lookup);
        tryLoadLootTable(tag);
    }

    @Override
    public @Nullable ResourceKey<LootTable> getLootTable() {
        return lootTable;
    }

    @Override
    public void setLootTable(@Nullable ResourceKey<LootTable> lootTable) {
        this.lootTable = lootTable;
    }

    @Override
    public long getLootTableSeed() {
        return lootTableSeed;
    }

    @Override
    public void setLootTableSeed(long seed) {
        lootTableSeed = seed;
    }

    private static final RandomSource rand = RandomSource.create();

    @Override
    public void unpackLootTable() {
        if(getLootTable() == null || level == null) return;

        rand.setSeed(getLootTableSeed());

        int size = rand.nextIntBetweenInclusive(1, getLootTable().location().getPath().equalsIgnoreCase("small") ? 4 : 12);
        setLootTable(null);
        int write;

        this.entries.clear();
        this.items.clear();

        for(int i = 0; i < size; i++){
            addItem(Items.PAPER.getDefaultInstance(), (rand.nextFloat() - .5f) * 180, false);
            write = rand.nextInt(3);
            for(int ii = 0; ii < write; ii++){
                write();
            }
        }
    }
}
