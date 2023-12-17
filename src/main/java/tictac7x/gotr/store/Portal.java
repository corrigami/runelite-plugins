package tictac7x.gotr.store;

import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.widgets.Widget;
import tictac7x.gotr.Notifications;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Portal {
    private final int PORTAL_TIME_WIDGET_GROUP = 746;
    private final int PORTAL_TIME_WIDGET_CHILD = 28;
    private final int FIRST_PORTAL_GAMETICKS = 268;
    private final int NEXT_PORTAL_GAMETICKS = 218;
    private final int GAMETICK_DURATION = 600;

    private final String regexGameStarted = "The rift becomes active!";
    private final Pattern regexPortalOpened = Pattern.compile(".* - 0:(?<time>.*)");

    private final Client client;
    private final Notifications notifications;

    private Optional<Instant> portalTimeLeft = Optional.empty();
    private Optional<Instant> timeToPortal = Optional.empty();

    public Portal(final Client client, final Notifications notifications) {
        this.client = client;
        this.notifications = notifications;
    }

    public Optional<Instant> getTimeLeft() {
        return portalTimeLeft;
    }

    public Optional<Instant> getTimeToPortal() {
        return timeToPortal;
    }

    public void onGameTick() {
        // Portal time ran out.
        if (portalTimeLeft.isPresent() && Duration.between(Instant.now(), portalTimeLeft.get()).getSeconds() < -10) {
            portalTimeLeft = Optional.empty();
            return;
        }

        // Portal already active.
        if (portalTimeLeft.isPresent()) return;

        // Portal widget not visible.
        final Optional<Widget> widget = Optional.ofNullable(client.getWidget(PORTAL_TIME_WIDGET_GROUP, PORTAL_TIME_WIDGET_CHILD));
        if (!widget.isPresent() || widget.get().isHidden()) return;

        // Portal opened.
        final Matcher matcher = regexPortalOpened.matcher(widget.get().getText());
        if (!matcher.find()) return;

        // Notify about portal.
        notifications.notifyAboutPortal(widget.get().getText());

        // Time until next portal.
        timeToPortal = Optional.of(Instant.now().plusMillis(NEXT_PORTAL_GAMETICKS * GAMETICK_DURATION));

        // Calculate portal time left.
        final double seconds = Double.parseDouble(matcher.group("time"));
        portalTimeLeft = Optional.of(Instant.now().plusMillis((long) (Math.ceil(seconds * 1000 / GAMETICK_DURATION) * GAMETICK_DURATION)));
    }

    public void onChatMessage(final ChatMessage message) {
        if (message.getMessage().equals(regexGameStarted)) {
            timeToPortal = Optional.of(Instant.now().plusMillis(FIRST_PORTAL_GAMETICKS * GAMETICK_DURATION));
        }
    }
}
