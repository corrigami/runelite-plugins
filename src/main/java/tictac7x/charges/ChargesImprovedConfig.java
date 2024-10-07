package tictac7x.charges;

import net.runelite.client.config.*;
import tictac7x.charges.store.Charges;
import tictac7x.charges.store.ItemActivity;

import java.awt.Color;

import static tictac7x.charges.ChargesImprovedConfig.group;

@ConfigGroup(group)
public interface ChargesImprovedConfig extends Config {
    String group = "tictac7x-charges";
    String version = "version";
    String storage = "storage";
    String date = "date";
    String debug_ids = "debug_ids";
    String infobox = "_infobox";
    String overlay = "_overlay";

    String arclight = "arclight";
    String ardougne_cloak = "ardougne_cloak";
    String ash_sanctifier = "ash_sanctifier";
    String ash_sanctifier_status = "ash_sanctifier_status";
    String barrows_gear = "barrows_gear";
    String bonecrusher = "bonecrusher";
    String bonecrusher_status = "bonecrusher_status";
    String bottomless_compost_bucket = "bottomless_compost_bucket";
    String bottomless_compost_bucket_type = "bottomless_compost_bucket_type";
    String bow_of_faerdhinen = "bow_of_faerdhinen";
    String bracelet_of_clay = "bracelet_of_clay";
    String bracelet_of_expeditious = "bracelet_of_expeditious";
    String bracelet_of_flamtaer = "bracelet_of_flamtaer";
    String bracelet_of_slaughter = "bracelet_of_slaughter";
    String bryophytas_staff = "bryophytas_staff";
    String camulet = "camulet";
    String celestial_ring = "celestial_ring";
    String chronicle = "chronicle";
    String circlet_of_water = "circlet_of_water";
    String coal_bag = "coal_bag";
    String coffin = "coffin";
    String crystal_body = "crystal_body";
    String crystal_bow = "crystal_bow";
    String crystal_halberd = "crystal_halberd";
    String crystal_helm = "crystal_helm";
    String crystal_legs = "crystal_legs";
    String crystal_saw = "crystal_saw";
    String crystal_shield = "crystal_shield";
    String desert_amulet = "desert_amulet";
    String dodgy_necklace = "dodgy_necklace";
    String dragonfire_shield = "dragonfire_shield";
    String enchanted_lyre = "enchanted_lyre";
    String escape_crystal = "escape_crystal";
    String escape_crystal_status = "escape_crystal_status";
    String escape_crystal_inactivity_period = "escape_crystal_inactivity_period";
    String escape_crystal_time_remaining_warning = "escape_crystal_time_remaining_warning";
    String explorers_ring = "explorers_ring";
    String falador_shield = "falador_shield";
    String fish_barrel = "fish_barrel";
    String forestry_kit = "forestry_kit";
    String fremennik_sea_boots = "fremennik_sea_boots";
    String fungicide_spray = "fungicide_spray";
    String fur_pouch = "fur_pouch";
    String gem_bag = "gem_bag";
    String gricollers_can = "gricollers_can";
    String herb_sack = "herb_sack";
    String ibans_staff = "ibans_staff";
    String jar_generator = "jar_generator";
    String kandarin_headgear = "kandarin_headgear";
    String kharedsts_memoirs = "kharedsts_memoirs";
    String log_basket = "log_basket";
    String magic_cape = "magic_cape";
    String master_scroll_book = "master_scroll_book";
    String meat_pouch = "meat_pouch";
    String huntsmans_kit = "huntsmans_kit";
    String necklace_of_passage = "necklace_of_passage";
    String ogre_bellows = "ogre_bellows";
    String pharaohs_sceptre = "pharaohs_sceptre";
    String phoenix_necklace = "phoenix_necklace";
    String plank_sack = "plank_sack";
    String quetzal_whistle = "quetzal_whistle";
    String ring_of_pursuit = "ring_of_pursuit";
    String ring_of_recoil = "ring_of_recoil";
    String ring_of_shadows = "ring_of_shadows";
    String ring_of_suffering = "ring_of_suffering";
    String ring_of_suffering_status = "ring_of_suffering_status";
    String ring_of_the_elements = "ring_of_the_elements";
    String sanguinesti_staff = "sanguinesti_staff";
    String seed_box = "seed_box";
    String skull_sceptre = "skull_sceptre";
    String slayer_ring = "slayer_ring";
    String slayer_staff_e = "slayer_staff_e";
    String soul_bearer = "soul_bearer";
    String strange_old_lockpick = "strange_old_lockpick";
    String tackle_box = "tackle_box";
    String teleport_crystal = "teleport_crystal";
    String tome_of_fire = "tome_of_fire";
    String tome_of_water = "tome_of_water";
    String toxic_staff_of_the_dead = "toxic_staff_of_the_dead";
    String trident_of_the_seas = "trident_of_the_seas";
    String venator_bow = "venator_bow";
    String warped_sceptre = "warped_sceptre";
    String waterskin = "waterskin";
    String western_banner = "western_banner";
    String xerics_talisman = "xerics_talisman";
    String ruby_harvest_mix = "ruby_harvest_mix";
    String sapphire_glacialis_mix = "sapphire_glacialis_mix";
    String snowy_knight_mix = "snowy_knight_mix";
    String black_warlock_mix = "black_warlock_mix";
    String sunlight_moth_mix = "sunlight_moth_mix";
    String moonlight_moth_mix = "moonlight_moth_mix";

    @ConfigSection(
        name = "General",
        description = "General settings",
        position = 1
    ) String general = "general";

        @ConfigItem(
            keyName = "show_infoboxes",
            name = "Show infoboxes",
            description = "Show or hide all charges infoboxes simultaneously.",
            section = general,
            position = 1
        ) default boolean showInfoboxes() { return true; }

        @ConfigItem(
            keyName = "show_overlays",
            name = "Show overlays",
            description = "Show or hide all charges overlays on top of items simultaneously.",
            section = general,
            position = 2
        ) default boolean showOverlays() { return true; }

        @ConfigItem(
            keyName = "bank_overlays",
            name = "Show overlays in bank",
            description = "Show charges of the items in bank",
            section = general,
            position = 3
        ) default boolean showBankOverlays() { return true; }

        @ConfigItem(
            keyName = "hide_outside_bank_overlays",
            name = "Show overlays only while in bank",
            description = "Shows item charges overlays only when in bank",
            section = general,
            position = 4
        ) default boolean showOverlaysOnlyInBank() { return false; }

        @ConfigItem(
            keyName = "storage_tooltips",
            name = "Show storage tooltips",
            description = "Show tooltips for items with storage",
            section = general,
            position = 5
        ) default boolean showStorageTooltips() { return true; }

