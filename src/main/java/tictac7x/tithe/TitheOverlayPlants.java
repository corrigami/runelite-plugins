package tictac7x.tithe;

import tictac7x.Overlay;
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

public class TitheOverlayPlants extends Overlay {
    private final Client client;
    private final TithePlugin plugin;
    private final TitheConfig config;

    public final Map<LocalPoint, TithePlant> plants = new HashMap<>();
    private WorldPoint location_player_planting_seed;

    public TitheOverlayPlants(final TithePlugin plugin, final TitheConfig config, final Client client) {
        this.plugin = plugin;
        this.config = config;
        this.client = client;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.UNDER_WIDGETS);
    }

    /**
     * Update plant state to watered based on the game object.
     * @param game_object - Tithe plant.
     */
    public void onGameObjectSpawned(final GameObject game_object) {
        if (!plugin.inTitheFarm()) return;

        if (TithePlant.isPatch(game_object)) {
            final LocalPoint location_patch = game_object.getLocalLocation();

            // Empty patch, plant completed.
            if (game_object.getId() == TithePlant.TITHE_EMPTY_PATCH) {
                this.plants.remove(location_patch);

            // Update plant state.
            } else if (this.plants.containsKey(location_patch)) {
                this.plants.get(location_patch).setGameObject(game_object);
            }

            // GameObject is seedling and player is next to the seedling.
            if (TithePlant.isSeedling(game_object) && isPlayerNextToSeedling(game_object, location_player_planting_seed)) {
                this.plants.put(location_patch, new TithePlant(config, game_object));
            }
        }
    }

    public void onGameTick() {
        if (!plugin.inTitheFarm()) return;

        // Save local point where player did seed planting animation.
        if (client.getLocalPlayer() != null && client.getLocalPlayer().getAnimation() == AnimationID.FARMING_PLANT_SEED) {
            this.location_player_planting_seed = client.getLocalPlayer().getWorldLocation();
        }

        // Update plants progress.
        for (final TithePlant plant : this.plants.values()) {
            plant.onGameTick();
        }
    }

    @Override
    public Dimension render(final Graphics2D graphics) {
        if (!plugin.inTitheFarm()) return null;

        for (final TithePlant plant : this.plants.values()) {
            plant.render(graphics);
        }

        return null;
    }

    private boolean isPlayerNextToSeedling(final GameObject seedling, final WorldPoint location_player) {
        return (
            seedling != null
            && location_player != null
            && location_player.getX() + 2 >= seedling.getWorldLocation().getX()
            && location_player.getX() - 2 <= seedling.getWorldLocation().getX()
            && location_player.getY() + 2 >= seedling.getWorldLocation().getY()
            && location_player.getY() - 2 <= seedling.getWorldLocation().getY()
        );
    }
}
