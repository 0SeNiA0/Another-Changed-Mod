package net.zaharenko424.a_changed.event;

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
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.model.DummyModel;
import net.zaharenko424.a_changed.client.model.BeiFengModel;
import net.zaharenko424.a_changed.client.model.LatexWolfFemaleModel;
import net.zaharenko424.a_changed.client.model.LatexWolfMaleModel;
import net.zaharenko424.a_changed.client.overlay.HazmatOverlay;
import net.zaharenko424.a_changed.client.overlay.TransfurOverlay;
import net.zaharenko424.a_changed.client.particle.BlueGasParticle;
import net.zaharenko424.a_changed.client.renderer.ChairRenderer;
import net.zaharenko424.a_changed.client.renderer.LatexEntityRenderer;
import net.zaharenko424.a_changed.client.renderer.blockEntity.BookStackRenderer;
import net.zaharenko424.a_changed.client.renderer.blockEntity.LatexContainerRenderer;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import net.zaharenko424.a_changed.registry.BlockRegistry;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.zaharenko424.a_changed.AChanged.*;
import static net.zaharenko424.a_changed.registry.EntityRegistry.*;
import static net.zaharenko424.a_changed.registry.TransfurRegistry.*;
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
        event.registerBelowAll(AChanged.resourceLoc("hazmat"), HazmatOverlay.OVERLAY);
        event.registerBelowAll(AChanged.resourceLoc("transfur"), TransfurOverlay.OVERLAY);
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
    public static void onRegisterParticleProviders(RegisterParticleProvidersEvent event){
        event.registerSpriteSet(BLUE_GAS_PARTICLE.get(), BlueGasParticle.Provider::new);
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(BlockEntityRegistry.BOOK_STACK_ENTITY.get(), BookStackRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.LATEX_CONTAINER_ENTITY.get(), LatexContainerRenderer::new);

        event.registerEntityRenderer(CHAIR_ENTITY.get(), ChairRenderer::new);//Dummy renderer

        event.registerEntityRenderer(BEI_FENG.get(),(a) -> new LatexEntityRenderer<>(a, BEI_FENG_TF));

        event.registerEntityRenderer(DARK_LATEX_WOLF_MALE.get(),(a) -> new LatexEntityRenderer<>(a, DARK_LATEX_WOLF_M_TF));
        event.registerEntityRenderer(DARK_LATEX_WOLF_FEMALE.get(),(a) -> new LatexEntityRenderer<>(a, DARK_LATEX_WOLF_F_TF));

        event.registerEntityRenderer(GAS_WOLF.get(),(a) -> new LatexEntityRenderer<>(a, GAS_WOLF_TF));

        event.registerEntityRenderer(WHITE_LATEX_WOLF_MALE.get(),(a) -> new LatexEntityRenderer<>(a, WHITE_LATEX_WOLF_M_TF));
        event.registerEntityRenderer(WHITE_LATEX_WOLF_FEMALE.get(),(a) -> new LatexEntityRenderer<>(a, WHITE_LATEX_WOLF_F_TF));
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(BookStackRenderer.LAYER, BookStackRenderer::bodyLayer);
        event.registerLayerDefinition(LatexContainerRenderer.LAYER, LatexContainerRenderer::bodyLayer);

        event.registerLayerDefinition(new ModelLayerLocation(new ResourceLocation(MODID,"dummy"),"main"), DummyModel::createBodyLayer);

        event.registerLayerDefinition(BeiFengModel.modelLayerLocation, BeiFengModel::createBodyLayer);
        event.registerLayerDefinition(BeiFengModel.armorLayerLocation, BeiFengModel::createArmorLayer);
        event.registerLayerDefinition(LatexWolfMaleModel.modelLayerLocation, LatexWolfMaleModel::createBodyLayer);
        event.registerLayerDefinition(LatexWolfMaleModel.armorLayerLocation, LatexWolfMaleModel::createArmorLayer);
        event.registerLayerDefinition(LatexWolfFemaleModel.modelLayerLocation, LatexWolfFemaleModel::createBodyLayer);
        event.registerLayerDefinition(LatexWolfFemaleModel.armorLayerLocation, LatexWolfFemaleModel::createArmorLayer);
    }
}