        @ConfigItem(
            keyName = "menu_replacements",
            name = "Unify menu entries",
            description = "Replace obscure menu entries like \"Reminisce\" and \"Divine\" with \"Teleport\" and \"Check\" and show detailed herb patches names.",
            section = general,
            position = 6
        ) default boolean useCommonMenuEntries() { return true; }

        @ConfigItem(
            keyName = "hide_destroy",
            name = "Hide destroy menu entries",
            description = "Hide destroy menu entry from items that make no sense to destroy",
            section = general,
            position = 7
        ) default boolean hideDestroy() { return true; }

        @ConfigItem(
            keyName = "show_unlimited_charges",
            name = "Show unlimited charges",
            description = "Show infinity symbol for items with unlimited charges",
            section = general,
            position = 8
        ) default boolean showUnlimited() { return true; }

    @ConfigSection(
        name = "Colors",
        description = "Colors of item overlays",
        position = 2
    ) String colors = "colors";

        @Alpha
        @ConfigItem(
            keyName = "colors_default",
            name = "Default",
            description = "Color of default charges",
            position = 1,
            section = colors
        ) default Color getColorDefault() { return Color.white; }

        @Alpha
        @ConfigItem(
            keyName = "colors_unknown",
            name = "Unknown",
            description = "Color of unknown charges",
            position = 2,
            section = colors
        ) default Color getColorUnknown() { return Color.gray; }

        @Alpha
        @ConfigItem(
            keyName = "colors_empty",
            name = "Empty",
            description = "Color of empty charges",
            position = 3,
            section = colors
        ) default Color getColorEmpty() { return Color.red; }

        @Alpha
        @ConfigItem(
            keyName = "colors_activated",
            name = "Activated",
            description = "Color of activated charges",
            position = 4,
            section = colors
        ) default Color getColorActivated() { return Color.green; }

    @ConfigSection(
        name = "Escape Crystal",
        description = "Escape Crystal",
        position = 3,
        closedByDefault = true
    ) String escape_crystal_section = "escape_crystal_section";

        @ConfigItem(
            keyName = escape_crystal_time_remaining_warning,
            name = "Time remaining warning",
            description = "How many seconds before you are warned before Escape crystal activates",
            position = 4,
            section = escape_crystal_section
        ) default int getEscapeCrystalTimeRemainingWarning() { return 5; }

    @ConfigSection(
        name = "Infoboxes",
        description = "Choose for which charged items infobox is visible",
        position = 4,
        closedByDefault = true
    ) String infoboxes = "infoboxes";

        @ConfigItem(
            keyName = master_scroll_book + infobox,
            name = "Master scroll book",
            description = "",
            section = infoboxes
        ) default boolean masterScrollBookInfobox() { return true; }

        @ConfigItem(
            keyName = ruby_harvest_mix + infobox,
            name = "Ruby harvest mix",
            description = "",
            section = infoboxes
        ) default boolean rubyHarvestMixInfobox() { return false; }

        @ConfigItem(
            keyName = sapphire_glacialis_mix + infobox,
            name = "Sapphire glacialis mix",
            description = "",
            section = infoboxes
        ) default boolean sapphireGlacialisMixInfobox() { return false; }

        @ConfigItem(
            keyName = snowy_knight_mix + infobox,
            name = "Snowy knight mix",
            description = "",
            section = infoboxes
        ) default boolean snowyKnightMixInfobox() { return false; }

        @ConfigItem(
            keyName = black_warlock_mix + infobox,
            name = "Black warlock mix",
            description = "",
            section = infoboxes
        ) default boolean blackWarlockInfobox() { return false; }

        @ConfigItem(
            keyName = sunlight_moth_mix + infobox,
            name = "Sunlight moth mix",
            description = "",
            section = infoboxes
        ) default boolean sunlightMothMixInfobox() { return false; }

        @ConfigItem(
            keyName = moonlight_moth_mix + infobox,
            name = "Moonlight moth mix",
            description = "",
            section = infoboxes
        ) default boolean moonlightMothMixInfobox() { return false; }

        @ConfigItem(
            keyName = ring_of_pursuit + infobox,
            name = "Ring of pursuit",
            description = "",
            section = infoboxes
        ) default boolean ringOfPursuitInfobox() { return true; }

        @ConfigItem(
            keyName = huntsmans_kit + infobox,
            name = "Huntsman's kit",
            description = "",
            section = infoboxes
        ) default boolean huntsmansKitInfobox() { return true; }

        @ConfigItem(
            keyName = bow_of_faerdhinen + infobox,
            name = "Bow of faerdhinen",
            description = "",
            section = infoboxes
        ) default boolean bowOfFaerdhinenInfobox() { return true; }

        @ConfigItem(
            keyName = venator_bow + infobox,
            name = "Venator bow",
            description = "",
            section = infoboxes
        ) default boolean venatorBowInfobox() { return true; }

        @ConfigItem(
            keyName = meat_pouch + infobox,
            name = "Meat pouch",
            description = "",
            section = infoboxes
        ) default boolean meatPouchInfobox() { return true; }

        @ConfigItem(
            keyName = western_banner + infobox,
            name = "Western banner",
            description = "",
            section = infoboxes
        ) default boolean westernBannerInfobox() { return true; }

        @ConfigItem(
            keyName = barrows_gear + infobox,
            name = "Barrows set",
            description = "",
            section = infoboxes
        ) default boolean barrowsInfobox() { return true; }

        @ConfigItem(
            keyName = crystal_body + infobox,
            name = "Crystal body",
            description = "",
            section = infoboxes
        ) default boolean crystalBodyInfobox() { return true; }

        @ConfigItem(
            keyName = crystal_helm + infobox,
            name = "Crystal helm",
            description = "",
            section = infoboxes
        ) default boolean crystalHelmInfobox() { return true; }

        @ConfigItem(
            keyName = crystal_legs + infobox,
            name = "Crystal legs",
            description = "",
            section = infoboxes
        ) default boolean crystalLegsInfobox() { return true; }

        @ConfigItem(
            keyName = fremennik_sea_boots + infobox,
            name = "Fremennik sea boots",
            description = "",
            section = infoboxes
        ) default boolean fremennikSeaBootsInfobox() { return true; }

        @ConfigItem(
            keyName = ardougne_cloak + infobox,
            name = "Ardougne cloak",
            description = "",
            section = infoboxes
        ) default boolean ardougneCloakInfobox() { return true; }

        @ConfigItem(
            keyName = coffin + infobox,
            name = "Coffin",
            description = "",
            section = infoboxes
        ) default boolean coffinInfobox() { return true; }

        @ConfigItem(
            keyName = forestry_kit + infobox,
            name = "Forestry kit",
            description = "",
            section = infoboxes
        ) default boolean forestryKitInfobox() { return true; }

        @ConfigItem(
            keyName = fur_pouch + infobox,
            name = "Fur pouch",
            description = "",
            section = infoboxes
        ) default boolean furPouchInfobox() { return true; }

