package tictac7x.rooftops;

import net.runelite.client.config.Units;
import tictac7x.Overlay;
import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("tictac7x-rooftops")
public interface RooftopsConfig extends Config {
	@ConfigItem(
		keyName = "debug",
		name = "debug",
		description = "debug",
		hidden = true
	) default boolean debugging() { return false; }

	@ConfigItem(
		keyName = "highlight_obstacles_all",
		name = "Highlight all obstacles",
		description = "Highlight all obstacles that even can't be clicked currently.",
		position = 1
	) default boolean highlightAllObstacles() { return true; }

	@ConfigItem(
		keyName = "obstacle_next_color",
		name = "Next obstacle",
		description = "Color of next obstacle.",
		position = 2
	) default Color getObstacleNextColor() { return Overlay.color_green; }

	@ConfigItem(
		keyName = "obstacle_unavailable",
		name = "Highlight unavailable obstacles",
		description = "Highlight unavailable obstacles different from next obstacle.",
		position = 3
	) default boolean showObstaclesUnavailable() { return true; }

	@ConfigItem(
		keyName = "obstacle_unavailable_color",
		name = "Unavailable obstacles",
		description = "Color of unavailable obstacles.",
		position = 4
	) default Color getObstacleUnavailableColor() { return Overlay.color_yellow; }

	@ConfigItem(
		keyName = "obstacle_stop",
		name = "Highlight stopping obstacle",
		description = "Highlight obstacle that should not be used before picking up Mark of grace.",
		position = 5
	) default boolean showObstacleStop() { return true; }

	@ConfigItem(
		keyName = "obstacle_stop_color",
		name = "Stopping obstacle",
		description = "Color of obstacle that should not be used, because Mark of grace is on the ground.",
		position = 6
	) default Color getObstacleStopColor() { return Overlay.color_red; }

	@ConfigItem(
		keyName = "mark_of_grace",
		name = "Highlight Mark of grace",
		description = "Color of mark of grace highlight.",
		position = 7
	) default boolean showMarkOfGrace() { return true; }

	@ConfigItem(
		keyName = "mark_of_grace_color",
		name = "Mark of grace",
		description = "Color of mark of grace highlight.",
		position = 8
	) default Color getMarkOfGraceColor() { return Overlay.color_green; }

	@Units(" px")
	@ConfigItem(
			keyName = "highlight_stroke",
			name = "Highlight stroke",
			description = "Choose the width of highlight strokes.",
			position = 9
	) default int getHighlightStroke() { return Overlay.clickbox_stroke_width; }

	@Units(" %")
	@ConfigItem(
			keyName = "highlight_fill",
			name = "Highlight fill",
			description = "Choose the opacity of highlight fills.",
			position = 10
	) default int getHighlightFill() { return Overlay.clickbox_fill_alpha; }
}
