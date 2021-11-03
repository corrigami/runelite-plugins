package tictac7x.rooftops;

import tictac7x.Overlay;
import java.awt.Shape;
import java.awt.Color;
import java.util.List;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import net.runelite.api.Tile;
import net.runelite.api.Model;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.TileObject;
import net.runelite.api.GameObject;
import net.runelite.api.GroundObject;
import net.runelite.api.DecorativeObject;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

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

    public void clear() {
        obstacles.clear();
    }

    public void onTileObjectSpawned(final TileObject object) {
        if (Obstacles.isObstacle(object)) obstacles.add(object);
    }

    public void onTileObjectDespawned(final TileObject object) {
        if (Obstacles.isObstacle(object)) obstacles.remove(object);
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

        // Render obstacles clickboxes.
        for (final TileObject obstacle : obstacles) {
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

            // Custom getClickbox to render obstacles correctly on different planes.
            final Shape clickbox = Perspective.getClickbox(client,
                obstacle instanceof GameObject
                    ? (Model) ((GameObject) obstacle).getRenderable() :
                obstacle instanceof GroundObject
                    ? (Model) ((GroundObject) obstacle).getRenderable() :
                obstacle instanceof DecorativeObject
                    ? (Model) ((DecorativeObject) obstacle).getRenderable() :
                null, 0, obstacle.getLocalLocation(), obstacle.getPlane() - client.getPlane()
            );


            if (clickbox != null) {
                renderShape(graphics, clickbox, color);
            }
        }

        return null;
    }
}
