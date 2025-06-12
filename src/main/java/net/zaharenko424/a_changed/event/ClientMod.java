package net.zaharenko424.a_changed.event;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectObjectMutablePair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.FoliageColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.IClientMobEffectExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.ModelManagerAccess;
import net.zaharenko424.a_changed.client.Keybindings;
import net.zaharenko424.a_changed.client.cmrs.CustomBEWLR;
import net.zaharenko424.a_changed.client.cmrs.event.RegisterModelDefinitionsEvent;
import net.zaharenko424.a_changed.client.cmrs.renderer.CustomModelRenderer;
import net.zaharenko424.a_changed.client.cmrs.renderer.DynamicModelRenderer;
import net.zaharenko424.a_changed.client.cmrs.event.RegisterBuiltInModelsEvent;
import net.zaharenko424.a_changed.client.model.*;
import net.zaharenko424.a_changed.client.overlay.*;
import net.zaharenko424.a_changed.client.particle.BlueGasParticle;
import net.zaharenko424.a_changed.client.renderer.MilkPuddingRenderer;
import net.zaharenko424.a_changed.client.renderer.RoombaRenderer;
import net.zaharenko424.a_changed.client.renderer.SyringeProjectileRenderer;
import net.zaharenko424.a_changed.client.renderer.blockEntity.*;
import net.zaharenko424.a_changed.client.renderer.misc.ChairRenderer;
import net.zaharenko424.a_changed.client.renderer.misc.SeatRenderer;
import net.zaharenko424.a_changed.client.screen.PneumaticSyringeRifleScreen;
import net.zaharenko424.a_changed.client.screen.SyringeCoilGunScreen;
import net.zaharenko424.a_changed.client.screen.machines.*;
import net.zaharenko424.a_changed.event.custom.AddSpritesToAtlasEvent;
import net.zaharenko424.a_changed.item.AbstractSyringe;
import net.zaharenko424.a_changed.item.AbstractSyringeRifle;
import net.zaharenko424.a_changed.registry.*;
import net.zaharenko424.a_changed.util.IOUtils;
import net.zaharenko424.a_changed.util.Thing;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

import static net.zaharenko424.a_changed.AChanged.*;
import static net.zaharenko424.a_changed.registry.EntityRegistry.*;
import static net.zaharenko424.a_changed.registry.TransfurRegistry.*;

