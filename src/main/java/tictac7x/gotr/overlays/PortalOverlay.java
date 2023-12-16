package tictac7x.gotr.overlays;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import tictac7x.gotr.Portal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

public class PortalOverlay extends Overlay {
    private final int PORTAL_ID = 43729;

    private final Client client;
    private final Portal portal;

    private Optional<GameObject> portalGameObject = Optional.empty();

    public PortalOverlay(final Client client, final Portal portal) {
        this.client = client;
        this.portal = portal;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.UNDER_WIDGETS);
    }

    public void onGameObjectSpawned(final GameObject gameObject) {
        if (gameObject.getId() == PORTAL_ID) {
            portalGameObject = Optional.of(gameObject);
            client.setHintArrow(gameObject.getLocalLocation());
        }
    }

    public void onGameObjectDespawned(final GameObject gameObject) {
        if (gameObject.getId() == PORTAL_ID) {
            portalGameObject = Optional.empty();
            client.clearHintArrow();
        }
    }

    public void onGameStateChanged(final GameState state) {
        if (state == GameState.LOADING) {
            portalGameObject = Optional.empty();
            client.clearHintArrow();
        }
    }

    @Override
    public Dimension render(final Graphics2D graphics) {
        if (!portalGameObject.isPresent() || !portal.getTimeLeft().isPresent()) return null;

        final long seconds = Duration.between(Instant.now(), portal.getTimeLeft().get()).getSeconds();
        if (seconds < 0) return null;

        final long milliseconds = Duration.between(Instant.now(), portal.getTimeLeft().get()).getNano() / 1_000_000 % 1000 / 100;
        final String time = seconds + "." + milliseconds;
        final Point location =  Perspective.getCanvasTextLocation(client, graphics, portalGameObject.get().getLocalLocation(), time, 120);

        try {
            OverlayUtil.renderTextLocation(graphics, location, time, Color.WHITE);
        } catch (final Exception ignored) {}
        return null;
    }
}
