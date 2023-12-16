package tictac7x.gotr;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.widgets.Widget;
import net.runelite.client.Notifier;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Portal {
    private final Pattern regexPortalOpened = Pattern.compile(".* - 0:(?<time>.*)");
    private final int PORTAL_TIME_WIDGET_GROUP = 746;
    private final int PORTAL_TIME_WIDGET_CHILD = 28;
    private final int GAMETICK_DURATION = 600;

    private final Client client;
    private final Notifications notifications;

    private Optional<Instant> portalTimeLeft = Optional.empty();

    public Portal(final Client client, final Notifications notifications) {
        this.client = client;
        this.notifications = notifications;
    }

    public Optional<Instant> getTimeLeft() {
        return portalTimeLeft;
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

        // Calculate portal time left.
        final double seconds = Double.parseDouble(matcher.group("time"));
        portalTimeLeft = Optional.of(Instant.now().plusMillis((long) (Math.ceil(seconds * 1000 / GAMETICK_DURATION) * GAMETICK_DURATION)));
    }
}
