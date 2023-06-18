package tictac7x.gotr;

import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.Notifier;

import java.awt.Color;
import java.time.Instant;
import java.util.Optional;

public class Portal {
    private final String game_started = "The rift becomes active!";
    private final String portal_opened = "A portal to the huge guardian fragment mine has opened";
    private final String game_ended = "The Portal Guardians will keep their rifts open";
    private final String portal_used = "You step through the portal and find yourself in another part of the temple.";

    private final Client client;
    private final Notifier notifier;

    private Optional<GameObject> portal = Optional.empty();
    private Optional<Instant> time_to_portal = Optional.empty();
    private Optional<Instant> portal_time_left = Optional.empty();

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

    public Color getTimeToPortalColor(final long seconds, final boolean panel) {
        return seconds <= 10 ? Color.RED : seconds <= 30 ? Color.ORANGE : panel ? Color.GREEN : Color.WHITE;
    }

    public void onGameObjectSpawned(final GameObject object) {
        if (object.getId() == 43729) {
            portal = Optional.of(object);
            portal_time_left = Optional.of(Instant.now().plusMillis(44 * 600));
            client.setHintArrow(object.getWorldLocation());
            notifier.notify("Portal has opened!");
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

        if (message.getMessage().equals(game_started)) {
            time_to_portal = Optional.of(Instant.now().plusSeconds(161));
        } else if (message.getMessage().contains(portal_opened)) {
            time_to_portal = Optional.of(Instant.now().plusSeconds(131));
        } else if (message.getMessage().contains(game_ended)) {
            time_to_portal = Optional.empty();
        } else if (message.getMessage().equals(portal_used)) {
            client.clearHintArrow();
        }
    }
}
