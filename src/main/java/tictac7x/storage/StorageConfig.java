package tictac7x.storage;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup(StorageConfig.group)
public interface StorageConfig extends Config {
	String group = "tictac7x-storage";

	enum InventoryEmpty { TOP, FIRST, LAST, BOTTOM, HIDDEN }
	enum InventoryDensity { COMPACT, REGULAR }

	@ConfigSection(
		name = "Inventory",
		description = "Inventory overlay",
		position = 1
	) String inventory = "inventory";

		@ConfigItem(
			keyName = "inventory",
			name = "inventory",
			description = "inventory",
			section = inventory,
			position = 1,
			hidden = true
		) default String getInventory() { return ""; }

		@ConfigItem(
			keyName = "inventory_show",
			name = "Show inventory overlay",
			description = "Show overlay of inventory items.",
			section = inventory,
			position = 2
		) default boolean showInventory() { return true; }

		@ConfigItem(
			keyName = "inventory_whitelist",
			name = "whitelist",
			description = "Names of items to show in the inventory overlay.",
			section = inventory,
			position = 3,
			hidden = true
		) default String getInventoryWhitelist() { return ""; }

		@ConfigItem(
			keyName = "inventory_blacklist",
			name = "Inventory blacklist",
			description = "Names of items to hide from the inventory overlay.",
			section = inventory,
			position = 4
		) default String getInventoryBlacklist() { return ""; }

		@ConfigItem(
			keyName = "inventory_empty",
			name = "Empty slots location",
			description = "Where to show how many empty slots inventory has.",
			section = inventory,
			position = 5
		) default InventoryEmpty getInventoryEmptySlots() { return InventoryEmpty.BOTTOM; }

	@ConfigSection(
		name = "Bank",
		description = "Bank overlay",
		position = 2
	) String bank = "bank";

		@ConfigItem(
			keyName = "bank",
			name = "bank",
			description = "bank",
			section = bank,
			position = 1,
			hidden = true
		) default String getBank() { return ""; }

		@ConfigItem(
			keyName = "bank_show",
			name = "Show bank overlay",
			description = "Show overlay of bank items.",
			section = bank,
			position = 2
		) default boolean showBank() { return true; }

		@ConfigItem(
			keyName = "bank_whitelist",
			name = "Bank whitelist",
			description = "Names of items to show in the bank overlay.",
			section = bank,
			position = 3
		) default String getBankWhitelist() { return ""; }

		@ConfigItem(
			keyName = "bank_blacklist",
			name = "Bank blacklist",
			description = "Names of items to hide from the bank overlay.",
			section = bank,
			position = 4
		) default String getBankBlacklist() { return ""; }

	@ConfigSection(
		name = "General",
		description = "General settings",
		position = 3
	) String general = "general";

		@ConfigItem(
			keyName = "general_density",
			name = "Items density",
			description = "Change the density of the overlays.",
			section = general,
			position = 1
		) default InventoryDensity getOverlayDensity() { return InventoryDensity.REGULAR; }
}