        @ConfigItem(
            keyName = magic_cape + infobox,
            name = "Magic cape",
            description = "",
            section = infoboxes
        ) default boolean magicCapeInfobox() { return true; }

        @ConfigItem(
            keyName = circlet_of_water + infobox,
            name = "Circlet of water",
            description = "",
            section = infoboxes
        ) default boolean circletOfWaterInfobox() { return true; }

        @ConfigItem(
            keyName = kandarin_headgear + infobox,
            name = "Kandarin Headgear",
            description = "",
            section = infoboxes
        ) default boolean kandarinHeadgearInfobox() { return true; }

        @ConfigItem(
            keyName = bracelet_of_clay + infobox,
            name = "Bracelet of clay",
            description = "",
            section = infoboxes
        ) default boolean braceletOfClayInfobox() { return true; }

        @ConfigItem(
            keyName = bracelet_of_expeditious + infobox,
            name = "Expeditious bracelet",
            description = "",
            section = infoboxes
        ) default boolean expeditiousBraceletInfobox() { return true; }

        @ConfigItem(
            keyName = bracelet_of_flamtaer + infobox,
            name = "Flamtaer bracelet",
            description = "",
            section = infoboxes
        ) default boolean flamtaerBraceletInfobox() { return true; }

        @ConfigItem(
            keyName = bracelet_of_slaughter + infobox,
            name = "Bracelet of slaughter",
            description = "",
            section = infoboxes
        ) default boolean braceletOfSlaughterInfobox() { return true; }

        @ConfigItem(
            keyName = camulet + infobox,
            name = "Camulet",
            description = "",
            section = infoboxes
        ) default boolean camuletInfobox() { return true; }

        @ConfigItem(
            keyName = desert_amulet + infobox,
            name = "Desert amulet",
            description = "",
            section = infoboxes
        ) default boolean desertAmuletInfobox() { return true; }

        @ConfigItem(
            keyName = escape_crystal + infobox,
            name = "Escape crystal",
            description = "",
            section = infoboxes
        ) default boolean escapeCrystalInfobox() { return true; }

        @ConfigItem(
            keyName = dodgy_necklace + infobox,
            name = "Dodgy necklace",
            description = "",
            section = infoboxes
        ) default boolean dodgyNecklaceInfobox() { return true; }

        @ConfigItem(
            keyName = necklace_of_passage + infobox,
            name = "Necklace of passage",
            description = "",
            section = infoboxes
        ) default boolean necklaceOfPassageInfobox() { return true; }

        @ConfigItem(
            keyName = phoenix_necklace + infobox,
            name = "Phoenix necklace",
            description = "",
            section = infoboxes
        ) default boolean phoenixNecklaceInfobox() { return true; }

        @ConfigItem(
            keyName = celestial_ring + infobox,
            name = "Celestial ring",
            description = "",
            section = infoboxes
        ) default boolean celestialRingInfobox() { return true; }

        @ConfigItem(
            keyName = ring_of_the_elements + infobox,
            name = "Ring of the elements",
            description = "",
            section = infoboxes
        ) default boolean ringOfTheElementsInfobox() { return true; }

        @ConfigItem(
            keyName = explorers_ring + infobox,
            name = "Explorer's ring",
            description = "",
            section = infoboxes
        ) default boolean explorersRingInfobox() { return true; }

        @ConfigItem(
            keyName = ring_of_recoil + infobox,
            name = "Ring of recoil",
            description = "",
            section = infoboxes
        ) default boolean ringOfRecoilInfobox() { return true; }

        @ConfigItem(
            keyName = ring_of_shadows + infobox,
            name = "Ring of shadows",
            description = "",
            section = infoboxes
        ) default boolean ringOfShadowsInfobox() { return true; }

        @ConfigItem(
            keyName = slayer_ring + infobox,
            name = "Slayer ring",
            description = "",
            section = infoboxes
        ) default boolean slayerRingInfobox() { return true; }

        @ConfigItem(
            keyName = ring_of_suffering + infobox,
            name = "Ring of suffering",
            description = "",
            section = infoboxes
        ) default boolean ringOfSufferingInfobox() { return true; }

        @ConfigItem(
            keyName = xerics_talisman + infobox,
            name = "Xeric's talisman",
            description = "",
            section = infoboxes
        ) default boolean xericsTalismanInfobox() { return true; }

        @ConfigItem(
            keyName = chronicle + infobox,
            name = "Chronicle",
            description = "",
            section = infoboxes
        ) default boolean chronicleInfobox() { return true; }

        @ConfigItem(
            keyName = crystal_shield + infobox,
            name = "Crystal shield",
            description = "",
            section = infoboxes
        ) default boolean crystalShieldInfobox() { return true; }

        @ConfigItem(
            keyName = dragonfire_shield + infobox,
            name = "Dragonfire shield",
            description = "",
            section = infoboxes
        ) default boolean dragonfireShieldInfobox() { return true; }

        @ConfigItem(
            keyName = falador_shield + infobox,
            name = "Falador shield",
            description = "",
            section = infoboxes
        ) default boolean faladorShieldInfobox() { return true; }

        @ConfigItem(
            keyName = kharedsts_memoirs + infobox,
            name = "Kharedst's memoirs",
            description = "",
            section = infoboxes
        ) default boolean kharedstsMemoirsInfobox() { return true; }

        @ConfigItem(
            keyName = tome_of_fire + infobox,
            name = "Tome of fire",
            description = "",
            section = infoboxes
        ) default boolean tomeOfFireInfobox() { return true; }

        @ConfigItem(
            keyName = tome_of_water + infobox,
            name = "Tome of water",
            description = "",
            section = infoboxes
        ) default boolean tomeOfWaterInfobox() { return true; }

        @ConfigItem(
            keyName = ash_sanctifier + infobox,
            name = "Ash sanctifier",
            description = "",
            section = infoboxes
        ) default boolean ashSanctifierInfobox() { return true; }

        @ConfigItem(
            keyName = bonecrusher + infobox,
            name = "Bonecrusher",
            description = "",
            section = infoboxes
        ) default boolean bonecrusherInfobox() { return true; }

        @ConfigItem(
            keyName = bottomless_compost_bucket + infobox,
            name = "Bottomless compost bucket",
            description = "",
            section = infoboxes
        ) default boolean bottomlessCompostBucketInfobox() { return true; }

        @ConfigItem(
            keyName = coal_bag + infobox,
            name = "Coal bag",
            description = "",
            section = infoboxes
        ) default boolean coalBagInfobox() { return true; }

        @ConfigItem(
            keyName = crystal_saw + infobox,
            name = "Crystal saw",
            description = "",
            section = infoboxes
        ) default boolean crystalSawInfobox() { return true; }

