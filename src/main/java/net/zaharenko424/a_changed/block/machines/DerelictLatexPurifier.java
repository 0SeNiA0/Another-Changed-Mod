package net.zaharenko424.a_changed.block.machines;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import org.jetbrains.annotations.NotNull;

public class DerelictLatexPurifier extends AbstractDerelictMachine {

    public DerelictLatexPurifier(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<? extends DerelictLatexPurifier> codec() {
        return simpleCodec(DerelictLatexPurifier::new);
    }

    @Override
    ItemStack getDrop(@NotNull RandomSource random) {
        return ItemRegistry.LATEX_PURIFIER_COMPONENTS.toStack(random.nextFloat() > .9 ? 2 : 1);
    }
}