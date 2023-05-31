package tictac7x.tithe;

import net.runelite.api.TileObject;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.client.ui.overlay.OverlayPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.AnimationID;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ProgressPieComponent;

public class TitheOverlayPlants extends OverlayPanel {
    private final TithePlugin plugin;
    private final TitheConfig config;

    public final Map<LocalPoint, TithePlant> plants = new HashMap<>();

    public TitheOverlayPlants(final TithePlugin plugin, final TitheConfig config) {
        this.plugin = plugin;
        this.config = config;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.UNDER_WIDGETS);
    }

    /**
     * Update plant state to watered based on the game object.
     * @param event - Tithe plant spawned event.
     */
    public void onGameObjectSpawned(final GameObjectSpawned event) {
        if (!plugin.inTitheFarm()) return;

        final GameObject game_object = event.getGameObject();
        if (!TithePlant.isPatch(game_object)) return;

        final LocalPoint location_patch = game_object.getLocalLocation();

        // Empty patch, plant completed.
        if (game_object.getId() == TithePlant.TITHE_EMPTY_PATCH) {
            this.plants.remove(location_patch);

        // Update plant state.
        } else if (this.plants.containsKey(location_patch)) {
            this.plants.get(location_patch).setGameObject(game_object);
        }

        // GameObject is seedling.
        if (TithePlant.isSeedling(game_object)) {
            this.plants.put(location_patch, new TithePlant(config, game_object));
        }
    }

    public void onGameTick() {
        // Update plants progress.
        for (final TithePlant plant : this.plants.values()) {
            plant.onGameTick();
        }
    }

    @Override
    public Dimension render(final Graphics2D graphics) {
        if (!plugin.inTitheFarm()) return null;

        for (final TithePlant plant : this.plants.values()) {
            renderPie(graphics, plant.getGameObject(), plant.getCycleColor(), plant.getCycleProgress());
        }

        return null;
    }

    private void renderPie(final Graphics2D graphics, final TileObject object, final Color color, final float progress) {
        if (color == null || color.getAlpha() == 0) return;

        try {
            final ProgressPieComponent pie = new ProgressPieComponent();
            pie.setPosition(object.getCanvasLocation(0));
            pie.setProgress(-progress);
            pie.setBorderColor(color.darker());
            pie.setFill(color);
            pie.render(graphics);
        } catch (final Exception ignored) {}
    }
}
