package tictac7x.sulliuscep;

import tictac7x.Overlay;

import java.awt.Color;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("tictac7x-sulliuscep")
public interface SulliuscepConfig extends Config {
	@ConfigItem(
		keyName = "sulliuscep",
		name = "Highlight sulliuscep",
		description = "Highlight sulliuscep that needs to be cut.",
		position = 1
	) default boolean highlightSulliuscep() { return true; }

	@ConfigItem(
		keyName = "sulliuscep_color",
		name = "Sulliuscep",
		description = "Choose the color of the sulliuscep.",
		position = 2
	) default Color getSulliuscepColor() { return Overlay.color_green; }

	@ConfigItem(
		keyName = "obstacles",
		name = "Highlight obstacles",
		description = "Highlight obstacles that need to be cleared.",
		position = 3
	) default boolean highlightObstacles() { return true; }

	@ConfigItem(
		keyName = "obstacles_color",
		name = "Obstacles",
		description = "Choose the color of the obstacles.",
		position = 4
	) default Color getObstaclesColor() { return Overlay.color_yellow; }

	@ConfigItem(
		keyName = "mud_pit",
		name = "Highlight mud pit",
		description = "Highlight mud pit that needs to be filled with mushrooms.",
		position = 5
	) default boolean highlightMudPit() { return true; }

	@ConfigItem(
		keyName = "mud_pit_color",
		name = "Mud pit",
		description = "Choose the color of the mud pit.",
		position = 6
	) default Color getMudPitColor() { return Overlay.color_red; }
}
