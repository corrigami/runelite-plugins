package tictac7x.gotr;

import net.runelite.client.config.*;

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

    @Alpha
    @ConfigItem(
        keyName = "color_catalytic",
        name = "Catalytic color",
        description = "Color to highlight catalytic objects",
        position = 1
    ) default Color getCatalyticColor() { return new Color(215, 240, 60); }

    @Alpha
    @ConfigItem(
            keyName = "color_elemental",
            name = "Elemental color",
            description = "Color to highlight elemental objects",
            position = 1
    ) default Color getElementalColor() { return new Color(50, 210, 160); }

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

        @ConfigItem(
            keyName = guardians_current,
            name = guardians_current,
            description = guardians_current,
            section = debug,
            position = 5
        ) default double getCurrentGuardians() { return 0; }

        @ConfigItem(
            keyName = guardians_total,
            name = guardians_total,
            description = guardians_total,
            section = debug,
            position = 6
        ) default double getTotalGuardians() { return 0; }
}
