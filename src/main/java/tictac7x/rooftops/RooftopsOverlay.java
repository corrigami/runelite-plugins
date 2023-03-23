package tictac7x.rooftops;

import net.runelite.api.Client;
import net.runelite.api.Tile;
import net.runelite.api.TileObject;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;

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

        // Obstacles.
        for (final TileObject obstacle : course_manager.getObstacles()) {
             final Color color =
                course_manager.isStoppingObstacle(obstacle.getId())
                    ? config.getObstacleStopColor()
                    : course_manager.getCourse().getNextObstacle().hasId(obstacle.getId())
                        ? course_manager.getCourse().isDoingObstacle()
                            ? config.getObstacleNextUnavailableColor()
                            : config.getObstacleNextColor()
                        : config.getObstacleUnavailableColor();

            renderShape(graphics, obstacle.getClickbox(), color);
        }

        // Mark of graces.
        for (final Tile mark : course_manager.getMarksOfGraces()) {
            renderShape(graphics, mark.getItemLayer().getCanvasTilePoly(), config.getMarkOfGraceColor());
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
}
