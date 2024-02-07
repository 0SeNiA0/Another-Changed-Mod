package net.zaharenko424.a_changed.event;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.FoliageColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.Keybindings;
import net.zaharenko424.a_changed.client.overlay.*;
import net.zaharenko424.a_changed.client.particle.BlueGasParticle;
import net.zaharenko424.a_changed.client.renderer.LatexEntityRenderer;
import net.zaharenko424.a_changed.client.renderer.blockEntity.BookStackRenderer;
import net.zaharenko424.a_changed.client.renderer.blockEntity.CryoChamberRenderer;
import net.zaharenko424.a_changed.client.renderer.blockEntity.LaserEmitterRenderer;
import net.zaharenko424.a_changed.client.renderer.blockEntity.LatexContainerRenderer;
import net.zaharenko424.a_changed.client.renderer.misc.ChairRenderer;
import net.zaharenko424.a_changed.client.screen.GeneratorScreen;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import net.zaharenko424.a_changed.registry.BlockRegistry;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.zaharenko424.a_changed.AChanged.BLUE_GAS_PARTICLE;
import static net.zaharenko424.a_changed.AChanged.MODID;
import static net.zaharenko424.a_changed.registry.EntityRegistry.*;
import static net.zaharenko424.a_changed.registry.TransfurRegistry.*;
@ParametersAreNonnullByDefault
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientMod {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event){
        event.enqueueWork(() -> MenuScreens.register(AChanged.GENERATOR_MENU.get(), GeneratorScreen::new));
    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event){
        event.register(Keybindings.GRAB_KEY);
        event.register(Keybindings.GRAB_MODE_KEY);
    }

    @SubscribeEvent
    public static void onRegisterGui(RegisterGuiOverlaysEvent event){
        event.registerBelowAll(AChanged.resourceLoc("grab_mode"), GrabModeOverlay.OVERLAY);
        event.registerBelowAll(AChanged.resourceLoc("transfur"), TransfurOverlay.OVERLAY);
        event.registerBelowAll(AChanged.resourceLoc("pure_white_latex"), PureWhiteLatexOverlay.OVERLAY);
        event.registerBelowAll(AChanged.resourceLoc("hazmat"), HazmatOverlay.OVERLAY);
        event.registerBelowAll(AChanged.resourceLoc("cryo_chamer"), CryoChamberOverlay.OVERLAY);
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
        event.registerBlockEntityRenderer(BlockEntityRegistry.CRYO_CHAMBER_ENTITY.get(), (a)-> new CryoChamberRenderer());
        event.registerBlockEntityRenderer(BlockEntityRegistry.LASER_EMITTER_ENTITY.get(), (a)-> new LaserEmitterRenderer());
        event.registerBlockEntityRenderer(BlockEntityRegistry.LATEX_CONTAINER_ENTITY.get(), LatexContainerRenderer::new);

        event.registerEntityRenderer(SEAT_ENTITY.get(), ChairRenderer::new);//Dummy renderer

        event.registerEntityRenderer(BEI_FENG.get(), a -> new LatexEntityRenderer<>(a, BEI_FENG_TF));

        event.registerEntityRenderer(BENIGN.get(), a -> new LatexEntityRenderer<>(a, BENIGN_TF));

        event.registerEntityRenderer(DARK_LATEX_WOLF_FEMALE.get(), a -> new LatexEntityRenderer<>(a, DARK_LATEX_WOLF_F_TF));
        event.registerEntityRenderer(DARK_LATEX_WOLF_MALE.get(), a -> new LatexEntityRenderer<>(a, DARK_LATEX_WOLF_M_TF));

        event.registerEntityRenderer(GAS_WOLF.get(), a -> new LatexEntityRenderer<>(a, GAS_WOLF_TF));

        event.registerEntityRenderer(PURE_WHITE_LATEX_WOLF.get(), a -> new LatexEntityRenderer<>(a, PURE_WHITE_LATEX_WOLF_TF));
        event.registerEntityRenderer(WHITE_LATEX_WOLF_FEMALE.get(), a -> new LatexEntityRenderer<>(a, WHITE_LATEX_WOLF_F_TF));
        event.registerEntityRenderer(WHITE_LATEX_WOLF_MALE.get(), a -> new LatexEntityRenderer<>(a, WHITE_LATEX_WOLF_M_TF));
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(BookStackRenderer.LAYER, BookStackRenderer::bodyLayer);
        event.registerLayerDefinition(LatexContainerRenderer.LAYER, LatexContainerRenderer::bodyLayer);
    }
}