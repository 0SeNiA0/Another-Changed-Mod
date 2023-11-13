package net.zaharenko424.testmod.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.zaharenko424.testmod.TestMod;
import net.zaharenko424.testmod.client.model.DummyModel;
import net.zaharenko424.testmod.client.model.WhiteLatexModel;
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
        event.registerEntityRenderer(TestMod.WHITE_LATEX_BEAST.get(),(a) -> new LatexEntityRenderer<>(a,WHITE_LATEX_TRANSFUR));
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.@NotNull RegisterLayerDefinitions event){
        event.registerLayerDefinition(new ModelLayerLocation(new ResourceLocation(TestMod.MODID,"dummy"),"main"), DummyModel::createBodyLayer);
        event.registerLayerDefinition(WHITE_LATEX_TRANSFUR.get().modelLayerLocation(), WhiteLatexModel::createBodyLayer);
    }
}