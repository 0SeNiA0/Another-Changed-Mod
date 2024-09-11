package net.zaharenko424.a_changed.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.registry.ArmorMaterialRegistry;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.zaharenko424.a_changed.AChanged.LATEX_RESISTANCE;

@ParametersAreNonnullByDefault
public class HazmatArmorItem extends ArmorItem {

    private static final float[] latexResistance = new float[]{.15f, .2f, .25f, .15f};

    public HazmatArmorItem(Type p_266831_, Properties p_40388_) {
        super(ArmorMaterialRegistry.HAZMAT, p_266831_, p_40388_.rarity(Rarity.UNCOMMON).setNoRepair());
    }

    @Override
    public @NotNull ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        EquipmentSlot slot = type.getSlot();
        if(slot == EquipmentSlot.BODY) return super.getDefaultAttributeModifiers(stack);
        return super.getDefaultAttributeModifiers(stack)
                .withModifierAdded(LATEX_RESISTANCE, new AttributeModifier(AChanged.resourceLoc("hazmat_latex_resistance"),
                        latexResistance[slot.ordinal() - 2], AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(slot));
    }
}