        @ConfigItem(
            keyName = fish_barrel + infobox,
            name = "Fish barrel",
            description = "",
            section = infoboxes
        ) default boolean fishBarrelInfobox() { return true; }

        @ConfigItem(
            keyName = fungicide_spray + infobox,
            name = "Fungicide spray",
            description = "",
            section = infoboxes
        ) default boolean fungicideSprayInfobox() { return true; }

        @ConfigItem(
            keyName = gem_bag + infobox,
            name = "Gem bag",
            description = "",
            section = infoboxes
        ) default boolean gemBagInfobox() { return true; }

        @ConfigItem(
            keyName = gricollers_can + infobox,
            name = "Gricoller's can",
            description = "",
            section = infoboxes
        ) default boolean gricollersCanInfobox() { return true; }

        @ConfigItem(
            keyName = herb_sack + infobox,
            name = "Herb sack",
            description = "",
            section = infoboxes
        ) default boolean herbSackInfobox() { return true; }

        @ConfigItem(
            keyName = jar_generator + infobox,
            name = "Jar generator",
            description = "",
            section = infoboxes
        ) default boolean jarGeneratorInfobox() { return true; }

        @ConfigItem(
            keyName = log_basket + infobox,
            name = "Log basket",
            description = "",
            section = infoboxes
        ) default boolean logBasketInfobox() { return true; }

        @ConfigItem(
            keyName = ogre_bellows + infobox,
            name = "Ogre bellows",
            description = "",
            section = infoboxes
        ) default boolean ogreBellowsInfobox() { return true; }

        @ConfigItem(
            keyName = plank_sack + infobox,
            name = "Plank sack",
            description = "",
            section = infoboxes
        ) default boolean plankSackInfobox() { return true; }

        @ConfigItem(
            keyName = quetzal_whistle + infobox,
            name = "Quetzal whistle",
            description = "",
            section = infoboxes
        ) default boolean quetzalWhistleInfobox() { return true; }

        @ConfigItem(
            keyName = seed_box + infobox,
            name = "Seed box",
            description = "",
            section = infoboxes
        ) default boolean seedBoxInfobox() { return true; }

        @ConfigItem(
            keyName = soul_bearer + infobox,
            name = "Soul bearer",
            description = "",
            section = infoboxes
        ) default boolean soulBearerInfobox() { return true; }

        @ConfigItem(
            keyName = strange_old_lockpick + infobox,
            name = "Strange old lockpick",
            description = "",
            section = infoboxes
        ) default boolean strangeOldLockpickInfobox() { return true; }

        @ConfigItem(
            keyName = tackle_box + infobox,
            name = "Tackle box",
            description = "",
            section = infoboxes
        ) default boolean tackleBoxInfobox() { return true; }

        @ConfigItem(
            keyName = teleport_crystal + infobox,
            name = "Teleport crystal",
            description = "",
            section = infoboxes
        ) default boolean teleportCrystalInfobox() { return true; }

        @ConfigItem(
            keyName = waterskin + infobox,
            name = "Waterskin",
            description = "",
            section = infoboxes
        ) default boolean waterskinInfobox() { return true; }

        @ConfigItem(
            keyName = arclight + infobox,
            name = "Arclight",
            description = "",
            section = infoboxes
        ) default boolean arclightInfobox() { return true; }

        @ConfigItem(
            keyName = bryophytas_staff + infobox,
            name = "Bryophyta's staff",
            description = "",
            section = infoboxes
        ) default boolean bryophytasStaffInfobox() { return true; }

        @ConfigItem(
            keyName = crystal_bow + infobox,
            name = "Crystal bow",
            description = "",
            section = infoboxes
        ) default boolean crystalBowInfobox() { return true; }

        @ConfigItem(
            keyName = crystal_halberd + infobox,
            name = "Crystal halberd",
            description = "",
            section = infoboxes
        ) default boolean crystalHalberdInfobox() { return true; }

        @ConfigItem(
            keyName = enchanted_lyre + infobox,
            name = "Enchanted Lyre",
            description = "",
            section = infoboxes
        ) default boolean enchantedLyreInfobox() { return true; }

        @ConfigItem(
            keyName = ibans_staff + infobox,
            name = "Iban's staff",
            description = "",
            section = infoboxes
        ) default boolean ibansStaffInfobox() { return true; }

        @ConfigItem(
            keyName = pharaohs_sceptre + infobox,
            name = "Pharaoh's sceptre",
            description = "",
            section = infoboxes
        ) default boolean pharaohsSceptreInfobox() { return true; }

        @ConfigItem(
            keyName = sanguinesti_staff + infobox,
            name = "Sanguinesti staff",
            description = "",
            section = infoboxes
        ) default boolean sanguinestiStaffInfobox() { return true; }

        @ConfigItem(
            keyName = skull_sceptre + infobox,
            name = "Skull sceptre",
            description = "",
            section = infoboxes
        ) default boolean skullSceptreInfobox() { return true; }

        @ConfigItem(
            keyName = slayer_staff_e + infobox,
            name = "Slayer staff (e)",
            description = "",
            section = infoboxes
        ) default boolean slayerStaffEInfobox() { return true; }

        @ConfigItem(
            keyName = toxic_staff_of_the_dead + infobox,
            name = "Toxic staff of the dead",
            description = "",
            section = infoboxes
        ) default boolean toxicStaffOfTheDeadInfobox() { return true; }

        @ConfigItem(
            keyName = trident_of_the_seas + infobox,
            name = "Trident of the seas",
            description = "",
            section = infoboxes
        ) default boolean tridentOfTheSeasInfobox() { return true; }

        @ConfigItem(
            keyName = warped_sceptre + infobox,
            name = "Warped sceptre",
            description = "",
            section = infoboxes
        ) default boolean warpedSceptreInfobox() { return true; }

    @ConfigSection(
        name = "Overlays",
        description = "Choose for which charged items number is shown next to it",
        position = 4,
        closedByDefault = true
    ) String overlays = "overlays";

        @ConfigItem(
            keyName = master_scroll_book + overlay,
            name = "Master scroll book",
            description = "",
            section = overlays
        ) default boolean masterScrollBookOverlay() { return true; }

        @ConfigItem(
            keyName = ruby_harvest_mix + overlay,
            name = "Ruby harvest mix",
            description = "",
            section = overlays
        ) default boolean rubyHarvestMixOverlay() { return true; }

        @ConfigItem(
            keyName = sapphire_glacialis_mix + overlay,
            name = "Sapphire glacialis mix",
            description = "",
            section = overlays
        ) default boolean sapphireGlacialisMixOverlay() { return true; }

        @ConfigItem(
            keyName = snowy_knight_mix + overlay,
            name = "Snowy knight mix",
            description = "",
            section = overlays
        ) default boolean snowyKnightMixOverlay() { return true; }

