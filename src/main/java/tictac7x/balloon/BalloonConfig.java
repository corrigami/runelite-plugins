package tictac7x.balloon;

import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigGroup;

@ConfigGroup(BalloonConfig.group)
public interface BalloonConfig extends Config {
	String group = "tictac7x-balloon";
	enum Show { NEAR_THE_BALLOON, ALL_THE_TIME }

	@ConfigSection(
		name = "Infoboxes",
		description = "Infoboxes",
		position = 1
	) String infoboxes = "infoboxes";

		@ConfigItem(
			keyName = "show",
			name = "Show",
			description = "Show storage logs amounts all the time.",
			position = 1,
			section = infoboxes
		) default Show show() { return Show.NEAR_THE_BALLOON; }

	@ConfigSection(
		name = "Debug",
		description = "Debug",
		position = 2,
		closedByDefault = true
	) String debug = "debug";

		String logs_regular = "logs_regular";
		@ConfigItem(
			keyName = logs_regular,
			name = "Regular logs",
			description = "Amount of regular logs",
			position = 1,
			section = debug
		) default int getLogsRegular() { return 0; }

		String logs_oak = "logs_oak";
		@ConfigItem(
			keyName = logs_oak,
			name = "Oak logs",
			description = "Amount of oak logs",
			position = 2,
			section = debug
		) default int getLogsOak() { return 0; }

		String logs_willow = "logs_willow";
		@ConfigItem(
			keyName = logs_willow,
			name = "Willow logs",
			description = "Amount of willow logs",
			position = 3,
			section = debug
		) default int getLogsWillow() { return 0; }

		String logs_yew = "logs_yew";
		@ConfigItem(
			keyName = logs_yew,
			name = "Yew logs",
			description = "Amount of yew logs",
			position = 4,
			section = debug
		) default int getLogsYew() { return 0; }

		String logs_magic = "logs_magic";
		@ConfigItem(
			keyName = logs_magic,
			name = "Magic logs",
			description = "Amount of magic logs",
			position = 5,
			section = debug
		) default int getLogsMagic() { return 0; }
}
