package net.zaharenko424.a_changed.client.model;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.model.animation.Animations;
import net.zaharenko424.a_changed.client.model.geom.*;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class LatexWolfMaleModel<E extends LivingEntity> extends AbstractLatexEntityModel<E> {
    public LatexWolfMaleModel() {
        super(bodyLayer);
    }

    public static final ModelLayerLocation bodyLayer = new ModelLayerLocation(AChanged.resourceLoc("latex_wolf_male"),"main");

    public static @NotNull ModelDefinition bodyLayer(){
        MeshDefinition meshDefinition = new MeshDefinition();
        GroupDefinition groupDefinition = meshDefinition.getRoot();

        GroupDefinition root = groupDefinition.addOrReplaceChild("root", GroupBuilder.create(), PartPose.offset(0f, 0f, 0f));
        GroupDefinition head = root.addOrReplaceChild("head", GroupBuilder.create()
                .addBox(-4f, 0f, -4f, 8f, 8f, 8f, new Vector3f(), ImmutableMap.of(Direction.WEST, new UVData(24f, 16f, 16f, 8f), Direction.NORTH, new UVData(8f, 20f, 0f, 12f), Direction.EAST, new UVData(16f, 20f, 8f, 12f), Direction.UP, new UVData(24f, 24f, 16f, 16f), Direction.DOWN, new UVData(8f, 20f, 0f, 28f), Direction.SOUTH, new UVData(24f, 8f, 16f, 0f)))
                .addBox(-2f, 1f, -6f, 4f, 2f, 2f, new Vector3f(), ImmutableMap.of(Direction.WEST, new UVData(47f, 26f, 45f, 24f), Direction.NORTH, new UVData(41f, 50f, 37f, 48f), Direction.EAST, new UVData(25f, 46f, 23f, 44f), Direction.UP, new UVData(52f, 41f, 48f, 39f), Direction.DOWN, new UVData(45f, 48f, 41f, 50f), Direction.SOUTH, new UVData(52f, 39f, 48f, 37f)))
                .addBox(-1.5f, 0f, -5.5f, 3f, 1f, 1.5f, new Vector3f(), ImmutableMap.of(Direction.WEST, new UVData(42.5f, 51f, 41f, 50f), Direction.NORTH, new UVData(16f, 36f, 13f, 35f), Direction.EAST, new UVData(40.5f, 51f, 39f, 50f), Direction.UP, new UVData(28f, 37.5f, 25f, 36f), Direction.DOWN, new UVData(52f, 47f, 49f, 48.5f), Direction.SOUTH, new UVData(40f, 34f, 37f, 33f))), PartPose.offset(0f, 24f, 0f));
        GroupDefinition right_ear = head.addOrReplaceChild("right_ear", GroupBuilder.create()
                .addBox(-1f, -2f, -1f, 2f, 7f, 5f, new Vector3f(), ImmutableMap.of(Direction.WEST, new UVData(13f, 39f, 8f, 32f), Direction.NORTH, new UVData(29f, 53f, 27f, 46f), Direction.EAST, new UVData(37f, 7f, 32f, 0f), Direction.UP, new UVData(37f, 51f, 35f, 46f), Direction.DOWN, new UVData(49f, 24f, 47f, 29f), Direction.SOUTH, new UVData(31f, 53f, 29f, 46f))), PartPose.offsetAndRotation(3f, 7f, -1f, 0.4276f, 0.384f, -0.3665f));
        right_ear.addOrReplaceChild("right_ear_inner", GroupBuilder.create()
                .addBox(-1f, 3.3f, -2.7f, 2f, 1f, 4f, new Vector3f(-0.01f), ImmutableMap.of(Direction.WEST, new UVData(41f, 7f, 37f, 6f), Direction.NORTH, new UVData(47f, 27f, 45f, 26f), Direction.EAST, new UVData(36f, 12f, 32f, 11f), Direction.UP, new UVData(51f, 4f, 49f, 0f), Direction.DOWN, new UVData(51f, 4f, 49f, 8f), Direction.SOUTH, new UVData(24f, 51f, 22f, 50f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0.7854f, 0f, 0f));
        GroupDefinition left_ear = head.addOrReplaceChild("left_ear", GroupBuilder.create()
                .addBox(-1f, -2f, -1f, 2f, 7f, 5f, new Vector3f(), ImmutableMap.of(Direction.WEST, new UVData(37f, 34f, 32f, 27f), Direction.NORTH, new UVData(33f, 53f, 31f, 46f), Direction.EAST, new UVData(37f, 27f, 32f, 20f), Direction.UP, new UVData(17f, 53f, 15f, 48f), Direction.DOWN, new UVData(19f, 48f, 17f, 53f), Direction.SOUTH, new UVData(35f, 53f, 33f, 46f))), PartPose.offsetAndRotation(-3f, 7f, -1f, 0.4276f, -0.384f, 0.3665f));
        left_ear.addOrReplaceChild("left_ear_inner", GroupBuilder.create()
                .addBox(-1f, 3.3f, -2.7f, 2f, 1f, 4f, new Vector3f(-0.01f), ImmutableMap.of(Direction.WEST, new UVData(53f, 50f, 49f, 49f), Direction.NORTH, new UVData(26f, 51f, 24f, 50f), Direction.EAST, new UVData(53f, 29f, 49f, 28f), Direction.UP, new UVData(10f, 53f, 8f, 49f), Direction.DOWN, new UVData(51f, 8f, 49f, 12f), Direction.SOUTH, new UVData(39f, 51f, 37f, 50f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0.7854f, 0f, 0f));
        head.addOrReplaceChild("armor_head", GroupBuilder.create()
                .addBox(-4f, 0f, -4f, 8f, 8f, 8f, new Vector3f(0.6f), ImmutableMap.of(Direction.WEST, new UVData(24f, 16f, 16f, 8f), Direction.NORTH, new UVData(16f, 16f, 8f, 8f), Direction.EAST, new UVData(8f, 16f, 0f, 8f), Direction.UP, new UVData(16f, 8f, 8f, 0f), Direction.DOWN, new UVData(24f, 0f, 16f, 8f), Direction.SOUTH, new UVData(32f, 16f, 24f, 8f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0f, 0f, 0f));
        GroupDefinition body = root.addOrReplaceChild("body", GroupBuilder.create()
                .addBox(-4f, 0f, -2f, 8f, 12f, 4f, new Vector3f(), ImmutableMap.of(Direction.WEST, new UVData(16f, 32f, 12f, 20f), Direction.NORTH, new UVData(8f, 12f, 0f, 0f), Direction.EAST, new UVData(12f, 32f, 8f, 20f), Direction.UP, new UVData(40f, 11f, 32f, 7f), Direction.DOWN, new UVData(40f, 34f, 32f, 38f), Direction.SOUTH, new UVData(16f, 12f, 8f, 0f))), PartPose.offset(0f, 12f, 0f));
        GroupDefinition tail = body.addOrReplaceChild("tail", GroupBuilder.create()
                .addBox(-1.5f, -1.5f, -2f, 3f, 3f, 6f, new Vector3f(), ImmutableMap.of(Direction.WEST, new UVData(47f, 33f, 41f, 30f), Direction.NORTH, new UVData(16f, 35f, 13f, 32f), Direction.EAST, new UVData(47f, 30f, 41f, 27f), Direction.UP, new UVData(44f, 44f, 41f, 38f), Direction.DOWN, new UVData(15f, 44f, 12f, 50f), Direction.SOUTH, new UVData(22f, 51f, 19f, 48f))), PartPose.offsetAndRotation(0f, 0f, 2f, 0.48f, 0f, 0f));
        GroupDefinition tail_0 = tail.addOrReplaceChild("tail_0", GroupBuilder.create()
                .addBox(-2f, -2f, -1f, 4f, 4f, 9f, new Vector3f(), ImmutableMap.of(Direction.WEST, new UVData(37f, 20f, 28f, 16f), Direction.NORTH, new UVData(19f, 48f, 15f, 44f), Direction.EAST, new UVData(37f, 16f, 28f, 12f), Direction.UP, new UVData(32f, 29f, 28f, 20f), Direction.DOWN, new UVData(32f, 29f, 28f, 38f), Direction.SOUTH, new UVData(23f, 48f, 19f, 44f)))
                .addBox(-1f, -1f, 7f, 2f, 2f, 2f, new Vector3f(), ImmutableMap.of(Direction.WEST, new UVData(6f, 52f, 4f, 50f), Direction.NORTH, new UVData(12f, 51f, 10f, 49f), Direction.EAST, new UVData(2f, 52f, 0f, 50f), Direction.UP, new UVData(8f, 52f, 6f, 50f), Direction.DOWN, new UVData(14f, 50f, 12f, 52f), Direction.SOUTH, new UVData(4f, 52f, 2f, 50f))), PartPose.offsetAndRotation(0f, 0f, 4f, 0.1309f, 0f, 0f));
        tail_0.addOrReplaceChild("armor_tail_0", GroupBuilder.create()
                .addBox(-2f, -2f, -1f, 4f, 4f, 9f, new Vector3f(0.6f), ImmutableMap.of(Direction.WEST, new UVData(8f, 29f, 4f, 23f), Direction.NORTH, new UVData(8f, 29f, 4f, 25f), Direction.EAST, new UVData(8f, 29f, 4f, 23f), Direction.UP, new UVData(8f, 29f, 4f, 23f), Direction.DOWN, new UVData(8f, 23f, 4f, 29f), Direction.SOUTH, new UVData(8f, 29f, 4f, 25f)))
                .addBox(-1f, -1f, 7f, 2f, 2f, 2f, new Vector3f(0.6f), ImmutableMap.of(Direction.WEST, new UVData(8f, 29f, 4f, 25f), Direction.EAST, new UVData(8f, 29f, 4f, 25f), Direction.UP, new UVData(8f, 29f, 4f, 25f), Direction.DOWN, new UVData(8f, 25f, 4f, 29f), Direction.SOUTH, new UVData(8f, 29f, 4f, 25f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0.1309f, 0f, 0f));
        tail.addOrReplaceChild("armor_tail", GroupBuilder.create()
                .addBox(-1.5f, -1.5f, -2f, 3f, 3f, 6f, new Vector3f(0.6f), ImmutableMap.of(Direction.WEST, new UVData(8f, 29f, 4f, 24f), Direction.EAST, new UVData(8f, 29f, 4f, 24f), Direction.UP, new UVData(8f, 29f, 4f, 24f), Direction.DOWN, new UVData(8f, 24f, 4f, 29f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0f, 0f, 0f));
        body.addOrReplaceChild("armor_body", GroupBuilder.create()
                .addBox(-4f, 0f, -2f, 8f, 12f, 4f, new Vector3f(0.6f), ImmutableMap.of(Direction.WEST, new UVData(20f, 32f, 16f, 20f), Direction.NORTH, new UVData(28f, 32f, 20f, 20f), Direction.EAST, new UVData(32f, 32f, 28f, 20f), Direction.UP, new UVData(40f, 11f, 32f, 7f), Direction.DOWN, new UVData(40f, 34f, 32f, 38f), Direction.SOUTH, new UVData(40f, 32f, 32f, 20f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0f, 0f, 0f));
        GroupDefinition right_arm = root.addOrReplaceChild("right_arm", GroupBuilder.create()
                .addBox(0f, -10f, -2f, 4f, 12f, 4f, new Vector3f(), ImmutableMap.of(Direction.WEST, new UVData(24f, 36f, 20f, 24f), Direction.NORTH, new UVData(28f, 12f, 24f, 0f), Direction.EAST, new UVData(28f, 24f, 24f, 12f), Direction.UP, new UVData(48f, 37f, 44f, 33f), Direction.DOWN, new UVData(41f, 44f, 37f, 48f), Direction.SOUTH, new UVData(20f, 36f, 16f, 24f))), PartPose.offset(4f, 22f, 0f));
        right_arm.addOrReplaceChild("armor_right_arm", GroupBuilder.create()
                .addBox(0f, -10f, -2f, 4f, 12f, 4f, new Vector3f(0.6f), ImmutableMap.of(Direction.WEST, new UVData(52f, 32f, 48f, 20f), Direction.NORTH, new UVData(48f, 32f, 44f, 20f), Direction.EAST, new UVData(44f, 32f, 40f, 20f), Direction.UP, new UVData(48f, 20f, 44f, 16f), Direction.SOUTH, new UVData(56f, 32f, 52f, 20f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0f, 0f, 0f));
        GroupDefinition left_arm = root.addOrReplaceChild("left_arm", GroupBuilder.create()
                .addBox(-4f, -10f, -2f, 4f, 12f, 4f, new Vector3f(), ImmutableMap.of(Direction.WEST, new UVData(8f, 40f, 4f, 28f), Direction.NORTH, new UVData(28f, 36f, 24f, 24f), Direction.EAST, new UVData(4f, 40f, 0f, 28f), Direction.UP, new UVData(48f, 41f, 44f, 37f), Direction.DOWN, new UVData(45f, 44f, 41f, 48f), Direction.SOUTH, new UVData(32f, 12f, 28f, 0f))), PartPose.offset(-4f, 22f, 0f));
        left_arm.addOrReplaceChild("armor_left_arm", GroupBuilder.create()
                .addBox(-4f, -10f, -2f, 4f, 12f, 4f, new Vector3f(0.6f), ImmutableMap.of(Direction.WEST, new UVData(40f, 32f, 44f, 20f), Direction.NORTH, new UVData(56f, 32f, 52f, 20f), Direction.EAST, new UVData(52f, 32f, 48f, 20f), Direction.UP, new UVData(44f, 20f, 48f, 16f), Direction.SOUTH, new UVData(48f, 32f, 44f, 20f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0f, 0f, 0f));
        GroupDefinition right_leg = root.addOrReplaceChild("right_leg", GroupBuilder.create(), PartPose.offsetAndRotation(2f, 13f, 0f, 0f, -0.1309f, 0.1309f));
        GroupDefinition right_leg_shin = right_leg.addOrReplaceChild("right_leg_shin", GroupBuilder.create(), PartPose.offset(0f, -6f, -2f));
        GroupDefinition right_leg_ = right_leg_shin.addOrReplaceChild("right_leg_", GroupBuilder.create(), PartPose.offset(0f, -1f, 3f));
        GroupDefinition right_foot = right_leg_.addOrReplaceChild("right_foot", GroupBuilder.create(), PartPose.offset(0f, 0f, 0f));
        right_foot.addOrReplaceChild("right_foot_i0", GroupBuilder.create()
                .addBox(-1.3f, -6f, -3.5f, 4f, 2f, 5f, new Vector3f(), ImmutableMap.of(Direction.WEST, new UVData(52f, 33f, 47f, 31f), Direction.NORTH, new UVData(53f, 22f, 49f, 20f), Direction.EAST, new UVData(52f, 31f, 47f, 29f), Direction.UP, new UVData(44f, 38f, 40f, 33f), Direction.DOWN, new UVData(45f, 12f, 41f, 17f), Direction.SOUTH, new UVData(53f, 24f, 49f, 22f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0f, 0f, -0.1309f));
        right_foot.addOrReplaceChild("armor_right_foot", GroupBuilder.create()
                .addBox(-1.3f, -6f, -3.5f, 4f, 2f, 5f, new Vector3f(0.6f), ImmutableMap.of(Direction.WEST, new UVData(12f, 20f, 8f, 16f), Direction.NORTH, new UVData(12f, 20f, 8f, 16f), Direction.EAST, new UVData(12f, 20f, 8f, 16f), Direction.UP, new UVData(12f, 20f, 8f, 16f), Direction.DOWN, new UVData(12f, 16f, 8f, 20f), Direction.SOUTH, new UVData(12f, 20f, 8f, 16f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0f, 0f, -0.1309f));
        right_leg_.addOrReplaceChild("right_leg__i0", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4f, 6f, 4f, new Vector3f(), ImmutableMap.of(Direction.WEST, new UVData(12f, 45f, 8f, 39f), Direction.NORTH, new UVData(41f, 6f, 37f, 0f), Direction.EAST, new UVData(41f, 33f, 37f, 27f), Direction.UP, new UVData(49f, 16f, 45f, 12f), Direction.DOWN, new UVData(49f, 16f, 45f, 20f), Direction.SOUTH, new UVData(41f, 44f, 37f, 38f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0.1309f, 0f, 0f));
        right_leg_.addOrReplaceChild("armor_right_leg_", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4f, 6f, 4f, new Vector3f(0.6f), ImmutableMap.of(Direction.WEST, new UVData(8f, 32f, 4f, 26f), Direction.NORTH, new UVData(4f, 32f, 0f, 26f), Direction.EAST, new UVData(8f, 32f, 4f, 26f), Direction.UP, new UVData(49f, 16f, 45f, 12f), Direction.DOWN, new UVData(49f, 16f, 45f, 20f), Direction.SOUTH, new UVData(4f, 32f, 0f, 26f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0.1309f, 0f, 0f));
        right_leg_shin.addOrReplaceChild("right_leg_shin_i0", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4f, 2f, 4f, new Vector3f(), ImmutableMap.of(Direction.WEST, new UVData(53f, 20f, 49f, 18f), Direction.NORTH, new UVData(53f, 14f, 49f, 12f), Direction.EAST, new UVData(53f, 16f, 49f, 14f), Direction.UP, new UVData(12f, 49f, 8f, 45f), Direction.DOWN, new UVData(49f, 8f, 45f, 12f), Direction.SOUTH, new UVData(53f, 18f, 49f, 16f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0.3491f, 0f, 0f));
        right_leg_shin.addOrReplaceChild("armor_right_leg_shin", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4f, 2f, 4f, new Vector3f(0.59f), ImmutableMap.of(Direction.WEST, new UVData(8f, 29f, 4f, 25f), Direction.EAST, new UVData(8f, 29f, 4f, 25f), Direction.UP, new UVData(8f, 29f, 4f, 25f), Direction.DOWN, new UVData(49f, 8f, 45f, 12f), Direction.SOUTH, new UVData(8f, 29f, 4f, 25f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0.3491f, 0f, 0f));
        right_leg.addOrReplaceChild("right_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4f, 8f, 4f, new Vector3f(), ImmutableMap.of(Direction.WEST, new UVData(41f, 19f, 37f, 11f), Direction.NORTH, new UVData(17f, 44f, 13f, 36f), Direction.EAST, new UVData(21f, 44f, 17f, 36f), Direction.UP, new UVData(49f, 4f, 45f, 0f), Direction.DOWN, new UVData(49f, 4f, 45f, 8f), Direction.SOUTH, new UVData(25f, 44f, 21f, 36f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0.3491f, 0f, 0f));
        right_leg.addOrReplaceChild("armor_right_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4f, 8f, 4f, new Vector3f(0.6f), ImmutableMap.of(Direction.WEST, new UVData(12f, 29f, 8f, 20f), Direction.NORTH, new UVData(8f, 29f, 4f, 20f), Direction.EAST, new UVData(12f, 29f, 8f, 20f), Direction.UP, new UVData(8f, 20f, 4f, 16f), Direction.DOWN, new UVData(8f, 25f, 4f, 29f), Direction.SOUTH, new UVData(16f, 29f, 12f, 20f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0.3491f, 0f, 0f));
        GroupDefinition left_leg = root.addOrReplaceChild("left_leg", GroupBuilder.create(), PartPose.offsetAndRotation(-2f, 13f, 0f, 0f, 0.1309f, -0.1309f));
        GroupDefinition left_leg_shin = left_leg.addOrReplaceChild("left_leg_shin", GroupBuilder.create(), PartPose.offset(0f, -6f, -2f));
        GroupDefinition left_leg_ = left_leg_shin.addOrReplaceChild("left_leg_", GroupBuilder.create(), PartPose.offset(0f, -1f, 3f));
        GroupDefinition left_foot = left_leg_.addOrReplaceChild("left_foot", GroupBuilder.create(), PartPose.offset(0f, 0f, 0f));
        left_foot.addOrReplaceChild("left_foot_i0", GroupBuilder.create()
                .addBox(-2.7f, -6f, -3.5f, 4f, 2f, 5f, new Vector3f(), ImmutableMap.of(Direction.WEST, new UVData(53f, 37f, 48f, 35f), Direction.NORTH, new UVData(49f, 51f, 45f, 49f), Direction.EAST, new UVData(53f, 35f, 48f, 33f), Direction.UP, new UVData(45f, 22f, 41f, 17f), Direction.DOWN, new UVData(45f, 22f, 41f, 27f), Direction.SOUTH, new UVData(53f, 47f, 49f, 45f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0f, 0f, 0.1309f));
        left_foot.addOrReplaceChild("armor_left_foot", GroupBuilder.create()
                .addBox(-2.7f, -6f, -3.5f, 4f, 2f, 5f, new Vector3f(0.6f), ImmutableMap.of(Direction.WEST, new UVData(12f, 16f, 8f, 20f), Direction.NORTH, new UVData(8f, 20f, 12f, 16f), Direction.EAST, new UVData(12f, 20f, 8f, 16f), Direction.UP, new UVData(12f, 20f, 8f, 16f), Direction.DOWN, new UVData(8f, 16f, 12f, 20f), Direction.SOUTH, new UVData(12f, 20f, 8f, 16f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0f, 0f, 0.1309f));
        left_leg_.addOrReplaceChild("left_leg__i0", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4f, 6f, 4f, new Vector3f(), ImmutableMap.of(Direction.WEST, new UVData(45f, 12f, 41f, 6f), Direction.NORTH, new UVData(4f, 46f, 0f, 40f), Direction.EAST, new UVData(8f, 46f, 4f, 40f), Direction.UP, new UVData(8f, 50f, 4f, 46f), Direction.DOWN, new UVData(27f, 46f, 23f, 50f), Direction.SOUTH, new UVData(45f, 6f, 41f, 0f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0.1309f, 0f, 0f));
        left_leg_.addOrReplaceChild("armor_left_leg_", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4f, 6f, 4f, new Vector3f(0.6f), ImmutableMap.of(Direction.WEST, new UVData(4f, 32f, 8f, 26f), Direction.NORTH, new UVData(0f, 32f, 4f, 26f), Direction.EAST, new UVData(8f, 32f, 4f, 26f), Direction.UP, new UVData(8f, 50f, 4f, 46f), Direction.DOWN, new UVData(27f, 46f, 23f, 50f), Direction.SOUTH, new UVData(4f, 32f, 0f, 26f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0.1309f, 0f, 0f));
        left_leg_shin.addOrReplaceChild("left_leg_shin_i0", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4f, 2f, 4f, new Vector3f(), ImmutableMap.of(Direction.WEST, new UVData(53f, 45f, 49f, 43f), Direction.NORTH, new UVData(53f, 26f, 49f, 24f), Direction.EAST, new UVData(53f, 28f, 49f, 26f), Direction.UP, new UVData(49f, 49f, 45f, 45f), Direction.DOWN, new UVData(4f, 46f, 0f, 50f), Direction.SOUTH, new UVData(53f, 43f, 49f, 41f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0.3491f, 0f, 0f));
        left_leg_shin.addOrReplaceChild("armor_left_leg_shin", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4f, 2f, 4f, new Vector3f(0.59f), ImmutableMap.of(Direction.WEST, new UVData(4f, 29f, 8f, 25f), Direction.NORTH, new UVData(53f, 26f, 49f, 24f), Direction.EAST, new UVData(8f, 29f, 4f, 25f), Direction.UP, new UVData(8f, 29f, 4f, 25f), Direction.DOWN, new UVData(4f, 46f, 0f, 50f), Direction.SOUTH, new UVData(8f, 29f, 4f, 25f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0.3491f, 0f, 0f));
        left_leg.addOrReplaceChild("left_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4f, 8f, 4f, new Vector3f(), ImmutableMap.of(Direction.WEST, new UVData(37f, 46f, 33f, 38f), Direction.NORTH, new UVData(41f, 27f, 37f, 19f), Direction.EAST, new UVData(29f, 46f, 25f, 38f), Direction.UP, new UVData(49f, 24f, 45f, 20f), Direction.DOWN, new UVData(49f, 41f, 45f, 45f), Direction.SOUTH, new UVData(33f, 46f, 29f, 38f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0.3491f, 0f, 0f));
        left_leg.addOrReplaceChild("armor_left_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4f, 8f, 4f, new Vector3f(0.6f), ImmutableMap.of(Direction.WEST, new UVData(12f, 29f, 8f, 20f), Direction.NORTH, new UVData(4f, 29f, 8f, 20f), Direction.EAST, new UVData(12f, 29f, 8f, 20f), Direction.UP, new UVData(8f, 20f, 4f, 16f), Direction.DOWN, new UVData(4f, 25f, 8f, 29f), Direction.SOUTH, new UVData(16f, 29f, 12f, 20f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0.3491f, 0f, 0f));

        return ModelDefinition.create(meshDefinition, 128, 128, 2);
    }

    AnimationState ears = new AnimationState();
    AnimationState tail = new AnimationState();

    @Override
    public void setupAnim(E entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch);
        if(!ears.isStarted()) ears.start((int) ageInTicks);
        if(!tail.isStarted()) tail.start((int) ageInTicks);
        animate(ears, Animations.EAR_ANIM,ageInTicks);
        animate(tail,Animations.TAIL_ANIM,ageInTicks);
    }
}