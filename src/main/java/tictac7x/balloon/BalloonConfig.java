package tictac7x.balloon;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(BalloonConfig.group)
public interface BalloonConfig extends Config {
	String group = "tictac7x.balloon";
	enum style { HORIZONTAL, VERTICAL, INFOBOXES }

	@ConfigItem(
		position = 1,
		keyName = "style",
		name = "Style",
		description = "Choose how the logs of the storages are displayed."
	) default style getStyle()
	{
		return style.HORIZONTAL;
	}

	@ConfigItem(
		position = 2,
		keyName = "recent",
		name = "Show only recent",
		description = "Show only recently used logs."
	) default boolean showRecentOnly() { return true; }

	@ConfigItem(
		position = 3,
		keyName = "duration",
		name = "Active duration",
		description = "Choose for how many minutes the information about logs is shown after using the balloon."
	) default int getDuration() { return 5; }

	String key_storage = "storage";
	@ConfigItem(
		keyName = key_storage,
		name = key_storage,
		description = key_storage,
		hidden = true
	) default String getStorage() { return "0,0,0,0,0"; }

	String key_storage_date_logs = "storage_date_logs";
	@ConfigItem(
		keyName = key_storage_date_logs,
		name = key_storage_date_logs,
		description = key_storage_date_logs,
		hidden = true
	) default String getStorageDateLogs() { return ""; }

	String key_storage_date_logs_oak = "storage_date_logs_oak";
	@ConfigItem(
		keyName = key_storage_date_logs_oak,
		name = key_storage_date_logs_oak,
		description = key_storage_date_logs_oak,
		hidden = true
	) default String getStorageDateLogsOak() { return ""; }

	String key_storage_date_logs_willow = "storage_date_logs_willow";
	@ConfigItem(
		keyName = key_storage_date_logs_willow,
		name = key_storage_date_logs_willow,
		description = key_storage_date_logs_willow,
		hidden = true
	) default String getStorageDateLogsWillow() { return ""; }

	String key_storage_date_logs_yew = "storage_date_logs_yew";
	@ConfigItem(
		keyName = key_storage_date_logs_yew,
		name = key_storage_date_logs_yew,
		description = key_storage_date_logs_yew,
		hidden = true
	) default String getStorageDateLogsYew() { return ""; }

	String key_storage_date_logs_magic = "storage_date_logs_magic";
	@ConfigItem(
		keyName = key_storage_date_logs_magic,
		name = key_storage_date_logs_magic,
		description = key_storage_date_logs_magic,
		hidden = true
	) default String getStorageDateLogsMagic() { return ""; }
}
