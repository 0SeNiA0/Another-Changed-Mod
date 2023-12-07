package net.zaharenko424.testmod.client.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.gui.overlay.IGuiOverlay;
import net.zaharenko424.testmod.TestMod;
import net.zaharenko424.testmod.registry.ItemRegistry;
@OnlyIn(Dist.CLIENT)
public class HazmatOverlay {
    private static final ResourceLocation HAZMAT_OVERLAY = TestMod.resourceLoc("textures/overlay/hazmat.png");

    public static final IGuiOverlay OVERLAY = (((gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        Minecraft minecraft=gui.getMinecraft();
        if(!minecraft.player.getItemBySlot(EquipmentSlot.HEAD).is(ItemRegistry.HAZMAT_HELMET.get())||!minecraft.options.getCameraType().isFirstPerson()) return;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f,1f,1f,1f);
        RenderSystem.setShaderTexture(0,HAZMAT_OVERLAY);
        guiGraphics.blit(HAZMAT_OVERLAY,0,0,0,0,screenWidth,screenHeight,screenWidth,screenHeight);
    }));
}