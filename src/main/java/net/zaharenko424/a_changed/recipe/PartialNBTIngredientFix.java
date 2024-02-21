package net.zaharenko424.a_changed.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class PartialNBTIngredientFix extends Ingredient {

    public static final PartialNBTIngredientFix EMPTY = new PartialNBTIngredientFix(Set.of(), null);

    public static final Codec<PartialNBTIngredientFix> CODEC = RecordCodecBuilder.create(
            builder -> builder
                    .group(
                            NeoForgeExtraCodecs.singularOrPluralCodec(BuiltInRegistries.ITEM.byNameCodec(), "item").forGetter(PartialNBTIngredientFix::getContainedItems),
                            CompoundTag.CODEC.optionalFieldOf("tag", new CompoundTag()).forGetter(PartialNBTIngredientFix::getTag))
                    .apply(builder, PartialNBTIngredientFix::new));

    public static final Codec<PartialNBTIngredientFix> CODEC_NONEMPTY = RecordCodecBuilder.create(
            builder -> builder
                    .group(
                            NeoForgeExtraCodecs.singularOrPluralCodecNotEmpty(BuiltInRegistries.ITEM.byNameCodec(), "item").forGetter(PartialNBTIngredientFix::getContainedItems),
                            CompoundTag.CODEC.optionalFieldOf("tag", new CompoundTag()).forGetter(PartialNBTIngredientFix::getTag))
                    .apply(builder, PartialNBTIngredientFix::new));

    protected PartialNBTIngredientFix(@NotNull Set<Item> items, @Nullable CompoundTag tag) {
        super(items.stream().map(item -> {
            ItemStack stack = new ItemStack(item);
            if(tag != null && !tag.isEmpty()) stack.setTag(tag);
            return new Ingredient.ItemValue(stack);
        }), NeoForgeMod.PARTIAL_NBT_INGREDIENT_TYPE);
    }

    /** Creates a new ingredient matching any item from the list, containing the given NBT */
    @Contract("_, _ -> new")
    public static @NotNull PartialNBTIngredientFix of(@NotNull CompoundTag nbt, @NotNull ItemLike... items) {
        return new PartialNBTIngredientFix(Arrays.stream(items).map(ItemLike::asItem).collect(Collectors.toSet()), nbt);
    }

    /** Creates a new ingredient matching the given item, containing the given NBT */
    @Contract("_, _ -> new")
    public static @NotNull PartialNBTIngredientFix of(@NotNull ItemLike item, @Nullable CompoundTag nbt) {
        return new PartialNBTIngredientFix(Set.of(item.asItem()), nbt);
    }

    @Override
    protected boolean areStacksEqual(@NotNull ItemStack left, @NotNull ItemStack right) {
        if(left.getItem() != right.getItem()) return false;

        CompoundTag leftTag = left.getTag();
        CompoundTag rightTag = right.getTag();

        if(leftTag == null || leftTag.isEmpty()) return true;
        if(rightTag == null || rightTag.isEmpty()) return false;

        if(!leftTag.getClass().equals(rightTag.getClass())) return false;

        return compareNBT(leftTag, rightTag);
    }

    public Set<Item> getContainedItems() {
        return Arrays.stream(getItems()).map(ItemStack::getItem).collect(Collectors.toSet());
    }

    public CompoundTag getTag() {
        final ItemStack[] items = getItems();
        if (items.length == 0)
            return new CompoundTag();

        return items[0].getOrCreateTag();
    }

    public boolean isSimple() {
        return false;
    }

    public void toNetwork0(@NotNull FriendlyByteBuf buf){
        if(isEmpty()) {
            buf.writeByte(0);
            return;
        }
        buf.writeByte(1);
        buf.writeNbt(getTag());
        buf.writeCollection(getContainedItems(), (b, item) -> b.writeId(BuiltInRegistries.ITEM, item));
    }

    public static @NotNull PartialNBTIngredientFix fromNetwork(@NotNull FriendlyByteBuf buf){
        if(buf.readByte() == 0) return EMPTY;
        CompoundTag tag = buf.readNbt();
        return new PartialNBTIngredientFix(buf.readCollection(HashSet::new, b -> b.readById(BuiltInRegistries.ITEM)), tag);
    }

    public static boolean compareNBT(Tag left, Tag right){
        if(left instanceof CompoundTag tag){
            CompoundTag tag1 = (CompoundTag)right;

            for(String s : tag.getAllKeys()) {
                Tag tag2 = tag.get(s);
                if (!compareNBT(tag2, tag1.get(s))) {
                    return false;
                }
            }

            return true;
        } else if(left instanceof ListTag list){
            ListTag list1 = (ListTag)right;
            if (list.isEmpty()) {
                return list1.isEmpty();
            }

            for(Tag tag : list) {
                boolean flag = false;

                for(Tag tag1 : list1) {
                    if (!compareNBT(tag, tag1)) {
                        flag = true;
                        break;
                    }
                }

                if (!flag) {
                    return false;
                }
            }

            return true;
        }
        return left.equals(right);
    }
}