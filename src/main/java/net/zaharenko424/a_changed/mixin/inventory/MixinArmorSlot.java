package net.zaharenko424.a_changed.mixin.inventory;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ArmorSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.ItemStack;
import net.zaharenko424.a_changed.ArmorSlotAccess;
import net.zaharenko424.a_changed.util.AbilityUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ArmorSlot.class)
public abstract class MixinArmorSlot extends Slot implements ArmorSlotAccess {

    @Shadow @Final
    private LivingEntity owner;
    @Shadow @Final
    private EquipmentSlot slot;

    public MixinArmorSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @ModifyReturnValue(at = @At("TAIL"), method = "mayPlace")
    private boolean onMayPlace(boolean original, @Local(argsOnly = true) ItemStack stack){
        if(owner instanceof Player && AbilityUtils.hasLatexPupAbilities(owner)){
            return slot == EquipmentSlot.FEET && stack.getItem() instanceof AnimalArmorItem armor && armor.getBodyType() == AnimalArmorItem.BodyType.CANINE;
        }
        return original;
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