@ParametersAreNonnullByDefault
@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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
        event.register(Keybindings.ABILITY_KEY);
        event.register(Keybindings.ABILITY_SELECTION);

        event.register(Keybindings.QUICK_SELECT_ABILITY_1);
        event.register(Keybindings.QUICK_SELECT_ABILITY_2);
        event.register(Keybindings.QUICK_SELECT_ABILITY_3);

        event.register(Keybindings.MODEL_MANAGER);
    }

    @SubscribeEvent
    public static void onRegisterGui(RegisterGuiLayersEvent event){
        event.registerBelowAll(AChanged.resourceLoc("ability_overlay"), AbilityOverlay.OVERLAY);
        event.registerBelowAll(AChanged.resourceLoc("transfur"), TransfurOverlay.OVERLAY);
        event.registerBelowAll(AChanged.resourceLoc("pure_white_latex"), PureWhiteLatexOverlay.OVERLAY);
        event.registerBelowAll(AChanged.resourceLoc("hazmat"), HazmatOverlay.OVERLAY);
        event.registerBelowAll(AChanged.resourceLoc("cryo_chamer"), CryoChamberOverlay.OVERLAY);
    }

    @SubscribeEvent
    public static void onAddSprites(AddSpritesToAtlasEvent event){
        if(!event.getAtlasLocation().equals(AddSpritesToAtlasEvent.BLOCK_ATLAS)) return;

        HashMap<ResourceLocation, Thing<File, File, File, File>> map = ((ModelManagerAccess)Minecraft.getInstance().getModelManager()).achanged$getConvertedTextures();
        if(!map.isEmpty()){
            map.forEach((loc, pair) -> {
                ResourceLocation loc1 = loc.withSuffix("_darkltx");
                if(pair.second() != null) {
                    event.addSprite(loc1,
                            () -> new FileInputStream(pair.first()),
                            event.streamToResource(() -> new FileInputStream(pair.second())));
                } else event.addSprite(loc1, () -> new FileInputStream(pair.first()), null);

                loc1 = loc.withSuffix("_whiteltx");
                if(pair.fourth() != null) {
                    event.addSprite(loc1,
                            () -> new FileInputStream(pair.third()),
                            event.streamToResource(() -> new FileInputStream(pair.fourth())));
                } else event.addSprite(loc1, () -> new FileInputStream(pair.third()), null);
            });
            map.clear();
            return;
        }

        File convertedDir = new File(Minecraft.getInstance().gameDirectory, "converted_textures");
        if(!convertedDir.exists()) return;
        File[] subDirectories = convertedDir.listFiles(File::isDirectory);
        if(subDirectories == null) return;
        HashMap<String, Pair<File, File>> textures = new HashMap<>();
        for(File subDir : subDirectories){
            collectAllTextures(textures, subDir, "block/");//only look for textures in block directory

            String namespace = subDir.getName();
            textures.forEach((k, pair) -> {
                if(pair.second() != null) {
                    event.addSprite(ResourceLocation.fromNamespaceAndPath(namespace, k),
                            () -> new FileInputStream(pair.first()),
                            event.streamToResource(() -> new FileInputStream(pair.second())));
                }

                event.addSprite(ResourceLocation.fromNamespaceAndPath(namespace, k),
                        () -> new FileInputStream(pair.first()), null);
            });
            textures.clear();
        }
    }

    public static void collectAllTextures(HashMap<String, Pair<File, File>> textures, File dir, String relativePath){
        IOUtils.visitAllFiles((path, file, filename) -> {
            if(filename.endsWith(".png")){
                textures.compute(relativePath + path + filename.replace(".png", ""), (k, v) -> {
                    if(v == null) return ObjectObjectMutablePair.of(file, null);
                    return v.left(file);
                });
                return;
            }

            if(filename.endsWith(".mcmeta")){
                textures.compute(relativePath + path + filename.replace(".png.mcmeta", ""), (k, v) -> {
                    if(v == null) return ObjectObjectMutablePair.of(null, file);
                    return v.right(file);
                });
            }
        }, dir, "");

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

        event.register((stack, i) -> switch (i){
            case 0 -> ((AbstractSyringe)stack.getItem()).getContentsColor(stack);
            case 1 -> ((AbstractSyringe)stack.getItem()).getSecondaryColor(stack);
            default -> -1;
        }, ItemRegistry.STABILIZED_LATEX_SYRINGE, ItemRegistry.LATEX_SYRINGE);
    }

    @SubscribeEvent
    public static void onRegisterItemExtensions(RegisterClientExtensionsEvent event){
        //Items
        event.registerItem(new IClientItemExtensions() {
            @Override
            public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return CustomBEWLR.getInstance();
            }
        }, ItemRegistry.ABSOLUTE_SOLVER.asItem());

        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                return HumanoidModel.ArmPose.EMPTY;
            }
        }, ItemRegistry.STUN_LANCE.asItem());

        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel.ArmPose getArmPose(@NotNull LivingEntity entityLiving, @NotNull InteractionHand hand, @NotNull ItemStack rifle) {
                IItemHandler handler = rifle.getCapability(Capabilities.ItemHandler.ITEM);
                AbstractSyringeRifle item = (AbstractSyringeRifle) rifle.getItem();
                return item.hasAmmo(handler) && item.hasFuel(rifle, handler) ? HumanoidModel.ArmPose.BOW_AND_ARROW
                        : HumanoidModel.ArmPose.ITEM;
            }
        }, ItemRegistry.PNEUMATIC_SYRINGE_RIFLE.asItem(), ItemRegistry.SYRINGE_COIL_GUN.asItem());

        //Fluids
        event.registerFluidType(new IClientFluidTypeExtensions(){
            @Override
            public @NotNull ResourceLocation getStillTexture() {
                return resourceLoc("block/latex_solvent_still");
            }

            @Override
            public @NotNull ResourceLocation getFlowingTexture() {
                return resourceLoc("block/latex_solvent_flowing");
            }
        }, FluidRegistry.LATEX_SOLVENT_TYPE.get());

        event.registerFluidType(new IClientFluidTypeExtensions(){
            @Override
            public @NotNull ResourceLocation getStillTexture() {
                return resourceLoc("block/white_latex_still");
            }

            @Override
            public @NotNull ResourceLocation getFlowingTexture() {
                return resourceLoc("block/white_latex_flowing");
            }
        }, FluidRegistry.WHITE_LATEX_TYPE.get());

        event.registerFluidType(new IClientFluidTypeExtensions(){
            @Override
            public @NotNull ResourceLocation getStillTexture() {
                return resourceLoc("block/dark_latex_still");
            }

            @Override
            public @NotNull ResourceLocation getFlowingTexture() {
                return resourceLoc("block/dark_latex_flowing");
            }
        }, FluidRegistry.DARK_LATEX_TYPE.get());

        //Effects
        event.registerMobEffect(new IClientMobEffectExtensions() {
            @Override
            public boolean isVisibleInInventory(MobEffectInstance instance) {
                return false;
            }
            @Override
            public boolean isVisibleInGui(MobEffectInstance instance) {
                return false;
            }
        }, MobEffectRegistry.FRESH_AIR.get(), MobEffectRegistry.GRAB_COOLDOWN.get(), MobEffectRegistry.INVISIBLE_SLOWDOWN.get(),
                MobEffectRegistry.UNTRANSFUR_STACK.get());
    }

    @SubscribeEvent
    public static void onRegisterParticleProviders(RegisterParticleProvidersEvent event){
        event.registerSpriteSet(BLUE_GAS_PARTICLE.get(), BlueGasParticle.Provider::new);
    }

    @SubscribeEvent
    public static void onRegisterModelDefinitions(RegisterModelDefinitionsEvent event){
        event.registerModelDefinition(CannedOrangesRenderer.LAYER, CannedOrangesRenderer.bodyLayer());
        event.registerModelDefinition(CryoChamberRenderer.LAYER, CryoChamberRenderer.bodyLayer());
        event.registerModelDefinition(DNAExtractorRenderer.LAYER, DNAExtractorRenderer.bodyLayer());
        event.registerModelDefinition(LaserEmitterRenderer.LAYER, LaserEmitterRenderer.bodyLayer());
        event.registerModelDefinition(LatexEncoderRenderer.LAYER, LatexEncoderRenderer.bodyLayer());
        event.registerModelDefinition(PileOfOrangesRenderer.LAYER, PileOfOrangesRenderer.bodyLayer());

        event.registerModelDefinition(ChairRenderer.LAYER, ChairRenderer.bodyLayer());

        event.registerModelDefinition(SyringeProjectileRenderer.LAYER, SyringeProjectileRenderer.bodyLayer());


        event.registerModelDefinition(MilkPuddingModel.bodyLayer, MilkPuddingModel.bodyLayer());
        event.registerModelDefinition(RoombaModel.bodyLayer, RoombaModel.bodyLayer());

        event.registerModelDefinition(BeiFengModel.bodyLayer, BeiFengModel.model());

        event.registerModelDefinition(DarkLatexPupModel.bodyLayer, DarkLatexPupModel.model());
        event.registerModelDefinition(DLPupMoltenModel.bodyLayer, DLPupMoltenModel.model());

        event.registerModelDefinition(HypnoCatModel.bodyLayer, HypnoCatModel.model());

        event.registerModelDefinition(SnowLeopardFemaleModel.bodyLayer, SnowLeopardFemaleModel.model());
        event.registerModelDefinition(SnowLeopardMaleModel.bodyLayer, SnowLeopardMaleModel.model());

        event.registerModelDefinition(LatexSharkFemaleModel.bodyLayer, LatexSharkFemaleModel.model());
        event.registerModelDefinition(LatexSharkMaleModel.bodyLayer, LatexSharkMaleModel.model());

        event.registerModelDefinition(LatexWolfFemaleModel.bodyLayer, LatexWolfFemaleModel.model());
        event.registerModelDefinition(LatexWolfMaleModel.bodyLayer, LatexWolfMaleModel.model());

        event.registerModelDefinition(YufengDragonModel.bodyLayer, YufengDragonModel.model());
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(BlockEntityRegistry.BOOK_STACK_ENTITY.get(), context -> new BookStackRenderer());
        event.registerBlockEntityRenderer(BlockEntityRegistry.CANNED_ORANGES_ENTITY.get(), (a)-> new CannedOrangesRenderer());
        event.registerBlockEntityRenderer(BlockEntityRegistry.CRYO_CHAMBER_ENTITY.get(), (a)-> new CryoChamberRenderer());
        event.registerBlockEntityRenderer(BlockEntityRegistry.DNA_EXTRACTOR_ENTITY.get(), (a)-> new DNAExtractorRenderer());
        event.registerBlockEntityRenderer(BlockEntityRegistry.HANGING_SIGN_ENTITY.get(), HangingSignRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.LASER_EMITTER_ENTITY.get(), (a)-> new LaserEmitterRenderer());
        event.registerBlockEntityRenderer(BlockEntityRegistry.LATEX_CONTAINER_ENTITY.get(), LatexContainerRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.LATEX_ENCODER_ENTITY.get(), (a)-> new LatexEncoderRenderer());
        event.registerBlockEntityRenderer(BlockEntityRegistry.PAPER_STACK_ENTITY.get(), context -> new PaperStackRenderer());
        event.registerBlockEntityRenderer(BlockEntityRegistry.PILE_OF_ORANGES_ENTITY.get(), (a)-> new PileOfOrangesRenderer());
        event.registerBlockEntityRenderer(BlockEntityRegistry.SIGN_ENTITY.get(), SignRenderer::new);

        event.registerEntityRenderer(SYRINGE_PROJECTILE.get(), SyringeProjectileRenderer::new);
        event.registerEntityRenderer(SEAT_ENTITY.get(), SeatRenderer::new);//Dummy renderer
        event.registerEntityRenderer(CHAIR_ENTITY.get(), ChairRenderer::new);

        event.registerEntityRenderer(MILK_PUDDING.get(), MilkPuddingRenderer::new);
        event.registerEntityRenderer(ROOMBA_ENTITY.get(), RoombaRenderer::new);

        event.registerEntityRenderer(BEI_FENG.get(), a -> new CustomModelRenderer<>(a, BEI_FENG_TF.get().getDefaultModel(), .5f));

        event.registerEntityRenderer(BENIGN.get(), a -> new CustomModelRenderer<>(a, BENIGN_TF.get().getDefaultModel(), .5f));

        event.registerEntityRenderer(DARK_LATEX_PUP.get(), a -> new DynamicModelRenderer<>(a, DARK_LATEX_PUP_TF.get()::getModelFor, .5f));

        event.registerEntityRenderer(DARK_LATEX_WOLF_FEMALE.get(), a -> new CustomModelRenderer<>(a, DARK_LATEX_WOLF_F_TF.get().getDefaultModel(), .5f));
        event.registerEntityRenderer(DARK_LATEX_WOLF_MALE.get(), a -> new CustomModelRenderer<>(a, DARK_LATEX_WOLF_M_TF.get().getDefaultModel(), .5f));

        event.registerEntityRenderer(GAS_WOLF.get(), a -> new CustomModelRenderer<>(a, GAS_WOLF_TF.get().getDefaultModel(), .5f));

        event.registerEntityRenderer(HYPNO_CAT.get(), a -> new CustomModelRenderer<>(a, HYPNO_CAT_TF.get().getDefaultModel(), .5f));

        event.registerEntityRenderer(LATEX_SHARK_FEMALE.get(), a -> new CustomModelRenderer<>(a, LATEX_SHARK_F_TF.get().getDefaultModel(), .5f));
        event.registerEntityRenderer(LATEX_SHARK_MALE.get(), a -> new CustomModelRenderer<>(a, LATEX_SHARK_M_TF.get().getDefaultModel(), .5f));

        event.registerEntityRenderer(PURE_WHITE_LATEX_WOLF.get(), a -> new CustomModelRenderer<>(a, PURE_WHITE_LATEX_WOLF_TF.get().getDefaultModel(), .5f));

        event.registerEntityRenderer(SNOW_LEOPARD_FEMALE.get(), a -> new CustomModelRenderer<>(a, SNOW_LEOPARD_F_TF.get().getDefaultModel(), .5f));
        event.registerEntityRenderer(SNOW_LEOPARD_MALE.get(), a -> new CustomModelRenderer<>(a, SNOW_LEOPARD_M_TF.get().getDefaultModel(), .5f));

        event.registerEntityRenderer(WHITE_LATEX_PUP.get(), a -> new CustomModelRenderer<>(a, WHITE_LATEX_PUP_TF.get().getDefaultModel(), .5f));

        event.registerEntityRenderer(WHITE_LATEX_WOLF_FEMALE.get(), a -> new CustomModelRenderer<>(a, WHITE_LATEX_WOLF_F_TF.get().getDefaultModel(), .5f));
        event.registerEntityRenderer(WHITE_LATEX_WOLF_MALE.get(), a -> new CustomModelRenderer<>(a, WHITE_LATEX_WOLF_M_TF.get().getDefaultModel(), .5f));

        event.registerEntityRenderer(YUFENG_DRAGON.get(), a -> new CustomModelRenderer<>(a, YUFENG_DRAGON_TF.get().getDefaultModel(), .5f));
    }

    @SubscribeEvent
    public static void onLoadModelsToCache(RegisterBuiltInModelsEvent event){
        TRANSFUR_REGISTRY.stream().forEach(transfurType -> transfurType.registerModels(event));
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(LatexContainerRenderer.LAYER, LatexContainerRenderer::bodyLayer);
    }
}