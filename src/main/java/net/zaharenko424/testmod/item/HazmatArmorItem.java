package net.zaharenko424.testmod.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.Util;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumMap;
import java.util.UUID;

import static net.zaharenko424.testmod.TestMod.LATEX_RESISTANCE;

@ParametersAreNonnullByDefault
public class HazmatArmorItem extends ArmorItem {

    private static final EnumMap<Type, UUID> UUIDS = Util.make(new EnumMap<>(ArmorItem.Type.class), p_266744_ -> {
        p_266744_.put(ArmorItem.Type.BOOTS, UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"));
        p_266744_.put(ArmorItem.Type.LEGGINGS, UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"));
        p_266744_.put(ArmorItem.Type.CHESTPLATE, UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"));
        p_266744_.put(ArmorItem.Type.HELMET, UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"));
    });

    public HazmatArmorItem(Type p_266831_, Properties p_40388_) {
        super(ArmorMaterials.HAZMAT, p_266831_, p_40388_.rarity(Rarity.UNCOMMON));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if(this.type.getSlot()==slot){
            HashMultimap<Attribute, AttributeModifier> map= HashMultimap.create(super.getAttributeModifiers(slot,stack));
            map.put(LATEX_RESISTANCE.get(),new AttributeModifier(UUIDS.get(type),"Latex Resistance",((ArmorMaterials)material).getLatexProtection(type), AttributeModifier.Operation.ADDITION));
            return ImmutableMultimap.copyOf(map);
        }
        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return false;
    }
}