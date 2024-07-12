package tictac7x.deposit_worn_items;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

import static tictac7x.deposit_worn_items.TicTac7xDepositWornItemsConfig.group;

@ConfigGroup(group)
public interface TicTac7xDepositWornItemsConfig extends Config {
	String group = "tictac7x-deposit-worn-items";
	String deposit_worn_items_enabled = "deposit_worn_items_enabled";

	@ConfigItem(
		keyName = deposit_worn_items_enabled,
		name = "Enable Deposit Worn Items",
		description = "Weather deposit worn items button is enabled or not"
	) default boolean isDepositWornItemsEnabled() { return true; }

	@ConfigItem(
		keyName = "toggle_deposit_worn_items",
		name = "Show toggle option",
		description = "Weather to show the option to enable/disable button from right click menu"
	) default boolean isDepositWornItemsToggleable() { return true; }
}
