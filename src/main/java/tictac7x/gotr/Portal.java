package tictac7x.gotr;

import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.widgets.Widget;
import net.runelite.client.Notifier;

import java.awt.Color;
import java.time.Instant;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Portal {
    private final String game_started = "The rift becomes active!";
    private final String game_ended = "The Portal Guardians will keep their rifts open";
    private final String portal_used = "You step through the portal and find yourself in another part of the temple.";
    private final Pattern portal_opened = Pattern.compile("A portal to the huge guardian fragment mine has opened (?<location>.*)!");

    private final Client client;
    private final Notifier notifier;

    private Optional<GameObject> portal = Optional.empty();
    private Optional<Instant> time_to_portal = Optional.empty();
    private Optional<Instant> portal_time_left = Optional.empty();

    private boolean first_portal;
    private boolean portal_active;

    public Portal(final Client client, final Notifier notifier) {
        this.client = client;
        this.notifier = notifier;
    }

    public Optional<Instant> getTimeToPortal() {
        return time_to_portal;
    }

    public Optional<Instant> getPortalTimeLeft() {
        return portal_time_left;
    }

    public Optional<GameObject> getPortal() {
        return portal;
    }

    public Color getTimeToPortalColor(final long seconds) {
        if (first_portal) {
            return seconds >= 160 ? Color.RED : seconds >= 140 ? Color.ORANGE : Color.WHITE;
        } else {
            return seconds >= 100 ? Color.RED : seconds >= 80 ? Color.ORANGE : Color.WHITE;
        }
    }

    public void onGameObjectSpawned(final GameObject object) {
        if (object.getId() == 43729) {
            portal = Optional.of(object);
            client.setHintArrow(object.getWorldLocation());
        }
    }

    public void onGameObjectDespawned(final GameObject object) {
        if (object.getId() == 43729) {
            portal = Optional.empty();
            client.clearHintArrow();
        }
    }

    public void onGameStateChanged(final GameState state) {
        if (state == GameState.LOADING) {
            portal = Optional.empty();
            client.clearHintArrow();
        }
    }

    public void onChatMessage(final ChatMessage message) {
        if (message.getType() != ChatMessageType.GAMEMESSAGE) return;

        final Matcher matcher = portal_opened.matcher(message.getMessage());

        if (message.getMessage().equals(game_started)) {
            first_portal = true;
            time_to_portal = Optional.of(Instant.now());
            notifier.notify("The Guardians of the Rift minigame has started!");
        } else if (message.getMessage().equals(portal_used)) {
            client.clearHintArrow();
        } else if (matcher.find()) {
            portal_time_left = Optional.of(Instant.now().plusMillis(44 * 600));
            notifier.notify("Portal opened " + matcher.group("location") + "!");
        }
    }

    public void onGameTick() {
        final Widget widget_gotr = client.getWidget(746, 2);
        if (widget_gotr == null || widget_gotr.isHidden()) return;

        final Widget widget_portal = client.getWidget(746, 26);

        if (portal_active && (widget_portal == null || widget_portal.isHidden())) {
            portal_active = false;
            time_to_portal = Optional.of(Instant.now());
        } else if (!portal_active && widget_portal != null && !widget_portal.isHidden()) {
            portal_active = true;
            first_portal = false;
        }
    }
}
