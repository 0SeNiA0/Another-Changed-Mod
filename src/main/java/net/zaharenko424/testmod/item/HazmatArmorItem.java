package net.zaharenko424.testmod.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class HazmatArmorItem extends ArmorItem {
    public HazmatArmorItem(Type p_266831_, Properties p_40388_) {
        super(ArmorMaterials.HAZMAT, p_266831_, p_40388_.rarity(Rarity.UNCOMMON));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if(this.type.getSlot()==slot){
            HashMultimap<Attribute, AttributeModifier> map= HashMultimap.create(getDefaultAttributeModifiers(slot));
            //TODO add latex protection modifier
            return ImmutableMultimap.copyOf(map);
        }
        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return false;
    }
}