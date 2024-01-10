package tictac7x.gotr.store;

import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.widgets.Widget;
import tictac7x.gotr.Notifications;
import tictac7x.gotr.TicTac7xGotrImprovedConfig;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Creatures {
    private final Client client;
    private final Notifications notifications;
    private final TicTac7xGotrImprovedConfig config;

    private final int TIME_WIDGET_GROUP = 746;
    private final int TIME_WIDGET_CHILD = 5;
    private final Pattern TIME_WIDGET_PATTERN = Pattern.compile("(?<minutes>.+):(?<seconds>.+)");

    private boolean notifiedBeforeFirstRift = false;

    public Creatures(final Client client, final Notifications notifications, final TicTac7xGotrImprovedConfig config) {
        this.client = client;
        this.notifications = notifications;
        this.config = config;
    }

    public void onChatMessage(final ChatMessage chatMessage) {
        if (chatMessage.getMessage().equals("Creatures from the Abyss will attack in 120 seconds.")) {
            notifiedBeforeFirstRift = false;
        }
    }

    public void onGameTick() {
        checkToNotifyBeforeFirstRift();
    }

    private void checkToNotifyBeforeFirstRift() {
        if (config.notifyBeforeFirstRift() == -1 || notifiedBeforeFirstRift) return;

        final Optional<Widget> timeWidget = Optional.ofNullable(client.getWidget(TIME_WIDGET_GROUP, TIME_WIDGET_CHILD));
        if (!timeWidget.isPresent()) return;

        final Matcher matcher = TIME_WIDGET_PATTERN.matcher(timeWidget.get().getText());
        if (!matcher.find()) return;

        final int seconds = Integer.parseInt(matcher.group("minutes")) * 60 + Integer.parseInt(matcher.group("seconds"));
        if (seconds <= config.notifyBeforeFirstRift()) {
            notifications.notifyBeforeFirstRift(seconds);
            notifiedBeforeFirstRift = true;
        }
    }
}
