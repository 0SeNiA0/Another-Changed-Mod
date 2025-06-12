package net.zaharenko424.a_changed.mixin.inventory;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ArmorSlot;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.ArmorSlotAccess;
import net.zaharenko424.a_changed.util.AbilityUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ArmorSlot.class)
public abstract class MixinArmorSlot extends Slot implements ArmorSlotAccess {

    @Unique
    private static final Pair<ResourceLocation, ResourceLocation> EMPTY_ELYTRA_SLOT = new Pair<>(InventoryMenu.BLOCK_ATLAS, AChanged.resourceLoc("item/empty_elytra_slot"));
    @Unique
    private static final Pair<ResourceLocation, ResourceLocation> EMPTY_BODY_ARMOR_SLOT = new Pair<>(InventoryMenu.BLOCK_ATLAS, AChanged.resourceLoc("item/empty_body_armor_slot"));

    @Shadow @Final
    private LivingEntity owner;
    @Shadow @Final
    private EquipmentSlot slot;

    public MixinArmorSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @ModifyReturnValue(at = @At("RETURN"), method = "mayPlace")
    private boolean onMayPlace(boolean original, @Local(argsOnly = true) ItemStack stack){
        if(!(owner instanceof Player) || !AbilityUtils.hasLatexPupAbilities(owner)) return original;

        return switch (slot){
            case CHEST -> stack.is(Items.ELYTRA);
            case FEET -> stack.getItem() instanceof AnimalArmorItem armor && armor.getBodyType() == AnimalArmorItem.BodyType.CANINE;
            default -> false;
        };
    }

    @ModifyReturnValue(at = @At("RETURN"), method = "getNoItemIcon")
    private Pair<ResourceLocation, ResourceLocation> onGetNoItemIcon(Pair<ResourceLocation, ResourceLocation> original){
        if(!(owner instanceof Player) || !AbilityUtils.hasLatexPupAbilities(owner)) return original;

        return switch (slot){
            case CHEST -> EMPTY_ELYTRA_SLOT;
            case FEET -> EMPTY_BODY_ARMOR_SLOT;
            default -> null;
        };
    }

    @Override
    public LivingEntity achanged$owner() {
        return owner;
    }

    @Override
    public EquipmentSlot achanged$slot() {
        return slot;
    }
}