package tictac7x.daily;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("tictac7x-daily")
public interface DailyConfig extends Config {
	String battlestaves_id = "battlestaves";
	String battlestaves_name = "Zaff's battlestaves";
	String battlestaves_description = "Reminds you to buy battlestaves from Zaff at Varrock.";

	@ConfigItem(
		keyName = battlestaves_id,
		name = battlestaves_name,
		description = battlestaves_description
	) default boolean showBattlestaves() { return true; }

	String buckets_of_sand_id = "buckets_of_sand";
	String buckets_of_sand_name = "Bert's buckets of sand";
	String buckets_of_sand_description = "Reminds you to collect 84 buckets of sand from Bert at Yanille.";

	@ConfigItem(
			keyName = buckets_of_sand_id,
			name = buckets_of_sand_name,
			description = buckets_of_sand_description
	) default boolean showBucketsOfSand() { return true; }
}
