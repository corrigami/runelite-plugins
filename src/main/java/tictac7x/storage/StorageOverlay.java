package tictac7x.storage;
import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import tictac7x.Overlay;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Graphics2D;

import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ImageComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;

import javax.annotation.Nullable;

public class StorageOverlay extends Overlay {
    private static final int PADDING = 10;
    private static final int DENSITY_HORIZONTAL = 6;
    private static final int DENSITY_VERTICAL = 4;
    private final Client client;
    final StorageConfig config;
    final Storage storage;
    final PanelComponent panel_items = new PanelComponent();

    @Nullable
    Widget widget = null;

    @Nullable
    final WidgetInfo widget_info;

    public StorageOverlay(final Client client, final StorageConfig config, final Storage storage, final @Nullable WidgetInfo widget_info) {
        this.client = client;
        this.config = config;
        this.storage = storage;
        this.widget_info = widget_info;

        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPosition(OverlayPosition.TOP_RIGHT);
        makePanelResizeable(panelComponent, panel_items);

        panel_items.setBorder(new Rectangle(PADDING, 0, PADDING - 4, 0));
        panelComponent.setBorder(new Rectangle(0, PADDING, 0, PADDING));
        panelComponent.setGap(new Point(PADDING, PADDING));
    }

    @Override
    public Dimension render(final Graphics2D graphics) {
        panelComponent.getChildren().clear();
        panel_items.getChildren().clear();

        // Storage overlay hidden.
        if (!storage.show()) return null;

        // Original widget visible.
        updateWidget();
        if (storage.hideWithOverlay() && widget != null && !widget.isHidden()) return null;

        // Storage overlay density.
        final boolean densityCompact = config.getOverlayDensity() == StorageConfig.InventoryDensity.COMPACT;
        panel_items.setGap(new Point(densityCompact ? 0 : DENSITY_HORIZONTAL, densityCompact ? 0 : DENSITY_VERTICAL));

        // Render section before items.
        renderBefore();

        // Render pre-items.
        renderBeforeItems();

        // Render storage visible items.
        for (final ImageComponent item : storage.getStorageImages()) {
            panel_items.getChildren().add(item);
        }

        // Render after-items.
        renderAfterItems();

        // Add items grid if there is something to render.
        if (!panel_items.getChildren().isEmpty()) {
            panelComponent.getChildren().add(panel_items);
        }

        // Render section after items.
        renderAfter();

        // Overlay has items, render it.
        if (!panel_items.getChildren().isEmpty() || !panelComponent.getChildren().isEmpty()) {
            return super.render(graphics);
        }

        // Overlay empty, don't render it.
        return null;
    }

    private void updateWidget() {
        if (this.widget != null) return;
        this.widget = client.getWidget(widget_info);
    }

    void renderBefore() {}

    void renderBeforeItems() {}

    void renderAfter() {}

    void renderAfterItems() {}
}
