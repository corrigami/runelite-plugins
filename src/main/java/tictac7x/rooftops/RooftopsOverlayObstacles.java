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
import net.runelite.api.Player;
import net.runelite.api.Client;
import javax.annotation.Nullable;
import net.runelite.api.MenuAction;
import net.runelite.api.TileObject;
import net.runelite.api.GameObject;
import net.runelite.api.GroundObject;
import net.runelite.api.DecorativeObject;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

public class RooftopsOverlayObstacles extends Overlay {
    private final RooftopsConfig config;
    private final Client client;
    private final RooftopsOverlayMarks overlay_marks;

    @Nullable
    private TileObject obstacle_clicked = null;
    private final List<TileObject> obstacles = new ArrayList<>();
    private final List<TileObject> obstacles_clicked = new ArrayList<>();
    private boolean doing_obstacle = false;

    public RooftopsOverlayObstacles(final RooftopsConfig config, final Client client, final RooftopsOverlayMarks overlay_marks) {
        this.config = config;
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

    public void onMenuOptionClicked(final MenuOptionClicked menu_option_clicked) {
        if (!doing_obstacle && menu_option_clicked.getMenuAction() == MenuAction.GAME_OBJECT_FIRST_OPTION) {
            final int x = menu_option_clicked.getParam0();
            final int y = menu_option_clicked.getParam1();
            final int id = menu_option_clicked.getId();
            final TileObject object = findTileObject(client, x, y, id);

            if (object != null && Obstacles.isObstacle(object)) {
                obstacle_clicked = object;
            }
        }
    }

//    // For debugging and finding out course animations.
//    static int animation;
//    static int pose;
//    static int idle;

    @Override
    public Dimension render(Graphics2D graphics) {
        final Player player = client.getLocalPlayer();
        if (player == null) return null;
        final boolean doing_obstacle = Obstacles.isDoingObstacle(player);

        // Player completed rooftop course (based on plane back to 0).
        if (client.getPlane() == 0 && !obstacles_clicked.isEmpty()) {
            obstacles_clicked.clear();
        }

        // Started an obstacle.
        if (!this.doing_obstacle && doing_obstacle && obstacle_clicked != null) {
            obstacles_clicked.add(obstacle_clicked);
            this.doing_obstacle = true;
        }

        // Finished an obstacle.
        if (this.doing_obstacle && !doing_obstacle) {
            this.doing_obstacle = false;
        }

//        // For debugging and finding out course animations.
//        if (player.getAnimation() != animation) {
//            animation = player.getAnimation();
//            System.out.println("Animation: " + animation);
//        }
//        if (player.getPoseAnimation() != pose) {
//            pose = player.getPoseAnimation();
//            System.out.println("Pose: " + pose);
//        }
//        if (player.getIdlePoseAnimation() != idle) {
//            idle = player.getIdlePoseAnimation();
//            System.out.println("Idle: " + idle);
//        }

        final Tile mark_of_grace = overlay_marks.getMarkOfGrace();
        int distance_min_player = Integer.MAX_VALUE;
        int distance_min_mark = Integer.MAX_VALUE;
        TileObject obstacle_closest_player = null;
        TileObject obstacle_closest_mark = null;

        for (final TileObject obstacle : obstacles) {
            // Find the closest obstacle to the player that was not clicked before.
            if (obstacle.getPlane() == client.getPlane() && !obstacles_clicked.contains(obstacle)) {
                final int distance = obstacle.getLocalLocation().distanceTo(player.getLocalLocation());
                if (distance < distance_min_player) {
                    distance_min_player = distance;
                    obstacle_closest_player = obstacle;
                }
            }

            // Find the closest obstacle to mark of grace.
            if (
                config.showObstacleStop() &&
                mark_of_grace != null &&
                obstacle.getPlane() == mark_of_grace.getPlane() &&
                !obstacles_clicked.contains(obstacle)
            ) {
                final int distance1 = obstacle.getWorldLocation().distanceTo(mark_of_grace.getWorldLocation());
                if (distance1 < distance_min_mark) {
                    distance_min_mark = distance1;
                    obstacle_closest_mark = obstacle;
                }
            }
        }

        // Render obstacles clickboxes.
        for (final TileObject obstacle : obstacles) {
            // Show only current plane obstacles.
            if (!config.highlightAllObstacles() && obstacle.getPlane() != client.getPlane()) continue;

            final Color color =
                obstacle == obstacle_closest_mark
                    ? config.getObstacleStopColor()
                : !doing_obstacle && obstacle == obstacle_closest_player
                || !config.showObstaclesUnavailable()
                    ? config.getObstacleNextColor()
                    : config.getObstacleUnavailableColor();

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
