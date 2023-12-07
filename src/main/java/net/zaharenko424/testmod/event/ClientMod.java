package net.zaharenko424.testmod.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.FoliageColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiOverlaysEvent;
import net.zaharenko424.testmod.TestMod;
import net.zaharenko424.testmod.client.model.DummyModel;
import net.zaharenko424.testmod.client.model.LatexWolfFemaleModel;
import net.zaharenko424.testmod.client.model.LatexWolfMaleModel;
import net.zaharenko424.testmod.client.overlay.HazmatOverlay;
import net.zaharenko424.testmod.client.renderer.ChairRenderer;
import net.zaharenko424.testmod.client.renderer.LatexEntityRenderer;
import net.zaharenko424.testmod.registry.BlockRegistry;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.zaharenko424.testmod.TestMod.LOGGER;
import static net.zaharenko424.testmod.TestMod.MODID;
import static net.zaharenko424.testmod.registry.EntityRegistry.*;
import static net.zaharenko424.testmod.registry.TransfurRegistry.*;
@ParametersAreNonnullByDefault
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientMod {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        LOGGER.info("HELLO FROM CLIENT SETUP");
        LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }

    @SubscribeEvent
    public static void onRegisterGui(RegisterGuiOverlaysEvent event){
        event.registerBelowAll(TestMod.resourceLoc("hazmat"), HazmatOverlay.OVERLAY);
    }

    @SubscribeEvent
    public static void onRegisterBlockColorHandlers(RegisterColorHandlersEvent.Block event){
        event.register((state,tintGetter,pos,d)->
                tintGetter != null && pos != null ? BiomeColors.getAverageFoliageColor(tintGetter, pos)
                : FoliageColor.getDefaultColor(), BlockRegistry.ORANGE_LEAVES.get());
    }

    @SubscribeEvent
    public static void onRegisterColorHandlers(RegisterColorHandlersEvent.Item event){
        event.register((itemStack,i)->
                event.getBlockColors().getColor(((BlockItem)itemStack.getItem()).getBlock().defaultBlockState()
                , null, null, i), BlockRegistry.ORANGE_LEAVES);
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(CHAIR_ENTITY.get(), ChairRenderer::new);//Dummy renderer

        event.registerEntityRenderer(WHITE_LATEX_WOLF_MALE.get(),(a) -> new LatexEntityRenderer<>(a, WHITE_LATEX_WOLF_M_TF));
        event.registerEntityRenderer(WHITE_LATEX_WOLF_FEMALE.get(),(a) -> new LatexEntityRenderer<>(a, WHITE_LATEX_WOLF_F_TF));

        event.registerEntityRenderer(DARK_LATEX_WOLF_MALE.get(),(a) -> new LatexEntityRenderer<>(a, DARK_LATEX_WOLF_M_TF));
        event.registerEntityRenderer(DARK_LATEX_WOLF_FEMALE.get(),(a) -> new LatexEntityRenderer<>(a, DARK_LATEX_WOLF_F_TF));
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(new ModelLayerLocation(new ResourceLocation(MODID,"dummy"),"main"), DummyModel::createBodyLayer);
        event.registerLayerDefinition(LatexWolfMaleModel.modelLayerLocation(), LatexWolfMaleModel::createBodyLayer);
        event.registerLayerDefinition(LatexWolfMaleModel.armorLayerLocation(), LatexWolfMaleModel::createArmorLayer);
        event.registerLayerDefinition(LatexWolfFemaleModel.modelLayerLocation(), LatexWolfFemaleModel::createBodyLayer);
        event.registerLayerDefinition(LatexWolfFemaleModel.armorLayerLocation(), LatexWolfFemaleModel::createArmorLayer);
    }
}