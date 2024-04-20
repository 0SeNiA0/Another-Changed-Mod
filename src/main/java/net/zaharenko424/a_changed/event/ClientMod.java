package net.zaharenko424.a_changed.event;

import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.FoliageColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.Keybindings;
import net.zaharenko424.a_changed.client.cmrs.CustomEntityRenderer;
import net.zaharenko424.a_changed.client.cmrs.CustomModelManager;
import net.zaharenko424.a_changed.client.overlay.*;
import net.zaharenko424.a_changed.client.particle.BlueGasParticle;
import net.zaharenko424.a_changed.client.renderer.SyringeProjectileRenderer;
import net.zaharenko424.a_changed.client.renderer.blockEntity.*;
import net.zaharenko424.a_changed.client.renderer.misc.ChairRenderer;
import net.zaharenko424.a_changed.client.screen.PneumaticSyringeRifleScreen;
import net.zaharenko424.a_changed.client.screen.SyringeCoilGunScreen;
import net.zaharenko424.a_changed.client.screen.machines.*;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import net.zaharenko424.a_changed.registry.BlockRegistry;
import net.zaharenko424.a_changed.registry.MenuRegistry;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.Special;

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
        Sheets.addWoodType(AChanged.ORANGE);

    }

    @SubscribeEvent
    public static void onRegisterMenuScreens(RegisterMenuScreensEvent event){
        event.register(MenuRegistry.CAPACITOR_MENU.get(), CapacitorScreen::new);
        event.register(MenuRegistry.COMPRESSOR_MENU.get(), CompressorScreen::new);
        event.register(MenuRegistry.DNA_EXTRACTOR_MENU.get(), DNAExtractorScreen::new);
        event.register(MenuRegistry.GENERATOR_MENU.get(), GeneratorScreen::new);
        event.register(MenuRegistry.LATEX_ENCODER_MENU.get(), LatexEncoderScreen::new);
        event.register(MenuRegistry.LATEX_PURIFIER_MENU.get(), LatexPurifierScreen::new);

        event.register(MenuRegistry.PNEUMATIC_SYRINGE_RIFLE_MENU.get(), PneumaticSyringeRifleScreen::new);
        event.register(MenuRegistry.SYRINGE_COIL_GUN_MENU.get(), SyringeCoilGunScreen::new);
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
        event.register((state, tintGetter, pos, d)->
                tintGetter != null && pos != null ? BiomeColors.getAverageFoliageColor(tintGetter, pos)
                : FoliageColor.getDefaultColor(), BlockRegistry.ORANGE_LEAVES.get());
    }

    @SubscribeEvent
    public static void onRegisterColorHandlers(RegisterColorHandlersEvent.Item event){
        event.register((itemStack, i) ->
                event.getBlockColors().getColor(((BlockItem) itemStack.getItem()).getBlock().defaultBlockState()
                , null, null, i), BlockRegistry.ORANGE_LEAVES);
    }

    @SubscribeEvent
    public static void onRegisterParticleProviders(RegisterParticleProvidersEvent event){
        event.registerSpriteSet(BLUE_GAS_PARTICLE.get(), BlueGasParticle.Provider::new);
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(BlockEntityRegistry.BOOK_STACK_ENTITY.get(), BookStackRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.CANNED_ORANGES_ENTITY.get(), (a)-> new CannedOrangesRenderer());
        event.registerBlockEntityRenderer(BlockEntityRegistry.CRYO_CHAMBER_ENTITY.get(), (a)-> new CryoChamberRenderer());
        event.registerBlockEntityRenderer(BlockEntityRegistry.DNA_EXTRACTOR_ENTITY.get(), (a)-> new DNAExtractorRenderer());
        event.registerBlockEntityRenderer(BlockEntityRegistry.HANGING_SIGN_ENTITY.get(), HangingSignRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.LASER_EMITTER_ENTITY.get(), (a)-> new LaserEmitterRenderer());
        event.registerBlockEntityRenderer(BlockEntityRegistry.LATEX_CONTAINER_ENTITY.get(), LatexContainerRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.LATEX_ENCODER_ENTITY.get(), (a)-> new LatexEncoderRenderer());
        event.registerBlockEntityRenderer(BlockEntityRegistry.PILE_OF_ORANGES_ENTITY.get(), (a)-> new PileOfOrangesRenderer());
        event.registerBlockEntityRenderer(BlockEntityRegistry.SIGN_ENTITY.get(), SignRenderer::new);

        event.registerEntityRenderer(SYRINGE_PROJECTILE.get(), SyringeProjectileRenderer::new);
        event.registerEntityRenderer(SEAT_ENTITY.get(), ChairRenderer::new);//Dummy renderer

        event.registerEntityRenderer(BEI_FENG.get(), a -> new CustomEntityRenderer<>(a, BEI_FENG_TF.get().getModel(0)));

        event.registerEntityRenderer(BENIGN.get(), a -> new CustomEntityRenderer<>(a, BENIGN_TF.get().getModel(0)));

        event.registerEntityRenderer(DARK_LATEX_WOLF_FEMALE.get(), a -> new CustomEntityRenderer<>(a, DARK_LATEX_WOLF_F_TF.get().getModel(0)));
        event.registerEntityRenderer(DARK_LATEX_WOLF_MALE.get(), a -> new CustomEntityRenderer<>(a, DARK_LATEX_WOLF_M_TF.get().getModel(0)));

        event.registerEntityRenderer(GAS_WOLF.get(), a -> new CustomEntityRenderer<>(a, GAS_WOLF_TF.get().getModel(0)));

        event.registerEntityRenderer(HYPNO_CAT.get(), a -> new CustomEntityRenderer<>(a, HYPNO_CAT_TF.get().getModel(0)));

        event.registerEntityRenderer(LATEX_SHARK_FEMALE.get(), a -> new CustomEntityRenderer<>(a, LATEX_SHARK_F_TF.get().getModel(0)));
        event.registerEntityRenderer(LATEX_SHARK_MALE.get(), a -> new CustomEntityRenderer<>(a, LATEX_SHARK_M_TF.get().getModel(0)));

        event.registerEntityRenderer(PURE_WHITE_LATEX_WOLF.get(), a -> new CustomEntityRenderer<>(a, PURE_WHITE_LATEX_WOLF_TF.get().getModel(0)));
        event.registerEntityRenderer(WHITE_LATEX_WOLF_FEMALE.get(), a -> new CustomEntityRenderer<>(a, WHITE_LATEX_WOLF_F_TF.get().getModel(0)));
        event.registerEntityRenderer(WHITE_LATEX_WOLF_MALE.get(), a -> new CustomEntityRenderer<>(a, WHITE_LATEX_WOLF_M_TF.get().getModel(0)));
    }

    @SubscribeEvent
    public static void onLoadModelsToCache(LoadModelsToCacheEvent event){
        CustomModelManager modelManager = CustomModelManager.getInstance();
        TRANSFUR_REGISTRY.stream().forEach(transfurType -> {
            if (!(transfurType instanceof Special))
                modelManager.registerModel(transfurType.id, transfurType.getModel(0));
        });
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(BookStackRenderer.LAYER, BookStackRenderer::bodyLayer);
        event.registerLayerDefinition(LatexContainerRenderer.LAYER, LatexContainerRenderer::bodyLayer);
    }
}