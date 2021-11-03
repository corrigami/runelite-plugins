package tictac7x.rooftops;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import tictac7x.Overlay;


@ConfigGroup("tictac7x-rooftops")
public interface RooftopsConfig extends Config {
	@ConfigItem(
		keyName = "obstacles_next",
		name = "Next obstacle",
		description = "Color of next obstacle.",
		position = 1
	) default Color getObstacleNext() { return Overlay.color_green; }

	@ConfigItem(
		keyName = "obstacles_unavailable",
		name = "Unavailable obstacles",
		description = "Color of unavailable obstacles.",
		position = 2
	) default Color getObstacleUnavailable() { return Overlay.color_yellow; }

	@ConfigItem(
		keyName = "obstacle_stop",
		name = "Stopping obstacles",
		description = "Color of obstacle that should not be used, because Mark of grace is on the ground.",
		position = 3
	) default Color getObstacleStop() { return Overlay.color_red; }

	@ConfigItem(
		keyName = "mark_of_grace",
		name = "Mark of grace",
		description = "Color of mark of grace highlight.",
		position = 4
	) default Color getMarkOfGrace() { return Overlay.color_green; }
}
