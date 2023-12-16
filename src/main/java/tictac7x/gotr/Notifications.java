package tictac7x.gotr;

import net.runelite.api.events.ChatMessage;
import net.runelite.client.Notifier;
import tictac7x.gotr.store.GameStartBefore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Notifications {
    private final Notifier notifier;
    private TicTac7xGotrImprovedConfig config;

    private final String regexGameStartsIn30Seconds = "The rift will become active in 30 seconds.";
    private final String regexGameStartsIn10Seconds = "The rift will become active in 10 seconds.";
    private final String regexGameStartsIn5Seconds = "The rift will become active in 5 seconds.";
    private final String regexGameStarted = "The rift becomes active!";
    private final Pattern regexPortalOpened = Pattern.compile("(?<location>.*) - .*");

    public Notifications(final Notifier notifier, final TicTac7xGotrImprovedConfig config) {
        this.notifier = notifier;
        this.config = config;
    }

    public void onChatMessage(final ChatMessage message) {
        if (message.getMessage().equals(regexGameStarted)) {
            if (config.notifyGameStarted()) {
                notifier.notify(regexGameStarted.replace("!", ""));
            }
        } else if (message.getMessage().equals(regexGameStartsIn30Seconds)) {
            if (config.notifyBeforeGameStarts() == GameStartBefore.THIRTY) {
                notifier.notify(regexGameStartsIn30Seconds.replace(".", ""));
            }
        } else if (message.getMessage().equals(regexGameStartsIn10Seconds)) {
            if (config.notifyBeforeGameStarts() == GameStartBefore.TEN) {
                notifier.notify(regexGameStartsIn10Seconds.replace(".", ""));
            }
        } else if (message.getMessage().equals(regexGameStartsIn5Seconds)) {
            if (config.notifyBeforeGameStarts() == GameStartBefore.FIVE) {
                notifier.notify(regexGameStartsIn5Seconds.replace(".", ""));
            }
        }
    }

    public void notifyAboutPortal(final String openedPortal) {
        if (!config.notifyPortalOpened()) return;

        final Matcher matcher = regexPortalOpened.matcher(openedPortal);
        if (!matcher.find()) return;

        final String location = matcher.group("location");

        notifier.notify("Portal opened to the " + (
            location.equals("S") ? "south" :
            location.equals("SW") ? "south west" :
            location.equals("SE") ? "south east" :
            location.equals("E") ? "east" :
            location.equals("W") ? "west" :
            "") + "!"
        );
    }
}
