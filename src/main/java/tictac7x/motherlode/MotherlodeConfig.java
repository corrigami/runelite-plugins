package tictac7x.motherlode;

import tictac7x.Overlay;
import java.awt.Color;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(MotherlodeConfig.group)
public interface MotherlodeConfig extends Config {
	String group = "tictac7x-motherlode";

	@Alpha
	@ConfigItem(
		keyName = "ore_veins",
		name = "Mineable Ore Veins",
		description = "Highlight ore veins that can be mined.",
		position = 1
	) default Color getOreVeinsColor() { return Overlay.getColor(Overlay.color_green, Overlay.alpha_vibrant); }

	@Alpha
	@ConfigItem(
		keyName = "ore_veins_depleted",
		name = "Depleted Ore Veins",
		description = "Highlight ore veins that are depleted.",
		position = 2
	) default Color getOreVeinsDepletedColor() { return Overlay.getColor(Overlay.color_yellow, Overlay.alpha_vibrant); }

	@Alpha
	@ConfigItem(
		keyName = "ore_veins_stop",
		name = "Stopping Ore Veins",
		description = "Highlight ore veins when they shouldn't be mined.",
		position = 3
	) default Color getOreVeinsStoppingColor() { return Overlay.getColor(Overlay.color_red, Overlay.alpha_vibrant); }

	@Alpha
	@ConfigItem(
		keyName = "rockfalls",
		name = "Rockfalls",
		description = "Highlight rockfalls that need to be cleared.",
		position = 4
	) default Color getRockfallsColor() { return Overlay.getColor(Overlay.color_red, Overlay.alpha_normal); }

	@ConfigItem(
		keyName = "draw_distance",
		name = "Draw distance",
		description = "Change how far away ore veins and rockfalls will be highlighted.",
		position = 5
	) default int getDrawDistance() { return 4000; }

	String custom_sack_widget = "custom_sack_widget";
	@ConfigItem(
		keyName = custom_sack_widget,
		name = "Custom sack widget",
		description = "Toggle custom sack widget",
		position = 6
	) default boolean showCustomSackWidget() { return true; }
}
