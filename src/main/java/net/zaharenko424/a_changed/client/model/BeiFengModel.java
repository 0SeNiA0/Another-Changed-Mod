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
public class BeiFengModel<E extends LivingEntity> extends AbstractLatexEntityModel<E> {
    public BeiFengModel() {
        super(bodyLayer);
    }

    public static ModelLayerLocation bodyLayer = new ModelLayerLocation(AChanged.resourceLoc("bei_feng"),"main");

    public static @NotNull ModelDefinition bodyLayer(){
        MeshDefinition meshDefinition = new MeshDefinition();
        GroupDefinition groupDefinition = meshDefinition.getRoot();

        GroupDefinition root = groupDefinition.addOrReplaceChild("root", GroupBuilder.create(), PartPose.offset(0f, 0f, 0f));
        GroupDefinition head = root.addOrReplaceChild("head", GroupBuilder.create()
                .addBox(-4f, 0f, -4f, 8f, 8f, 8f, new Vector3f(), ImmutableMap.of(Direction.EAST, new UVData(16f, 20f, 8f, 12f), Direction.UP, new UVData(24f, 24f, 16f, 16f), Direction.DOWN, new UVData(8f, 20f, 0f, 28f), Direction.NORTH, new UVData(8f, 20f, 0f, 12f), Direction.SOUTH, new UVData(24f, 8f, 16f, 0f), Direction.WEST, new UVData(24f, 16f, 16f, 8f)))
                .addBox(-2f, 1f, -6f, 4f, 2f, 2f, new Vector3f(), ImmutableMap.of(Direction.EAST, new UVData(42f, 14f, 40f, 12f), Direction.UP, new UVData(55f, 18f, 51f, 16f), Direction.DOWN, new UVData(55f, 18f, 51f, 20f), Direction.NORTH, new UVData(45f, 8f, 41f, 6f), Direction.SOUTH, new UVData(51f, 52f, 47f, 50f), Direction.WEST, new UVData(55f, 29f, 53f, 27f)))
                .addBox(-1.5f, 0f, -5.5f, 3f, 1f, 1.5f, new Vector3f(), ImmutableMap.of(Direction.EAST, new UVData(54.5f, 39f, 53f, 38f), Direction.UP, new UVData(56f, 6.5f, 53f, 5f), Direction.DOWN, new UVData(29f, 53f, 26f, 54.5f), Direction.NORTH, new UVData(40f, 9f, 37f, 8f), Direction.SOUTH, new UVData(41f, 28f, 38f, 27f), Direction.WEST, new UVData(54.5f, 40f, 53f, 39f))), PartPose.offset(0f, 24f, 0f));
        GroupDefinition right_ear = head.addOrReplaceChild("right_ear", GroupBuilder.create(), PartPose.offset(3f, 7f, -1f));
        right_ear.addOrReplaceChild("ear", GroupBuilder.create()
                .addBox(-1f, -2f, -1f, 2f, 9f, 5f, new Vector3f(), ImmutableMap.of(Direction.EAST, new UVData(33f, 21f, 28f, 12f), Direction.UP, new UVData(6f, 55f, 4f, 50f), Direction.DOWN, new UVData(8f, 50f, 6f, 55f), Direction.NORTH, new UVData(38f, 53f, 36f, 44f), Direction.SOUTH, new UVData(40f, 53f, 38f, 44f), Direction.WEST, new UVData(33f, 30f, 28f, 21f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0.7295f, 0.3578f, -0.1745f));
        right_ear.addOrReplaceChild("ear_inner", GroupBuilder.create()
                .addBox(-1f, 2.2f, -5.8f, 2f, 1f, 8f, new Vector3f(-0.01f), ImmutableMap.of(Direction.EAST, new UVData(55f, 31f, 53f, 29f), Direction.UP, new UVData(35f, 55f, 33f, 53f), Direction.DOWN, new UVData(55f, 33f, 53f, 35f), Direction.NORTH, new UVData(31f, 55f, 29f, 53f), Direction.SOUTH, new UVData(33f, 55f, 31f, 53f), Direction.WEST, new UVData(55f, 33f, 53f, 31f))), PartPose.offsetAndRotation(0f, 0f, 0f, 1.7331f, 0.3578f, -0.1745f));
        GroupDefinition left_ear = head.addOrReplaceChild("left_ear", GroupBuilder.create(), PartPose.offset(-3f, 7f, -1f));
        left_ear.addOrReplaceChild("ear", GroupBuilder.create()
                .addBox(-1f, -2f, -1f, 2f, 9f, 5f, new Vector3f(), ImmutableMap.of(Direction.EAST, new UVData(33f, 39f, 28f, 30f), Direction.UP, new UVData(52f, 12f, 50f, 7f), Direction.DOWN, new UVData(14f, 50f, 12f, 55f), Direction.NORTH, new UVData(10f, 54f, 8f, 45f), Direction.SOUTH, new UVData(12f, 54f, 10f, 45f), Direction.WEST, new UVData(37f, 9f, 32f, 0f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0.7295f, -0.3578f, 0.1745f));
        left_ear.addOrReplaceChild("ear_inner", GroupBuilder.create()
                .addBox(-1f, 2.2f, -5.8f, 2f, 1f, 8f, new Vector3f(-0.01f), ImmutableMap.of(Direction.EAST, new UVData(53f, 33f, 55f, 31f), Direction.UP, new UVData(33f, 55f, 35f, 53f), Direction.DOWN, new UVData(53f, 33f, 55f, 35f), Direction.NORTH, new UVData(29f, 55f, 31f, 53f), Direction.SOUTH, new UVData(31f, 55f, 33f, 53f), Direction.WEST, new UVData(53f, 31f, 55f, 29f))), PartPose.offsetAndRotation(0f, 0f, 0f, 1.7331f, -0.3578f, 0.1745f));
        GroupDefinition body = root.addOrReplaceChild("body", GroupBuilder.create()
                .addBox(-4f, 0f, -2f, 8f, 12f, 4f, new Vector3f(), ImmutableMap.of(Direction.EAST, new UVData(12f, 32f, 8f, 20f), Direction.UP, new UVData(41f, 32f, 33f, 28f), Direction.DOWN, new UVData(41f, 32f, 33f, 36f), Direction.NORTH, new UVData(8f, 12f, 0f, 0f), Direction.SOUTH, new UVData(16f, 12f, 8f, 0f), Direction.WEST, new UVData(16f, 32f, 12f, 20f)))
                .addBox(-0.5f, 10f, 2f, 1f, 1f, 1.5f, new Vector3f(), ImmutableMap.of(Direction.EAST, new UVData(41.5f, 54f, 40f, 53f), Direction.UP, new UVData(20f, 51.5f, 19f, 50f), Direction.DOWN, new UVData(54f, 41f, 53f, 42.5f), Direction.NORTH, new UVData(16f, 36f, 15f, 35f), Direction.SOUTH, new UVData(41f, 45f, 40f, 44f), Direction.WEST, new UVData(54.5f, 41f, 53f, 40f)))
                .addBox(-0.5f, 7f, 2f, 1f, 1f, 1.5f, new Vector3f(), ImmutableMap.of(Direction.EAST, new UVData(43.5f, 54f, 42f, 53f), Direction.UP, new UVData(45f, 54.5f, 44f, 53f), Direction.DOWN, new UVData(46f, 53f, 45f, 54.5f), Direction.NORTH, new UVData(45f, 46f, 44f, 45f), Direction.SOUTH, new UVData(55f, 3f, 54f, 2f), Direction.WEST, new UVData(54.5f, 44f, 53f, 43f)))
                .addBox(-0.5f, 4f, 2f, 1f, 1f, 1.5f, new Vector3f(), ImmutableMap.of(Direction.EAST, new UVData(55.5f, 1f, 54f, 0f), Direction.UP, new UVData(47f, 54.5f, 46f, 53f), Direction.DOWN, new UVData(9f, 54f, 8f, 55.5f), Direction.NORTH, new UVData(16f, 55f, 15f, 54f), Direction.SOUTH, new UVData(17f, 55f, 16f, 54f), Direction.WEST, new UVData(55.5f, 2f, 54f, 1f))), PartPose.offset(0f, 12f, 0f));
        GroupDefinition tail = body.addOrReplaceChild("tail", GroupBuilder.create()
                .addBox(-2.4f, -2.4f, -3f, 4.8f, 4.8f, 7f, new Vector3f(), ImmutableMap.of(Direction.EAST, new UVData(15f, 37f, 8f, 32f), Direction.UP, new UVData(38f, 21f, 33f, 14f), Direction.DOWN, new UVData(38f, 21f, 33f, 28f), Direction.NORTH, new UVData(43f, 27f, 38f, 22f), Direction.SOUTH, new UVData(32f, 44f, 27f, 39f), Direction.WEST, new UVData(40f, 14f, 33f, 9f)))
                .addBox(-0.5f, 2.5f, 0f, 1f, 1f, 1f, new Vector3f(), ImmutableMap.of(Direction.EAST, new UVData(36f, 55f, 35f, 54f), Direction.UP, new UVData(39f, 55f, 38f, 54f), Direction.DOWN, new UVData(40f, 54f, 39f, 55f), Direction.NORTH, new UVData(18f, 55f, 17f, 54f), Direction.SOUTH, new UVData(37f, 55f, 36f, 54f), Direction.WEST, new UVData(38f, 55f, 37f, 54f))), PartPose.offsetAndRotation(0f, 0f, 2f, 0.7418f, 0f, 0f));
        GroupDefinition tail_0 = tail.addOrReplaceChild("tail_0", GroupBuilder.create()
                .addBox(-2.1f, -1.8f, -0.5f, 4.2f, 4.2f, 6f, new Vector3f(), ImmutableMap.of(Direction.EAST, new UVData(6f, 44f, 0f, 40f), Direction.UP, new UVData(45f, 6f, 41f, 0f), Direction.DOWN, new UVData(45f, 27f, 41f, 33f), Direction.NORTH, new UVData(49f, 38f, 45f, 34f), Direction.SOUTH, new UVData(49f, 42f, 45f, 38f), Direction.WEST, new UVData(46f, 12f, 40f, 8f)))
                .addBox(-0.5f, 2.4f, 2f, 1f, 1f, 1f, new Vector3f(), ImmutableMap.of(Direction.EAST, new UVData(42f, 55f, 41f, 54f), Direction.UP, new UVData(55f, 43f, 54f, 42f), Direction.DOWN, new UVData(44f, 54f, 43f, 55f), Direction.NORTH, new UVData(41f, 55f, 40f, 54f), Direction.SOUTH, new UVData(55f, 42f, 54f, 41f), Direction.WEST, new UVData(43f, 55f, 42f, 54f))), PartPose.offsetAndRotation(0f, 0f, 4f, -0.1309f, 0f, 0f));
        GroupDefinition tail_1 = tail_0.addOrReplaceChild("tail_1", GroupBuilder.create()
                .addBox(-1.8f, -1.3f, 0f, 3.6f, 3.7f, 5f, new Vector3f(), ImmutableMap.of(Direction.EAST, new UVData(50f, 3.5f, 45f, 0f), Direction.UP, new UVData(48.5f, 29f, 45f, 24f), Direction.DOWN, new UVData(48.5f, 29f, 45f, 34f), Direction.NORTH, new UVData(43.5f, 52.5f, 40f, 49f), Direction.SOUTH, new UVData(52.5f, 43.5f, 49f, 40f), Direction.WEST, new UVData(50f, 7.5f, 45f, 4f)))
                .addBox(-0.5f, 2.4f, 2f, 1f, 1f, 1f, new Vector3f(), ImmutableMap.of(Direction.EAST, new UVData(55f, 46f, 54f, 45f), Direction.UP, new UVData(50f, 55f, 49f, 54f), Direction.DOWN, new UVData(51f, 54f, 50f, 55f), Direction.NORTH, new UVData(55f, 45f, 54f, 44f), Direction.SOUTH, new UVData(48f, 55f, 47f, 54f), Direction.WEST, new UVData(49f, 55f, 48f, 54f))), PartPose.offsetAndRotation(0f, 0f, 5f, -0.1309f, 0f, 0f));
        GroupDefinition tail_2 = tail_1.addOrReplaceChild("tail_2", GroupBuilder.create()
                .addBox(-1.5f, -1.3f, -0.5f, 3f, 3.2f, 4.5f, new Vector3f(), ImmutableMap.of(Direction.EAST, new UVData(4f, 53f, 0f, 50f), Direction.UP, new UVData(15f, 41f, 12f, 37f), Direction.DOWN, new UVData(53f, 3f, 50f, 7f), Direction.NORTH, new UVData(15f, 44f, 12f, 41f), Direction.SOUTH, new UVData(47f, 53f, 44f, 50f), Direction.WEST, new UVData(54f, 3f, 50f, 0f)))
                .addBox(-0.5f, 1.9f, 1.5f, 1f, 1f, 1f, new Vector3f(), ImmutableMap.of(Direction.EAST, new UVData(53f, 55f, 52f, 54f), Direction.UP, new UVData(1f, 56f, 0f, 55f), Direction.DOWN, new UVData(2f, 55f, 1f, 56f), Direction.NORTH, new UVData(52f, 55f, 51f, 54f), Direction.SOUTH, new UVData(54f, 55f, 53f, 54f), Direction.WEST, new UVData(55f, 55f, 54f, 54f))), PartPose.offsetAndRotation(0f, 0.5f, 5f, -0.0873f, 0f, 0f));
        tail_2.addOrReplaceChild("tail_3", GroupBuilder.create()
                .addBox(-1f, -1f, 0f, 2f, 2.4f, 3f, new Vector3f(), ImmutableMap.of(Direction.EAST, new UVData(21f, 55.5f, 18f, 53f), Direction.UP, new UVData(8f, 43f, 6f, 40f), Direction.DOWN, new UVData(45f, 24f, 43f, 27f), Direction.NORTH, new UVData(26f, 55.5f, 24f, 53f), Direction.SOUTH, new UVData(55f, 26.5f, 53f, 24f), Direction.WEST, new UVData(24f, 55.5f, 21f, 53f)))
                .addBox(-0.5f, 1.4f, 1f, 1f, 1.5f, 1f, new Vector3f(), ImmutableMap.of(Direction.EAST, new UVData(11f, 55.5f, 10f, 54f), Direction.UP, new UVData(3f, 56f, 2f, 55f), Direction.DOWN, new UVData(56f, 2f, 55f, 3f), Direction.NORTH, new UVData(10f, 55.5f, 9f, 54f), Direction.SOUTH, new UVData(12f, 55.5f, 11f, 54f), Direction.WEST, new UVData(15f, 55.5f, 14f, 54f))), PartPose.offsetAndRotation(0f, 0.5f, 4f, -0.0873f, 0f, 0f));
        root.addOrReplaceChild("right_arm", GroupBuilder.create()
                .addBox(0f, -10f, -2f, 4f, 12f, 4f, new Vector3f(), ImmutableMap.of(Direction.EAST, new UVData(28f, 24f, 24f, 12f), Direction.UP, new UVData(44f, 49f, 40f, 45f), Direction.DOWN, new UVData(49f, 42f, 45f, 46f), Direction.NORTH, new UVData(28f, 12f, 24f, 0f), Direction.SOUTH, new UVData(20f, 36f, 16f, 24f), Direction.WEST, new UVData(24f, 36f, 20f, 24f)))
                .addBox(1.5f, -4f, 1f, 1f, 1f, 2.5f, new Vector3f(), ImmutableMap.of(Direction.EAST, new UVData(54.5f, 12f, 52f, 11f), Direction.UP, new UVData(33f, 11.5f, 32f, 9f), Direction.DOWN, new UVData(16f, 32f, 15f, 34.5f), Direction.NORTH, new UVData(4f, 56f, 3f, 55f), Direction.SOUTH, new UVData(5f, 56f, 4f, 55f), Direction.WEST, new UVData(37.5f, 54f, 35f, 53f)))
                .addBox(1.5f, -5f, 1f, 1f, 1f, 1.8f, new Vector3f(), ImmutableMap.of(Direction.EAST, new UVData(8f, 44f, 6f, 43f), Direction.UP, new UVData(43f, 20f, 42f, 18f), Direction.DOWN, new UVData(43f, 20f, 42f, 22f), Direction.NORTH, new UVData(6f, 56f, 5f, 55f), Direction.SOUTH, new UVData(7f, 56f, 6f, 55f), Direction.WEST, new UVData(20f, 53f, 18f, 52f))), PartPose.offset(4f, 22f, 0f));
        root.addOrReplaceChild("left_arm", GroupBuilder.create()
                .addBox(-4f, -10f, -2f, 4f, 12f, 4f, new Vector3f(), ImmutableMap.of(Direction.EAST, new UVData(4f, 40f, 0f, 28f), Direction.UP, new UVData(50f, 12f, 46f, 8f), Direction.DOWN, new UVData(50f, 12f, 46f, 16f), Direction.NORTH, new UVData(28f, 36f, 24f, 24f), Direction.SOUTH, new UVData(32f, 12f, 28f, 0f), Direction.WEST, new UVData(8f, 40f, 4f, 28f)))
                .addBox(-2.5f, -5f, 1f, 1f, 1f, 1.8f, new Vector3f(), ImmutableMap.of(Direction.EAST, new UVData(55f, 38f, 53f, 37f), Direction.UP, new UVData(33f, 44f, 32f, 42f), Direction.DOWN, new UVData(47f, 16f, 46f, 18f), Direction.NORTH, new UVData(8f, 56f, 7f, 55f), Direction.SOUTH, new UVData(56f, 12f, 55f, 11f), Direction.WEST, new UVData(40f, 54f, 38f, 53f)))
                .addBox(-2.5f, -4f, 1f, 1f, 1f, 2.5f, new Vector3f(), ImmutableMap.of(Direction.EAST, new UVData(55.5f, 36f, 53f, 35f), Direction.UP, new UVData(28f, 38.5f, 27f, 36f), Direction.DOWN, new UVData(33f, 39f, 32f, 41.5f), Direction.NORTH, new UVData(13f, 56f, 12f, 55f), Direction.SOUTH, new UVData(56f, 13f, 55f, 12f), Direction.WEST, new UVData(55.5f, 37f, 53f, 36f))), PartPose.offset(-4f, 22f, 0f));
        GroupDefinition right_leg = root.addOrReplaceChild("right_leg", GroupBuilder.create(), PartPose.offsetAndRotation(2f, 13f, 0f, 0f, -0.1309f, 0.1309f));
        GroupDefinition right_leg_shin = right_leg.addOrReplaceChild("right_leg_shin", GroupBuilder.create(), PartPose.offset(0f, -6f, -2f));
        GroupDefinition right_leg_ = right_leg_shin.addOrReplaceChild("right_leg_", GroupBuilder.create(), PartPose.offset(0f, -1f, 3f));
        GroupDefinition right_foot = right_leg_.addOrReplaceChild("right_foot", GroupBuilder.create(), PartPose.offset(0f, 0f, 0f));
        right_foot.addOrReplaceChild("right_foot", GroupBuilder.create()
                .addBox(-1.3f, -6f, -3.5f, 4f, 2f, 5f, new Vector3f(), ImmutableMap.of(Direction.EAST, new UVData(54f, 46f, 49f, 44f), Direction.UP, new UVData(24f, 49f, 20f, 44f), Direction.DOWN, new UVData(28f, 44f, 24f, 49f), Direction.NORTH, new UVData(56f, 11f, 52f, 9f), Direction.SOUTH, new UVData(18f, 54f, 14f, 52f), Direction.WEST, new UVData(55f, 14f, 50f, 12f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0f, 0f, -0.1309f));
        right_leg_.addOrReplaceChild("right_leg_", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4f, 6f, 4f, new Vector3f(), ImmutableMap.of(Direction.EAST, new UVData(45f, 45f, 41f, 39f), Direction.UP, new UVData(24f, 53f, 20f, 49f), Direction.DOWN, new UVData(28f, 49f, 24f, 53f), Direction.NORTH, new UVData(45f, 39f, 41f, 33f), Direction.SOUTH, new UVData(46f, 18f, 42f, 12f), Direction.WEST, new UVData(47f, 24f, 43f, 18f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0.1309f, 0f, 0f));
        right_leg_shin.addOrReplaceChild("right_leg_shin", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4f, 2f, 4f, new Vector3f(), ImmutableMap.of(Direction.EAST, new UVData(55f, 24f, 51f, 22f), Direction.UP, new UVData(51f, 24f, 47f, 20f), Direction.DOWN, new UVData(52f, 46f, 48f, 50f), Direction.NORTH, new UVData(55f, 22f, 51f, 20f), Direction.SOUTH, new UVData(55f, 52f, 51f, 50f), Direction.WEST, new UVData(56f, 9f, 52f, 7f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0.3491f, 0f, 0f));
        right_leg.addOrReplaceChild("right_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4f, 8f, 4f, new Vector3f(), ImmutableMap.of(Direction.EAST, new UVData(23f, 44f, 19f, 36f), Direction.UP, new UVData(48f, 50f, 44f, 46f), Direction.DOWN, new UVData(51f, 16f, 47f, 20f), Direction.NORTH, new UVData(19f, 44f, 15f, 36f), Direction.SOUTH, new UVData(27f, 44f, 23f, 36f), Direction.WEST, new UVData(37f, 44f, 33f, 36f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0.3491f, 0f, 0f));
        GroupDefinition left_leg = root.addOrReplaceChild("left_leg", GroupBuilder.create(), PartPose.offsetAndRotation(-2f, 13f, 0f, 0f, 0.1309f, -0.1309f));
        GroupDefinition left_leg_shin = left_leg.addOrReplaceChild("left_leg_shin", GroupBuilder.create(), PartPose.offset(0f, -6f, -2f));
        GroupDefinition left_leg_ = left_leg_shin.addOrReplaceChild("left_leg_", GroupBuilder.create(), PartPose.offset(0f, -1f, 3f));
        GroupDefinition left_foot = left_leg_.addOrReplaceChild("left_foot", GroupBuilder.create(), PartPose.offset(0f, 0f, 0f));
        left_foot.addOrReplaceChild("left_foot", GroupBuilder.create()
                .addBox(-2.7f, -6f, -3.5f, 4f, 2f, 5f, new Vector3f(), ImmutableMap.of(Direction.EAST, new UVData(19f, 52f, 14f, 50f), Direction.UP, new UVData(32f, 49f, 28f, 44f), Direction.DOWN, new UVData(36f, 44f, 32f, 49f), Direction.NORTH, new UVData(4f, 55f, 0f, 53f), Direction.SOUTH, new UVData(57f, 5f, 53f, 3f), Direction.WEST, new UVData(55f, 16f, 50f, 14f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0f, 0f, 0.1309f));
        left_leg_.addOrReplaceChild("left_leg_", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4f, 6f, 4f, new Vector3f(), ImmutableMap.of(Direction.EAST, new UVData(8f, 50f, 4f, 44f), Direction.UP, new UVData(53f, 36f, 49f, 32f), Direction.DOWN, new UVData(53f, 36f, 49f, 40f), Direction.NORTH, new UVData(4f, 50f, 0f, 44f), Direction.SOUTH, new UVData(16f, 50f, 12f, 44f), Direction.WEST, new UVData(20f, 50f, 16f, 44f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0.1309f, 0f, 0f));
        left_leg_shin.addOrReplaceChild("left_leg_shin", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4f, 2f, 4f, new Vector3f(), ImmutableMap.of(Direction.EAST, new UVData(51f, 54f, 47f, 52f), Direction.UP, new UVData(53f, 32f, 49f, 28f), Direction.DOWN, new UVData(36f, 49f, 32f, 53f), Direction.NORTH, new UVData(56f, 48f, 52f, 46f), Direction.SOUTH, new UVData(56f, 50f, 52f, 48f), Direction.WEST, new UVData(55f, 54f, 51f, 52f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0.3491f, 0f, 0f));
        left_leg.addOrReplaceChild("left_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4f, 8f, 4f, new Vector3f(), ImmutableMap.of(Direction.EAST, new UVData(12f, 45f, 8f, 37f), Direction.UP, new UVData(53f, 28f, 49f, 24f), Direction.DOWN, new UVData(32f, 49f, 28f, 53f), Direction.NORTH, new UVData(41f, 8f, 37f, 0f), Direction.SOUTH, new UVData(41f, 44f, 37f, 36f), Direction.WEST, new UVData(42f, 22f, 38f, 14f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0.3491f, 0f, 0f));
        root.addOrReplaceChild("armor_head", GroupBuilder.create()
                .addBox(-4f, 0f, -4f, 8f, 8f, 8f, new Vector3f(0.6f), ImmutableMap.of(Direction.EAST, new UVData(8f, 16f, 0f, 8f), Direction.UP, new UVData(16f, 8f, 8f, 0f), Direction.DOWN, new UVData(24f, 0f, 16f, 8f), Direction.NORTH, new UVData(16f, 16f, 8f, 8f), Direction.SOUTH, new UVData(32f, 16f, 24f, 8f), Direction.WEST, new UVData(24f, 16f, 16f, 8f))), PartPose.offset(0f, 24f, 0f));
        GroupDefinition armor_body = root.addOrReplaceChild("armor_body", GroupBuilder.create()
                .addBox(-4f, 0f, -2f, 8f, 12f, 4f, new Vector3f(0.6f), ImmutableMap.of(Direction.EAST, new UVData(32f, 32f, 28f, 20f), Direction.UP, new UVData(40f, 11f, 32f, 7f), Direction.DOWN, new UVData(40f, 34f, 32f, 38f), Direction.NORTH, new UVData(28f, 32f, 20f, 20f), Direction.SOUTH, new UVData(40f, 32f, 32f, 20f), Direction.WEST, new UVData(20f, 32f, 16f, 20f))), PartPose.offset(0f, 12f, 0f));
        GroupDefinition ar_tail = armor_body.addOrReplaceChild("ar_tail", GroupBuilder.create()
                .addBox(-2.4f, -2.4f, -3f, 4.8f, 4.8f, 7f, new Vector3f(0.6f), ImmutableMap.of(Direction.EAST, new UVData(8f, 20f, 4f, 16f), Direction.UP, new UVData(8f, 20f, 4f, 16f), Direction.DOWN, new UVData(8f, 16f, 4f, 20f), Direction.SOUTH, new UVData(8f, 20f, 4f, 16f), Direction.WEST, new UVData(8f, 20f, 4f, 16f))), PartPose.offsetAndRotation(0f, 0f, 2f, 0.7418f, 0f, 0f));
        GroupDefinition ar_tail_0 = ar_tail.addOrReplaceChild("ar_tail_0", GroupBuilder.create()
                .addBox(-2.1f, -1.8f, -0.5f, 4.2f, 4.2f, 6f, new Vector3f(0.6f), ImmutableMap.of(Direction.EAST, new UVData(8f, 20f, 4f, 16f), Direction.UP, new UVData(8f, 20f, 4f, 16f), Direction.DOWN, new UVData(8f, 16f, 4f, 20f), Direction.SOUTH, new UVData(8f, 20f, 4f, 16f), Direction.WEST, new UVData(8f, 20f, 4f, 16f))), PartPose.offsetAndRotation(0f, 0f, 4f, -0.1309f, 0f, 0f));
        GroupDefinition ar_tail_1 = ar_tail_0.addOrReplaceChild("ar_tail_1", GroupBuilder.create()
                .addBox(-1.8f, -1.3f, 0f, 3.6f, 3.7f, 5f, new Vector3f(0.6f), ImmutableMap.of(Direction.EAST, new UVData(8f, 20f, 4f, 16f), Direction.UP, new UVData(8f, 20f, 4f, 16f), Direction.DOWN, new UVData(8f, 16f, 4f, 20f), Direction.SOUTH, new UVData(8f, 20f, 4f, 16f), Direction.WEST, new UVData(8f, 20f, 4f, 16f))), PartPose.offsetAndRotation(0f, 0f, 5f, -0.1309f, 0f, 0f));
        GroupDefinition ar_tail_2 = ar_tail_1.addOrReplaceChild("ar_tail_2", GroupBuilder.create()
                .addBox(-1.5f, -1.3f, -0.5f, 3f, 3.2f, 4.5f, new Vector3f(0.6f), ImmutableMap.of(Direction.EAST, new UVData(8f, 20f, 4f, 16f), Direction.UP, new UVData(8f, 20f, 4f, 16f), Direction.DOWN, new UVData(8f, 16f, 4f, 20f), Direction.SOUTH, new UVData(8f, 20f, 4f, 16f), Direction.WEST, new UVData(8f, 20f, 4f, 16f))), PartPose.offsetAndRotation(0f, 0.5f, 5f, -0.0873f, 0f, 0f));
        ar_tail_2.addOrReplaceChild("ar_tail_3", GroupBuilder.create()
                .addBox(-1f, -1f, 0f, 2f, 2.4f, 3f, new Vector3f(0.6f), ImmutableMap.of(Direction.EAST, new UVData(8f, 20f, 4f, 16f), Direction.UP, new UVData(8f, 20f, 4f, 16f), Direction.DOWN, new UVData(8f, 16f, 4f, 20f), Direction.SOUTH, new UVData(8f, 20f, 4f, 16f), Direction.WEST, new UVData(8f, 21f, 4f, 16f))), PartPose.offsetAndRotation(0f, 0.5f, 4f, -0.0873f, 0f, 0f));
        root.addOrReplaceChild("armor_right_arm", GroupBuilder.create()
                .addBox(0f, -10f, -2f, 4f, 12f, 4f, new Vector3f(0.6f), ImmutableMap.of(Direction.EAST, new UVData(44f, 32f, 40f, 20f), Direction.UP, new UVData(48f, 20f, 44f, 16f), Direction.NORTH, new UVData(48f, 32f, 44f, 20f), Direction.SOUTH, new UVData(56f, 32f, 52f, 20f), Direction.WEST, new UVData(52f, 32f, 48f, 20f))), PartPose.offset(4f, 22f, 0f));
        root.addOrReplaceChild("armor_left_arm", GroupBuilder.create()
                .addBox(-4f, -10f, -2f, 4f, 12f, 4f, new Vector3f(0.6f), ImmutableMap.of(Direction.EAST, new UVData(52f, 32f, 48f, 20f), Direction.UP, new UVData(44f, 20f, 48f, 16f), Direction.NORTH, new UVData(56f, 32f, 52f, 20f), Direction.SOUTH, new UVData(48f, 32f, 44f, 20f), Direction.WEST, new UVData(40f, 32f, 44f, 20f))), PartPose.offset(-4f, 22f, 0f));
        GroupDefinition armor_right_leg = root.addOrReplaceChild("armor_right_leg", GroupBuilder.create(), PartPose.offsetAndRotation(2f, 13f, 0f, 0f, -0.1309f, 0.1309f));
        GroupDefinition ar_right_leg_shin = armor_right_leg.addOrReplaceChild("ar_right_leg_shin", GroupBuilder.create(), PartPose.offset(0f, -6f, -2f));
        ar_right_leg_shin.addOrReplaceChild("ar_right_leg_shin", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4f, 2f, 4f, new Vector3f(0.59f), ImmutableMap.of(Direction.EAST, new UVData(8f, 20f, 4f, 16f), Direction.UP, new UVData(8f, 20f, 4f, 16f), Direction.DOWN, new UVData(49f, 8f, 45f, 12f), Direction.NORTH, new UVData(8f, 20f, 4f, 16f), Direction.SOUTH, new UVData(8f, 20f, 4f, 16f), Direction.WEST, new UVData(8f, 20f, 4f, 16f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0.3491f, 0f, 0f));
        armor_right_leg.addOrReplaceChild("ar_right_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4f, 8f, 4f, new Vector3f(0.6f), ImmutableMap.of(Direction.EAST, new UVData(12f, 29f, 8f, 20f), Direction.UP, new UVData(8f, 20f, 4f, 16f), Direction.DOWN, new UVData(8f, 16f, 4f, 20f), Direction.NORTH, new UVData(8f, 29f, 4f, 20f), Direction.SOUTH, new UVData(16f, 29f, 12f, 20f), Direction.WEST, new UVData(12f, 29f, 8f, 20f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0.3491f, 0f, 0f));
        GroupDefinition armor_left_leg = root.addOrReplaceChild("armor_left_leg", GroupBuilder.create(), PartPose.offsetAndRotation(-2f, 13f, 0f, 0f, 0.1309f, -0.1309f));
        GroupDefinition ar_left_leg_shin = armor_left_leg.addOrReplaceChild("ar_left_leg_shin", GroupBuilder.create(), PartPose.offset(0f, -6f, -2f));
        ar_left_leg_shin.addOrReplaceChild("ar_left_leg_shin", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4f, 2f, 4f, new Vector3f(0.59f), ImmutableMap.of(Direction.EAST, new UVData(8f, 20f, 4f, 16f), Direction.UP, new UVData(8f, 20f, 4f, 16f), Direction.DOWN, new UVData(4f, 46f, 0f, 50f), Direction.NORTH, new UVData(53f, 26f, 49f, 24f), Direction.SOUTH, new UVData(8f, 20f, 4f, 16f), Direction.WEST, new UVData(4f, 20f, 8f, 16f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0.3491f, 0f, 0f));
        armor_left_leg.addOrReplaceChild("ar_left_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4f, 8f, 4f, new Vector3f(0.6f), ImmutableMap.of(Direction.EAST, new UVData(12f, 29f, 8f, 20f), Direction.UP, new UVData(8f, 20f, 4f, 16f), Direction.DOWN, new UVData(4f, 16f, 8f, 20f), Direction.NORTH, new UVData(4f, 29f, 8f, 20f), Direction.SOUTH, new UVData(16f, 29f, 12f, 20f), Direction.WEST, new UVData(12f, 29f, 8f, 20f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0.3491f, 0f, 0f));
        GroupDefinition armor_right_foot = root.addOrReplaceChild("armor_right_foot", GroupBuilder.create(), PartPose.offsetAndRotation(2f, 13f, 0f, 0f, -0.1309f, 0.1309f));
        GroupDefinition arf_right_leg_shin = armor_right_foot.addOrReplaceChild("arf_right_leg_shin", GroupBuilder.create(), PartPose.offset(0f, -6f, -2f));
        GroupDefinition arf_right_leg_ = arf_right_leg_shin.addOrReplaceChild("arf_right_leg_", GroupBuilder.create(), PartPose.offset(0f, -1f, 3f));
        GroupDefinition arf_right_foot = arf_right_leg_.addOrReplaceChild("arf_right_foot", GroupBuilder.create(), PartPose.offset(0f, 0f, 0f));
        arf_right_foot.addOrReplaceChild("arf_right_foot", GroupBuilder.create()
                .addBox(-1.3f, -6f, -3.5f, 4f, 2f, 5f, new Vector3f(0.6f), ImmutableMap.of(Direction.EAST, new UVData(12f, 20f, 8f, 16f), Direction.UP, new UVData(12f, 20f, 8f, 16f), Direction.DOWN, new UVData(12f, 16f, 8f, 20f), Direction.NORTH, new UVData(12f, 20f, 8f, 16f), Direction.SOUTH, new UVData(12f, 20f, 8f, 16f), Direction.WEST, new UVData(12f, 20f, 8f, 16f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0f, 0f, -0.1309f));
        arf_right_leg_.addOrReplaceChild("arf_right_leg_", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4f, 6f, 4f, new Vector3f(0.6f), ImmutableMap.of(Direction.EAST, new UVData(4f, 32f, 0f, 26f), Direction.UP, new UVData(49f, 16f, 45f, 12f), Direction.DOWN, new UVData(49f, 16f, 45f, 20f), Direction.NORTH, new UVData(8f, 32f, 4f, 26f), Direction.SOUTH, new UVData(8f, 32f, 4f, 26f), Direction.WEST, new UVData(4f, 32f, 0f, 26f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0.1309f, 0f, 0f));
        GroupDefinition armor_left_foot = root.addOrReplaceChild("armor_left_foot", GroupBuilder.create(), PartPose.offsetAndRotation(-2f, 13f, 0f, 0f, 0.1309f, -0.1309f));
        GroupDefinition arf_left_leg_shin = armor_left_foot.addOrReplaceChild("arf_left_leg_shin", GroupBuilder.create(), PartPose.offset(0f, -6f, -2f));
        GroupDefinition arf_left_leg_ = arf_left_leg_shin.addOrReplaceChild("arf_left_leg_", GroupBuilder.create(), PartPose.offset(0f, -1f, 3f));
        GroupDefinition arf_left_foot = arf_left_leg_.addOrReplaceChild("arf_left_foot", GroupBuilder.create(), PartPose.offset(0f, 0f, 0f));
        arf_left_foot.addOrReplaceChild("arf_left_foot", GroupBuilder.create()
                .addBox(-2.7f, -6f, -3.5f, 4f, 2f, 5f, new Vector3f(0.6f), ImmutableMap.of(Direction.EAST, new UVData(12f, 20f, 8f, 16f), Direction.UP, new UVData(12f, 20f, 8f, 16f), Direction.DOWN, new UVData(8f, 16f, 12f, 20f), Direction.NORTH, new UVData(8f, 20f, 12f, 16f), Direction.SOUTH, new UVData(12f, 20f, 8f, 16f), Direction.WEST, new UVData(12f, 16f, 8f, 20f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0f, 0f, 0.1309f));
        arf_left_leg_.addOrReplaceChild("arf_left_leg_", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4f, 6f, 4f, new Vector3f(0.6f), ImmutableMap.of(Direction.EAST, new UVData(4f, 32f, 0f, 26f), Direction.UP, new UVData(8f, 50f, 4f, 46f), Direction.DOWN, new UVData(27f, 46f, 23f, 50f), Direction.NORTH, new UVData(4f, 32f, 8f, 26f), Direction.SOUTH, new UVData(4f, 32f, 0f, 26f), Direction.WEST, new UVData(0f, 32f, 4f, 26f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0.1309f, 0f, 0f));

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
        animate(tail,Animations.DRAGON_TAIL_ANIM,ageInTicks);
    }
}