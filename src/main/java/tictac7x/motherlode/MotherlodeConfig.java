package tictac7x.motherlode;

import net.runelite.client.config.*;
import tictac7x.Overlay;
import java.awt.Color;

@ConfigGroup(MotherlodeConfig.group)
public interface MotherlodeConfig extends Config {
	String group = "tictac7x-motherlode";

	@ConfigSection(
		name = "Ore veins and rockfalls",
		description = "Highlight ore veins and rockfalls.",
		position = 1
	) String ore_veins_and_rockfalls = "ore_veins_and_rockfalls";

		@Alpha
		@ConfigItem(
			keyName = "ore_veins",
			name = "Mineable Ore Veins",
			description = "Highlight ore veins that can be mined.",
			position = 1,
			section = ore_veins_and_rockfalls
		) default Color getOreVeinsColor() { return Overlay.getColor(Overlay.color_green, Overlay.alpha_vibrant); }

		@Alpha
		@ConfigItem(
			keyName = "ore_veins_depleted",
			name = "Depleted Ore Veins",
			description = "Highlight ore veins that are depleted.",
			position = 2,
			section = ore_veins_and_rockfalls
		) default Color getOreVeinsDepletedColor() { return Overlay.getColor(Overlay.color_yellow, Overlay.alpha_vibrant); }

		@Alpha
		@ConfigItem(
			keyName = "ore_veins_stop",
			name = "Stopping Ore Veins",
			description = "Highlight ore veins when they shouldn't be mined.",
			position = 3,
			section = ore_veins_and_rockfalls
		) default Color getOreVeinsStoppingColor() { return Overlay.getColor(Overlay.color_red, Overlay.alpha_vibrant); }

		@Alpha
		@ConfigItem(
			keyName = "rockfalls",
			name = "Rockfalls",
			description = "Highlight rockfalls that need to be cleared.",
			position = 4,
			section = ore_veins_and_rockfalls
		) default Color getRockfallsColor() { return Overlay.getColor(Overlay.color_red, Overlay.alpha_normal); }

		@ConfigItem(
			keyName = "draw_distance",
			name = "Draw distance",
			description = "Change how far away ore veins and rockfalls will be highlighted.",
			position = 5,
			section = ore_veins_and_rockfalls
		) default int getDrawDistance() { return 4000; }

	@ConfigSection(
		name = "Custom sack widget",
		description = "Show custom sack widget with helpful information.",
		position = 2
	) String custom_sack_widget = "custom_sack_widget";

		@ConfigItem(
			keyName = "sack_paydirt",
			name = "Sack pay-dirt",
			description = "Show total number of pay-dirt in sack and hopper.",
			position = 1,
			section = custom_sack_widget
		) default boolean showSackPaydirt() { return true; }

		@ConfigItem(
			keyName = "sack_deposits",
			name = "Deposits left",
			description = "Show number of inventories you can store before sack gets full.",
			position = 2,
			section = custom_sack_widget
		) default boolean showSackDeposits() { return true; }

		@ConfigItem(
			keyName = "sack_needed",
			name = "Needed pay-dirt",
			description = "Show number of pay-dirt needed to mine before you should deposit the pay-dirt.",
			position = 3,
			section = custom_sack_widget
		) default boolean showSackNeeded() { return true; }

	@ConfigSection(
		name = "General",
		description = "General options to improve overall experience.",
		position = 3
	) String general = "general";

		String maximize_sack_paydirt = "maximize_sack_paydirt";
		@ConfigItem(
			keyName = maximize_sack_paydirt,
			name = "Maximize sack pay-dirt",
			description = "Calculate needed pay-dirt differently to maximize the possible amount of pay-dirt in the sack.",
			position = 1,
			section = general
		) default boolean maximizeSackPaydirt() { return true; }

		@ConfigItem(
			keyName = "upstairs_only",
			name = "Only upstairs veins and rockfalls",
			description = "Highlight only upstairs ore veins and rockfalls.",
			position = 2,
			section = general
		) default boolean upstairsOnly() { return false; }
}
