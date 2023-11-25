package net.zaharenko424.testmod.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.zaharenko424.testmod.client.model.DummyModel;
import net.zaharenko424.testmod.client.model.LatexWolfFemaleModel;
import net.zaharenko424.testmod.client.model.LatexWolfMaleModel;
import net.zaharenko424.testmod.client.renderer.LatexEntityRenderer;
import org.jetbrains.annotations.NotNull;

import static net.zaharenko424.testmod.TestMod.*;
import static net.zaharenko424.testmod.registry.EntityRegistry.*;
import static net.zaharenko424.testmod.registry.TransfurRegistry.*;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientMod {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        LOGGER.info("HELLO FROM CLIENT SETUP");
        LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.@NotNull RegisterRenderers event){
        event.registerEntityRenderer(WHITE_LATEX_WOLF_MALE.get(),(a) -> new LatexEntityRenderer<>(a, WHITE_LATEX_WOLF_M_TF));
        event.registerEntityRenderer(WHITE_LATEX_WOLF_FEMALE.get(),(a) -> new LatexEntityRenderer<>(a, WHITE_LATEX_WOLF_F_TF));

        event.registerEntityRenderer(DARK_LATEX_WOLF_MALE.get(),(a) -> new LatexEntityRenderer<>(a, DARK_LATEX_WOLF_M_TF));
        event.registerEntityRenderer(DARK_LATEX_WOLF_FEMALE.get(),(a) -> new LatexEntityRenderer<>(a, DARK_LATEX_WOLF_F_TF));
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.@NotNull RegisterLayerDefinitions event){
        event.registerLayerDefinition(new ModelLayerLocation(new ResourceLocation(MODID,"dummy"),"main"), DummyModel::createBodyLayer);
        event.registerLayerDefinition(LatexWolfMaleModel.modelLayerLocation(), LatexWolfMaleModel::createBodyLayer);
        event.registerLayerDefinition(LatexWolfMaleModel.armorLayerLocation(), LatexWolfMaleModel::createArmorLayer);
        event.registerLayerDefinition(LatexWolfFemaleModel.modelLayerLocation(), LatexWolfFemaleModel::createBodyLayer);
        event.registerLayerDefinition(LatexWolfFemaleModel.armorLayerLocation(), LatexWolfFemaleModel::createArmorLayer);
    }
}