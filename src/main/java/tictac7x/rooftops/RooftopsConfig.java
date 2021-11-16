package tictac7x.rooftops;

import tictac7x.Overlay;

import java.awt.Color;

import net.runelite.client.config.Config;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigGroup;

@ConfigGroup("tictac7x-rooftops")
public interface RooftopsConfig extends Config {
	@ConfigItem(
		keyName = "debug",
		name = "debug",
		description = "debug",
		hidden = true
	) default boolean debugging() { return false; }

	@Alpha
	@ConfigItem(
		keyName = "obstacle_next",
		name = "Next obstacle",
		description = "Color of the next obstacle.",
		position = 1
	) default Color getObstacleNextColor() { return Overlay.color_green; }

	@Alpha
	@ConfigItem(
		keyName = "obstacle_next_unavailable",
		name = "Next unavailable obstacle",
		description = "Color of the next obstacle.",
		position = 2
	) default Color getObstacleNextUnavailableColor() { return new Color(165, 199, 7, 255); }

	@Alpha
	@ConfigItem(
		keyName = "obstacle_unavailable",
		name = "Unavailable obstacles",
		description = "Color of unavailable obstacles.",
		position = 3
	) default Color getObstacleUnavailableColor() { return Overlay.color_yellow; }

	@Alpha
	@ConfigItem(
		keyName = "obstacle_stop",
		name = "Stopping obstacle",
		description = "Color of obstacle that should not be used, because Mark of grace is on the ground.",
		position = 4
	) default Color getObstacleStopColor() { return Overlay.color_red; }

	@Alpha
	@ConfigItem(
		keyName = "mark_of_grace",
		name = "Mark of grace",
		description = "Color of the Mark of grace.",
		position = 5
	) default Color getMarkOfGraceColor() { return Overlay.color_green; }
}