        @ConfigItem(
            keyName = black_warlock_mix + overlay,
            name = "Black warlock mix",
            description = "",
            section = overlays
        ) default boolean blackWarlockOverlay() { return true; }

        @ConfigItem(
            keyName = sunlight_moth_mix + overlay,
            name = "Sunlight moth mix",
            description = "",
            section = overlays
        ) default boolean sunlightMothMixOverlay() { return true; }

        @ConfigItem(
            keyName = moonlight_moth_mix + overlay,
            name = "Moonlight moth mix",
            description = "",
            section = overlays
        ) default boolean moonlightMothMixOverlay() { return true; }

        @ConfigItem(
            keyName = ring_of_pursuit + overlay,
            name = "Ring of pursuit",
            description = "",
            section = overlays
        ) default boolean ringOfPursuitOverlay() { return true; }

        @ConfigItem(
            keyName = huntsmans_kit + overlay,
            name = "Huntsman's kit",
            description = "",
            section = overlays
        ) default boolean huntsmansKitOverlay() { return true; }

        @ConfigItem(
            keyName = arclight + overlay,
            name = "Arclight",
            description = "",
            section = overlays
        ) default boolean arclightOverlay() { return true; }

        @ConfigItem(
            keyName = ardougne_cloak + overlay,
            name = "Ardougne cloak",
            description = "",
            section = overlays
        ) default boolean ardougneCloakOverlay() { return true; }

        @ConfigItem(
            keyName = ash_sanctifier + overlay,
            name = "Ash sanctifier",
            description = "",
            section = overlays
        ) default boolean ashSanctifierOverlay() { return true; }

        @ConfigItem(
            keyName = barrows_gear + overlay,
            name = "Barrows set",
            description = "",
            section = overlays
        ) default boolean barrowsOverlay() { return true; }

        @ConfigItem(
            keyName = bonecrusher + overlay,
            name = "Bonecrusher",
            description = "",
            section = overlays
        ) default boolean bonecrusherOverlay() { return true; }

        @ConfigItem(
            keyName = bottomless_compost_bucket + overlay,
            name = "Bottomless compost bucket",
            description = "",
            section = overlays
        ) default boolean bottomlessCompostBucketOverlay() { return true; }

        @ConfigItem(
            keyName = bow_of_faerdhinen + overlay,
            name = "Bow of faerdhinen",
            description = "",
            section = overlays
        ) default boolean bowOfFaerdhinenOverlay() { return true; }

        @ConfigItem(
            keyName = bracelet_of_clay + overlay,
            name = "Bracelet of clay",
            description = "",
            section = overlays
        ) default boolean braceletOfClayOverlay() { return true; }

        @ConfigItem(
            keyName = bracelet_of_expeditious + overlay,
            name = "Expeditious bracelet",
            description = "",
            section = overlays
        ) default boolean expeditiousBraceletOverlay() { return true; }

        @ConfigItem(
            keyName = bracelet_of_flamtaer + overlay,
            name = "Flamtaer bracelet",
            description = "",
            section = overlays
        ) default boolean flamtaerBraceletOverlay() { return true; }

        @ConfigItem(
            keyName = bracelet_of_slaughter + overlay,
            name = "Bracelet of slaughter",
            description = "",
            section = overlays
        ) default boolean braceletOfSlaughterOverlay() { return true; }

        @ConfigItem(
            keyName = camulet + overlay,
            name = "Camulet",
            description = "",
            section = overlays
        ) default boolean camuletOverlay() { return true; }

        @ConfigItem(
            keyName = celestial_ring + overlay,
            name = "Celestial ring",
            description = "",
            section = overlays
        ) default boolean celestialRingOverlay() { return true; }

        @ConfigItem(
            keyName = chronicle + overlay,
            name = "Chronicle",
            description = "",
            section = overlays
        ) default boolean chronicleOverlay() { return true; }

        @ConfigItem(
            keyName = circlet_of_water + overlay,
            name = "Circlet of water",
            description = "",
            section = overlays
        ) default boolean circletOfWaterOverlay() { return true; }

        @ConfigItem(
            keyName = coal_bag + overlay,
            name = "Coal bag",
            description = "",
            section = overlays
        ) default boolean coalBagOverlay() { return true; }

        @ConfigItem(
            keyName = coffin + overlay,
            name = "Coffin",
            description = "",
            section = overlays
        ) default boolean coffinOverlay() { return true; }

        @ConfigItem(
            keyName = crystal_body + overlay,
            name = "Crystal body",
            description = "",
            section = overlays
        ) default boolean crystalBodyOverlay() { return true; }

        @ConfigItem(
            keyName = crystal_helm + overlay,
            name = "Crystal helm",
            description = "",
            section = overlays
        ) default boolean crystalHelmOverlay() { return true; }

        @ConfigItem(
            keyName = crystal_legs + overlay,
            name = "Crystal legs",
            description = "",
            section = overlays
        ) default boolean crystalLegsOverlay() { return true; }

        @ConfigItem(
            keyName = crystal_saw + overlay,
            name = "Crystal saw",
            description = "",
            section = overlays
        ) default boolean crystalSawOverlay() { return true; }

        @ConfigItem(
            keyName = crystal_shield + overlay,
            name = "Crystal shield",
            description = "",
            section = overlays
        ) default boolean crystalShieldOverlay() { return true; }

        @ConfigItem(
            keyName = desert_amulet + overlay,
            name = "Desert amulet",
            description = "",
            section = overlays
        ) default boolean desertAmuletOverlay() { return true; }

        @ConfigItem(
            keyName = dragonfire_shield + overlay,
            name = "Dragonfire shield",
            description = "",
            section = overlays
        ) default boolean dragonfireShieldOverlay() { return true; }

        @ConfigItem(
            keyName = falador_shield + overlay,
            name = "Falador shield",
            description = "",
            section = overlays
        ) default boolean faladorShieldOverlay() { return true; }

        @ConfigItem(
            keyName = fur_pouch + overlay,
            name = "Fur pouch",
            description = "",
            section = overlays
        ) default boolean furPouchOverlay() { return true; }

        @ConfigItem(
            keyName = escape_crystal + overlay,
            name = "Escape crystal",
            description = "",
            section = overlays
        ) default boolean escapeCrystalOverlay() { return true; }

        @ConfigItem(
            keyName = explorers_ring + overlay,
            name = "Explorer's ring",
            description = "",
            section = overlays
        ) default boolean explorersRingOverlay() { return true; }

        @ConfigItem(
            keyName = dodgy_necklace + overlay,
            name = "Dodgy necklace",
            description = "",
            section = overlays
        ) default boolean dodgyNecklaceOverlay() { return true; }

        @ConfigItem(
            keyName = fish_barrel + overlay,
            name = "Fish barrel",
            description = "",
            section = overlays
        ) default boolean fishBarrelOverlay() { return true; }

