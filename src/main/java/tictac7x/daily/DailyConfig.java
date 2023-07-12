package tictac7x.daily;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.*;

import java.util.Date;

@ConfigGroup(DailyConfig.group)
public interface DailyConfig extends Config {
    String group = "tictac7x-daily";
    String battlestaves = "battlestaves";
    String bow_strings = "bow_strings";
    String buckets_of_sand = "buckets_of_sand";
    String buckets_of_slime = "buckets_of_slime";
    String ogre_arrows = "ogre_arrows";
    String pure_essence = "pure_essence";
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
            name = "Battlestaves by Zaff",
            description = "Reminds you to buy battlestaves from Zaff at Varrock.",
            section = infoboxes
        ) default boolean showBattlestaves() { return true; }

        @ConfigItem(
            keyName = bow_strings,
            name = "Bow strings by Flax Keeper",
            description = "Reminds you to exchange flax for Bow Strings from the Flax Keeper at Seers Village.",
            section = infoboxes
        ) default boolean showBowStrings() { return true; }

        @ConfigItem(
            keyName = buckets_of_sand,
            name = "Buckets of sand by Bert",
            description = "Reminds you to collect 84 buckets of sand from Bert at Yanille.",
            section = infoboxes
        ) default boolean showBucketsOfSand() { return true; }

        @ConfigItem(
            keyName = buckets_of_slime,
            name = "Buckets of slime and bonemeal by Robin",
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
            name = "Ogre arrows by Rantz",
            description = "Reminds you to collect ogre arrows from Rantz near Feldip Hills cave.",
            section = infoboxes
        ) default boolean showOgreArrows() { return true; }

        @ConfigItem(
            keyName = pure_essence,
            name = "Pure essence by Wizard Cromperty",
            description = "Reminds you to collect pure essence from Wizard Cromperty at East-Ardougne.",
            section = infoboxes
        ) default boolean showPureEssence() { return true; }

    @ConfigSection(
        name = "Debug",
        description = "Debug",
        position = 99,
        closedByDefault = true
    ) String debug = "debug";

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
