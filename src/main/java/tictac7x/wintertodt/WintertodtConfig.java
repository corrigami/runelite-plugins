package tictac7x.wintertodt;

import net.runelite.client.config.*;

import java.awt.Color;

import static tictac7x.wintertodt.WintertodtConfig.group;

@ConfigGroup(group)
public interface WintertodtConfig extends Config {
    String group = "tictac7x-wintertodt";
    String version = "version";

    @ConfigItem(
        keyName = version,
        name = "Version",
        description = "Version of the plugin for update message",
        hidden = true
    ) default String getVersion() {
        return "";
    }

    @ConfigSection(
        name = "Notifications",
        description = "Manage notifications",
        position = 1
    ) String notifications = "notifications";

        @ConfigItem(
            keyName = "breaking_braziers",
            name = "Breaking braziers",
            description = "Notify about the brazier that you are standing next to that is about to break",
            position = 1,
            section = notifications
        ) default boolean notifyBreakingBrazier() {
            return true;
        }

        @ConfigItem(
            keyName = "falling_snow",
            name = "Falling snow",
            description = "Notify about the falling snow that is about to fall on you",
            position = 2,
            section = notifications
        ) default boolean notifyFallingSnow() {
            return true;
        }

    @ConfigSection(
        name = "Highlights",
        description = "Manage highlights",
        position = 2
    ) String highlights = "highlights";

        @Alpha
        @ConfigItem(
            keyName = "highlight_breaking_braziers",
            name = "Breaking braziers",
            description = "Highlight breaking braziers",
            position = 1,
            section = highlights
        ) default Color highlightBreakingBrazier() {
            return new Color(255, 0, 0, 80);
        }

        @Alpha
        @ConfigItem(
            keyName = "highlight_falling_snow",
            name = "Falling snow",
            description = "Highlight falling snow tiles",
            position = 2,
            section = highlights
        ) default Color highlightFallingSnow() {
            return new Color(255, 0, 0, 80);
        }

    @ConfigSection(
        name = "Goal panel",
        description = "Panel to see how many bruma logs are required",
        position = 3
    ) String panel = "panel";

        @ConfigItem(
            keyName = "goal_show",
            name = "Show",
            description = "Show goal panel",
            position = 1,
            section = panel
        ) default boolean showGoalPanel() {
            return true;
        }

        @ConfigItem(
            keyName = "goal",
            name = "Goal",
            description = "Amount of points you want to get",
            position = 2,
            section = panel
        ) default int getGoal() {
            return 500;
        }

        @ConfigItem(
            keyName = "goal_fletching",
            name = "Fletching",
            description = "Calculate points based on if you fletch the logs ",
            position = 3,
            section = panel
        ) default boolean isFletchingForGoal() {
            return true;
        }
}
