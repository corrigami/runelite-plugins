package tictac7x.rooftops;

import net.runelite.api.*;
import net.runelite.api.events.ItemDespawned;
import net.runelite.api.events.ItemSpawned;
import net.runelite.api.events.StatChanged;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import tictac7x.Overlay;
import java.awt.Shape;
import java.awt.Color;
import java.util.*;
import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.annotation.Nullable;

import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

public class RooftopsOverlay extends Overlay {
    private final RooftopsConfig config;
    private final Client client;

    @Nullable
    public static Tile mark_of_grace = null;

    @Nullable
    public static TileObject obstacle_clicked = null;

    public static Optional<Integer> obstacle_next = Optional.empty();

    private final List<TileObject> obstacles = new ArrayList<>();
    public static final List<Integer> obstacles_visited = new ArrayList<>();
    public static boolean doing_obstacle = false;
    private List<Integer> region_obstacles = null;
    public static boolean xp_drop = true;
    private Obstacles.Region region;
    private Map<String, Integer> obstacle_marks = new HashMap<>();

    public RooftopsOverlay(final RooftopsConfig config, final Client client) {
        this.config = config;
        this.client = client;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    public void clear() {
        obstacles.clear();
    }

    public void onTileObjectSpawned(final TileObject object) {
        if (Obstacles.isObstacle(object)) {
            obstacles.add(object);

            Obstacles.Region region = Obstacles.getRegion(client.getMapRegions());

            if (region != null && region != this.region) {
                this.region = region;
                region_obstacles = Obstacles.getObstacles(region);
                obstacle_next = Optional.empty();
            }
        }
    }

    public void onTileObjectDespawned(final TileObject object) {
        if (Obstacles.isObstacle(object)) {
            obstacles.remove(object);
        }
    }

    public void onItemSpawned(final ItemSpawned item) {
        if (item.getItem().getId() == ItemID.MARK_OF_GRACE) {
            mark_of_grace = item.getTile();

            final Optional<Integer> obstacle_mark = Obstacles.getMarkObstacle(region, item.getTile());

            if (obstacle_mark.isPresent()) {
                obstacle_marks.put(
                    item.getTile().getWorldLocation().getX() + "_" + item.getTile().getWorldLocation().getY(),
                    obstacle_mark.get()
                );
            }
        }
    }

    public void onItemDespawned(final ItemDespawned item) {
        if (item.getItem().getId() == ItemID.MARK_OF_GRACE) {
            obstacle_marks.remove(item.getTile().getWorldLocation().getX() + "_" + item.getTile().getWorldLocation().getY());
            mark_of_grace = null;
        }
    }

    public void onMenuOptionClicked(final MenuOptionClicked menu_option_clicked) {
        if (menu_option_clicked.getMenuAction() == MenuAction.GAME_OBJECT_FIRST_OPTION) {
            final int x = menu_option_clicked.getParam0();
            final int y = menu_option_clicked.getParam1();
            final int id = menu_option_clicked.getId();
            final TileObject object = findTileObject(client, x, y, id);

            if (object != null && Obstacles.isObstacle(object) && !obstacles_visited.contains(object.getId())) {
                obstacle_clicked = object;
            }
        }
    }

    public void onStatChanged(final StatChanged stat_changed) {
        if (stat_changed.getSkill() == Skill.AGILITY) {
            xp_drop = true;
        }
    }

    private Optional<Integer> getNextObstacle(final Player player) {
        Optional<Integer> obstacle_next = Optional.empty();

        // No obstacles.
        if (obstacles.isEmpty() || region_obstacles.isEmpty()) {
            obstacle_next = Optional.empty();

        // First obstacle in session.
        } else if (obstacles_visited.isEmpty()) {
            int distance_min = Integer.MAX_VALUE;

            for (final TileObject obstacle : obstacles) {
                final int distance = player.getWorldLocation().distanceTo(obstacle.getWorldLocation());

                if (distance < distance_min) {
                    obstacle_next = Optional.of(obstacle.getId());
                    distance_min = distance;
                }
            }

        // Find next obstacle based on previous.
        } else {
            final int previous_obstacle_id = obstacles_visited.get(obstacles_visited.size() - 1);
            final int obstacle_index = region_obstacles.indexOf(previous_obstacle_id);

            // Previous obstacle was last.
            if (obstacle_index + 1 >= region_obstacles.size()) {
                obstacle_next = Optional.of(region_obstacles.get(0));
                obstacles_visited.clear();

            // Next obstacle is available.
            } else {
                obstacle_next = Optional.of(region_obstacles.get(obstacle_index + 1));
            }
        }

        return obstacle_next;
    }

    private Color getObstacleColor(final TileObject obstacle) {
        final Color color;

        final Optional<Integer> mark_obstacle = getMarkObstacle();

        if (mark_obstacle.isPresent() && obstacle.getId() == mark_obstacle.get()) {
            color = config.getObstacleStopColor();
        } else if (!doing_obstacle && xp_drop && obstacle_next.isPresent() && obstacle.getId() == obstacle_next.get()) {
            color = config.getObstacleNextColor();
        } else {
            color = config.getObstacleUnavailableColor();
        }

        return color;
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        final Player player = client.getLocalPlayer();
        if (player == null || obstacles.isEmpty() || region_obstacles == null) return null;
        final boolean doing_obstacle = Obstacles.isDoingObstacle(region, player, obstacle_clicked);

        // First time finding next obstacle.
        if (obstacle_next.isEmpty()) {
            obstacle_next = getNextObstacle(player);

        // Started an obstacle, find next one.
        } else if (!this.doing_obstacle && this.xp_drop && doing_obstacle && obstacle_clicked != null && !obstacles_visited.contains(obstacle_clicked.getId())) {
            obstacles_visited.add(obstacle_clicked.getId());
            obstacle_next = getNextObstacle(player);
            this.doing_obstacle = true;
            this.xp_drop = false;
            obstacle_clicked = null;

            for (final int visited : obstacles_visited) {
                System.out.print(visited + " ");
            }
            System.out.println();
        } else {
            this.doing_obstacle = doing_obstacle;
        }

        // Highlight mark of grace.
        if (mark_of_grace != null && config.showMarkOfGrace()) {
            renderItem(graphics, mark_of_grace, config.getMarkOfGraceColor(), clickbox_stroke_width, 25);
        }

        for (final TileObject obstacle : obstacles) {
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
                renderShape(graphics, clickbox, getObstacleColor(obstacle));
            }
        }
        return null;
    }

    private Optional<Integer> getMarkObstacle() {
        Optional<Integer> obstacle_mark = Optional.empty();

        if (mark_of_grace == null) {
            obstacle_mark = Optional.empty();
        } else if (Obstacles.getMarkObstacle(region, mark_of_grace).isPresent()) {
            obstacle_mark = Obstacles.getMarkObstacle(region, mark_of_grace);
        } else {
            int distance_min = Integer.MAX_VALUE;

            for (final TileObject obstacle : obstacles) {
                final int distance = mark_of_grace.getWorldLocation().distanceTo(obstacle.getWorldLocation());
                if (distance < distance_min) {
                    distance_min = distance;
                    obstacle_mark = Optional.of(obstacle.getId());
                }
            }
        }

        return obstacle_mark;
    }
}
