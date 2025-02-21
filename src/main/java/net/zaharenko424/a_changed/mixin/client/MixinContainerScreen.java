package net.zaharenko424.a_changed.mixin.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.zaharenko424.a_changed.util.AbilityUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractContainerScreen.class)
public abstract class MixinContainerScreen {

    /**
     * Disallow swapping hands when DL Pup.
     */
    @WrapWithCondition(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;slotClicked(Lnet/minecraft/world/inventory/Slot;IILnet/minecraft/world/inventory/ClickType;)V"),
            method = "checkHotbarMouseClicked")
    private boolean onCheckHotbarMouseClicked(AbstractContainerScreen<?> instance, Slot slot, int slotId, int mouseButton, ClickType type){
        return !AbilityUtils.hasDLPupAbilities(Minecraft.getInstance().player);
    }

    @WrapWithCondition(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;slotClicked(Lnet/minecraft/world/inventory/Slot;IILnet/minecraft/world/inventory/ClickType;)V"),
            method = "checkHotbarKeyPressed")
    private boolean onCheckHotbarKeyPressed(AbstractContainerScreen<?> instance, Slot slot, int slotId, int mouseButton, ClickType type){
        return !AbilityUtils.hasDLPupAbilities(Minecraft.getInstance().player);
    }
}