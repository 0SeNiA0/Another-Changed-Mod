package net.zaharenko424.testmod.event;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.zaharenko424.testmod.Config;
import net.zaharenko424.testmod.TestMod;
import net.zaharenko424.testmod.entity.LatexBeast;
import net.zaharenko424.testmod.network.PacketHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.zaharenko424.testmod.TestMod.*;
import static net.zaharenko424.testmod.registry.EntityRegistry.*;
@ParametersAreNonnullByDefault
@Mod.EventBusSubscriber(modid = TestMod.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
class CommonMod {

    @SubscribeEvent
    public static void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));

        PacketHandler.init();
    }

    @SubscribeEvent
    public static void onAttributeModify(EntityAttributeModificationEvent event){
        event.getTypes().forEach((type)-> {
            if(event.has(type,LATEX_RESISTANCE.get())) return;
            event.add(type, LATEX_RESISTANCE.get());
        });
    }

    @SubscribeEvent
    public static void onEntityAttributes(@NotNull EntityAttributeCreationEvent event){
        event.put(WHITE_LATEX_WOLF_MALE.get(), LatexBeast.createAttributes().build());
        event.put(WHITE_LATEX_WOLF_FEMALE.get(), LatexBeast.createAttributes().build());

        event.put(DARK_LATEX_WOLF_MALE.get(), LatexBeast.createAttributes().build());
        event.put(DARK_LATEX_WOLF_FEMALE.get(), LatexBeast.createAttributes().build());
    }
}