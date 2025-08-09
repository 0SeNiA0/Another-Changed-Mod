package net.zaharenko424.a_changed.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
public class LabRotProcessor extends StructureProcessor {

    public static final MapCodec<LabRotProcessor> CODEC = RecordCodecBuilder.mapCodec(
            builder -> builder.group(
                            Codec.floatRange(0.0F, 1.0F).fieldOf("integrity").forGetter(processor -> processor.integrity),
                            RegistryCodecs.homogeneousList(Registries.BLOCK).optionalFieldOf("protected_blocks").forGetter(processor -> processor.protectedBlocks),
                            RegistryCodecs.homogeneousList(Registries.BLOCK).optionalFieldOf("rottable_blocks").forGetter(processor -> processor.rottableBlocks)
                    ).apply(builder, LabRotProcessor::new)
    );

    private final Optional<HolderSet<Block>> protectedBlocks;
    private final Optional<HolderSet<Block>> rottableBlocks;
    private final float integrity;

    public LabRotProcessor(float integrity) {
        this(integrity, Optional.empty(), Optional.empty());
    }

    public LabRotProcessor(float integrity, HolderSet<Block> protectedBlocks) {
        this(integrity, Optional.of(protectedBlocks), Optional.empty());
    }

    public LabRotProcessor(float integrity, Optional<HolderSet<Block>> protectedBlocks, Optional<HolderSet<Block>> rottableBlocks) {
        this.integrity = integrity;
        this.protectedBlocks = protectedBlocks;
        this.rottableBlocks = rottableBlocks;
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(
            LevelReader level,
            BlockPos offset,
            BlockPos pos,
            StructureTemplate.StructureBlockInfo blockInfo,
            StructureTemplate.StructureBlockInfo relativeBlockInfo,
            StructurePlaceSettings settings
    ) {
        if(protectedBlocks.isPresent() && blockInfo.state().is(protectedBlocks.get())) return relativeBlockInfo;

        RandomSource randomsource = settings.getRandom(relativeBlockInfo.pos());
        return (rottableBlocks.isEmpty() || blockInfo.state().is(rottableBlocks.get())) && !(randomsource.nextFloat() <= this.integrity)
                ? null
                : relativeBlockInfo;
    }

    @Override
    protected @NotNull StructureProcessorType<?> getType() {
        return AChanged.LAB_ROT_PROCESSOR.get();
    }
}
