package net.zaharenko424.testmod.events;

import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.zaharenko424.testmod.Config;
import net.zaharenko424.testmod.TestMod;
import net.zaharenko424.testmod.entity.TestEntity;
import net.zaharenko424.testmod.network.PacketHandler;
import org.jetbrains.annotations.NotNull;

import static net.zaharenko424.testmod.TestMod.LOGGER;

@Mod.EventBusSubscriber(modid = TestMod.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
class CommonMod {

    @SubscribeEvent
    public static void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));

        PacketHandler.init();
    }

    @SubscribeEvent
    public static void onEntityAttributes(@NotNull EntityAttributeCreationEvent event){
        event.put(TestMod.TEST_ENTITY.get(), TestEntity.createAttributes().build());
    }
}