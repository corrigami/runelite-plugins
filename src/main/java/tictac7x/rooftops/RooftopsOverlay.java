package tictac7x.rooftops;

import net.runelite.api.Client;
import net.runelite.api.TileObject;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;

//package tictac7x.rooftops;
//
//import net.runelite.api.Client;
//import net.runelite.api.GameState;
//import net.runelite.api.ItemID;
//import net.runelite.api.Player;
//import net.runelite.api.Tile;
//import net.runelite.api.TileObject;
//import net.runelite.api.events.GameStateChanged;
//import net.runelite.api.events.ItemDespawned;
//import net.runelite.api.events.ItemSpawned;
//import net.runelite.client.ui.overlay.Overlay;
//import net.runelite.client.ui.overlay.OverlayLayer;
//import net.runelite.client.ui.overlay.OverlayPosition;
//import tictac7x.rooftops.courses.Courses;
//
//import java.awt.BasicStroke;
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.Graphics2D;
//import java.awt.Shape;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//
public class RooftopsOverlay extends Overlay {
    private final Client client;
    private final RooftopsConfig config;
    private final RooftopsCourseManager course_manager;

    public RooftopsOverlay(final Client client, final RooftopsConfig config, final RooftopsCourseManager course_manager) {
        this.client = client;
        this.config = config;
        this.course_manager = course_manager;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (course_manager.getCourse() == null || !course_manager.isNearCourse()) return null;

        for (final TileObject obstacle : course_manager.getObstacles()) {
            final Shape clickbox = obstacle.getClickbox();

            final Color color =
                course_manager.getCourse().getNextObstacle().hasId(obstacle.getId())
                    ? course_manager.getCourse().isDoingObstacle()
                        ? config.getObstacleNextUnavailableColor()
                        : config.getObstacleNextColor()
                    : config.getObstacleUnavailableColor();

            renderShape(graphics, clickbox, color);
        }

        return null;
    }

    private void renderShape(final Graphics2D graphics, final Shape shape, final Color color) {
        if (shape == null || color.getAlpha() == 0) return;

        try {
            // Area border.
            graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.min(255, color.getAlpha() + 20)));
            graphics.setStroke(new BasicStroke(1));
            graphics.draw(shape);

            // Area fill.
            graphics.setColor(color);
            graphics.fill(shape);
        } catch (final Exception ignored) {}
    }
//    private final Client client;
//    private final RooftopsConfig config;
//    private final Courses courses;
//
//    private final List<TileObject> obstacles = new ArrayList<>();
//    private final List<Tile> mark_of_graces = new ArrayList<>();
//
//    public RooftopsOverlay(final RooftopsConfig config, final Client client, final Courses courses) {
//        this.courses = courses;
//        this.config = config;
//        this.client = client;
//
//        setPosition(OverlayPosition.DYNAMIC);
//        setLayer(OverlayLayer.ABOVE_SCENE);
//    }
//
//    public void onGameStateChanged(final GameStateChanged event) {
//        if (event.getGameState() == GameState.LOADING) {
//            obstacles.clear();
//            mark_of_graces.clear();
//        }
//    }
//
//    public void onTileObjectSpawned(final TileObject object) {
//        if (courses.getCourseBasedOnObstacle(object) != null) {
//            obstacles.add(object);
//        }
//    }
//
//    public void onItemSpawned(final ItemSpawned event) {
//        if (event.getItem().getId() == ItemID.MARK_OF_GRACE) {
//            mark_of_graces.add(event.getTile());
//        }
//    }
//
//    public void onItemDespawned(final ItemDespawned event) {
//        if (event.getItem().getId() == ItemID.MARK_OF_GRACE) {
//            mark_of_graces.remove(event.getTile());
//        }
//    }
//
//    @Override
//    public Dimension render(Graphics2D graphics) {
//        final Player player = client.getLocalPlayer();
//        if (player == null) return null;
//
//        // Main cycle to observe for obstacle changes.
//        courses.cycle(player);
//
//        // Mark of graces.
//        for (final Tile mark_of_grace : mark_of_graces) {
//            // Debug - highlight undefined mark of graces.
//            if (config.debugging()) {
//                final Optional<MarkOfGrace> mark = courses.getMarkOfGracesPredefined().stream().filter(m -> m.x == mark_of_grace.getWorldLocation().getX() && m.y == mark_of_grace.getWorldLocation().getY()).findFirst();
//
//                if (mark.isPresent()) {
//                    renderShape(graphics, mark_of_grace.getItemLayer().getCanvasTilePoly(), config.getMarkOfGraceColor());
//                } else {
//                    renderShape(graphics, mark_of_grace.getItemLayer().getCanvasTilePoly(), Color.MAGENTA);
//                }
//
//                // Production.
//            } else {
//                renderShape(graphics, mark_of_grace.getItemLayer().getCanvasTilePoly(), config.getMarkOfGraceColor());
//            }
//        }
//
//        // Obstacles.
//        for (final TileObject obstacle : obstacles) {
//            final Shape clickbox = obstacle.getClickbox();
//            if (clickbox != null) renderShape(graphics, clickbox, courses.getObstacleColor(obstacle));
//        }
//
//        return null;
//    }
//
//    private void renderShape(final Graphics2D graphics, final Shape shape, final Color color) {
//        if (color.getAlpha() == 0) return;
//
//        try {
//            // Area border.
//            graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.min(255, color.getAlpha() + 20)));
//            graphics.setStroke(new BasicStroke(1));
//            graphics.draw(shape);
//
//            // Area fill.
//            graphics.setColor(color);
//            graphics.fill(shape);
//        } catch (final Exception ignored) {}
//    }
}
