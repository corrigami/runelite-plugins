package tictac7x.rooftops;

import com.google.common.collect.ImmutableSet;
import net.runelite.api.*;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import tictac7x.Overlay;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class RooftopsOverlayObstacles extends Overlay {
    private final Client client;
    private final RooftopsOverlayMarks overlay_marks;

    private final List<TileObject> obstacles = new ArrayList<>();

    public RooftopsOverlayObstacles(final Client client, final RooftopsOverlayMarks overlay_marks) {
        this.client = client;
        this.overlay_marks = overlay_marks;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    public void onTileObjectSpawned(final TileObject tile_object) {
        if (Obstacles.isObstacle(tile_object)) {
            obstacles.add(tile_object);
        }
    }

    public void onTileObjectDespawned(final TileObject tile_object) {
        if (Obstacles.isObstacle(tile_object)) {
            obstacles.remove(tile_object);
        }
    }

    public void clear() {
        obstacles.clear();
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        final Player player = client.getLocalPlayer();
        if (player == null) return null;
        final boolean doing_obstacle = Obstacles.isDoingObstacle(player);

        final Tile mark_of_grace = overlay_marks.getMarkOfGrace();
        int distance_min_player = Integer.MAX_VALUE;
        int distance_min_mark = Integer.MAX_VALUE;
        TileObject obstacle_closest_player = null;
        TileObject obstacle_closest_mark = null;

        // Find the closest obstacle to the player and the mark of grace.
        for (final TileObject obstacle : obstacles) {
            // Player obstacle.
            if (obstacle.getPlane() == client.getPlane()) {
                final int distance = obstacle.getWorldLocation().distanceTo(player.getWorldLocation());
                if (distance < distance_min_player) {
                    distance_min_player = distance;
                    obstacle_closest_player = obstacle;
                }
            }

            // Mark of grace obstacle.
            if (mark_of_grace != null && obstacle.getPlane() == mark_of_grace.getPlane()) {
                final int distance = obstacle.getWorldLocation().distanceTo(mark_of_grace.getWorldLocation());
                if (distance < distance_min_mark) {
                    distance_min_mark = distance;
                    obstacle_closest_mark = obstacle;
                }
            }
        }

        for (final TileObject obstacle : obstacles) {
            if (obstacle.getPlane() == client.getPlane()) {
                final Color color;
                if (obstacle == obstacle_closest_mark) {
                    color = color_red;
                } else if (doing_obstacle) {
                    color = color_yellow;
                } else if (obstacle == obstacle_closest_player) {
                    color = color_green;
                } else {
                    color = color_yellow;
                }
                renderClickbox(graphics, obstacle, color, 1);
            }
        }

        return null;
    }
}
