package net.zaharenko424.a_changed.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.zaharenko424.a_changed.entity.block.StructureRandomizable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(StructureTemplate.class)
public class MixinStructureTemplate {

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/BlockEntity;loadWithComponents(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/core/HolderLookup$Provider;)V"),
            method = "placeInWorld")
    private void seedAndUnpackRandomizable(BlockEntity instance, CompoundTag tag, HolderLookup.Provider registries, Operation<Void> original, @Local(argsOnly = true) RandomSource random){
        original.call(instance, tag, registries);

        if(instance instanceof StructureRandomizable randomizable && randomizable.getLootTable() != null){
            randomizable.setLootTableSeed(random.nextLong());
            randomizable.unpackLootTable();
        }
    }
}
