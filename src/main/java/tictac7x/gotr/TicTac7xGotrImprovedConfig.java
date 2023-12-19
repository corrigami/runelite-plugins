package tictac7x.gotr;

import net.runelite.client.config.*;
import tictac7x.gotr.types.BeforeGameStarts;

import java.awt.Color;

import static tictac7x.gotr.TicTac7xGotrImprovedConfig.group;

@ConfigGroup(group)
public interface TicTac7xGotrImprovedConfig extends Config {
    String group = "tictac7x-gotr";

    String version = "version";
    String energy_catalytic = "energy_catalytic";
    String energy_elemental = "energy_elemental";
    String guardians_current = "guardians_current";
    String guardians_total = "guardians_total";

    @ConfigSection(
        name = "Colors",
        description = "Change colors of various items",
        position = 1
    ) String colors = "colors";

        @Alpha
        @ConfigItem(
            keyName = "color_elemental",
            name = "Elemental color",
            description = "Color to highlight elemental objects",
            position = 1,
            section = colors
        ) default Color getElementalColor() {
            return new Color(50, 210, 160);
        }

        @Alpha
        @ConfigItem(
            keyName = "color_catalytic",
            name = "Catalytic color",
            description = "Color to highlight catalytic objects",
            position = 2,
            section = colors
        ) default Color getCatalyticColor() {
            return new Color(215, 240, 60);
        }

        @ConfigItem(
            keyName = "color_widget_energies",
            name = "Colorful energy points",
            description = "If enabled shows energy points in respective colors",
            position = 3,
            section = colors
        ) default boolean showWidgetEnergyPointColors() {
            return true;
        }

        @ConfigItem(
            keyName = "color_widget_portal",
            name = "Colorful portal countdown",
            description = "If enabled shows portal countdown in different colors based on time left",
            position = 4,
            section = colors
        ) default boolean showWidgetPortalCountdownColors() {
            return true;
        }

    @ConfigSection(
        name = "Teleporters",
        description = "Teleporters",
        position = 2
    ) String teleporters = "teleporters";

        @ConfigItem(
            keyName = "highlight_teleporters",
            name = "Highlight teleporters",
            description = "Highlight active teleporters",
            position = 1,
            section = teleporters
        ) default boolean highlightActiveTeleporters() {
            return true;
        }

        @ConfigItem(
            keyName = "teleporters_level_requirements",
            name = "Check level requirements",
            description = "Check teleporter level requirement for highlighting",
            position = 2,
            section = teleporters
        ) default boolean checkTeleporterLevelRequirements() {
        return true;
    }

        @ConfigItem(
            keyName = "unusable_teleporters",
            name = "Indicate unusable teleporters",
            description = "Indicate that teleporters are unusable, because player has guardian stones in their inventory",
            position = 3,
            section = teleporters
        ) default boolean indicateUnusableTeleporters() {
            return true;
        }

    @ConfigSection(
        name = "Guardians",
        description = "Guardians",
        position = 3
    ) String guardians = "guardians";

        @ConfigItem(
            keyName = "highlight_guardians",
            name = "Highlight guardians",
            description = "Highlight guardians if it's possible to build them",
            position = 1,
            section = guardians
        ) default boolean highlightGuardians() {
            return true;
        }

        @ConfigItem(
            keyName = "highlight_guardians_without_cells",
            name = "Highlight guardians without cells",
            description = "Highlight guardians even if player has no cells in their inventory",
            position = 2,
            section = guardians
        ) default boolean highlightGuardiansWithoutCells() {
            return false;
        }

    @ConfigSection(
        name = "Great Guardian",
        description = "Great Guardian",
        position = 4
    ) String greatGuardian = "great_guardian";

        @ConfigItem(
            keyName = "highlight_great_guardian",
            name = "Highlight great guardian",
            description = "Highlight great guardian if you have guardian stones in your inventory",
            position = 1,
            section = greatGuardian
        ) default boolean highlightGreatGuardian() {
            return true;
        }

