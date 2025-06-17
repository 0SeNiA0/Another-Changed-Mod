package net.zaharenko424.a_changed.datagen.lang;

import net.minecraft.data.PackOutput;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.Keybindings;
import net.zaharenko424.a_changed.registry.*;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;

import static net.zaharenko424.a_changed.registry.BlockRegistry.*;
import static net.zaharenko424.a_changed.registry.ItemRegistry.*;

public class ENLanguageProvider extends LanguageProvider {

    public ENLanguageProvider(PackOutput output) {
        super(output, AChanged.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        //Advancements
        addAdvancement("root", "Another Changed Mod", "");

        addAdvancement("orange", "Orange!", "Obtain orange");
        addAdvancement("orange_juice", "A liquid orange?", "Obtain orange juice");
        addAdvancement("canned_oranges", "A can of oranges", "Obtain canned oranges");

        addAdvancement("step_on_syringe", "Who placed this here?", "Step on a syringe");
        addAdvancement("step_on_all_syringes", "The area is now clear", "Step on all syringe types");

        addAdvancement("get_transfurred", "SomeInterestingName", "Get transfurred");
        addAdvancement("cat_transfur", "Hmm, that's actually useful", "Get transfurred into cat latex");
        addAdvancement("swimming_transfur", "Drowned fear them", "Get transfurred into water latex");
        addAdvancement("flying_transfur", "Who needs elytra?", "Get transfurred into flying latex");
        addAdvancement("all_transfurs", "Gotta transfur into them all!", "Transfur into all latexes");

        addAdvancement("ranged_transfur", "Ranged transfurring", "Shoot with your syringe rifle");
        addAdvancement("ranged_transfur1", "Even deadlier technology", "Hit a transfurrable target from at least 64 blocks away");

        addAdvancement("armor_or_luck", "Was that armor or pure luck?", "Have a syringe bounce off you");

        //Attributes
        addAttribute(AChanged.AIR_DECREASE_SPEED, "Air Decrease Speed");
        addAttribute(AChanged.LATEX_RESISTANCE, "Latex Resistance");

        //Blocks
        addBlockFromId(AIR_CONDITIONER);
        addBlockFromId(BACKUP_GENERATOR);
        addBlockFromId(BIG_LAB_DOOR);
        addBlockFromId(BIG_LAB_LAMP);
        addBlockFromId(BIG_LIBRARY_DOOR);
        addBlockFromId(BIG_MAINTENANCE_DOOR);
        addBlockFromId(BLUE_LAB_BLOCK);
        addBlockFromId(BLUE_LAB_TILE);
        addBlockFromId(BLUE_LAB_TILE_SLAB);
        addBlockFromId(BLUE_LAB_TILE_STAIRS);
        addBlockFromId(BOLTED_BLUE_LAB_TILE);
        addBlockFromId(BOLTED_LAB_TILE);
        addBlockFromId(BROKEN_CUP);
        addBlockFromId(BROKEN_FLASK);
        addBlockFromId(BROWN_LAB_BLOCK);
        addBlockFromId(CANNED_ORANGES);
        addBlockFromId(CAPACITOR);
        addBlockFromId(CARDBOARD_BOX);
        addBlockFromId(CARPET_BLOCK);
        addBlockFromId(CHAIR);
        addBlockFromId(COMPRESSOR);
        addBlockFromId(COMPUTER);
        addBlockFromId(CONNECTED_BLUE_LAB_TILE);
        addBlockFromId(CONNECTED_LAB_TILE);
        addBlockFromId(COPPER_WIRE);
        addBlockFromId(CRYO_CHAMBER);
        addBlockFromId(CUP);
        addBlockFromId(DANGER_SIGN);
        addBlockFromId(DARK_LATEX_BLOCK);
        addBlockFromId(DARK_LATEX_CRYSTAL);
        addBlockFromId(DARK_LATEX_CRYSTAL_ICE);
        addBlock(DARK_LATEX_PUDDLE_F, "Dark Latex Puddle (f)");
        addBlock(DARK_LATEX_PUDDLE_M, "Dark Latex Puddle (m)");
        addBlockFromId(DERELICT_LATEX_ENCODER);
        addBlockFromId(DERELICT_LATEX_PURIFIER);
        addBlockFromId(DISC);
        addBlockFromId(DNA_EXTRACTOR);
        addBlockFromId(EXPOSED_PIPES);
        addBlockFromId(FLASK);
        addBlock(GAS_TANK, "Red Gas Tank");
        addBlockFromId(GENERATOR);
        addBlockFromId(GREEN_CRYSTAL);
        addBlockFromId(HAZARD_BLOCK);
        addBlockFromId(HAZARD_SLAB);
        addBlockFromId(HAZARD_STAIRS);
        addBlockFromId(HAZARD_LAB_BLOCK);
        addBlock(IV_RACK, "IV Rack");
        addBlockFromId(KEYPAD);
        addBlockFromId(LAB_BLOCK);
        addBlockFromId(LAB_SLAB);
        addBlockFromId(LAB_STAIRS);
        addBlockFromId(LAB_DOOR);
        addBlockFromId(LAB_LAMP);
        addBlockFromId(LAB_TILE);
        addBlockFromId(LAB_TILE_SLAB);
        addBlockFromId(LAB_TILE_STAIRS);
        addBlockFromId(LASER_EMITTER);
        addBlockFromId(LATEX_CONTAINER);
        addBlockFromId(LATEX_ENCODER);
        addBlockFromId(LATEX_PURIFIER);
        addBlockFromId(LATEX_RESISTANT_BLOCK);
        addBlockFromId(LATEX_RESISTANT_GLASS);
        addBlockFromId(LATEX_RESISTANT_GLASS_PANE);
        addBlockFromId(LIBRARY_DOOR);
        addBlockFromId(LIGHT_BLUE_LAB_BLOCK);
        addBlockFromId(LIME_FLOOR_CIRCLE);
        addBlockFromId(MAINTENANCE_DOOR);
        addBlockFromId(METAL_BOX);
        addBlockFromId(METAL_CAN);
        addBlockFromId(NOTE);
        addBlockFromId(NOTEPAD);
        addBlockFromId(ORANGE_BUTTON);
        addBlockFromId(ORANGE_DOOR);
        addBlockFromId(ORANGE_FENCE);
        addBlockFromId(ORANGE_FENCE_GATE);
        addBlockFromId(ORANGE_HANGING_SIGN);
        addBlockFromId(ORANGE_LAB_BLOCK);
        addBlockFromId(ORANGE_LAB_SLAB);
        addBlockFromId(ORANGE_LAB_STAIRS);
        addBlock(ORANGE_LEAVES, "Orange Tree Leaves");
        addBlockFromId(ORANGE_PLANKS);
        addBlockFromId(ORANGE_PRESSURE_PLATE);
        addBlockFromId(ORANGE_SAPLING);
        addBlockFromId(ORANGE_SIGN);
        addBlockFromId(ORANGE_SLAB);
        addBlockFromId(ORANGE_STAIRS);
        addBlockFromId(ORANGE_TRAPDOOR);
        addBlockFromId(ORANGE_TREE_LOG);
        addBlockFromId(ORANGE_WOOD);
        addBlockFromId(PILE_OF_ORANGES);
        addBlockFromId(PIPE);
        addBlockFromId(POTTED_ORANGE_SAPLING);
        addBlockFromId(RED_FLOOR_CIRCLE);
        addBlockFromId(ROTATING_CHAIR);
        addBlockFromId(SCANNER);
        addBlockFromId(SMALL_CARDBOARD_BOX);
        addBlockFromId(SMART_SEWAGE_SYSTEM);
        addBlockFromId(STRIPED_LIGHT_BLUE_LAB_BLOCK);
        addBlockFromId(STRIPED_ORANGE_LAB_BLOCK);
        addBlockFromId(STRIPPED_ORANGE_LOG);
        addBlockFromId(STRIPPED_ORANGE_WOOD);
        addBlockFromId(TABLE);
        addBlockFromId(TALL_CARDBOARD_BOX);
        addBlockFromId(TEST_TUBES);
        addBlockFromId(TRAFFIC_CONE);
        addBlock(TV_SCREEN, "TV Screen");
        addBlockFromId(VENT_DUCT);
        addBlockFromId(VENT_HATCH);
        addBlockFromId(VENT_WALL);
        addBlockFromId(WHITEBOARD);
        addBlockFromId(WHITE_LATEX_BLOCK);
        addBlockFromId(WHITE_LATEX_PILLAR);
        addBlock(WHITE_LATEX_PUDDLE_F, "White Latex Puddle (f)");
        addBlock(WHITE_LATEX_PUDDLE_M, "White Latex Puddle (m)");
        addBlockFromId(YELLOW_LAB_BLOCK);
        addBlockFromId(YELLOW_LAB_SLAB);
        addBlockFromId(YELLOW_LAB_STAIRS);

        //Command
        addCommand("latex_grab_chance.get", "Latex grab chance is ");
        addCommand("latex_grab_chance.set", "Latex grab chance is set to ");
        addCommand("transfur_tolerance.get", "Transfur tolerance is ");
        addCommand("transfur_tolerance.set", "Transfur tolerance is set to ");

        //Container
        addContainer("capacitor", "Capacitor");
        addContainer("compressor", "Compressor");
        addContainer("dna_extractor", "DNA Extractor");
        addContainer("generator", "Generator");
        addContainer("latex_encoder", "Latex Encoder");
        addContainer("latex_purifier", "Latex Purifier");

        //Death Messages
        addDeathMessage(DamageSources.assimilation, "%1$s was assimilated by %2$s", null, "%1$s was assimilated by %2$s");
        addDeathMessage(DamageSources.electricity, "%1$s was electrocuted to death", "%1$s was electrocuted to death by %2$s using %3$s", "%1$s was electrocuted to death by %2$s");
        addDeathMessage(DamageSources.solvent, "%1$s was dissolved", "%1$s was dissolved by %2$s using %3$s", "%1$s was dissolved by %2$s");
        addDeathMessage(DamageSources.syringe, "%1$s was pricked with a syringe a bit too hard", "%1$s was pricked with a syringe a bit too hard by %2$s using %3$s", "%1$s was pricked with a syringe a bit too hard by %2$s");
        addDeathMessage(DamageSources.placedSyringe, "%1$s was pricked a bit too hard by a placed syringe", null, "%1$s was pricked a bit too hard by a placed syringe of %2$s");
        addDeathMessage(DamageSources.transfur, "%1$s was transfurred", "%1$s was transfurred by %2$s using %3$s", "%1$s was transfurred by %2$s");
        addDeathMessage(DamageSources.transfurKill, "%1$s was transfurred", "%1$s was transfurred by %2$s using %3$s", "%1$s was transfurred by %2$s");
        addDeathMessage(DamageSources.untransfur, "%1$s was lethally untransfurred", "%1$s was lethally untransfurred by %2$s using %3$s", "%1$s was lethally untransfurred by %2$s");
        addDeathMessage(DamageSources.untransfurKill, "%1$s was lethally untransfurred", "%1$s was lethally untransfurred by %2$s using %3$s", "%1$s was lethally untransfurred by %2$s");

        //DNA Types
        addDNA(DNATypeRegistry.APPLE_DNA, "Apple DNA");
        addDNA(DNATypeRegistry.CAT_DNA, "Cat DNA");
        addDNA(DNATypeRegistry.COD_DNA, "Cod DNA");
        addDNA(DNATypeRegistry.SALMON_DNA, "Salmon DNA");
        addDNA(DNATypeRegistry.WOLF_DNA, "Wolf DNA");

        //Effects
        addEffect(MobEffectRegistry.ADRENALINE, "Adrenaline");
        addEffect(MobEffectRegistry.ASSIMILATION_BUFF, "Assimilation Buff");
        addEffect(MobEffectRegistry.FRIENDLY_GRAB, "You are friendly grabbed");
        addEffect(MobEffectRegistry.GRABBED_DEBUFF, "You are grabbed");
        addEffect(MobEffectRegistry.HOLDING_DEBUFF, "You are holding someone");
        addEffect(MobEffectRegistry.LATEX_SOLVENT, "Latex Solvent");
        addEffect(MobEffectRegistry.UNTRANSFUR, "Untransfur");

        //Entities
        addEntityType(EntityRegistry.BEI_FENG, "Bei Feng");
        addEntityType(EntityRegistry.BENIGN, "Benign");
        addEntityType(EntityRegistry.DARK_LATEX_PUP, "Dark Latex Pup");
        addEntityType(EntityRegistry.DARK_LATEX_WOLF_FEMALE, "Dark Latex Wolf Female");
        addEntityType(EntityRegistry.DARK_LATEX_WOLF_MALE, "Dark Latex Wolf Male");
        addEntityType(EntityRegistry.GAS_WOLF, "Gas Wolf");
        addEntityType(EntityRegistry.HYPNO_CAT, "Hypno Cat");
        addEntityType(EntityRegistry.LATEX_SHARK_FEMALE, "Latex Shark Female");
        addEntityType(EntityRegistry.LATEX_SHARK_MALE, "Latex Shark Male");
        addEntityType(EntityRegistry.MILK_PUDDING, "Milk Pudding");
        addEntityType(EntityRegistry.PURE_WHITE_LATEX_WOLF, "Pure White Latex Wolf");
        addEntityType(EntityRegistry.ROOMBA_ENTITY, "Roomba");
        addEntityType(EntityRegistry.SNOW_LEOPARD_FEMALE, "Snow Leopard Female");
        addEntityType(EntityRegistry.SNOW_LEOPARD_MALE, "Snow Leopard Male");
        addEntityType(EntityRegistry.WHITE_LATEX_WOLF_FEMALE, "White Latex Wolf Female");
        addEntityType(EntityRegistry.WHITE_LATEX_WOLF_MALE, "White Latex Wolf Male");
        addEntityType(EntityRegistry.YUFENG_DRAGON, "Yufeng Dragon");

        //Game rules
        addGamerule(AChanged.CHOOSE_TF_OR_DIE, "Allow player to choose between being transfurred and dying");
        addGamerule(AChanged.DO_LATEX_SPREAD, "Enable/disable spreading of latex covered blocks");
        addGamerule(AChanged.KEEP_TRANSFUR, "Keep transfur on death");
        addGamerule(AChanged.TRANSFUR_IS_DEATH, "Kill player if it is transfurred (ignores chooseTransfurOrDie)");

        //Keybindings
        addKey(Keybindings.ABILITY_KEY, "Ability Key");
        addKey(Keybindings.ABILITY_SELECTION, "Ability selection menu / Ability menu");
        addKey(Keybindings.QUICK_SELECT_ABILITY_1, "Quick select first active ability");
        addKey(Keybindings.QUICK_SELECT_ABILITY_2, "Quick select second active ability");
        addKey(Keybindings.QUICK_SELECT_ABILITY_3, "Quick select third active ability");
        addKey(Keybindings.MODEL_MANAGER, "Model manager");
        add("key." + modid + ".keyCategory", "Another Changed Mod");

        //Items
        addItemFromId(ABSOLUTE_SOLVER);
        addItemFromId(ADRENALINE_SYRINGE);
        addItemFromId(BIO_WASTE);
        addItemFromId(BLACK_LATEX_SHORTS);
        addItemFromId(BLOOD_SYRINGE);
        addItemFromId(CARDBOARD);
        addItemFromId(COPPER_COIL);
        addItemFromId(COPPER_PLATE);
        addItemFromId(COPPER_WRENCH);
        addItemFromId(COMPRESSED_AIR_CANISTER);
        addItemFromId(DARK_LATEX_ITEM);
        addItemFromId(DARK_LATEX_BASE);
        addItemFromId(DARK_LATEX_BUCKET);
        addItemFromId(DARK_LATEX_CRYSTAL_SHARD);
        addDItem(DNA_SAMPLE, "DNA Sample");
        addItemFromId(EMPTY_CANISTER);
        addItemFromId(GOLDEN_PLATE);
        addItemFromId(GREEN_CRYSTAL_SHARD);
        addItemFromId(HAZMAT_HELMET);
        addItemFromId(HAZMAT_CHESTPLATE);
        addItemFromId(HAZMAT_LEGGINGS);
        addItemFromId(HAZMAT_BOOTS);
        addItemFromId(IRON_PLATE);
        addItemFromId(LATEX_ENCODER_COMPONENTS);
        addItemFromId(LATEX_MANIPULATOR);
        addItemFromId(LATEX_PURIFIER_COMPONENTS);
        addItemFromId(LATEX_RESISTANT_COATING);
        addItemFromId(LATEX_RESISTANT_COMPOUND);
        addItemFromId(LATEX_RESISTANT_FABRIC);
        addItemFromId(LATEX_SOLVENT_BUCKET);
        addItemFromId(LATEX_SOLVENT_SYRINGE);
        addItemFromId(LATEX_SYRINGE);
        addItemFromId(ORANGE_ITEM);
        addItemFromId(ORANGE_JUICE_ITEM);
        addItemFromId(PNEUMATIC_SYRINGE_RIFLE);
        addItemFromId(POWER_CELL);
        addItemFromId(STABILIZED_LATEX_SYRINGE);
        addItemFromId(STATE_KEY);
        addItemFromId(STUN_BATON);
        addItemFromId(STUN_LANCE);
        addItemFromId(SYRINGE_ITEM);
        addItemFromId(SYRINGE_COIL_GUN);
        addItemFromId(UNIVERSAL_UNTRANSFUR_SYRINGE);
        addItemFromId(DARK_LATEX_UNTRANSFUR_SYRINGE);
        addItemFromId(WHITE_LATEX_UNTRANSFUR_SYRINGE);
        addDItem(UNTRANSFUR_BOTTLE_ITEM, "Bottle of Untransfur");
        addItemFromId(UNTRANSFUR_SYNTHESIZER_COMPONENTS);
        addItemFromId(WHITE_LATEX_ITEM);
        addItemFromId(WHITE_LATEX_BASE);
        addItemFromId(WHITE_LATEX_BUCKET);

        addItemFromId(BEI_FENG_EGG);
        addItemFromId(BENIGN_EGG);
        addItemFromId(DARK_LATEX_PUP_EGG);
        addItemFromId(DARK_LATEX_WOLF_F_EGG);
        addItemFromId(DARK_LATEX_WOLF_M_EGG);
        addItemFromId(GAS_WOLF_EGG);
        addItemFromId(HYPNO_CAT_EGG);
        addItemFromId(LATEX_SHARK_F_EGG);
        addItemFromId(LATEX_SHARK_M_EGG);
        addItemFromId(MILK_PUDDING);
        addItemFromId(PURE_WHITE_LATEX_WOLF_EGG);
        addItemFromId(ROOMBA_SPAWN_EGG);
        addItemFromId(SNOW_LEOPARD_F_EGG);
        addItemFromId(SNOW_LEOPARD_M_EGG);
        addItemFromId(WHITE_LATEX_PUP_EGG);
        addItemFromId(WHITE_LATEX_WOLF_F_EGG);
        addItemFromId(WHITE_LATEX_WOLF_M_EGG);
        addItemFromId(YUFENG_DRAGON_EGG);

        add("itemGroup." + modid + ".main", "Another Changed Mod");
        add("itemGroup." + modid + ".transfurs", "Transfurs & Equipment");

        //Message
        addMessage("grab_cooldown", "Grab ability is on cooldown for %1$s s");
        addMessage("self_held_already", "%1$s is holding you already!");
        addMessage("target_held_already", "%1$s is holding %2$s already!");
        addMessage("cannot_grab_with_selected_mode", "Cannot grab %1$s with selected mode!");
        addMessage("grabbed_entity_changed_dim", "%1$s changed dimension");
        addMessage("grabbed_entity_died", "%1$s died");
        addMessage("grabbed_player_left", "%1$s left the game");
        addMessage("player_doesnt_want_to_be_grabbed", "%1$s doesn't want to be grabbed!");
        addMessage("try_escape_tip", "Press %1$s to try to escape");

        //Misc
        addMisc("keypad_attempt", "Try");
        addMisc("keypad_save_password", "Save password");
        addMisc("transfur", "Transfur and keep control");
        addMisc("transfur_die", "Die");

        //Screen
        addScreen("grab_escape.clicks", "Clicks: %1$s / %2$s");
        addScreen("grab_escape.time_remaining", "Seconds remaining: %1$s");

        //Sounds
        addSound(SoundRegistry.BUTTON_PRESSED, "Button pressed");
        addSound(SoundRegistry.COMPRESSOR, "Compressor is active");
        addSound(SoundRegistry.DOOR_CLOSE, "Door closed");
        addSound(SoundRegistry.DOOR_LOCKED, "Door is locked");
        addSound(SoundRegistry.DOOR_OPEN, "Door opened");
        addSound(SoundRegistry.GAS_LEAK, "Gas leak");
        addSound(SoundRegistry.KEYPAD_UNLOCKED, "Keypad unlocked");
        addSound(SoundRegistry.KEYPAD_WRONG_PASSWORD, "Wrong password entered");
        addSound(SoundRegistry.LASER, "Laser activated / deactivated");
        addSound(SoundRegistry.PNEUMATIC_RIFLE, "Pneumatic rifle shot");
        addSound(SoundRegistry.PUSH, "Box pushed");
        addSound(SoundRegistry.SAVE, "Checkpoint saved");
        addSound(SoundRegistry.SMART_SEWAGE_CONSUME, "Smart sewage absorbed something");
        addSound(SoundRegistry.SPACE_DOOR_CLOSE, "Cryo chamber closed");
        addSound(SoundRegistry.SPACE_DOOR_OPEN, "Cryo chamber opened");
        addSound(SoundRegistry.TRANSFUR, "Something was transfurred");
        addSound(SoundRegistry.TRANSFUR_1, "Something was transfurred by crystal");

        //Tooltips
        addTooltip("books", "Shift right click on a block to place a book stack");
        addTooltip("blood_syringe", "Blood of %1$s");
        addTooltip("floor_circle", "Shift right click on a block to draw a circle");
        addTooltip("latex_manipulator", "Saved transfur: %1$s");
        addTooltip("notes", "Shift right click on a wall to place a note or on floor to place paper");
        addTooltip("paper_stack.erase", "R - click on paper stack to erase");
        addTooltip("paper_stack.write", "R - click on paper stack to write");
        addTooltip("state_key", "(Un)locks state of connected lab blocks & carpet");
        addTooltip("stun_baton.off", "Disabled");
        addTooltip("stun_baton.on", "Enabled");
        addTooltip("syringe_rifle.air", "Enough compressed air for %1$s shots");
        addTooltip("syringe_rifle.energy", "Enough energy for %1$s shots");
        addTooltip("syringe_rifle.shots", "Syringes loaded: %1$s");
        addTooltip("wires", "Throughput: %1$s EU/t");

        //Transfurs
        TransfurRegistry.TRANSFUR_TYPES.getEntries().forEach(this::addTransfur);
    }
}