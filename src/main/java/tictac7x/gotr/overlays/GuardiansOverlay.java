package tictac7x.gotr.overlays;

import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.ObjectID;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;
import tictac7x.gotr.store.Guardians;
import tictac7x.gotr.TicTac7xGotrImprovedConfig;
import tictac7x.gotr.store.Inventory;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Optional;

public class GuardiansOverlay extends Overlay {
    private final ModelOutlineRenderer modelOutlineRenderer;
    private final TicTac7xGotrImprovedConfig config;
    private final Guardians guardians;
    private final Inventory inventory;

    private Optional<GameObject> elementalGuardian = Optional.empty();
    private Optional<GameObject> catalyticGuardian = Optional.empty();

    public GuardiansOverlay(final ModelOutlineRenderer modelOutlineRenderer, final TicTac7xGotrImprovedConfig config, final Guardians guardians, final Inventory inventory) {
        this.modelOutlineRenderer = modelOutlineRenderer;
        this.config = config;
        this.guardians = guardians;
        this.inventory = inventory;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.UNDER_WIDGETS);
    }

    public void onGameObjectSpawned(final GameObject object) {
        switch (object.getId()) {
            case ObjectID.ESSENCE_PILE_ELEMENTAL:
                elementalGuardian = Optional.of(object);
                break;
            case ObjectID.ESSENCE_PILE_CATALYTIC:
                catalyticGuardian = Optional.of(object);
        }
    }

    public void onGameObjectDespawned(final GameObject object) {
        switch (object.getId()) {
            case ObjectID.ESSENCE_PILE_ELEMENTAL:
                elementalGuardian = Optional.empty();
                break;
            case ObjectID.ESSENCE_PILE_CATALYTIC:
                catalyticGuardian = Optional.empty();
        }
    }

    public void onGameStateChanged(final GameState state) {
        if (state == GameState.LOADING) {
            elementalGuardian = Optional.empty();
            catalyticGuardian = Optional.empty();
        }
    }

    @Override
    public Dimension render(final Graphics2D graphics) {
        // Guardians highlighting disabled.
        if (!config.highlightGuardians()) return null;

        // No game objects.
        if (!elementalGuardian.isPresent() || !catalyticGuardian.isPresent()) return null;

        // No guardians available to build.
        if (!guardians.canBuildGuardians()) return null;

        // No cell.
        if (!config.highlightGuardiansWithoutCells() && !inventory.hasCell()) return null;

        try {
            modelOutlineRenderer.drawOutline(elementalGuardian.get(), 2, config.getElementalColor(), 2);
        } catch (final Exception ignored) {}

        try {
            modelOutlineRenderer.drawOutline(catalyticGuardian.get(), 2, config.getCatalyticColor(), 2);
        } catch (final Exception ignored) {}

        return null;
    }
}
