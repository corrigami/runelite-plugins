package tictac7x.tithe;

import net.runelite.client.config.*;
import tictac7x.Overlay;
import java.awt.Color;

@ConfigGroup(TitheConfig.group)
public interface TitheConfig extends Config {
	String group = "tictac7x-tithe";

	@ConfigSection(
		position = 1,
		name = "Farming patches",
		description = "Highlight farming patches and show progress of plants"
	) String section_patches = "farming_patches";

		@Alpha
		@ConfigItem(
			position = 1,
			keyName = "plants_dry",
			name = "Dry plants",
			description = "Highlight dry plants that need to be watered.",
			section = section_patches
		) default Color getPlantsDryColor() {
			return Overlay.getColor(Overlay.color_red, Overlay.alpha_vibrant);
		}

		@Alpha
		@ConfigItem(
			position = 2,
			keyName = "plants_watered",
			name = "Watered plants",
			description = "Highlight watered plants",
			section = section_patches
		) default Color getPlantsWateredColor() {
			return Overlay.getColor(Overlay.color_green, Overlay.alpha_vibrant);
		}

		@Alpha
		@ConfigItem(
			position = 3,
			keyName = "plants_grown",
			name = "Grown plants",
			description = "Highlight grown plants",
			section = section_patches
		) default Color getPlantsGrownColor() {
			return Overlay.getColor(Overlay.color_yellow, Overlay.alpha_vibrant);
		}

		@Alpha
		@ConfigItem(
			position = 4,
			keyName = "plants_blighted",
			name = "Blighted plants",
			description = "Highlight blighted plants",
			section = section_patches
		) default Color getPlantsBlightedColor() {
			return Overlay.getColor(Overlay.color_gray, Overlay.alpha_vibrant);
		}

		@Alpha
		@ConfigItem(
			position = 5,
			keyName = "farm_patches_hover",
			name = "Farm patches",
			description = "Highlight farm patches on hover",
			section = section_patches
		) default Color getPatchesHighlightOnHoverColor() {
			return Overlay.getColor(Overlay.color_gray, Overlay.alpha_normal);
		}

	@ConfigSection(
		position = 2,
		name = "Points",
		description = "Show custom information about tithe farm points"
	) String section_points = "points";

		String points = "points";
		@ConfigItem(
			position = 1,
			keyName = points,
			name = "Show custom points widget",
			description = "Show total, earned points and harvested fruits.",
			section = section_points
		) default boolean showCustomPoints() {
			return true;
		}
}
