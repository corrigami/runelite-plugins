package tictac7x.gotr.store;

import net.runelite.api.events.ChatMessage;
import net.runelite.client.config.ConfigManager;
import tictac7x.gotr.TicTac7xGotrImprovedConfig;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Energy {
    private final ConfigManager configManager;
    private final TicTac7xGotrImprovedConfig config;

    private final Pattern regexLoot = Pattern.compile("You found some loot: .*");
    private final Pattern regexCheck = Pattern.compile("You have (?<catalytic>.+) catalytic energy and (?<elemental>.+) elemental energy. You can use them to search the rift (?<searches>.+) times. You have searched the rift (?<total>.+) times.");
    private final Pattern regexTotal = Pattern.compile("Total elemental energy: (?<elemental>.+). Total catalytic energy: ( )?(?<catalytic>.+).");

    public Energy(final ConfigManager configManager, final TicTac7xGotrImprovedConfig config) {
        this.configManager = configManager;
        this.config = config;
    }

    public void onChatMessage(final ChatMessage message) {
        final Matcher matcherCheck = regexCheck.matcher(message.getMessage());
        final Matcher matcherTotal = regexTotal.matcher(message.getMessage());
        final Matcher matcherLoot = regexLoot.matcher(message.getMessage());

        // Check manually.
        if (matcherCheck.find()) {
            final int catalytic = Integer.parseInt(matcherCheck.group("catalytic"));
            final int elemental = Integer.parseInt(matcherCheck.group("elemental"));

            configManager.setConfiguration(TicTac7xGotrImprovedConfig.group, TicTac7xGotrImprovedConfig.energy_catalytic, catalytic);
            configManager.setConfiguration(TicTac7xGotrImprovedConfig.group, TicTac7xGotrImprovedConfig.energy_elemental, elemental);

        // Game finished total message.
        } else if (matcherTotal.find()) {
            final int catalytic = Integer.parseInt(matcherTotal.group("catalytic"));
            final int elemental = Integer.parseInt(matcherTotal.group("elemental"));

            configManager.setConfiguration(TicTac7xGotrImprovedConfig.group, TicTac7xGotrImprovedConfig.energy_catalytic, catalytic);
            configManager.setConfiguration(TicTac7xGotrImprovedConfig.group, TicTac7xGotrImprovedConfig.energy_elemental, elemental);

        // Loot.
        } else if (matcherLoot.find()) {
            configManager.setConfiguration(TicTac7xGotrImprovedConfig.group, TicTac7xGotrImprovedConfig.energy_elemental, config.getElementalEnergy() - 1);
            configManager.setConfiguration(TicTac7xGotrImprovedConfig.group, TicTac7xGotrImprovedConfig.energy_catalytic, config.getCatalyticEnergy() - 1);
        }
    }
}