    @ConfigSection(
        name = "Uncharged Cells",
        description = "Uncharged Cells",
        position = 5
    ) String unchargedCells = "unchargedCells";

        @ConfigItem(
            keyName = "highlightUnchargedCellsBench",
            name = "Highlight Uncharged Cells Bench",
            description = "Highlight uncharged cells bench if player has no uncharged cells in their inventory",
            position = 1,
            section = unchargedCells
        ) default boolean highlightUnchargedCellsBench() {
        return true;
    }

    @ConfigSection(
        name = "Barrier",
        description = "Barrier",
        position = 6
    ) String barrier = "barrier";

        @ConfigItem(
            keyName = "barrierLeavePreventer",
            name = "Prevent leave",
            description = "Rename quick-pass option to make sure you can't left click it after passing",
            position = 1,
            section = barrier
        ) default boolean preventQuickLeave() {
        return true;
    }


        @ConfigItem(
            keyName = "barrierTimeRemaining",
            name = "Remaining Time",
            description = "Show remaining time until the barrier is passable",
            position = 2,
            section = barrier
        ) default boolean showBarrierRemainingTime() {
        return true;
    }

        @ConfigItem(
            keyName = "barrierPie",
            name = "Remaining Time Pie",
            description = "Show remaining time as pie",
            position = 3,
            section = barrier
        ) default boolean showBarrierRemainingTimePie() {
        return true;
    }

    @ConfigSection(
        name = "Notifications",
        description = "Manage notifications",
        position = 7
    ) String notifications = "notifications";

        @ConfigItem(
            keyName = "notification_before_game_starts",
            name = "Before game starts",
            description = "Notify before the game starts ",
            position = 1,
            section = notifications
        ) default BeforeGameStarts notifyBeforeGameStarts() {
        return BeforeGameStarts.NEVER;
    }

        @ConfigItem(
            keyName = "notification_game_started",
            name = "Game started",
            description = "Notify that the game has started",
            position = 2,
            section = notifications
        ) default boolean notifyGameStarted() {
        return true;
    }

        @ConfigItem(
            keyName = "notification_portal_opened",
            name = "Portal opened",
            description = "Notify about opened portal to huge guardian remains",
            position = 3,
            section = notifications
        ) default boolean notifyPortalOpened() {
        return true;
    }

        @ConfigItem(
            keyName = "barrier_passable",
            name = "Passable barrier",
            description = "Notify about barrier being passable",
            position = 4,
            section = notifications
        ) default boolean notifyBarrierPassable() {
        return true;
    }

        @ConfigItem(
            keyName = "before_barrier_passable",
            name = "Before passable barrier",
            description = "Notify about barrier being passable",
            position = 5,
            section = notifications
        ) default boolean notifyBeforePassableBarrier() {
        return true;
    }

        @ConfigItem(
            keyName = "notify_eye_robes",
            name = "Unused eye robes",
            description = "Notify about unused eye robes when you are near altar with essence in inventory",
            position = 6,
            section = notifications
        ) default boolean notifyUnusedEyeRobes() {
        return true;
    }

    @ConfigSection(
        name = "Debug",
        description = "Values of charges for all items under the hood",
        position = 99,
        closedByDefault = true
    ) String debug = "debug";

        @ConfigItem(
            keyName = version,
            name = version,
            description = version,
            section = debug,
            position = 1
        ) default String getVersion() { return "v0.1"; }

        @ConfigItem(
            keyName = energy_catalytic,
            name = energy_catalytic,
            description = energy_catalytic,
            section = debug,
            position = 2
        ) default int getCatalyticEnergy() { return 0; }

        @ConfigItem(
            keyName = energy_elemental,
            name = energy_elemental,
            description = energy_elemental,
            section = debug,
            position = 3
        ) default int getElementalEnergy() { return 0; }
}
