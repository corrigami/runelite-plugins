package tictac7x;

import java.util.Map;
import java.awt.Color;
import java.awt.Shape;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import net.runelite.api.Tile;
import net.runelite.api.Scene;
import net.runelite.api.Client;
import net.runelite.api.TileObject;
import net.runelite.api.GameObject;
import net.runelite.api.WallObject;
import net.runelite.api.GroundObject;
import net.runelite.api.widgets.Widget;
import net.runelite.api.DecorativeObject;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.ui.overlay.components.ProgressPieComponent;

public abstract class Overlay extends net.runelite.client.ui.overlay.OverlayPanel {
    protected final int panel_background_alpha = 80;
    public static final int clickbox_stroke_width = 1;
    public static final int clickbox_fill_alpha = 30;
    protected final int pie_fill_alpha = 90;
    protected final int inventory_highlight_alpha = 60;
    protected final int pie_progress = 1;
    public static final Color color_red    = new Color(255, 0, 0);
    public static final Color color_green  = new Color(0, 255, 0);
    public static final Color color_blue   = new Color(0, 150, 255);
    public static final Color color_yellow = new Color(255, 180, 0);
    public static final Color color_orange = new Color(255, 120, 30);
    public static final Color color_gray   = new Color(200, 200, 200);

    public Color getColor(final Color color, final int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public Color getPanelBackgroundColor(final Color color) {
        return getColor(color, panel_background_alpha);
    }

    public void renderClickbox(final Graphics2D graphics, final TileObject object, final Color color) {
        renderClickbox(graphics, object, color, clickbox_stroke_width);
    }

    public void renderClickbox(final Graphics2D graphics, final TileObject object, final Color color, final int stroke_width) {
        renderClickbox(graphics, object, color, stroke_width, clickbox_fill_alpha);
    }

    public void renderClickbox(final Graphics2D graphics, final TileObject object, final Color color, final int stroke_width, final int fill_alpha) {
        renderShape(graphics, object.getClickbox(), color, stroke_width, fill_alpha);
    }

    public void renderShape(final Graphics2D graphics, final Shape shape, final Color color, final int stroke_width, final int fill_alpha) {
        try {
            // Area border.
            graphics.setColor(color);
            graphics.setStroke(new BasicStroke(stroke_width));
            graphics.draw(shape);

            // Area fill.
            graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), fill_alpha));
            graphics.fill(shape);
        } catch (Exception ignored) {}
    }
}
