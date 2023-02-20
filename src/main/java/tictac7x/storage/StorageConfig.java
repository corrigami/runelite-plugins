package tictac7x.storage;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigSection;

@ConfigGroup(StorageConfig.group)
public interface StorageConfig extends Config {
	String group = "tictac7x-storage";

	enum InventoryEmpty { TOP, FIRST, LAST, BOTTOM, HIDDEN }
	enum InventoryDensity { COMPACT, REGULAR }

	@ConfigSection(
		name = "Inventory",
		description = "Inventory overlay settings",
		position = 1
	) String inventory = "inventory";

		@ConfigItem(
			keyName = inventory,
			name = inventory,
			description = inventory,
			section = inventory
		) default String getInventory() { return "{}"; }

	@ConfigSection(
		name = "Bank",
		description = "Bank overlay settings",
		position = 2
	) String bank = "bank";

		@ConfigItem(
			keyName = bank,
			name = bank,
			description = bank,
			section = bank
		) default String getBank() { return "{}"; }

		@ConfigItem(
			keyName = bank + "_whitelist",
			name = "Whitelist",
			description = "Names of items to show in bank overlay",
			section = bank
		) default String getBankWhitelist() { return "Coins"; }
}
