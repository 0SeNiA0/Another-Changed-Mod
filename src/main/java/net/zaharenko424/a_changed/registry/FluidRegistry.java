package net.zaharenko424.a_changed.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static net.zaharenko424.a_changed.AChanged.MODID;
import static net.zaharenko424.a_changed.AChanged.resourceLoc;

public class FluidRegistry {

    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES,MODID);
    public static final DeferredRegister<net.minecraft.world.level.material.Fluid> FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUID,MODID);

    //Fluid types
    public static final DeferredHolder<FluidType,FluidType> LATEX_SOLVENT_TYPE = FLUID_TYPES.register("latex_solvent",() -> new FluidType(FluidType.Properties.create()) {
        @Override
        public void initializeClient(@NotNull Consumer<IClientFluidTypeExtensions> consumer) {
            consumer.accept(new IClientFluidTypeExtensions(){
                @Override
                public @NotNull ResourceLocation getStillTexture() {
                    return resourceLoc("block/latex_solvent_still");
                }

                @Override
                public @NotNull ResourceLocation getFlowingTexture() {
                    return resourceLoc("block/latex_solvent_flowing");
                }
            });
        }
    });

    public static final DeferredHolder<FluidType,FluidType> WHITE_LATEX_TYPE = FLUID_TYPES.register("white_latex",() -> new FluidType(FluidType.Properties.create()) {
        @Override
        public void initializeClient(@NotNull Consumer<IClientFluidTypeExtensions> consumer) {
            consumer.accept(new IClientFluidTypeExtensions(){
                @Override
                public @NotNull ResourceLocation getStillTexture() {
                    return resourceLoc("block/white_latex_still");
                }

                @Override
                public @NotNull ResourceLocation getFlowingTexture() {
                    return resourceLoc("block/white_latex_flowing");
                }
            });
        }
    });

    public static final DeferredHolder<FluidType,FluidType> DARK_LATEX_TYPE = FLUID_TYPES.register("dark_latex",() -> new FluidType(FluidType.Properties.create()) {
        @Override
        public void initializeClient(@NotNull Consumer<IClientFluidTypeExtensions> consumer) {
            consumer.accept(new IClientFluidTypeExtensions(){
                @Override
                public @NotNull ResourceLocation getStillTexture() {
                    return resourceLoc("block/dark_latex_still");
                }

                @Override
                public @NotNull ResourceLocation getFlowingTexture() {
                    return resourceLoc("block/dark_latex_flowing");
                }
            });
        }
    });

    //Fluids
    public static final DeferredHolder<Fluid, BaseFlowingFluid> LATEX_SOLVENT_STILL = FLUIDS.register("latex_solvent", ()-> new BaseFlowingFluid.Source(solventProp()));
    public static final DeferredHolder<Fluid, BaseFlowingFluid> LATEX_SOLVENT_FLOWING = FLUIDS.register("latex_solvent_flowing", () -> new BaseFlowingFluid.Flowing(solventProp()));

    public static final DeferredHolder<Fluid, BaseFlowingFluid> WHITE_LATEX_STILL = FLUIDS.register("white_latex", ()-> new BaseFlowingFluid.Source(whiteLatexProp()));
    public static final DeferredHolder<Fluid, BaseFlowingFluid> WHITE_LATEX_FLOWING = FLUIDS.register("white_latex_flowing", () -> new BaseFlowingFluid.Flowing(whiteLatexProp()));

    public static final DeferredHolder<Fluid, BaseFlowingFluid> DARK_LATEX_STILL = FLUIDS.register("dark_latex", ()-> new BaseFlowingFluid.Source(darkLatexProp()));
    public static final DeferredHolder<Fluid, BaseFlowingFluid> DARK_LATEX_FLOWING = FLUIDS.register("dark_latex_flowing", () -> new BaseFlowingFluid.Flowing(darkLatexProp()));

    private static BaseFlowingFluid.Properties solventProp(){
        return new BaseFlowingFluid.Properties(LATEX_SOLVENT_TYPE,LATEX_SOLVENT_STILL,LATEX_SOLVENT_FLOWING).block(BlockRegistry.LATEX_SOLVENT_BLOCK).bucket(ItemRegistry.LATEX_SOLVENT_BUCKET);
    }

    private static BaseFlowingFluid.@NotNull Properties whiteLatexProp(){
        return new BaseFlowingFluid.Properties(WHITE_LATEX_TYPE,WHITE_LATEX_STILL,WHITE_LATEX_FLOWING).block(BlockRegistry.WHITE_LATEX_FLUID_BLOCK).bucket(ItemRegistry.WHITE_LATEX_BUCKET);
    }

    private static BaseFlowingFluid.@NotNull Properties darkLatexProp(){
        return new BaseFlowingFluid.Properties(DARK_LATEX_TYPE,DARK_LATEX_STILL,DARK_LATEX_FLOWING).block(BlockRegistry.DARK_LATEX_FLUID_BLOCK).bucket(ItemRegistry.DARK_LATEX_BUCKET);
    }
}