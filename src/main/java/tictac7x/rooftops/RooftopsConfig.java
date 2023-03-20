package tictac7x.rooftops;

import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

import java.awt.Color;

@ConfigGroup("tictac7x-rooftops")
public interface RooftopsConfig extends Config {
	@ConfigSection(
		name = "Obstacles",
		description = "Obstacles",
		position = 1
	) String obstacles = "obstacles";

		@Alpha
		@ConfigItem(
			keyName = "obstacle_next",
			name = "Next obstacle",
			description = "Color of the next obstacle.",
			position = 1,
			section = obstacles
		) default Color getObstacleNextColor() { return new Color(0, 255, 0, 80); }

		@Alpha
		@ConfigItem(
			keyName = "obstacle_next_unavailable",
			name = "Next unavailable obstacle",
			description = "Color of the next obstacle.",
			position = 2,
			section = obstacles
		) default Color getObstacleNextUnavailableColor() { return new Color(200, 255, 0, 80); }

		@Alpha
		@ConfigItem(
			keyName = "obstacle_unavailable",
			name = "Unavailable obstacles",
			description = "Color of unavailable obstacles.",
			position = 3,
			section = obstacles
		) default Color getObstacleUnavailableColor() { return new Color(255, 150, 0, 80); }

		@Alpha
		@ConfigItem(
			keyName = "obstacle_stop",
			name = "Stopping obstacle",
			description = "Color of obstacle that should not be used, because Mark of grace is on the ground.",
			position = 4,
			section = obstacles
		) default Color getObstacleStopColor() { return new Color(255, 0, 0, 80); }

	@ConfigSection(
		name = "Marks of graces",
		description = "Marks of graces",
		position = 2
	) String marks_of_graces = "marks_of_graces";

		@Alpha
		@ConfigItem(
			keyName = "mark_of_grace",
			name = "Mark of grace",
			description = "Color of the Mark of grace.",
			position = 1,
			section = marks_of_graces
		) default Color getMarkOfGraceColor() { return new Color(0, 255, 0, 80); }

	@ConfigSection(
		name = "Debug",
		description = "Debug",
		closedByDefault = true,
		position = 3
	) String debug = "debug";

		@ConfigItem(
			keyName = "debug",
			name = "Enable debug overlay",
			description = "Enable debug overlay, if some marks of grace and the next obstacle are showing incorrectly. Create a new issue in GitHub with the details.",
			position = 1,
			section = debug
		) default boolean debugging() { return false; }
}
