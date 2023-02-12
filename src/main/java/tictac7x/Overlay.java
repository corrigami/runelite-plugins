package tictac7x;

import net.runelite.api.Client;
import net.runelite.api.DecorativeObject;
import net.runelite.api.GameObject;
import net.runelite.api.GroundObject;
import net.runelite.api.Scene;
import net.runelite.api.Tile;
import net.runelite.api.TileObject;
import net.runelite.api.WallObject;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.ProgressPieComponent;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

public abstract class Overlay extends OverlayPanel {
    private boolean isValidColor(final Color color) {
        return (color != null && color.getAlpha() > 0);
    }

    public void renderTile(final Graphics2D graphics, final TileObject object, final Color color) {
        if (!isValidColor(color)) return;

        try {
            final Shape shape = object.getCanvasTilePoly();

            // Area border.
            graphics.setColor(color.darker());
            graphics.setStroke(new BasicStroke(1));
            graphics.draw(shape);

            // Area fill.
            graphics.setColor(color);
            graphics.fill(shape);
        } catch (Exception ignored) {}
    }

    public void renderPie(final Graphics2D graphics, final TileObject object, final Color color, final float progress) {
        if (!isValidColor(color)) return;

        try {
            final ProgressPieComponent progressPieComponent = new ProgressPieComponent();
            progressPieComponent.setPosition(object.getCanvasLocation(0));
            progressPieComponent.setProgress(-progress);
            progressPieComponent.setBorderColor(color.darker());
            progressPieComponent.setFill(color);
            progressPieComponent.render(graphics);
        } catch (Exception ignored) {}
    }

    public TileObject findTileObject(final Client client, final int x, final int y, final int id) {
        try {
            final Scene scene = client.getScene();
            final Tile[][][] tiles = scene.getTiles();
            final Tile tile = tiles[client.getPlane()][x][y];

            if (tile != null) {
                for (GameObject game_object : tile.getGameObjects()) {
                    if (game_object != null && game_object.getId() == id) {
                        return game_object;
                    }
                }

                final WallObject wall_object = tile.getWallObject();
                if (wall_object != null && wall_object.getId() == id) {
                    return wall_object;
                }

                final DecorativeObject decorative_object = tile.getDecorativeObject();
                if (decorative_object != null && decorative_object.getId() == id) {
                    return decorative_object;
                }

                final GroundObject ground_object = tile.getGroundObject();
                if (ground_object != null && ground_object.getId() == id) {
                    return ground_object;
                }
            }
        } catch (Exception ignored) {}

        return null;
    }
}