        @ConfigItem(
            keyName = forestry_kit + overlay,
            name = "Forestry kit",
            description = "",
            section = overlays
        ) default boolean forestryKitOverlay() { return true; }

        @ConfigItem(
            keyName = fremennik_sea_boots + overlay,
            name = "Fremennik sea boots",
            description = "",
            section = overlays
        ) default boolean fremennikSeaBootsOverlay() { return true; }

        @ConfigItem(
            keyName = fungicide_spray + overlay,
            name = "Fungicide spray",
            description = "",
            section = overlays
        ) default boolean fungicideSprayOverlay() { return true; }

        @ConfigItem(
            keyName = gem_bag + overlay,
            name = "Gem bag",
            description = "",
            section = overlays
        ) default boolean gemBagOverlay() { return true; }

        @ConfigItem(
            keyName = gricollers_can + overlay,
            name = "Gricoller's can",
            description = "",
            section = overlays
        ) default boolean gricollersCanOverlay() { return true; }

        @ConfigItem(
            keyName = herb_sack + overlay,
            name = "Herb sack",
            description = "",
            section = overlays
        ) default boolean herbSackOverlay() { return true; }

        @ConfigItem(
            keyName = jar_generator + overlay,
            name = "Jar generator",
            description = "",
            section = overlays
        ) default boolean jarGeneratorOverlay() { return true; }

        @ConfigItem(
            keyName = kandarin_headgear + overlay,
            name = "Kandarin Headgear",
            description = "",
            section = overlays
        ) default boolean kandarinHeadgearOverlay() { return true; }

        @ConfigItem(
            keyName = kharedsts_memoirs + overlay,
            name = "Kharedst's memoirs",
            description = "",
            section = overlays
        ) default boolean kharedstsMemoirsOverlay() { return true; }

        @ConfigItem(
            keyName = log_basket + overlay,
            name = "Log basket",
            description = "",
            section = overlays
        ) default boolean logBasketOverlay() { return true; }

        @ConfigItem(
            keyName = magic_cape + overlay,
            name = "Magic cape",
            description = "",
            section = overlays
        ) default boolean magicCapeOverlay() { return true; }

        @ConfigItem(
            keyName = meat_pouch + overlay,
            name = "Meat pouch",
            description = "",
            section = overlays
        ) default boolean meatPouchOverlay() { return true; }

        @ConfigItem(
            keyName = necklace_of_passage + overlay,
            name = "Necklace of passage",
            description = "",
            section = overlays
        ) default boolean necklaceOfPassageOverlay() { return true; }

        @ConfigItem(
            keyName = ogre_bellows + overlay,
            name = "Ogre bellows",
            description = "",
            section = overlays
        ) default boolean ogreBellowsOverlay() { return true; }

        @ConfigItem(
            keyName = phoenix_necklace + overlay,
            name = "Phoenix necklace",
            description = "",
            section = overlays
        ) default boolean phoenixNecklaceOverlay() { return true; }

        @ConfigItem(
            keyName = plank_sack + overlay,
            name = "Plank sack",
            description = "",
            section = overlays
        ) default boolean plankSackOverlay() { return true; }

        @ConfigItem(
            keyName = ring_of_recoil + overlay,
            name = "Ring of recoil",
            description = "",
            section = overlays
        ) default boolean ringOfRecoilOverlay() { return true; }

        @ConfigItem(
            keyName = ring_of_shadows + overlay,
            name = "Ring of shadows",
            description = "",
            section = overlays
        ) default boolean ringOfShadowsOverlay() { return true; }

        @ConfigItem(
            keyName = ring_of_suffering + overlay,
            name = "Ring of suffering",
            description = "",
            section = overlays
        ) default boolean ringOfSufferingOverlay() { return true; }

        @ConfigItem(
            keyName = ring_of_the_elements + overlay,
            name = "Ring of the elements",
            description = "",
            section = overlays
        ) default boolean ringOfTheElementsOverlay() { return true; }

        @ConfigItem(
            keyName = seed_box + overlay,
            name = "Seed box",
            description = "",
            section = overlays
        ) default boolean seedBoxOverlay() { return true; }

        @ConfigItem(
            keyName = slayer_ring + overlay,
            name = "Slayer ring",
            description = "",
            section = overlays
        ) default boolean slayerRingOverlay() { return true; }

        @ConfigItem(
            keyName = soul_bearer + overlay,
            name = "Soul bearer",
            description = "",
            section = overlays
        ) default boolean soulBearerOverlay() { return true; }

        @ConfigItem(
            keyName = strange_old_lockpick + overlay,
            name = "Strange old lockpick",
            description = "",
            section = overlays
        ) default boolean strangeOldLockpickOverlay() { return true; }

        @ConfigItem(
            keyName = tackle_box + overlay,
            name = "Tackle box",
            description = "",
            section = overlays
        ) default boolean tackleBoxOverlay() { return true; }

        @ConfigItem(
            keyName = teleport_crystal + overlay,
            name = "Teleport crystal",
            description = "",
            section = overlays
        ) default boolean teleportCrystalOverlay() { return true; }

        @ConfigItem(
            keyName = tome_of_fire + overlay,
            name = "Tome of fire",
            description = "",
            section = overlays
        ) default boolean tomeOfFireOverlay() { return true; }

        @ConfigItem(
            keyName = tome_of_water + overlay,
            name = "Tome of water",
            description = "",
            section = overlays
        ) default boolean tomeOfWaterOverlay() { return true; }

        @ConfigItem(
            keyName = venator_bow + overlay,
            name = "Venator bow",
            description = "",
            section = overlays
        ) default boolean venatorBowOverlay() { return true; }

        @ConfigItem(
            keyName = waterskin + overlay,
            name = "Waterskin",
            description = "",
            section = overlays
        ) default boolean waterskinOverlay() { return true; }

        @ConfigItem(
            keyName = western_banner + overlay,
            name = "Western banner",
            description = "",
            section = overlays
        ) default boolean westernBannerOverlay() { return true; }

        @ConfigItem(
            keyName = bryophytas_staff + overlay,
            name = "Bryophyta's staff",
            description = "",
            section = overlays
        ) default boolean bryophytasStaffOverlay() { return true; }

        @ConfigItem(
            keyName = crystal_bow + overlay,
            name = "Crystal bow",
            description = "",
            section = overlays
        ) default boolean crystalBowOverlay() { return true; }

        @ConfigItem(
            keyName = crystal_halberd + overlay,
            name = "Crystal halberd",
            description = "",
            section = overlays
        ) default boolean crystalHalberdOverlay() { return true; }

        @ConfigItem(
            keyName = enchanted_lyre + overlay,
            name = "Enchanted Lyre",
            description = "",
            section = overlays
        ) default boolean enchantedLyreOverlay() { return true; }

