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
		name = "Enable debug overlay",
		description = "Enable debug overlay, if some marks of grace and the next obstacle are showing incorrectly. Create a new issue in GitHub with the details.",
		hidden = false,
		position = 100
	) default boolean debugging() { return false; }

	@Alpha
	@ConfigItem(
		keyName = "obstacle_next",
		name = "Next obstacle",
		description = "Color of the next obstacle.",
		position = 1
	) default Color getObstacleNextColor() { return Overlay.getColor(Overlay.color_green, Overlay.alpha_normal); }

	@Alpha
	@ConfigItem(
		keyName = "obstacle_next_unavailable",
		name = "Next unavailable obstacle",
		description = "Color of the next obstacle.",
		position = 2
	) default Color getObstacleNextUnavailableColor() { return Overlay.getColor(new Color(200, 255, 0), Overlay.alpha_normal); }

	@Alpha
	@ConfigItem(
		keyName = "obstacle_unavailable",
		name = "Unavailable obstacles",
		description = "Color of unavailable obstacles.",
		position = 3
	) default Color getObstacleUnavailableColor() { return Overlay.getColor(Overlay.color_yellow, Overlay.alpha_normal); }

	@Alpha
	@ConfigItem(
		keyName = "obstacle_stop",
		name = "Stopping obstacle",
		description = "Color of obstacle that should not be used, because Mark of grace is on the ground.",
		position = 4
	) default Color getObstacleStopColor() { return Overlay.getColor(Overlay.color_red, Overlay.alpha_normal); }

	@Alpha
	@ConfigItem(
		keyName = "mark_of_grace",
		name = "Mark of grace",
		description = "Color of the Mark of grace.",
		position = 5
	) default Color getMarkOfGraceColor() { return Overlay.getColor(Overlay.color_green, Overlay.alpha_vibrant); }
}
