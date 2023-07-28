package tictac7x.gotr;

import net.runelite.api.ChatMessageType;
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
    private final Pattern regex_time = Pattern.compile("(?<location>.*) - 0:(?<time>.*)");
    private final Pattern regex_portal_spawned = Pattern.compile("A portal to the huge guardian fragment mine has (?<location>.*)");
    private final int PORTAL_ID = 43729;
    private final int PORTAL_TIME_WIDGET_GROUP = 746;
    private final int PORTAL_TIME_WIDGET_CHILD = 28;
    private final int PORTAL_ACTIVE_IN_GAME_GAMETICKS = 44;
    private final int GAMETICK_DURATION = 600;

    private final Client client;
    private final Notifier notifier;

    private Optional<GameObject> portal = Optional.empty();
    private Optional<Instant> time_left = Optional.empty();

    public Portal(final Client client, final Notifier notifier) {
        this.client = client;
        this.notifier = notifier;
    }

    public Optional<Instant> getTimeLeft() {
        return time_left;
    }

    public Optional<GameObject> getPortal() {
        return portal;
    }

    public void onGameObjectSpawned(final GameObject object) {
        if (object.getId() == PORTAL_ID) {
            portal = Optional.of(object);
        }
    }

    public void onGameObjectDespawned(final GameObject object) {
        if (object.getId() == PORTAL_ID) {
            portal = Optional.empty();
        }
    }

    public void onGameStateChanged(final GameState state) {
        if (state == GameState.LOADING) {
            portal = Optional.empty();
        }
    }

    public void onGameTick() {
        final Widget widget = client.getWidget(PORTAL_TIME_WIDGET_GROUP, PORTAL_TIME_WIDGET_CHILD);
        if (widget != null && !widget.isHidden() && !time_left.isPresent()) {
            final Matcher matcher = regex_time.matcher(widget.getText());

            if (matcher.find()) {
                final String location = matcher.group("location");
                final double seconds = Integer.parseInt(matcher.group("time"));
                time_left = Optional.of(Instant.now().plusMillis((long) (Math.ceil(seconds * 1000 / GAMETICK_DURATION) * GAMETICK_DURATION)));
                notifier.notify("Portal has opened " + (
                    location.equals("S") ? "south" :
                    location.equals("SW") ? "south west" :
                    location.equals("SE") ? "south east" :
                    location.equals("E") ? "east" :
                    location.equals("W") ? "west" :
                "") + "!");
            }
        }

        // Portal time has run out.
        if (time_left.isPresent() && Duration.between(Instant.now(), time_left.get()).getSeconds() < -10) {
            time_left = Optional.empty();
        }
    }

    public void onChatMessage(final ChatMessage message) {
//        if (message.getType() != ChatMessageType.GAMEMESSAGE) return;
//
//        final Matcher matcher = regex_portal_spawned.matcher(message.getMessage());
//        if (matcher.find()) {
//            time_left = Optional.of(Instant.now().plusMillis(PORTAL_ACTIVE_IN_GAME_GAMETICKS * GAMETICK_DURATION));
//            notifier.notify("Portal " + matcher.group("location"));
//        }
    }
}
