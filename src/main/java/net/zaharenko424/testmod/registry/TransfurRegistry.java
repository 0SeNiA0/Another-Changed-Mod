package net.zaharenko424.testmod.registry;

import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.testmod.entity.transfurTypes.AbstractTransfurType;
import net.zaharenko424.testmod.entity.transfurTypes.LatexWolf;

import static net.zaharenko424.testmod.TestMod.MODID;
import static net.zaharenko424.testmod.TestMod.resourceLocation;

public final class TransfurRegistry {

    public static final DeferredRegister<AbstractTransfurType> TRANSFUR_TYPES = DeferredRegister.create(resourceLocation("transfur_registry"),MODID);
    public static final Registry<AbstractTransfurType> TRANSFUR_REGISTRY = TRANSFUR_TYPES.makeRegistry(builder->{});

    //Transfur types
    public static final DeferredHolder<AbstractTransfurType, LatexWolf> WHITE_LATEX_WOLF_M_TF = TRANSFUR_TYPES.register("white_latex_wolf_male", ()-> new LatexWolf(resourceLocation("white_latex_wolf_male"),true));
    public static final DeferredHolder<AbstractTransfurType, LatexWolf> WHITE_LATEX_WOLF_F_TF = TRANSFUR_TYPES.register("white_latex_wolf_female", ()-> new LatexWolf(resourceLocation("white_latex_wolf_female"),true));

    public static final DeferredHolder<AbstractTransfurType, LatexWolf> DARK_LATEX_WOLF_M_TF = TRANSFUR_TYPES.register("dark_latex_wolf_male", ()-> new LatexWolf(resourceLocation("dark_latex_wolf_male"),true));
    public static final DeferredHolder<AbstractTransfurType, LatexWolf> DARK_LATEX_WOLF_F_TF = TRANSFUR_TYPES.register("dark_latex_wolf_female", ()-> new LatexWolf(resourceLocation("dark_latex_wolf_female"),false));

}