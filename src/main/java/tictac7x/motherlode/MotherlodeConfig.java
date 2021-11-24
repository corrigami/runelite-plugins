package tictac7x.motherlode;

import tictac7x.Overlay;

import java.awt.Color;

import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;


@ConfigGroup("tictac7x-motherlode")
public interface MotherlodeConfig extends Config {
	@Alpha
	@ConfigItem(
		keyName = "ore_veins",
		name = "Mineable Ore Veins",
		description = "Highlight ore veins that can be mined.",
		position = 1
	) default Color getOreVeinsColor() { return Overlay.color_green; }

	@Alpha
	@ConfigItem(
		keyName = "ore_veins_depleted",
		name = "Depleted Ore Veins",
		description = "Highlight ore veins that are depleted.",
		position = 2
	) default Color getOreVeinsDepletedColor() { return Overlay.color_yellow; }

	@Alpha
	@ConfigItem(
		keyName = "ore_veins_dont",
		name = "Stopping Ore Veins",
		description = "Highlight ore veins when they shouldn't be mined.",
		position = 3
	) default Color getOreVeinsStoppingColor() { return Overlay.color_red; }

	@Alpha
	@ConfigItem(
		keyName = "rockfalls",
		name = "Rockfalls",
		description = "Highlight rockfalls that need to be cleared.",
		position = 4
	) default Color getRockfallsColor() { return new Color(Overlay.color_red.getRed(), Overlay.color_red.getGreen(), Overlay.color_red.getBlue(), 70); }
}
