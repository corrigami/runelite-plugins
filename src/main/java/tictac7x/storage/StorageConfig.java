package tictac7x.storage;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup(StorageConfig.group)
public interface StorageConfig extends Config {
	String group = "tictac7x-storage";

	@ConfigSection(
		name = "Bank",
		description = "Bank overlay",
		position = 1
	) String bank = "bank";

		@ConfigItem(
			keyName = "bank",
			name = "bank",
			description = "bank",
			section = bank,
			position = 1,
			hidden = false
		) default String getBank() { return ""; }

		@ConfigItem(
			keyName = "bank_whitelist",
			name = "whitelist",
			description = "Names of items to show in bank overlay.",
			section = bank,
			position = 2
		) default String getBankWhitelist() { return ""; }
}
