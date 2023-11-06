package tictac7x.daily;

import net.runelite.client.config.*;

@ConfigGroup(TicTac7xDailyTasksConfig.group)
public interface TicTac7xDailyTasksConfig extends Config {
    String group = "tictac7x-daily";
    String version = "version";
    String battlestaves = "battlestaves";
    String herb_boxes = "herb_boxes";
    String random_runes = "lundail_runes";
    String bow_strings = "bow_strings";
    String impling_jars = "impling_jars";
    String buckets_of_sand = "buckets_of_sand";
    String buckets_of_slime = "buckets_of_slime";
    String ogre_arrows = "ogre_arrows";
    String pure_essence = "pure_essence";
    String dynamite = "dynamite";
    String explorers_ring_alchemy = "explorers_ring_alchemy";
    String kingdom_of_miscellania = "kingdom_of_miscellania";
    String kingdom_of_miscellania_favor = "kingdom_of_miscellania_favor";
    String kingdom_of_miscellania_favor_date = "miscellania_favor_date";

    @ConfigSection(
        name = "Infoboxes",
        description = "Infoboxes",
        position = 1
    ) String infoboxes = "infoboxes";

        @ConfigItem(
            keyName = battlestaves,
            name = "Battlestaves from Zaff",
            description = "Reminds you to buy battlestaves from Zaff at Varrock.",
            section = infoboxes
        ) default boolean showBattlestaves() { return true; }

        @ConfigItem(
            keyName = bow_strings,
            name = "Bow strings from Flax Keeper",
            description = "Reminds you to exchange flax for Bow Strings from the Flax Keeper at Seers Village.",
            section = infoboxes
        ) default boolean showBowStrings() { return true; }

        @ConfigItem(
            keyName = buckets_of_sand,
            name = "Buckets of sand from Bert",
            description = "Reminds you to collect 84 buckets of sand from Bert at Yanille.",
            section = infoboxes
        ) default boolean showBucketsOfSand() { return true; }

        @ConfigItem(
            keyName = buckets_of_slime,
            name = "Buckets of slime and bonemeal from Robin",
            description = "Reminds you to exchange bones for buckets of slime and bonemeal from Robin at Port Phasmatys.",
            section = infoboxes
        ) default boolean showBucketsOfSlime() { return true; }

        @Range(min = -1, max = 99)
        @ConfigItem(
            keyName = kingdom_of_miscellania,
            name = "Miscellania favor",
            description = "Reminds you to keep favor of Miscellania over certain percentage.",
            position = 99,
            section = infoboxes
        ) default int showKingdomOfMiscellaniaFavor() { return 99; }

        @ConfigItem(
            keyName = ogre_arrows,
            name = "Ogre arrows from Rantz",
            description = "Reminds you to collect ogre arrows from Rantz near Feldip Hills cave.",
            section = infoboxes
        ) default boolean showOgreArrows() { return true; }

        @ConfigItem(
            keyName = pure_essence,
            name = "Pure essence from Wizard Cromperty",
            description = "Reminds you to collect pure essence from Wizard Cromperty at East-Ardougne.",
            section = infoboxes
        ) default boolean showPureEssence() { return true; }

        @ConfigItem(
            keyName = dynamite,
            name = "Dynamite from Thirus",
            description = "Reminds you to collect dynamite from Thirus.",
            section = infoboxes
        ) default boolean showDynamite() { return true; }

        @ConfigItem(
            keyName = random_runes,
            name = "Random runes from Lundail",
            description = "Reminds you to collect random runes from Lundail.",
            section = infoboxes
        ) default boolean showRandomRunes() { return true; }

        @ConfigItem(
            keyName = herb_boxes,
            name = "Herb boxes from Nightmare Zone",
            description = "Reminds you to buy herb boxes from Nightmare Zone.",
            section = infoboxes
        ) default boolean showHerbBoxes() { return true; }

        @ConfigItem(
            keyName = impling_jars,
            name = "Impling jars from Elnock Inquisitor",
            description = "Reminds you to buy impling jars from Elnock Inquisitor.",
            section = infoboxes
        ) default boolean showImplingJars() { return true; }

        @ConfigItem(
            keyName = explorers_ring_alchemy,
            name = "Explorers ring alchemy uses",
            description = "Reminds you to use explorers ring alchemy charges.",
            section = infoboxes
        ) default boolean showExplorersRingAlchemy() { return true; }

    @ConfigSection(
        name = "Debug",
        description = "Debug",
        position = 99,
        closedByDefault = true
    ) String debug = "debug";

        @ConfigItem(
            keyName = version,
            name = "Version",
            description = "Version of the plugin for update message",
            section = debug,
            position = 99
        ) default String getVersion() { return ""; }

        @Range(min = 0, max = 127)
        @ConfigItem(
            keyName = kingdom_of_miscellania_favor,
            name = kingdom_of_miscellania_favor,
            description = kingdom_of_miscellania_favor,
            section = debug
        ) default int getKingdomOfMiscellaniaFavor() { return 0; }

        @ConfigItem(
            keyName = kingdom_of_miscellania_favor_date,
            name = kingdom_of_miscellania_favor_date,
            description = kingdom_of_miscellania_favor_date,
            section = debug
        ) default String getKingdomOfMiscellaniaFavorDate() { return null; }
}
