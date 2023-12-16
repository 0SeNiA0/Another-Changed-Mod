package net.zaharenko424.a_changed.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;
@ParametersAreNonnullByDefault
public enum ArmorMaterials implements ArmorMaterial {

    HAZMAT("hazmat",20,new int[]{2, 4, 3, 2},new float[]{.15f,.25f,.2f,.15f},15, SoundEvents.ARMOR_EQUIP_LEATHER,1,
            .1f, ()->Ingredient.EMPTY);

    private final String name;
    private final int durabilityMultiplier;
    private final int[] protection;
    private final float[] latexProtection;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    ArmorMaterials(String name, int durabilityMult, int[] protection, float[] latexProtection, int enchValue, SoundEvent sound,
                   float toughness, float knockbackRes, Supplier<Ingredient> repairIngredient){
        this.name = name;
        this.durabilityMultiplier = durabilityMult;
        this.protection = protection;
        this.latexProtection=latexProtection;
        this.enchantmentValue = enchValue;
        this.sound = sound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackRes;
        this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type p_266807_) {
        return 12*durabilityMultiplier;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type p_267168_) {
        return protection[p_267168_.ordinal()];
    }

    public float getLatexProtection(ArmorItem.Type type) {
        return latexProtection[type.ordinal()];
    }

    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    @Override
    public @NotNull SoundEvent getEquipSound() {
        return sound;
    }

    @Override
    public @NotNull Ingredient getRepairIngredient() {
        return repairIngredient.get();
    }

    @Override
    public @NotNull String getName() {
        return AChanged.MODID+":"+name;
    }

    @Override
    public float getToughness() {
        return toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return knockbackResistance;
    }
}