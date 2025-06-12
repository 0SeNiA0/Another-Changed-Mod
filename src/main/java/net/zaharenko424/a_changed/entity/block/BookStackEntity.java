package net.zaharenko424.a_changed.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import net.zaharenko424.a_changed.util.NBTUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class BookStackEntity extends AbstractStackEntity implements StructureRandomizable {

    protected ResourceKey<LootTable> lootTable;
    protected long lootTableSeed;

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
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookup) {
        super.saveAdditional(tag, lookup);
        trySaveLootTable(tag);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookup) {
        if(tag.contains(NBTUtils.KEY)) dataFix(tag);
        super.loadAdditional(tag, lookup);
        tryLoadLootTable(tag);
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

    @Override
    public void unpackLootTable() {
        if(getLootTable() == null || level == null) return;

        MinecraftServer server = level.getServer();
        if(server == null) return;

        LootTable loottable = server.reloadableRegistries().getLootTable(getLootTable());

        setLootTable(null);
        List<ItemStack> items = loottable.getRandomItems(new LootParams.Builder((ServerLevel) level)
                .withParameter(LootContextParams.ORIGIN, getBlockPos().getCenter())
                .create(LootContextParamSet.builder().required(LootContextParams.ORIGIN).build()), getLootTableSeed());
        setLootTableSeed(0);

        this.items.clear();
        this.entries.clear();

        for(ItemStack stack : items){
            if(!hasSpace()) return;
            if(!isItemValid(stack)) continue;

            while(!stack.isEmpty() && hasSpace()){
                addItem(stack, (level.random.nextFloat() - .5f) * 180, true);
            }
        }
    }
}