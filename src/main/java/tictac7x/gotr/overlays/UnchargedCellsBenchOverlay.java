package tictac7x.gotr.overlays;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;
import tictac7x.gotr.TicTac7xGotrImprovedConfig;
import tictac7x.gotr.store.Inventory;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Optional;

public class UnchargedCellsBenchOverlay extends Overlay {
    private final int UNCHARGED_CELLS_BENCH_ID = 43732;

    private final Client client;
    private final ModelOutlineRenderer modelOutlineRenderer;
    private final TicTac7xGotrImprovedConfig config;
    private final Inventory inventory;

    private Optional<GameObject> unchargedCellsBench = Optional.empty();

    public UnchargedCellsBenchOverlay(final Client client, final ModelOutlineRenderer modelOutlineRenderer, final TicTac7xGotrImprovedConfig config, final Inventory inventory) {
        this.client = client;
        this.modelOutlineRenderer = modelOutlineRenderer;
        this.config = config;
        this.inventory = inventory;
    }

    public void onGameObjectSpawned(final GameObject gameObject) {
        if (gameObject.getId() == UNCHARGED_CELLS_BENCH_ID) {
            unchargedCellsBench = Optional.of(gameObject);
        }
    }

    public void onGameObjectDespawned(final GameObject gameObject) {
        if (gameObject.getId() == UNCHARGED_CELLS_BENCH_ID) {
            unchargedCellsBench = Optional.empty();
        }
    }

    public void onGameStateChanged(final GameState gameState) {
        if (gameState == GameState.LOADING) {
            unchargedCellsBench = Optional.empty();
        }
    }

    @Override
    public Dimension render(final Graphics2D graphics) {
        if (isBehindBarrier() || !config.highlightUnchargedCellsBench() || inventory.hasUnchargedCells() || !unchargedCellsBench.isPresent()) return null;

        try {
            modelOutlineRenderer.drawOutline(unchargedCellsBench.get(), 2, Color.red, 2);
        } catch (final Exception ignored) {}

        return null;
    }

    private boolean isBehindBarrier() {
        return client.getLocalPlayer().getWorldLocation().getY() <= 9482;
    }
}
