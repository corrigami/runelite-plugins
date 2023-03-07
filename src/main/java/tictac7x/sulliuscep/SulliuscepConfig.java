package tictac7x.sulliuscep;

import java.awt.Color;

import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("tictac7x-sulliuscep")
public interface SulliuscepConfig extends Config {
	@ConfigSection(
		name = "Sulliusceps",
		description = "Sulliusceps",
		position = 1
	) String sulliusceps = "sulliusceps";

		@Alpha
		@ConfigItem(
			keyName = "sulliuscep",
			name = "Sulliuscep color",
			description = "Choose the color of the sulliuscep.",
			position = 1,
			section = sulliusceps
		) default Color getSulliuscepColor() { return new Color(0, 255, 0, 40); }

	@ConfigSection(
		name = "Obstacles",
		description = "Obstacles",
		position = 2
	) String obstacles = "obstacles";

		@ConfigItem(
			keyName = "obstacles",
			name = "Highlighted obstacles",
			description = "Select which obstacles to highlight.",
			position = 1,
			section = obstacles
		) default Obstacles highlightObstacles() { return Obstacles.ALL; }

		@Alpha
		@ConfigItem(
			keyName = "obstacles_color",
			name = "Obstacles",
			description = "Choose the color of the obstacles.",
			position = 2,
			section = obstacles
		) default Color getObstaclesColor() { return new Color(255, 150, 0, 40); }

	@ConfigSection(
		name = "Mud pit",
		description = "Mudpit",
		position = 3
	) String mudpit = "mudpit";

		@ConfigItem(
			keyName = "mudpit_widget",
			name = "Show empty warning",
			description = "Show warning if the mud pit is empty and needs to be filled with mushrooms.",
			position = 1,
			section = mudpit
		) default boolean showMudPitWidget() { return true; }

		@Alpha
		@ConfigItem(
			keyName = "mudpit_color",
			name = "Mud pit color",
			description = "Choose the color of the mud pit.",
			position = 2,
			section = mudpit
		) default Color getMudPitColor() { return new Color(255, 0, 0, 40); }
}