        @ConfigItem(
            keyName = ibans_staff + overlay,
            name = "Iban's staff",
            description = "",
            section = overlays
        ) default boolean ibansStaffOverlay() { return true; }

        @ConfigItem(
            keyName = pharaohs_sceptre + overlay,
            name = "Pharaoh's sceptre",
            description = "",
            section = overlays
        ) default boolean pharaohsSceptreOverlay() { return true; }

        @ConfigItem(
            keyName = quetzal_whistle + overlay,
            name = "Quetzal whistle",
            description = "",
            section = overlays
        ) default boolean quetzalWhistleOverlay() { return true; }

        @ConfigItem(
            keyName = sanguinesti_staff + overlay,
            name = "Sanguinesti staff",
            description = "",
            section = overlays
        ) default boolean sanguinestiStaffOverlay() { return true; }

        @ConfigItem(
            keyName = skull_sceptre + overlay,
            name = "Skull sceptre",
            description = "",
            section = overlays
        ) default boolean skullSceptreOverlay() { return true; }

        @ConfigItem(
            keyName = slayer_staff_e + overlay,
            name = "Slayer staff (e)",
            description = "",
            section = overlays
        ) default boolean slayerStaffEOverlay() { return true; }

        @ConfigItem(
            keyName = toxic_staff_of_the_dead + overlay,
            name = "Toxic staff of the dead",
            description = "",
            section = overlays
        ) default boolean toxicStaffOfTheDeadOverlay() { return true; }

        @ConfigItem(
            keyName = trident_of_the_seas + overlay,
            name = "Trident of the seas",
            description = "",
            section = overlays
        ) default boolean tridentOfTheSeasOverlay() { return true; }

        @ConfigItem(
            keyName = warped_sceptre + overlay,
            name = "Warped sceptre",
            description = "",
            section = overlays
        ) default boolean warpedSceptreOverlay() { return true; }

        @ConfigItem(
            keyName = xerics_talisman + overlay,
            name = "Xeric's talisman",
            description = "",
            section = overlays
        ) default boolean xericsTalismanOverlay() { return true; }

    @ConfigSection(
        name = "Debug",
        description = "Values of charges for all items under the hood",
        position = 99,
        closedByDefault = true
    ) String debug = "debug";

        @ConfigItem(
            keyName = version,
            name = version,
            description = "Version of the plugin for update message",
            section = debug,
            position = 1
        ) default String getVersion() { return ""; }

        @ConfigItem(
            keyName = date,
            name = "Date",
            description = "Date to check for charges reset when logging in",
            section = debug,
            position = 2
        ) default String getResetDate() { return ""; }

        @ConfigItem(
            keyName = storage,
            name = storage,
            description = "All player items to check for daily resets",
            section = debug,
            position = 3
        ) default String getStorage() { return ""; }

        @ConfigItem(
            keyName = debug_ids,
            name = "Debug IDs",
            description = "Shows animation and graphics ids within ingame messages to add support for new items",
            section = debug,
            position = 4
        ) default boolean showDebugIds() { return false; }

