package net.zaharenko424.a_changed.util;

import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class Utils {

    public static final ResourceLocation NULL_LOC = new ResourceLocation("null", "null");
    public static final String NULL_STR = "null";

    public static <T> @NotNull ResourceKey<T> resourceKey(ResourceKey<? extends Registry<T>> registry, String str){
        return ResourceKey.create(registry, new ResourceLocation(AChanged.MODID, str));
    }

    @Contract(value = "null, _ -> fail; !null, _ -> param1", pure = true)
    public static <T> @NotNull T nonNullOrThrow(@Nullable T obj, RuntimeException exc) {
        if(obj == null) throw exc;
        return obj;
    }

    public static int booleansToInt(boolean[] booleans){
        int bits = 0;
        for (int i = 0; i < booleans.length; i++)
            if (booleans[i])
                bits |= 1 << i;
        return bits;
    }

    @Contract(pure = true)
    public static boolean @NotNull [] intToBooleans(int bits, int size){
        if(size > 32) size = 32;
        boolean[] booleans = new boolean[size]; /*max. length: 32*/
        for (int i = 0; i < booleans.length; i++)
            if ((bits & 1 << i) != 0)
                booleans[i] = true;
        return booleans;
    }

    public static boolean canStacksStack(ItemStack stack, ItemStack stackWith){
        if(stackWith.isEmpty()) return true;
        return stackWith.getCount() < stackWith.getMaxStackSize() && ItemHandlerHelper.canItemStacksStack(stack, stackWith);
    }

    private static final DecimalFormat FORMAT = new DecimalFormat("#.##");

    @Contract(pure = true)
    public static @NotNull String formatEnergy(int energy){
        if(energy >= 1000000000) return FORMAT.format((float) energy / 1000000000) + "B";
        if(energy >= 1000000) return FORMAT.format((float) energy / 1000000) + "M";
        if(energy >= 1000) return FORMAT.format((float) energy / 1000) + "k";
        return String.valueOf(energy);
    }

    public static <T> @NotNull NonNullList<T> toNonNull(List<T> list, T def){
        NonNullList<T> nonNull = NonNullList.withSize(list.size(), def);
        T obj;
        for(int i = 0; i < list.size(); i++){
            obj = list.get(i);
            if(obj != null) nonNull.set(i, obj);
        }
        return nonNull;
    }

    public static @NotNull VoxelShape rotateShape(Direction direction, VoxelShape source) {
        AtomicReference<VoxelShape> newShape = new AtomicReference<>(Shapes.empty());
        source.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
            Vec3 min = new Vec3(minX - 0.5, minY - 0.5, minZ - 0.5);
            Vec3 max = new Vec3(maxX - 0.5, maxY - 0.5, maxZ - 0.5);
            Vec3 v1 = rotateVec3(min, direction);
            Vec3 v2 = rotateVec3(max, direction);
            VoxelShape s = Shapes.create(0.5 + Math.min(v1.x, v2.x), 0.5 + Math.min(v1.y, v2.y), 0.5 + Math.min(v1.z, v2.z),
                    0.5 + Math.max(v1.x, v2.x), 0.5 + Math.max(v1.y, v2.y), 0.5 + Math.max(v1.z, v2.z));
            newShape.set(Shapes.or(newShape.get(), s));
        });
        return newShape.get();
    }

    public static @NotNull Vec3 rotateVec3(Vec3 vec, Direction dir) {
        double cos = 1;
        double sin = 0;
        switch (dir) {
            case SOUTH -> {
                cos = -1;
                sin = 0;
            }
            case WEST -> {
                cos = 0;
                sin = 1;
            }
            case EAST -> {
                cos = 0;
                sin = -1;
            }
        }

        return new Vec3(vec.x * cos + vec.z * sin, vec.y, vec.z * cos - vec.x * sin);
    }

    public static float rotlerpRad(float angle, float maxAngle, float mul) {
        float f = (mul - maxAngle) % (float) (Math.PI * 2);
        if (f < (float) -Math.PI) {
            f += (float) (Math.PI * 2);
        }

        if (f >= (float) Math.PI) {
            f -= (float) (Math.PI * 2);
        }

        return maxAngle + angle * f;
    }

    public static float quadraticArmUpdate(float limbSwing) {
        return -65.0F * limbSwing + limbSwing * limbSwing;
    }

    public static @NotNull String getArmorTexture(ItemStack stack, EquipmentSlot slot, @Nullable String type) {
        ArmorItem item = (ArmorItem) stack.getItem();
        String texture = item.getMaterial().getName();
        String domain = "minecraft";
        int idx = texture.indexOf(':');
        if (idx != -1) {
            domain = texture.substring(0, idx);
            texture = texture.substring(idx + 1);
        }
        return String.format(java.util.Locale.ROOT, "%s:textures/models/armor/%s_layer_%d%s.png", domain, texture, (slot == EquipmentSlot.LEGS ? 2 : 1), type == null ? "" : String.format(java.util.Locale.ROOT, "_%s", type));
    }

    public static boolean test(@Nullable ItemStack stack, Ingredient ingredient){
        if(stack == null) return false;
        for(ItemStack itemstack : ingredient.getItems()) {
            if(stack.getItem() == itemstack.getItem() && stack.getCount() >= itemstack.getCount() && Objects.equals(stack.getTag(), itemstack.getTag()) && stack.areAttachmentsCompatible(itemstack)) {
                return true;
            }
        }
        return false;
    }

    public static <E extends MobEffectInstance> E makeUnremovable(E effect){
        effect.getCures().clear();
        return effect;
    }

    public static boolean containsClass(Class<?> clazz, List<Class<?>> list){
        for (Class<?> block : list){
            if(block.isAssignableFrom(clazz)) return true;
        }
        return false;
    }

    /**
     * A way to scam Java/Minecraft to not crash trying to load client only stuff.
     */
    public static  <T> T get(Supplier<T> supplier){
        return supplier.get();
    }
}