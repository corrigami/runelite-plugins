package tictac7x.gotr.overlays;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;
import tictac7x.gotr.store.Barrier;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

public class BarrierOverlay extends Overlay {
    private final int BARRIER_ACTIVE = 43700;

    private final Client client;
    private final ModelOutlineRenderer modelOutlineRenderer;
    private final Barrier barrier;

    private Optional<GameObject> barrierGameObject = Optional.empty();

    public BarrierOverlay(final Client client, final ModelOutlineRenderer modelOutlineRenderer, final Barrier barrier) {
        this.client = client;
        this.modelOutlineRenderer = modelOutlineRenderer;
        this.barrier = barrier;
    }

    public void onGameObjectSpawned(final GameObject gameObject) {
        if (gameObject.getId() == BARRIER_ACTIVE) {
            barrierGameObject = Optional.of(gameObject);
        }
    }

    public void onGameObjectDespawned(final GameObject gameObject) {
        if (gameObject.getId() == BARRIER_ACTIVE) {
            barrierGameObject = Optional.empty();
        }
    }

    public void onGameStateChanged(final GameState gameState) {
        if (gameState == GameState.LOADING) {
            barrierGameObject = Optional.empty();
        }
    }

    @Override
    public Dimension render(final Graphics2D graphics) {
        if (!barrier.getBarrierReenterTimeLeft().isPresent()) return null;
        if (!barrierGameObject.isPresent()) return null;
        if (!isBehindBarrier()) return null;

        final long seconds = Duration.between(Instant.now(), barrier.getBarrierReenterTimeLeft().get()).getSeconds();

        if (seconds >= 0) {
            final long milliseconds = Duration.between(Instant.now(), barrier.getBarrierReenterTimeLeft().get()).getNano() / 1_000_000 % 1000 / 100;
            final String time = seconds + "." + milliseconds;
            final Point location =  Perspective.getCanvasTextLocation(client, graphics, barrierGameObject.get().getLocalLocation(), time, 600);

            try {
                OverlayUtil.renderTextLocation(graphics, location, time, Color.WHITE);
            } catch (final Exception ignored) {}
        }

        if (seconds < 0) {
            try {
                modelOutlineRenderer.drawOutline(barrierGameObject.get(), 2, Color.green, 2);
            } catch (final Exception ignored) {}
        }

        return null;
    }

    private boolean isBehindBarrier() {
        return client.getLocalPlayer().getWorldLocation().getY() <= 9482;
    }
}