        @ConfigItem(
                keyName = ring_of_pursuit,
                name = ring_of_pursuit,
                description = ring_of_pursuit,
                section = debug
        ) default int ringOfPursuitCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = arclight,
            name = arclight,
            description = arclight,
            section = debug
        ) default int getArclightCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = ash_sanctifier,
            name = ash_sanctifier,
            description = ash_sanctifier,
            section = debug
        ) default int getAshSanctifierCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = ash_sanctifier_status,
            name = ash_sanctifier_status,
            description = ash_sanctifier_status,
            section = debug
        ) default ItemActivity getAshSanctifierStatus() { return ItemActivity.ACTIVATED; }

        @ConfigItem(
            keyName = bonecrusher,
            name = bonecrusher,
            description = bonecrusher,
            section = debug
        ) default int getBoneCrusherCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = bonecrusher_status,
            name = bonecrusher_status,
            description = bonecrusher_status,
            section = debug
        ) default ItemActivity getBoneCrusherStatus() { return ItemActivity.ACTIVATED; }

        @ConfigItem(
            keyName = kharedsts_memoirs,
            name = kharedsts_memoirs,
            description = kharedsts_memoirs,
            section = debug
        ) default int getKharedstsMemoirsCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = bottomless_compost_bucket,
            name = bottomless_compost_bucket,
            description = bottomless_compost_bucket,
            section = debug
        ) default int getBottomlessCompostBucketCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = bottomless_compost_bucket_type,
            name = bottomless_compost_bucket_type,
            description = bottomless_compost_bucket_type,
            section = debug
        ) default String getBottomlessCompostBucketType() { return ""; }

        @ConfigItem(
            keyName = bracelet_of_slaughter,
            name = bracelet_of_slaughter,
            description = bracelet_of_slaughter,
            section = debug
        ) default int getBraceletOfSlaughterCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = bryophytas_staff,
            name = bryophytas_staff,
            description = bryophytas_staff,
            section = debug
        ) default int getBryophytasStaffCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = celestial_ring,
            name = celestial_ring,
            description = celestial_ring,
            section = debug
        ) default int getCelestialRingCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = chronicle,
            name = chronicle,
            description = chronicle,
            section = debug
        ) default int getChronicleCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = crystal_shield,
            name = crystal_shield,
            description = crystal_shield,
            section = debug
        ) default int getCrystalShieldCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = crystal_bow,
            name = crystal_bow,
            description = crystal_bow,
            section = debug
        ) default int getCrystalBowCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = bracelet_of_expeditious,
            name = bracelet_of_expeditious,
            description = bracelet_of_expeditious,
            section = debug
        ) default int getBraceletOfExpeditiousCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = falador_shield,
            name = falador_shield,
            description = falador_shield,
            section = debug
        ) default int getFaladorShieldCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = fish_barrel,
            name = fish_barrel,
            description = fish_barrel,
            section = debug
        ) default int getFishBarrelCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = gricollers_can,
            name = gricollers_can,
            description = gricollers_can,
            section = debug
        ) default int getGricollersCanCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = ibans_staff,
            name = ibans_staff,
            description = ibans_staff,
            section = debug
        ) default int getIbansStaffCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = pharaohs_sceptre,
            name = pharaohs_sceptre,
            description = pharaohs_sceptre,
            section = debug
        ) default int getPharaohsSceptreCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = ring_of_suffering,
            name = ring_of_suffering,
            description = ring_of_suffering,
            section = debug
        ) default int getRingOfSufferingCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = ring_of_suffering_status,
            name = ring_of_suffering_status,
            description = ring_of_suffering_status,
            section = debug
        ) default ItemActivity getRingOfSufferingStatus() { return ItemActivity.ACTIVATED; }

        @ConfigItem(
            keyName = sanguinesti_staff,
            name = sanguinesti_staff,
            description = sanguinesti_staff,
            section = debug
        ) default int getSanguinestiStaffCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = skull_sceptre,
            name = skull_sceptre,
            description = skull_sceptre,
            section = debug
        ) default int getSkullSceptreCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = soul_bearer,
            name = soul_bearer,
            description = soul_bearer,
            section = debug
        ) default int getSoulBearerCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = trident_of_the_seas,
            name = trident_of_the_seas,
            description = trident_of_the_seas,
            section = debug
        ) default int getTridentOfTheSeasCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = xerics_talisman,
            name = xerics_talisman,
            description = xerics_talisman,
            section = debug
        ) default int getXericsTalismanCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = dragonfire_shield,
            name = dragonfire_shield,
            description = dragonfire_shield,
            section = debug
        ) default int getDragonfireShieldCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = camulet,
            name = camulet,
            description = camulet,
            section = debug
        ) default int getCamuletCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = circlet_of_water,
            name = circlet_of_water,
            description = circlet_of_water,
            section = debug
        ) default int getCircletOfWaterCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = teleport_crystal,
            name = teleport_crystal,
            description = teleport_crystal,
            section = debug
        ) default int getTeleportCrystalCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = bracelet_of_clay,
            name = bracelet_of_clay,
            description = bracelet_of_clay,
            section = debug
        ) default int getBraceletOfClayCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = coffin,
            name = coffin,
            description = coffin,
            section = debug
        ) default int getCoffinCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = log_basket,
            name = log_basket,
            description = log_basket,
            section = debug
        ) default int getLogBasketCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = huntsmans_kit + "_storage",
            name = huntsmans_kit + "_storage",
            description = huntsmans_kit + "_storage",
            section = debug
        ) default String getHuntsmansKitStorage() { return ""; }

        @ConfigItem(
            keyName = log_basket + "_storage",
            name = log_basket + "_storage",
            description = log_basket + "_storage",
            section = debug
        ) default String getLogBasketStorage() { return ""; }

        @ConfigItem(
            keyName = ardougne_cloak,
            name = ardougne_cloak,
            description = ardougne_cloak,
            section = debug
        ) default int getArdougneCloakCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = magic_cape,
            name = magic_cape,
            description = magic_cape,
            section = debug
        ) default int getMagicCapeCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = meat_pouch + "_storage",
            name = meat_pouch + "_storage",
            description = meat_pouch + "_storage",
            section = debug
        ) default String getMeatPouchStorageCharges() { return ""; }

        @ConfigItem(
            keyName = gem_bag + "_storage",
            name = gem_bag + "_storage",
            description = gem_bag + "_storage",
            section = debug
        ) default String getGemBagStorageCharges() { return ""; }

        @ConfigItem(
            keyName = seed_box,
            name = seed_box,
            description = seed_box,
            section = debug
        ) default int getSeedBoxCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = seed_box + "_storage",
            name = seed_box + "_storage",
            description = seed_box + "_storage",
            section = debug
        ) default String getSeedBoxStorage() { return ""; }

        @ConfigItem(
            keyName = crystal_helm,
            name = crystal_helm,
            description = crystal_helm,
            section = debug
        ) default int getCrystalHelmCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = crystal_body,
            name = crystal_body,
            description = crystal_body,
            section = debug
        ) default int getCrystalBodyCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = crystal_legs,
            name = crystal_legs,
            description = crystal_legs,
            section = debug
        ) default int getCrystalLegsCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = crystal_halberd,
            name = crystal_halberd,
            description = crystal_halberd,
            section = debug
        ) default int getCrystalHalberdCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = ring_of_shadows,
            name = ring_of_shadows,
            description = ring_of_shadows,
            section = debug
        ) default int getRingOfShadowsCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = coal_bag,
            name = coal_bag,
            description = coal_bag,
            section = debug
        ) default int getCoalBagCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = herb_sack,
            name = herb_sack,
            description = herb_sack,
            section = debug
        ) default int getHerbSackCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = escape_crystal_status,
            name = escape_crystal_status,
            description = escape_crystal_status,
            section = debug
        ) default ItemActivity getEscapeCrystalStatus() { return ItemActivity.DEACTIVATED; }

        @ConfigItem(
            keyName = escape_crystal_inactivity_period,
            name = escape_crystal_inactivity_period,
            description = escape_crystal_inactivity_period,
            section = debug
        ) default int getEscapeCrystalInactivityPeriod() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = strange_old_lockpick,
            name = strange_old_lockpick,
            description = strange_old_lockpick,
            section = debug
        ) default int getStrangeOldLockCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = desert_amulet,
            name = desert_amulet,
            description = desert_amulet,
            section = debug
        ) default int getDesertAmuletCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = tome_of_fire,
            name = tome_of_fire,
            description = tome_of_fire,
            section = debug
        ) default int getTomeOfFireCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = dodgy_necklace,
            name = dodgy_necklace,
            description = dodgy_necklace,
            section = debug
        ) default int getDodgyNecklaceCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = kandarin_headgear,
            name = kandarin_headgear,
            description = kandarin_headgear,
            section = debug
        ) default int getKandarinHeadgearCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = fremennik_sea_boots,
            name = fremennik_sea_boots,
            description = fremennik_sea_boots,
            section = debug
        ) default int getFremennikSeaBootsCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = fur_pouch + "_storage",
            name = fur_pouch + "_storage",
            description = fur_pouch + "_storage",
            section = debug
        ) default String getFurPouchStorageCharges() { return ""; }

        @ConfigItem(
            keyName = jar_generator,
            name = jar_generator,
            description = jar_generator,
            section = debug
        ) default int getJarGeneratorCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = explorers_ring + "_storage",
            name = explorers_ring + "_storage",
            description = explorers_ring + "_storage",
            section = debug
        ) default String getExplorersRingCharges() { return ""; }

        @ConfigItem(
            keyName = enchanted_lyre,
            name = enchanted_lyre,
            description = enchanted_lyre,
            section = debug
        ) default int getEnchantedLyreCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = ring_of_the_elements,
            name = ring_of_the_elements,
            description = ring_of_the_elements,
            section = debug
        ) default int getRingOfElementsCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = plank_sack,
            name = plank_sack,
            description = plank_sack,
            section = debug
        ) default int getPlankSackCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = slayer_staff_e,
            name = slayer_staff_e,
            description = slayer_staff_e,
            section = debug
        ) default int getSlayerStaffECharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = warped_sceptre,
            name = warped_sceptre,
            description = warped_sceptre,
            section = debug
        ) default int getWarpedSceptreCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = crystal_saw,
            name = crystal_saw,
            description = crystal_saw,
            section = debug
        ) default int getCrystalSawCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = quetzal_whistle,
            name = quetzal_whistle,
            description = quetzal_whistle,
            section = debug
        ) default int getQuetzalWhistleCharges() { return Charges.UNKNOWN; }
}
