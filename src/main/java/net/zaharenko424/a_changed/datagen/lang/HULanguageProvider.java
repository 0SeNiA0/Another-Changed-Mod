package net.zaharenko424.a_changed.datagen.lang;

import net.minecraft.data.PackOutput;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.Keybindings;
import net.zaharenko424.a_changed.registry.*;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;

import static net.zaharenko424.a_changed.registry.BlockRegistry.*;
import static net.zaharenko424.a_changed.registry.ItemRegistry.*;

public class HULanguageProvider extends LanguageProvider {

    public HULanguageProvider(PackOutput output) {
        super(output, AChanged.MODID, "hu_hu");
    }

    @Override
    protected void addTranslations() {
        //Advancements
        addAdvancement("root", "Another Changed Mod", "");

        addAdvancement("orange", "Narancs!", "Szerezzen narancsot");
        addAdvancement("orange_juice", "Folyékony narancs?", "Szerezzen narancslevet");
        addAdvancement("canned_oranges", "Egy narancskonzerv", "Szerezzen narancskonzervet");
        
        addAdvancement("step_on_syringe", "Ki helyezte ezt ide?", "Lépjen rá egy fecskendőre");
        addAdvancement("step_on_all_syringes", "A terület most már tiszta", "Lépjen rá minden típusú fecskendőre");

        addAdvancement("get_transfurred", "Egy érdekes név", "Legyél transzfurálva");
        addAdvancement("cat_transfur", "Hmm, ez tényleg hasznos", "Legyél transzfurálva macska latexszé");
        addAdvancement("swimming_transfur", "Vízbefúltak félnek tőlük", "Legyél transzfurálva vízi latexszé");
        addAdvancement("flying_transfur", "Kinek kell kitinszárny?", "Legyél transzfurálva repülő latexszé");
        addAdvancement("all_transfurs", "Transzfurálnom kell mindegyikké!", "Legyél transzfurálva minden latexszé");

        addAdvancement("ranged_transfur", "Távolsági transzfurálás", "Lőj a fecskendőt lövő puskáddal");
        addAdvancement("ranged_transfur1", "Még halálosabb technológia", "Találj el egy transzfurálható célpontot legalább 64 blokk távolságról.");

        addAdvancement("armor_or_luck", "Ez páncél volt, vagy tiszta szerencse?", "Pattanjon le rólad egy fecskendő");

        //Attributes
        addAttribute(AChanged.AIR_DECREASE_SPEED, "Légveszteség sebessége");
        addAttribute(AChanged.LATEX_RESISTANCE, "Latexellenállás");

        //Blocks
        addBlock(AIR_CONDITIONER, "Légkondicionáló");
        addBlock(BACKUP_GENERATOR, "Tartalék generátor");
        addBlock(BIG_LAB_DOOR, "Nagy laborajtó");
        addBlock(BIG_LAB_LAMP, "Nagy laborlámpa");
        addBlock(BIG_LIBRARY_DOOR, "Nagy könyvtárajtó");
        addBlock(BIG_MAINTENANCE_DOOR, "Nagy karbantartási ajtó");
        addBlock(BLUE_LAB_BLOCK, "Kék labor blokk");
        addBlock(BLUE_LAB_TILE, "Kék labor burkolat");
        addBlock(BLUE_LAB_TILE_SLAB, "Kék labor burkolat lap");
        addBlock(BLUE_LAB_TILE_STAIRS, "Kék labor burkolatlépcső");
        addBlock(BOLTED_BLUE_LAB_TILE, "Csavarozott kék labor burkolat");
        addBlock(BOLTED_LAB_TILE, "Csavarozott labor burkolat");
        addBlock(BROKEN_CUP, "Törött csésze");
        addBlock(BROKEN_FLASK, "Törött lombik");
        addBlock(BROWN_LAB_BLOCK, "Barna labor blokk");
        addBlock(CANNED_ORANGES, "Narancskonzerv");
        addBlock(CAPACITOR, "Kondenzátor");
        addBlock(CARDBOARD_BOX, "Kartondoboz");
        addBlock(CARPET_BLOCK, "Szőnyegblokk");
        addBlock(CHAIR, "Szék");
        addBlock(COMPRESSOR, "Kompresszor");
        addBlock(COMPUTER, "Számítógép");
        addBlock(CONNECTED_BLUE_LAB_TILE, "Csatlakoztatható kék labor burkolat");
        addBlock(CONNECTED_LAB_TILE, "Csatlakoztatható labor burkolat");
        addBlock(COPPER_WIRE, "Réz huzal");
        addBlock(CRYO_CHAMBER, "Kriokamra");
        addBlock(CUP, "Csésze");
        addBlock(DANGER_SIGN, "Vészjelző tábla");
        addBlock(DARK_LATEX_BLOCK, "Sötét latex blokk");
        addBlock(DARK_LATEX_CRYSTAL, "Sötét latex kristály");
        addBlock(DARK_LATEX_CRYSTAL_ICE, "Sötét latex jég");
        addBlock(DARK_LATEX_PUDDLE_F, "Nőstény sötét latex pocsolya");
        addBlock(DARK_LATEX_PUDDLE_M, "Hím sötét latex pocsolya");
        addBlock(DERELICT_LATEX_ENCODER, "Elhagyott latex enkóder");
        addBlock(DERELICT_LATEX_PURIFIER, "Elhagyott latex tisztító");
        addBlock(DISC, "Lemez");
        addBlock(DNA_EXTRACTOR, "DNS-kivonó");
        addBlock(EXPOSED_PIPES, "Fedetlen csövek");
        addBlock(FLASK, "Lombik");
        addBlock(GAS_TANK, "Vörös gáztartály");
        addBlock(GENERATOR, "Generátor");
        addBlock(GREEN_CRYSTAL, "Zöld kristály");
        addBlock(HAZARD_BLOCK, "Figyelmeztető blokk");
        addBlock(HAZARD_SLAB, "Figyelmeztető lap");
        addBlock(HAZARD_STAIRS, "Figyelmeztető lépcső");
        addBlock(HAZARD_LAB_BLOCK, "Figyelmeztető labor blokk");
        addBlock(IV_RACK, "Infúziós állvány");
        addBlock(KEYPAD, "Billentyűzet");
        addBlock(LAB_BLOCK, "Labor blokk");
        addBlock(LAB_SLAB, "Labor lap");
        addBlock(LAB_STAIRS, "Labor lépcső");
        addBlock(LAB_DOOR, "Laborajtó");
        addBlock(LAB_LAMP, "Laborlámpa");
        addBlock(LAB_TILE, "Labor burkolat");
        addBlock(LAB_TILE_SLAB, "Labor burkolat lap");
        addBlock(LAB_TILE_STAIRS, "Labor burkolatlépcső");
        addBlock(LASER_EMITTER, "Lézerkibocsátó");
        addBlock(LATEX_CONTAINER, "Latex tároló");
        addBlock(LATEX_ENCODER, "Latex enkóder");
        addBlock(LATEX_PURIFIER, "Latex tisztító");
        addBlock(LATEX_RESISTANT_BLOCK, "Latexálló blokk");
        addBlock(LATEX_RESISTANT_GLASS, "Latexálló üveg");
        addBlock(LATEX_RESISTANT_GLASS_PANE, "Latexálló üveglap");
        addBlock(LIBRARY_DOOR, "Könyvtárajtó");
        addBlock(LIGHT_BLUE_LAB_BLOCK, "Világoskék labor blokk");
        addBlock(LIME_FLOOR_CIRCLE, "Világoszöld padlókör");
        addBlock(MAINTENANCE_DOOR, "Karbantartási ajtó");
        addBlock(METAL_BOX, "Fémdoboz");
        addBlock(METAL_CAN, "Fém konzervdoboz");
        addBlock(NOTE, "Jegyzet");
        addBlock(NOTEPAD, "Jegyzettömb");
        addBlock(ORANGE_BUTTON, "Narancsfa gomb");
        addBlock(ORANGE_DOOR, "Narancsfa ajtó");
        addBlock(ORANGE_FENCE, "Narancsfa kerítés");
        addBlock(ORANGE_FENCE_GATE, "Narancsfa kerítéskapu");
        addBlock(ORANGE_HANGING_SIGN, "Narancsfa függőtábla");
        addBlock(ORANGE_LAB_BLOCK, "Narancssárga labor blokk");
        addBlock(ORANGE_LAB_SLAB, "Narancssárga labor lap");
        addBlock(ORANGE_LAB_STAIRS, "Narancssárga labor lépcső");
        addBlock(ORANGE_LEAVES, "Narancsfalevelek");
        addBlock(ORANGE_PLANKS, "Narancsfa deszka");
        addBlock(ORANGE_PRESSURE_PLATE, "Narancsfa nyomólap");
        addBlock(ORANGE_SAPLING, "Narancsfa-csemete");
        addBlock(ORANGE_SIGN, "Narancsfa tábla");
        addBlock(ORANGE_SLAB, "Narancsfa lap");
        addBlock(ORANGE_STAIRS, "Narancsfa lépcső");
        addBlock(ORANGE_TRAPDOOR, "Narancsfa csapóajtó");
        addBlock(ORANGE_TREE_LOG, "Narancsfarönk");
        addBlock(ORANGE_WOOD, "Narancsfablokk");
        addBlock(PILE_OF_ORANGES, "Narancshalom");
        addBlock(PIPE, "Cső");
        addBlock(POTTED_ORANGE_SAPLING, "Virágcserép narancsfa-csemetével");
        addBlock(RED_FLOOR_CIRCLE, "Vörös padlókör");
        addBlock(ROTATING_CHAIR, "Forgószék");
        addBlock(SCANNER, "Szkenner");
        addBlock(SMALL_CARDBOARD_BOX, "Kicsi kartondoboz");
        addBlock(SMART_SEWAGE_SYSTEM, "Okos szennyvízrendszer");
        addBlock(STRIPED_LIGHT_BLUE_LAB_BLOCK, "Csíkos világoskék labor blokk");
        addBlock(STRIPED_ORANGE_LAB_BLOCK, "Csíkos narancssárga labor blokk");
        addBlock(STRIPPED_ORANGE_LOG, "Kérgezett narancsfarönk");
        addBlock(STRIPPED_ORANGE_WOOD, "Kérgezett narancsfablokk");
        addBlock(TABLE, "Asztal");
        addBlock(TALL_CARDBOARD_BOX, "Magas kartondoboz");
        addBlock(TEST_TUBES, "Kémcsövek");
        addBlock(TRAFFIC_CONE, "Bólya");
        addBlock(TV_SCREEN, "TV-képernyő");
        addBlock(VENT_DUCT, "Szellőztető csatorna");
        addBlock(VENT_HATCH, "Szellőzőnyílás");
        addBlock(VENT_WALL, "Szellőztetőfal");
        addBlock(WHITEBOARD, "Fehér tábla");
        addBlock(WHITE_LATEX_BLOCK, "Fehér latex blokk");
        addBlock(WHITE_LATEX_PILLAR, "Fehér latex oszlop");
        addBlock(WHITE_LATEX_PUDDLE_F, "Nőstény fehér latex pocsolya");
        addBlock(WHITE_LATEX_PUDDLE_M, "Hím fehér latex pocsolya");
        addBlock(YELLOW_LAB_BLOCK, "Sárga labor blokk");
        addBlock(YELLOW_LAB_SLAB, "Sárga labor lap");
        addBlock(YELLOW_LAB_STAIRS, "Sárga labor lépcső");

        //Command
        addCommand("latex_grab_chance.get", "Latex megragadásának esélye ");
        addCommand("latex_grab_chance.set", "Latex megragadásának esélye megváltoztatva erre: ");
        addCommand("transfur_tolerance.get", "Transzfur tolerancia értéke ");
        addCommand("transfur_tolerance.set", "Transzfur tolerancia értéke megváltoztatva erre: ");

        //Container
        addContainer("capacitor", "Kondenzátor");
        addContainer("compressor", "Kompresszor");
        addContainer("dna_extractor", "DNS-kivonó");
        addContainer("generator", "Generátor");
        addContainer("latex_encoder", "Latex enkóder");
        addContainer("latex_purifier", "Latex tisztító");

        //Death Messages
        addDeathMessage(DamageSources.assimilation, "%2$s elnyelte %1$s játékost", null, "%2$s elnyelte %1$s játékost");
        addDeathMessage(DamageSources.electricity, "%1$s halálos áramütés érte", "%1$s halálos áramütést szenvedett %2$s által %3$s használatával", "%1$s halálos áramütést szenvedett %2$s által");
        addDeathMessage(DamageSources.solvent, "%1$s feloldódott", "%2$s feloldotta %1$s játékost %3$s használatával", "%2$s feloldotta %1$s játékost");
        addDeathMessage(DamageSources.syringe, "%1$s játékost egy kicsit túl erősen szúrta meg %2$s egy fecskendővel", "%1$s játékost egy kicsit túl erősen szúrta meg %2$s egy fecskendővel", "%1$s játékost egy kicsit túl erősen szúrta meg %2$s egy fecskendővel");
        addDeathMessage(DamageSources.placedSyringe, "%1$s játékost egy kicsit túl erősen megszúrta egy fecskendő", null, "%1$s-t egy kicsit túl erősen megszúrta egy %2$s által elhelyezett fecskendő");
        addDeathMessage(DamageSources.transfur, "%1$s transzfurált", "%2$s transzfurálta %1$s játékost %3$s használatával", "%2$s transzfurálta %1$s játékost");
        addDeathMessage(DamageSources.transfurKill, "%1$s transzfurált", "%2$s transzfurálta %1$s játékost %3$s használatával", "%2$s transzfurálta %1$s játékost");
        addDeathMessage(DamageSources.untransfur, "%1$s halálosan visszatranszfurált", "%2$s halálosan visszatranszfurálta %1$s játékost %3$s használatával", "%2$s halálosan visszatranszfurálta %1$s játékost");
        addDeathMessage(DamageSources.untransfurKill, "%1$s halálosan visszatranszfurált", "%2$s halálosan visszatranszfurálta %1$s játékost %3$s használatával", "%2$s halálosan visszatranszfurálta %1$s játékost");

        //DNA Types
        addDNA(DNATypeRegistry.APPLE_DNA, "Alma DNS");
        addDNA(DNATypeRegistry.CAT_DNA, "Macska DNS");
        addDNA(DNATypeRegistry.COD_DNA, "Tőkehal DNS");
        addDNA(DNATypeRegistry.SALMON_DNA, "Lazac DNS");
        addDNA(DNATypeRegistry.WOLF_DNA, "Farkas DNS");

        //Effects
        addEffect(MobEffectRegistry.ADRENALINE, "Adrenalin");
        addEffect(MobEffectRegistry.ASSIMILATION_BUFF, "Elnyelési buff");
        addEffect(MobEffectRegistry.FRIENDLY_GRAB, "Barátságosan megragadtak");
        addEffect(MobEffectRegistry.GRABBED_DEBUFF, "Megragadtak");
        addEffect(MobEffectRegistry.HOLDING_DEBUFF, "Tartasz valakit");
        addEffect(MobEffectRegistry.LATEX_SOLVENT, "Latexoldás");
        addEffect(MobEffectRegistry.UNTRANSFUR, "Visszatranszfur");

        //Entities
        addEntityType(EntityRegistry.BEI_FENG, "Pejfeng");
        addEntityType(EntityRegistry.BENIGN, "Jóságos latex");
        addEntityType(EntityRegistry.DARK_LATEX_PUP, "Sötét latex farkaskölyök");
        addEntityType(EntityRegistry.DARK_LATEX_WOLF_FEMALE, "Nőstény sötét latex farkas");
        addEntityType(EntityRegistry.DARK_LATEX_WOLF_MALE, "Hím sötét latex farkas");
        addEntityType(EntityRegistry.GAS_WOLF, "Gáz farkas");
        addEntityType(EntityRegistry.HYPNO_CAT, "Hipnómacska");
        addEntityType(EntityRegistry.LATEX_SHARK_FEMALE, "Nőstény latex cápa");
        addEntityType(EntityRegistry.LATEX_SHARK_MALE, "Hím latex cápa");
        addEntityType(EntityRegistry.MILK_PUDDING, "Tejpuding");
        addEntityType(EntityRegistry.PURE_WHITE_LATEX_WOLF, "Tiszta fehér latex farkas");
        addEntityType(EntityRegistry.ROOMBA_ENTITY, "Robotporszívó");
        addEntityType(EntityRegistry.SNOW_LEOPARD_FEMALE, "Nőstény hópárduc");
        addEntityType(EntityRegistry.SNOW_LEOPARD_MALE, "Hím hópárduc");
        addEntityType(EntityRegistry.WHITE_LATEX_PUP, "Fehér latex farkaskölyök");
        addEntityType(EntityRegistry.WHITE_LATEX_WOLF_FEMALE, "Nőstény fehér latex farkas");
        addEntityType(EntityRegistry.WHITE_LATEX_WOLF_MALE, "Hím fehér latex farkas");
        addEntityType(EntityRegistry.YUFENG_DRAGON, "Jüfeng sárkány");

        //Game rules
        addGamerule(AChanged.CHOOSE_TF_OR_DIE, "Engedd meg a játékosnak, hogy válasszon a transzfurt vagy a halált");
        addGamerule(AChanged.DO_LATEX_SPREAD, "Latex bevonatú blokkok terjedésének engedélyezése/letiltása");
        addGamerule(AChanged.KEEP_TRANSFUR, "Transzfur megtartása halálkor");
        addGamerule(AChanged.TRANSFUR_IS_DEATH, "Játékos megölése, amikor transzfurálva lett (figyelmen kívül hagyja a válasszonTranszfurtVagyHalált játékszabályt)");

        //Keybindings
        addKey(Keybindings.ABILITY_KEY, "Képességbillentyű");
        addKey(Keybindings.ABILITY_SELECTION, "Képességválasztó menü / Képességmenü");
        addKey(Keybindings.QUICK_SELECT_ABILITY_1, "Első aktív képesség gyors kiválasztása");
        addKey(Keybindings.QUICK_SELECT_ABILITY_2, "Második aktív képesség gyors kiválasztása");
        addKey(Keybindings.QUICK_SELECT_ABILITY_3, "Harmadik aktív képesség gyors kiválasztása");
        addKey(Keybindings.MODEL_MANAGER, "Modellkezelő");
        add("key." + modid + ".keyCategory", "Another Changed Mod");

        //Items
        addDItem(ABSOLUTE_SOLVER, "Abszolút oldószer");
        addDItem(ADRENALINE_SYRINGE, "Adrenalinnal teli fecskendő");
        addDItem(BIO_WASTE, "Biohulladék");
        addDItem(BLACK_LATEX_SHORTS, "Fekete latex rövidnadrág");
        addDItem(BLOOD_SYRINGE, "Vérrel teli fecskendő");
        addDItem(CARDBOARD, "Karton");
        addDItem(COPPER_COIL, "Réz tekercs");
        addDItem(COPPER_PLATE, "Rézlemez");
        addDItem(COPPER_WRENCH, "Réz csavarkulcs");
        addDItem(COMPRESSED_AIR_CANISTER, "Sűrített levegős tartály");
        addDItem(DARK_LATEX_ITEM, "Sötét latex");
        addDItem(DARK_LATEX_BASE, "Sötét latex alap");
        addDItem(DARK_LATEX_BUCKET, "Sötét latexes vödör");
        addDItem(DARK_LATEX_CRYSTAL_SHARD, "Sötét latex kristályszilánk");
        addDItem(DNA_SAMPLE, "DNS minta");
        addDItem(EMPTY_CANISTER, "Üres tartály");
        addDItem(GOLDEN_PLATE, "Aranylemez");
        addDItem(GREEN_CRYSTAL_SHARD, "Zöld kristályszilánk");
        addDItem(HAZMAT_HELMET, "Védőruha sisak");
        addDItem(HAZMAT_CHESTPLATE, "Védőruha mellvért");
        addDItem(HAZMAT_LEGGINGS, "Védőruha lábszárvédő");
        addDItem(HAZMAT_BOOTS, "Védőruha csizma");
        addDItem(IRON_PLATE, "Vaslemez");
        addDItem(LATEX_ENCODER_COMPONENTS, "Latex enkóder alkatrészek");
        addDItem(LATEX_MANIPULATOR, "Latex manipulátor");
        addDItem(LATEX_PURIFIER_COMPONENTS, "Latex tisztító alkatrészek");
        addDItem(LATEX_RESISTANT_COATING, "Latexálló bevonat");
        addDItem(LATEX_RESISTANT_COMPOUND, "Latexálló keverék");
        addDItem(LATEX_RESISTANT_FABRIC, "Latexálló szövet");
        addDItem(LATEX_SOLVENT_BUCKET, "Latexoldóval teli vödör");
        addDItem(LATEX_SOLVENT_SYRINGE, "Latexoldóval teli fecskendő");
        addDItem(LATEX_SYRINGE, "Latexszel teli fecskendő");
        addDItem(ORANGE_ITEM, "Narancs");
        addDItem(ORANGE_JUICE_ITEM, "Narancslé");
        addDItem(PNEUMATIC_SYRINGE_RIFLE, "Pneumatikus fecskendőt lövő puska");
        addDItem(POWER_CELL, "Tápegység");
        addDItem(STABILIZED_LATEX_SYRINGE, "Stabilizált latexszel teli fecskendő");
        addDItem(STATE_KEY, "Állapotkulcs");
        addDItem(STUN_BATON, "Elektromos bot");
        addDItem(STUN_LANCE, "Elektromos lándzsa");
        addDItem(SYRINGE_ITEM, "Fecskendő");
        addDItem(SYRINGE_COIL_GUN, "Fecskendőt lövő coilgun");
        addDItem(UNIVERSAL_UNTRANSFUR_SYRINGE, "Univerzális visszatranszfuráló fecskendő");
        addDItem(DARK_LATEX_UNTRANSFUR_SYRINGE, "Sötét latexes visszatranszfuráló fecskendő");
        addDItem(WHITE_LATEX_UNTRANSFUR_SYRINGE, "Fehér latexes visszatranszfuráló fecskendő");
        addDItem(UNTRANSFUR_BOTTLE_ITEM, "Visszatranszfuros palack");
        addDItem(UNTRANSFUR_SYNTHESIZER_COMPONENTS, "Visszatranszfur előállító alkatrészek");
        addDItem(WHITE_LATEX_ITEM, "Fehér latex");
        addDItem(WHITE_LATEX_BASE, "Fehér latex alap");
        addDItem(WHITE_LATEX_BUCKET, "Fehér latexes vödör");

        addDItem(BEI_FENG_EGG, "Pejfengidéző tojás");
        addDItem(BENIGN_EGG, "Jóságos latexet idéző tojás");
        addDItem(DARK_LATEX_PUP_EGG, "Sötét latex farkaskölyköt idéző tojás");
        addDItem(DARK_LATEX_WOLF_F_EGG, "Nőstény sötét latex farkast idéző tojás");
        addDItem(DARK_LATEX_WOLF_M_EGG, "Hím sötét latex farkast idéző tojás");
        addDItem(GAS_WOLF_EGG, "Gáz farkast idéző tojás");
        addDItem(HYPNO_CAT_EGG, "Hipnómacskaidéző tojás");
        addDItem(LATEX_SHARK_F_EGG, "Nőstény latex cápát idéző tojás");
        addDItem(LATEX_SHARK_M_EGG, "Hím latex cápát idéző tojás");
        addDItem(MILK_PUDDING, "Tejpudingidéző tojás");
        addDItem(PURE_WHITE_LATEX_WOLF_EGG, "Tiszta fehér latex farkast idéző tojás");
        addDItem(ROOMBA_SPAWN_EGG, "Robotporszívót idéző tojás");
        addDItem(SNOW_LEOPARD_F_EGG, "Nőstény hópárducot idéző tojás");
        addDItem(SNOW_LEOPARD_M_EGG, "Hím hópárducot idéző tojás");
        addDItem(WHITE_LATEX_PUP_EGG, "Fehér latex farkaskölyköt idéző tojás");
        addDItem(WHITE_LATEX_WOLF_F_EGG, "Nőstény fehér latex farkast idéző tojás");
        addDItem(WHITE_LATEX_WOLF_M_EGG, "Hím fehér latex farkast idéző tojás");
        addDItem(YUFENG_DRAGON_EGG, "Jüfeng sárkányt idéző tojás");

        add("itemGroup." + modid + ".main", "Another Changed Mod");
        add("itemGroup." + modid + ".transfurs", "Transzfurok & felszerelések");

        //Message
        addMessage("grab_cooldown", "A megragadási képesség %1$s másodpercig visszatöltési időben van.");
        addMessage("target_held_already", "%1$s már %2$s-t tart a kezében!");
        addMessage("cannot_grab_with_selected_mode", "A %1$s nem ragadható meg a kiválasztott móddal!");
        addMessage("grabbed_entity_changed_dim", "%1$s dimenziót változott");
        addMessage("grabbed_entity_died", "%1$s meghalt");
        addMessage("grabbed_player_left", "%1$s elhagyta a játékot");
        addMessage("player_doesnt_want_to_be_grabbed", "%1$s nem akarja, hogy megragadják!");
        addMessage("try_escape_tip", "Nyomja meg a(z) %1$s gombot a kimeneküléshez");

        //Misc
        addMisc("keypad_attempt", "Próbálj");
        addMisc("keypad_save_password", "Jelszó mentése");
        addMisc("transfur", "Legyél transzfurálva és maradj önmagad");
        addMisc("transfur_die", "Halj meg");

        //Screen
        addScreen("grab_escape.clicks", "Kattintások: %1$s / %2$s");
        addScreen("grab_escape.time_remaining", "Hátralévő másodpercek: %1$s");

        //Sounds
        addSound(SoundRegistry.BUTTON_PRESSED, "Gomb megnyomva");
        addSound(SoundRegistry.COMPRESSOR, "Kompresszor aktív");
        addSound(SoundRegistry.DOOR_CLOSE, "Ajtó bezárva");
        addSound(SoundRegistry.DOOR_LOCKED, "Ajtó zárva van");
        addSound(SoundRegistry.DOOR_OPEN, "Ajtó kinyítva");
        addSound(SoundRegistry.GAS_LEAK, "Gázszivárgás");
        addSound(SoundRegistry.KEYPAD_UNLOCKED, "Billentyűzet feloldva");
        addSound(SoundRegistry.KEYPAD_WRONG_PASSWORD, "Rossz jelszót adott meg");
        addSound(SoundRegistry.LASER, "Lézer aktiválva / deaktiválva");
        addSound(SoundRegistry.PNEUMATIC_RIFLE, "Pneumatikus puska lőve");
        addSound(SoundRegistry.PUSH, "Doboz tolva");
        addSound(SoundRegistry.SAVE, "Ellenőrzőpont mentve");
        addSound(SoundRegistry.SMART_SEWAGE_CONSUME, "Okos szennyvízrendszer elnyelt valamit");
        addSound(SoundRegistry.SPACE_DOOR_CLOSE, "Kriokamra bezárva");
        addSound(SoundRegistry.SPACE_DOOR_OPEN, "Kriokamra kinyítva");
        addSound(SoundRegistry.TRANSFUR, "Valami transzfurálva lett");
        addSound(SoundRegistry.TRANSFUR_1, "Valami transzfurálva lett kristály által");

        //Tooltips
        addTooltip("books", "Shift billentyűt nyomva jobb gombbal kattintson egy blokkra könyvköteg elhelyezéséhez");
        addTooltip("blood_syringe", "%1$s vére");
        addTooltip("floor_circle", "Shift billentyűt nyomva jobb gombbal kattintson egy blokkra kör rajzolásához");
        addTooltip("latex_manipulator", "Mentett transzfur: %1$s");
        addTooltip("notes", "Shift billentyűt nyomva jobb gombbal kattintson egy falra jegyzet elhelyezéséhez vagy a padlóra papír elhelyezéséhez");
        addTooltip("paper_stack.erase", "A törléshez kattintson a jobb gombbal a papírkötegre");
        addTooltip("paper_stack.write", "Íráshoz kattintson a jobb gombbal a papírkötegre");
        addTooltip("state_key", "Feloldja/Zárolja a csatlakoztatott labor blokkok és szőnyegek állapotát");
        addTooltip("stun_baton.off", "Kikapcsolva");
        addTooltip("stun_baton.on", "Bekapcsolva");
        addTooltip("syringe_rifle.air", "Elegendő sűrített levegő %1$s lövéshez");
        addTooltip("syringe_rifle.energy", "Elég energia %1$s lövéshez");
        addTooltip("syringe_rifle.shots", "Betöltött fecskendők: %1$s");
        addTooltip("wires", "Áteresztőképesség: %1$s EU/t");

        //Transfurs
        add("transfur." + modid + ".bei_feng", "Pejfeng");
        add("transfur." + modid + ".benign", "Jóságos latex");
        add("transfur." + modid + ".dark_latex_pup", "Sötét latex farkaskölyök");
        add("transfur." + modid + ".dark_latex_wolf_female", "Nőstény sötét latex farkas");
        add("transfur." + modid + ".dark_latex_wolf_male", "Hím sötét latex farkas");
        add("transfur." + modid + ".gas_wolf", "Gáz farkas");
        add("transfur." + modid + ".hypno_cat", "Hipnómacska");
        add("transfur." + modid + ".latex_shark_female", "Nőstény latex cápa");
        add("transfur." + modid + ".latex_shark_male", "Hím latex cápa");
        add("transfur." + modid + ".pure_white_latex_wolf", "Tiszta fehér latex farkas");
        add("transfur." + modid + ".snow_leopard_female", "Nőstény hópárduc");
        add("transfur." + modid + ".snow_leopard_male", "Hím hópárduc");
        add("transfur." + modid + ".special", "Speciális");
        add("transfur." + modid + ".white_latex_pup", "Fehér latex farkaskölyök");
        add("transfur." + modid + ".white_latex_wolf_female", "Nőstény fehér latex farkas");
        add("transfur." + modid + ".white_latex_wolf_male", "Hím fehér latex farkas");
        add("transfur." + modid + ".yufeng_dragon", "Jüfeng sárkány");
    }
}