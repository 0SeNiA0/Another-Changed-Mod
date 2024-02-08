package net.zaharenko424.a_changed;

import com.mojang.logging.LogUtils;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.a_changed.menu.GeneratorMenu;
import net.zaharenko424.a_changed.menu.LatexPurifierMenu;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.zaharenko424.a_changed.registry.BlockEntityRegistry.BLOCK_ENTITIES;
import static net.zaharenko424.a_changed.registry.BlockRegistry.BLOCKS;
import static net.zaharenko424.a_changed.registry.CreativeTabs.CREATIVE_MODE_TABS;
import static net.zaharenko424.a_changed.registry.EntityRegistry.ENTITIES;
import static net.zaharenko424.a_changed.registry.FluidRegistry.FLUIDS;
import static net.zaharenko424.a_changed.registry.FluidRegistry.FLUID_TYPES;
import static net.zaharenko424.a_changed.registry.ItemRegistry.ITEMS;
import static net.zaharenko424.a_changed.registry.MobEffectRegistry.EFFECTS;
import static net.zaharenko424.a_changed.registry.SoundRegistry.SOUNDS;
import static net.zaharenko424.a_changed.registry.TransfurRegistry.TRANSFUR_TYPES;

@ParametersAreNonnullByDefault
@Mod(AChanged.MODID)
public class AChanged {

    public static final String MODID = "a_changed";
    public static final Logger LOGGER = LogUtils.getLogger();

    //Registries
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, MODID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, MODID);
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, MODID);

    //Attributes
    public static final DeferredHolder<Attribute, Attribute> AIR_DECREASE_SPEED = ATTRIBUTES.register("air_decrease_speed",
            () -> new RangedAttribute("attribute."+MODID+".air_decrease_speed",1,0,256));
    public static final DeferredHolder<Attribute, Attribute> LATEX_RESISTANCE = ATTRIBUTES.register("latex_resistance",
            () -> new RangedAttribute("attribute."+MODID+".latex_resistance",0,0,1));

    public static final DeferredHolder<MenuType<?>, MenuType<GeneratorMenu>> GENERATOR_MENU = MENU_TYPES.register("generator", ()-> IMenuTypeExtension.create(GeneratorMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<LatexPurifierMenu>> LATEX_PURIFIER_MENU = MENU_TYPES.register("latex_purifier", ()-> IMenuTypeExtension.create(LatexPurifierMenu::new));

    //Particles
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLUE_GAS_PARTICLE = PARTICLE_TYPES.register("blue_gas", ()-> new SimpleParticleType(true));

    //Tags
    public static final TagKey<Block> LASER_TRANSPARENT = TagKey.create(Registries.BLOCK, resourceLoc("laser_transparent"));
    public static final TagKey<EntityType<?>> TRANSFURRABLE_TAG = TagKey.create(Registries.ENTITY_TYPE, resourceLoc("transfurrable"));
    public static final TagKey<EntityType<?>> SEWAGE_SYSTEM_CONSUMABLE = TagKey.create(Registries.ENTITY_TYPE, resourceLoc("sewage_system_consumable"));

    //Game rules
    public static final GameRules.Key<GameRules.BooleanValue> CHOOSE_TF_OR_DIE = GameRules.register("chooseTransfurOrDie", GameRules.Category.MISC, GameRules.BooleanValue.create(true));
    public static final GameRules.Key<GameRules.BooleanValue> KEEP_TRANSFUR = GameRules.register("keepTransfurOnDeath", GameRules.Category.MISC, GameRules.BooleanValue.create(true));
    public static final GameRules.Key<GameRules.BooleanValue> TRANSFUR_IS_DEATH = GameRules.register("transfurIsDeath", GameRules.Category.MISC, GameRules.BooleanValue.create(true));

    public static final WoodType ORANGE = WoodType.register(new WoodType(MODID + ":orange", BlockSetType.ACACIA));

    @Contract("_ -> new")
    public static @NotNull ResourceLocation resourceLoc(String path){
        return new ResourceLocation(MODID,path);
    }

    @Contract("_ -> new")
    public static @NotNull ResourceLocation textureLoc(String path){
        return new ResourceLocation(MODID,"textures/"+path+".png");
    }

    public static @NotNull ResourceLocation textureLoc(ResourceLocation loc){
        return loc.withPrefix("textures/").withSuffix(".png");
    }

    public AChanged() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        TRANSFUR_TYPES.register(modEventBus);
        ATTRIBUTES.register(modEventBus);
        EFFECTS.register(modEventBus);
        BLOCKS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        FLUID_TYPES.register(modEventBus);
        FLUIDS.register(modEventBus);
        ITEMS.register(modEventBus);
        ENTITIES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        SOUNDS.register(modEventBus);
        PARTICLE_TYPES.register(modEventBus);
        MENU_TYPES.register(modEventBus);
    }
}