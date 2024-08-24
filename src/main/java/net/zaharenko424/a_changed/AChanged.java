package net.zaharenko424.a_changed;

import com.mojang.logging.LogUtils;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.SimpleTier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.a_changed.criterion.TransfurTrigger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.zaharenko424.a_changed.registry.AbilityRegistry.ABILITIES;
import static net.zaharenko424.a_changed.registry.AttachmentRegistry.ATTACHMENTS;
import static net.zaharenko424.a_changed.registry.BlockEntityRegistry.BLOCK_ENTITIES;
import static net.zaharenko424.a_changed.registry.BlockRegistry.BLOCKS;
import static net.zaharenko424.a_changed.registry.CreativeTabs.CREATIVE_MODE_TABS;
import static net.zaharenko424.a_changed.registry.DNATypeRegistry.DNA_TYPES;
import static net.zaharenko424.a_changed.registry.EntityRegistry.ENTITIES;
import static net.zaharenko424.a_changed.registry.FluidRegistry.FLUIDS;
import static net.zaharenko424.a_changed.registry.FluidRegistry.FLUID_TYPES;
import static net.zaharenko424.a_changed.registry.ItemRegistry.ITEMS;
import static net.zaharenko424.a_changed.registry.MemoryTypeRegistry.MEMORY_TYPES;
import static net.zaharenko424.a_changed.registry.MenuRegistry.MENU_TYPES;
import static net.zaharenko424.a_changed.registry.MobEffectRegistry.EFFECTS;
import static net.zaharenko424.a_changed.registry.RecipeSerializerRegistry.RECIPE_SERIALIZERS;
import static net.zaharenko424.a_changed.registry.SoundRegistry.SOUNDS;
import static net.zaharenko424.a_changed.registry.TransfurRegistry.TRANSFUR_TYPES;

@ParametersAreNonnullByDefault
@Mod(AChanged.MODID)
public class AChanged {

    public static final String MODID = "a_changed";
    public static final Logger LOGGER = LogUtils.getLogger();

    //Registries
    public static final DeferredRegister<Activity> ACTIVITIES = DeferredRegister.create(BuiltInRegistries.ACTIVITY, MODID);
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, MODID);
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, MODID);
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGER_TYPES = DeferredRegister.create(BuiltInRegistries.TRIGGER_TYPES, MODID);

    //Activities
    public static final DeferredHolder<Activity, Activity> TRANSFUR_ATTACK = ACTIVITIES.register("transfur_attack", () -> new Activity("transfur_attack"));
    public static final DeferredHolder<Activity, Activity> TRANSFUR_HOLD = ACTIVITIES.register("transfur_hold", () -> new Activity("transfur_hold"));

    //Attributes
    /**
     * 1 -> default air depletion speed <p>Total value > 1 -> faster air depletion. <p>0 < totalValue < 1 -> slower depletion. <p>Total value == 0 -> no depletion.
     */
    public static final DeferredHolder<Attribute, Attribute> AIR_DECREASE_SPEED = ATTRIBUTES.register("air_decrease_speed",
            () -> new RangedAttribute("attribute." + MODID + ".air_decrease_speed",1,0,256).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> LATEX_RESISTANCE = ATTRIBUTES.register("latex_resistance",
            () -> new RangedAttribute("attribute." + MODID + ".latex_resistance",0,0,1));

    //Particles
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLUE_GAS_PARTICLE = PARTICLE_TYPES.register("blue_gas", ()-> new SimpleParticleType(true));

    //Trigger types
    public static final DeferredHolder<CriterionTrigger<?>, TransfurTrigger> PLAYER_TRANSFURRED = TRIGGER_TYPES.register("player_transfurred", TransfurTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, TransfurTrigger> PLAYER_TRANSFURRED_ENTITY = TRIGGER_TYPES.register("player_transfurred_entity", TransfurTrigger::new);

    //Tags
    public static final TagKey<Block> LATEX_RESISTANT = TagKey.create(Registries.BLOCK, resourceLoc("latex_resistant"));
    public static final TagKey<Block> LASER_TRANSPARENT = TagKey.create(Registries.BLOCK, resourceLoc("laser_transparent"));
    public static final TagKey<EntityType<?>> TRANSFURRABLE_TAG = TagKey.create(Registries.ENTITY_TYPE, resourceLoc("transfurrable"));
    public static final TagKey<EntityType<?>> SEWAGE_SYSTEM_CONSUMABLE = TagKey.create(Registries.ENTITY_TYPE, resourceLoc("sewage_system_consumable"));

    //Item tier
    public static final SimpleTier COPPER = new SimpleTier(2, 128, 5, 1, 12, BlockTags.NEEDS_IRON_TOOL, ()-> Ingredient.of(Items.COPPER_INGOT));

    //Game rules
    public static final GameRules.Key<GameRules.BooleanValue> CHOOSE_TF_OR_DIE = GameRules.register("chooseTransfurOrDie", GameRules.Category.MISC, GameRules.BooleanValue.create(true));
    public static final GameRules.Key<GameRules.BooleanValue> DO_LATEX_SPREAD = GameRules.register("doLatexSpread", GameRules.Category.MISC, GameRules.BooleanValue.create(true));
    public static final GameRules.Key<GameRules.BooleanValue> KEEP_TRANSFUR = GameRules.register("keepTransfurOnDeath", GameRules.Category.MISC, GameRules.BooleanValue.create(true));
    public static final GameRules.Key<GameRules.BooleanValue> TRANSFUR_IS_DEATH = GameRules.register("transfurIsDeath", GameRules.Category.MISC, GameRules.BooleanValue.create(true));

    public static final WoodType ORANGE = WoodType.register(new WoodType(MODID + ":orange", BlockSetType.ACACIA));

    @Contract("_ -> new")
    public static @NotNull ResourceLocation resourceLoc(String path){
        return new ResourceLocation(MODID, path);
    }

    @Contract("_ -> new")
    public static @NotNull ResourceLocation textureLoc(String path){
        return new ResourceLocation(MODID,"textures/" + path + ".png");
    }

    public static @NotNull ResourceLocation textureLoc(ResourceLocation loc){
        return loc.withPrefix("textures/").withSuffix(".png");
    }

    public AChanged(IEventBus modEventBus) {
        ABILITIES.register(modEventBus);
        ACTIVITIES.register(modEventBus);
        ATTACHMENTS.register(modEventBus);
        ATTRIBUTES.register(modEventBus);
        BLOCKS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        TRANSFUR_TYPES.register(modEventBus);
        DNA_TYPES.register(modEventBus);
        EFFECTS.register(modEventBus);
        ENTITIES.register(modEventBus);
        FLUIDS.register(modEventBus);
        FLUID_TYPES.register(modEventBus);
        ITEMS.register(modEventBus);
        MEMORY_TYPES.register(modEventBus);
        MENU_TYPES.register(modEventBus);
        PARTICLE_TYPES.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
        SOUNDS.register(modEventBus);
        TRIGGER_TYPES.register(modEventBus);
    }
}