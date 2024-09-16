package net.zaharenko424.a_changed.client.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.registry.ItemRegistry;

public class HazmatOverlay {

    private static final ResourceLocation TEXTURE = AChanged.textureLoc("overlay/hazmat");

    public static final LayeredDraw.Layer OVERLAY = (guiGraphics, partialTick) -> {
        Minecraft minecraft = Minecraft.getInstance();
        if(!minecraft.player.getItemBySlot(EquipmentSlot.HEAD).is(ItemRegistry.HAZMAT_HELMET.get()) || !minecraft.options.getCameraType().isFirstPerson()) return;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f,1f,1f,1f);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int screenWidth = guiGraphics.guiWidth();
        int screenHeight = guiGraphics.guiHeight();
        guiGraphics.blit(TEXTURE,0,0,0,0, screenWidth, screenHeight, screenWidth, screenHeight);
    };
}