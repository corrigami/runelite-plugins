package tictac7x.tithe;

import net.runelite.api.DecorativeObject;
import net.runelite.api.GameObject;
import net.runelite.api.GroundObject;
import net.runelite.api.Scene;
import net.runelite.api.Tile;
import net.runelite.api.WallObject;
import net.runelite.client.ui.overlay.OverlayPanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;

import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.TileObject;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

public class TitheOverlayPatches extends OverlayPanel {
    private final Client client;
    private final TithePlugin plugin;
    private final TitheConfig config;

    public TitheOverlayPatches(final TithePlugin plugin, final TitheConfig config, final Client client) {
        this.plugin = plugin;
        this.config = config;
        this.client = client;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.UNDER_WIDGETS);
    }

    @Override
    public Dimension render(final Graphics2D graphics) {
        if (plugin.inTitheFarm()) {
            final MenuEntry[] menu_entries = client.getMenuEntries();

            for (final MenuEntry menu_entry : menu_entries) {
                final MenuAction menu_option = menu_entry.getType();
                if (menu_option == MenuAction.CANCEL || menu_option == MenuAction.WALK) continue;

                final TileObject object = findTileObject(client, menu_entry.getParam0(), menu_entry.getParam1(), menu_entry.getIdentifier());

                if (object != null && TithePlant.isPatch(object)) {
                    renderTile(graphics, object, config.getPatchesHighlightOnHoverColor());
                    break;
                }
            }
        }

        return null;
    }

    private TileObject findTileObject(final Client client, final int x, final int y, final int id) {
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

    private void renderTile(final Graphics2D graphics, final TileObject object, final Color color) {
        if (color == null || color.getAlpha() == 0) return;

        try {
            final Shape shape = object.getCanvasTilePoly();

            // Area border.
            graphics.setColor(color.darker());
            graphics.setStroke(new BasicStroke(1));
            graphics.draw(shape);

            // Area fill.
            graphics.setColor(color);
            graphics.fill(shape);
        } catch (final Exception ignored) {}
    }
}
