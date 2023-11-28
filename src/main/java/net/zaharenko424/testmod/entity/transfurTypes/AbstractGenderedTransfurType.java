package net.zaharenko424.testmod.entity.transfurTypes;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractGenderedTransfurType extends AbstractTransfurType{

    protected final boolean male;

    public AbstractGenderedTransfurType(@NotNull ResourceLocation resourceLocation, boolean male) {
        super(resourceLocation);
        this.male=male;
    }
}