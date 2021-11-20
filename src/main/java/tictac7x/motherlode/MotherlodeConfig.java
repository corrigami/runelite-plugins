package tictac7x.motherlode;

import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import tictac7x.Overlay;

import java.awt.*;

@ConfigGroup("tictac7x-motherlode")
public interface MotherlodeConfig extends Config {
	@Alpha
	@ConfigItem(
		keyName = "ore_veins",
		name = "Mineable Ore Veins",
		description = "Highlight ore veins that can be mined."
	) default Color getOreVeinsColor() { return Overlay.color_green; }

	@Alpha
	@ConfigItem(
		keyName = "ore_veins_depleted",
		name = "Depleted Ore Veins",
		description = "Highlight ore veins that are depleted."
	) default Color getOreVeinsDepletedColor() { return Overlay.color_yellow; }

	@Alpha
	@ConfigItem(
		keyName = "ore_veins_dont",
		name = "Stopping Ore Veins",
		description = "Highlight ore veins when they shouldn't be mined."
	) default Color getOreVeinsStoppingColor() { return Overlay.color_red; }
}
