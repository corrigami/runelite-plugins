package tictac7x.gotr;

import net.runelite.api.events.ChatMessage;
import net.runelite.client.config.ConfigManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Energy {
    private final ConfigManager configManager;

    private final Pattern regexCheck = Pattern.compile("You have (?<catalytic>.+) catalytic energy and (?<elemental>.+) elemental energy. You can use them to search the rift (?<searches>.+) times. You have searched the rift (?<total>.+) times.");

    public Energy(final ConfigManager configManager) {
        this.configManager = configManager;
    }

    public void onChatMessage(final ChatMessage message) {
        final Matcher matcher = regexCheck.matcher(message.getMessage());

        if (matcher.find()) {
            final int catalytic = Integer.parseInt(matcher.group("catalytic"));
            final int elemental = Integer.parseInt(matcher.group("elemental"));

            configManager.setConfiguration(TicTac7xGotrImprovedConfig.group, TicTac7xGotrImprovedConfig.energy_catalytic, catalytic);
            configManager.setConfiguration(TicTac7xGotrImprovedConfig.group, TicTac7xGotrImprovedConfig.energy_elemental, elemental);
        }
    }
}
