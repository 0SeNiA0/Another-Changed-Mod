package net.zaharenko424.testmod.events;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.zaharenko424.testmod.TestMod;
import net.zaharenko424.testmod.client.model.LatexEntityModel;
import net.zaharenko424.testmod.client.renderer.LatexEntityRenderer;
import org.jetbrains.annotations.NotNull;

import static net.zaharenko424.testmod.TestMod.LOGGER;
import static net.zaharenko424.testmod.TestMod.WHITE_LATEX_TRANSFUR;

@Mod.EventBusSubscriber(modid = TestMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientMod {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        LOGGER.info("HELLO FROM CLIENT SETUP");
        LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.@NotNull RegisterRenderers event){
        event.registerEntityRenderer(TestMod.TEST_ENTITY.get(), LatexEntityRenderer::new);
        //TODO add damageType TRANSFUR or LATEX
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.@NotNull RegisterLayerDefinitions event){
        event.registerLayerDefinition(WHITE_LATEX_TRANSFUR.get().modelLayerLocation, LatexEntityModel::createBodyLayer);
    }
}
