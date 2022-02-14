package tictac7x.balloon;

import net.runelite.client.config.Units;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigGroup;

@ConfigGroup(BalloonConfig.group)
public interface BalloonConfig extends Config {
	enum Logs { LOGS_REGULAR, LOGS_OAK, LOGS_WILLOW, LOGS_YEW, LOGS_MAGIC }
	String location_varrock = "Varrock";
	String location_entrana = "Entrana";
	String location_taverley = "Taverley";
	String location_crafting_guild = "Crafting Guild";
	String location_castle_wars = "Castle Wars";
	String location_grand_tree = "Gnome Stronghold";

	String group = "tictac7x-balloon";
	enum style { WIDGET, INFOBOXES }
	enum Show { RECENTLY_USED, ALL, INDEFINITELY }

	@ConfigItem(
		position = 1,
		keyName = "style",
		name = "Style",
		description = "Choose how the logs of the storages are displayed."
	) default style getStyle() { return style.WIDGET; }

	@ConfigItem(
		position = 2,
		keyName = "show",
		name = "Show",
		description = "Show storage logs amounts all the time."
	) default Show show() { return Show.RECENTLY_USED; }

	@Units(" min")
	@ConfigItem(
		position = 3,
		keyName = "duration",
		name = "Display duration",
		description = "Choose for how many minutes the information about recently used logs is visible."
	) default int getDisplayDuration() { return 10; }

	@ConfigItem(
		position = 4,
		keyName = "show_near_balloon",
		name = "Always show near balloon",
		description = "Show storage logs amounts when you are near any of the balloons."
	) default boolean showNearBalloon() { return true; }

	String logs_regular = "logs_regular";
	@ConfigItem(
		keyName = logs_regular,
		name = logs_regular,
		description = logs_regular,
		hidden = true
	) default int getLogsRegular() { return 0; }

	String logs_regular_date = "logs_regular_date";
	@ConfigItem(
		keyName = logs_regular_date,
		name = logs_regular_date,
		description = logs_regular_date,
		hidden = true
	) default String getLogsRegularDate() { return ""; }

	String logs_oak = "logs_oak";
	@ConfigItem(
		keyName = logs_oak,
		name = logs_oak,
		description = logs_oak,
		hidden = true
	) default int getLogsOak() { return 0; }

	String logs_oak_date = "logs_oak_date";
	@ConfigItem(
		keyName = logs_oak_date,
		name = logs_oak_date,
		description = logs_oak_date,
		hidden = true
	) default String getLogsOakDate() { return ""; }

	String logs_willow = "logs_willow";
	@ConfigItem(
		keyName = logs_willow,
		name = logs_willow,
		description = logs_willow,
		hidden = true
	) default int getLogsWillow() { return 0; }

	String logs_willow_date = "logs_willow_date";
	@ConfigItem(
		keyName = logs_willow_date,
		name = logs_willow_date,
		description = logs_willow_date,
		hidden = true
	) default String getWillowLogsDate() { return ""; }

	String logs_yew = "logs_yew";
	@ConfigItem(
		keyName = logs_yew,
		name = logs_yew,
		description = logs_yew,
		hidden = true
	) default int getLogsYew() { return 0; }

	String logs_yew_date = "logs_yew_date";
	@ConfigItem(
		keyName = logs_yew_date,
		name = logs_yew_date,
		description = logs_yew_date,
		hidden = true
	) default String getLogsYewDate() { return ""; }

	String logs_magic = "logs_magic";
	@ConfigItem(
		keyName = logs_magic,
		name = logs_magic,
		description = logs_magic,
		hidden = true
	) default int getLogsMagic() { return 0; }

	String logs_magic_date = "logs_magic_date";
	@ConfigItem(
		keyName = logs_magic_date,
		name = logs_magic_date,
		description = logs_magic_date,
		hidden = true
	) default String getLogsMagicDate() { return ""; }
}
