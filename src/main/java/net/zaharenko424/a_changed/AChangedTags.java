package net.zaharenko424.a_changed;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public final class AChangedTags {

    public static final class Block {

        public static final TagKey<net.minecraft.world.level.block.Block> LATEX_RESISTANT = BlockTags.create(AChanged.resourceLoc("latex_resistant"));
        public static final TagKey<net.minecraft.world.level.block.Block> LASER_TRANSPARENT = BlockTags.create(AChanged.resourceLoc("laser_transparent"));
        public static final TagKey<net.minecraft.world.level.block.Block> LAB_ROT_PROTECTED = BlockTags.create(AChanged.resourceLoc("lab_rot_protected"));
    }

    public static final class Entity {

        public static final TagKey<EntityType<?>> TRANSFURRABLE_TAG = TagKey.create(Registries.ENTITY_TYPE, AChanged.resourceLoc("transfurrable"));
        public static final TagKey<EntityType<?>> SEWAGE_SYSTEM_CONSUMABLE = TagKey.create(Registries.ENTITY_TYPE, AChanged.resourceLoc("sewage_system_consumable"));
    }
}
