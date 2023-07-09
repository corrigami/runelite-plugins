package tictac7x.daily;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

import java.util.Date;

@ConfigGroup(DailyConfig.group)
public interface DailyConfig extends Config {
    String group = "tictac7x-daily";

    String battlestaves_id = "battlestaves";
    String battlestaves_name = "Battlestaves by Zaff";
    String battlestaves_description = "Reminds you to buy battlestaves from Zaff at Varrock.";

    @ConfigItem(
        keyName = battlestaves_id,
        name = battlestaves_name,
        description = battlestaves_description,
        position = 1
    ) default boolean showBattlestaves() { return true; }

    String bow_strings_id = "bow_strings";
    String bow_strings_name = "Bow strings by Flax Keeper";
    String bow_strings_description = "Reminds you to exchange flax for Bow Strings from the Flax Keeper at Seers Village.";

    @ConfigItem(
        keyName = bow_strings_id,
        name = bow_strings_name,
        description = bow_strings_description,
        position = 2
    ) default boolean showBowStrings() { return true; }

    String buckets_of_sand_id = "buckets_of_sand";
    String buckets_of_sand_name = "Buckets of sand by Bert";
    String buckets_of_sand_description = "Reminds you to collect 84 buckets of sand from Bert at Yanille.";

    @ConfigItem(
        keyName = buckets_of_sand_id,
        name = buckets_of_sand_name,
        description = buckets_of_sand_description,
        position = 3
    ) default boolean showBucketsOfSand() { return true; }

    String buckets_of_slime_id = "buckets_of_slime";
    String buckets_of_slime_name = "Buckets of slime and bonemeal by Robin";
    String buckets_of_slime_description = "Reminds you to exchange bones for buckets of slime and bonemeal from Robin at Port Phasmatys.";

    @ConfigItem(
        keyName = buckets_of_slime_id,
        name = buckets_of_slime_name,
        description = buckets_of_slime_description,
        position = 4
    ) default boolean showBucketsOfSlime() { return true; }

    String kingdom_of_miscellania_id = "kingdom_of_miscellania";
    String kingdom_of_miscellania_name = "Miscellania favor";
    String kingdom_of_miscellania_description = "Reminds you to keep favor of Miscellania people at 100%.";
    String kingdom_of_miscellania_favor = "kingdom_of_miscellania_favor";
    String kingdom_of_miscellania_favor_date = "miscellania_favor_date";

    @ConfigItem(
        keyName = kingdom_of_miscellania_id,
        name = kingdom_of_miscellania_name,
        description = kingdom_of_miscellania_description,
        position = 5
    ) default boolean showKingdomOfMiscellaniaFavor() { return true; }

    @ConfigItem(
        keyName = kingdom_of_miscellania_favor,
        name = kingdom_of_miscellania_favor,
        description = kingdom_of_miscellania_favor,
        hidden = true
    ) default int getKingdomOfMiscellaniaFavor() { return 0; }

    @ConfigItem(
        keyName = kingdom_of_miscellania_favor_date,
        name = kingdom_of_miscellania_favor_date,
        description = kingdom_of_miscellania_favor_date,
        hidden = true
    ) default String getKingdomOfMiscellaniaFavorDate() { return null; }

    String ogre_arrows_id = "ogre_arrows";
    String ogre_arrows_name = "Ogre arrows by Rantz";
    String ogre_arrows_description = "Reminds you to collect ogre arrows from Rantz near Feldip Hills cave.";

    @ConfigItem(
        keyName = ogre_arrows_id,
        name = ogre_arrows_name,
        description = ogre_arrows_description,
        position = 6
    ) default boolean showOgreArrows() { return true; }

    String pure_essence_id = "pure_essence";
    String pure_essence_name = "Pure essence by Wizard Cromperty";
    String pure_essence_description = "Reminds you to collect pure essence from Wizard Cromperty at East-Ardougne.";

    @ConfigItem(
        keyName = pure_essence_id,
        name = pure_essence_name,
        description = pure_essence_description,
        position = 7
    ) default boolean showPureEssence() { return true; }
}
