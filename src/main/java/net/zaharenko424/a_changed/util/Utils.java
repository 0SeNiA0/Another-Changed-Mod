package net.zaharenko424.a_changed.util;

import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static net.zaharenko424.cmrs.util.Utils.FORMAT;

@ParametersAreNonnullByDefault
public class Utils {

    public static final ResourceLocation NULL_LOC = ResourceLocation.fromNamespaceAndPath("null", "null");
    public static final String NULL_STR = "null";

    public static <T> @NotNull ResourceKey<T> resourceKey(ResourceKey<? extends Registry<T>> registry, String str){
        return ResourceKey.create(registry, ResourceLocation.fromNamespaceAndPath(AChanged.MODID, str));
    }

    public static void sendVanillaToClient(ServerPlayer player, Packet<?> packet){
        player.connection.send(packet);
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

    public static VoxelShape orUnoptimized(VoxelShape shape1, VoxelShape shape2){
        return Shapes.joinUnoptimized(shape1, shape2, BooleanOp.OR);
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
            newShape.set(orUnoptimized(newShape.get(), s));
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

    public static boolean containsClass(Class<?> clazz, List<Class<?>> list){
        for (Class<?> block : list){
            if(block.isAssignableFrom(clazz)) return true;
        }
        return false;
    }

    /**
     * A way to not load client only stuff on server.
     */
    public static  <T> T get(Supplier<T> supplier){
        return supplier.get();
    }
}