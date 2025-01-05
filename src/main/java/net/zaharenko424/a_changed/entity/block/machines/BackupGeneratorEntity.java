package net.zaharenko424.a_changed.entity.block.machines;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.zaharenko424.a_changed.block.blocks.BackupGenerator;
import net.zaharenko424.a_changed.capability.energy.ExtendedEnergyStorage;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import net.zaharenko424.a_changed.util.StateProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BackupGeneratorEntity extends BlockEntity {

    ExtendedEnergyStorage energyStorage = new ExtendedEnergyStorage(32, 0, 32);

    public BackupGeneratorEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.BACKUP_GENERATOR_ENTITY.get(), pos, blockState);
    }

    public void tick(){
        if(!getBlockState().getValue(StateProperties.ACTIVE)) return;
        if(energyStorage.getEnergyStored() < energyStorage.getMaxExtract()) energyStorage.addEnergy(energyStorage.getMaxExtract());

        BlockEntity entity;
        BlockPos pos;
        for(Direction direction : Direction.values()){
            if(energyStorage.isEmpty()) break;
            pos = worldPosition.relative(direction);
            entity = level.getBlockEntity(pos);
            if(entity == null) continue;
            if(entity instanceof AbstractProxyWire wire){
                wire.tickNetwork();
                continue;
            }
            if(energyStorage.transferEnergyTo(
                    level.getCapability(Capabilities.EnergyStorage.BLOCK, pos, direction.getOpposite()),
                    energyStorage.getMaxExtract(), false) != 0) {
                if(entity instanceof AbstractMachineEntity<?, ?> machineEntity) machineEntity.update();
            }
        }
    }

    public @Nullable <CT> CT getCapability(@NotNull BlockCapability<CT, ?> cap, @Nullable Direction direction) {
        if(cap == Capabilities.EnergyStorage.BLOCK && direction != getBlockState().getValue(BackupGenerator.FACING)){
            return (CT) energyStorage;
        }
        return null;
    }
}