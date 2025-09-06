package net.zaharenko424.a_changed.util;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
public class ClipUtil {

    public static @Nullable EntityHitResult getLookingAt(LivingEntity looker, float reach, boolean testLineOfSight){
        return getLookingAt(looker, reach, testLineOfSight, e -> !e.isSpectator());
    }

    public static @Nullable EntityHitResult getLookingAt(LivingEntity looker, float reach, boolean testLineOfSight, Predicate<Entity> targetPredicate){
        Vec3 start = looker.getEyePosition();

        float reachSqr = reach * reach;
        if(testLineOfSight) {
            HitResult blockHit = looker.pick(reach, 1, false);

            if (blockHit.getType() != HitResult.Type.MISS) {
                reachSqr = (float) blockHit.getLocation().distanceToSqr(start);
                reach = Mth.sqrt(reachSqr);
            }
        }

        Vec3 viewVec = looker.getLookAngle();
        Vec3 end = start.add(viewVec.x * reach, viewVec.y * reach, viewVec.z * reach);
        return ProjectileUtil.getEntityHitResult(looker, start, end, new AABB(start, end), targetPredicate, reachSqr);
    }
}
