package tictac7x.storage;

import java.awt.Point;
import java.awt.Color;

import tictac7x.Overlay;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.FontMetrics;
import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ImageComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;

public class StorageOverlay extends Overlay {
    private static final String LABEL_FREE = "%d free";
    private static final int PADDING = 10;
    private static final int DENSITY_HORIZONTAL = 6;
    private static final int DENSITY_VERTICAL = 4;

    private final StorageConfig config;
    private final Storage storage;
    private final BufferedImage inventory_image;
    private final PanelComponent panel_items = new PanelComponent();

    @Nullable
    private ImageComponent panel_items_free = null;
    private int panel_items_width = 0;
    private int items_free = -1;

    public StorageOverlay(final StorageConfig config, final Storage storage) {
        this.config = config;
        this.storage = storage;
        this.inventory_image = ImageUtil.getResourceStreamFromClass(getClass(), "inventory.png");

        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPosition(OverlayPosition.TOP_RIGHT);
        makePanelResizeable(panelComponent, panel_items);

        panel_items.setBorder(new Rectangle(PADDING, 0, PADDING - 4, 0));
        panelComponent.setBorder(new Rectangle(0, PADDING, 0, PADDING));
        panelComponent.setGap(new Point(PADDING, PADDING));
    }

    private int getItemsFreePadding() {
        return PADDING;
//        return storage.getEmptySlotsCount() == 28 ? PADDING : 0;
    }

    @Override
    public Dimension render(final Graphics2D graphics) {
        panelComponent.getChildren().clear();
        panel_items.getChildren().clear();

        // Storage overlay hidden.
        if (!storage.show()) return null;

        // Storage overlay density.
        if (config.getOverlayDensity() == StorageConfig.InventoryDensity.REGULAR) {
            panel_items.setGap(new Point(DENSITY_HORIZONTAL, DENSITY_VERTICAL));
        } else {
            panel_items.setGap(new Point(0, 0));
        }

        // Inventory empty slots in top.
        if (
            storage.storage_id.equals(StorageConfig.inventory) &&
            config.getInventoryEmptyStyle() == StorageConfig.InventoryEmpty.TOP &&
            (storage.getEmptySlotsCount() > 0 || config.showInventoryZeroSpaceLeft())
        ) {
            addFreeSlotsImageComponent();
        }

        // Inventory empty slots as first item.
        if (
            storage.storage_id.equals(StorageConfig.inventory) &&
            config.getInventoryEmptyStyle() == StorageConfig.InventoryEmpty.FIRST &&
            (storage.getEmptySlotsCount() > 0 || config.showInventoryZeroSpaceLeft())
        ) {
            panel_items.getChildren().add(createInventoryItem());
        }

        // Render storage visible items.
        for (final ImageComponent item : storage.getStorageImages()) {
            panel_items.getChildren().add(item);
        }

        // Inventory empty slots as last item.
        if (
            storage.storage_id.equals(StorageConfig.inventory) &&
            config.getInventoryEmptyStyle() == StorageConfig.InventoryEmpty.LAST &&
            (storage.getEmptySlotsCount() > 0 || config.showInventoryZeroSpaceLeft())
        ) {
            panel_items.getChildren().add(createInventoryItem());
        }

        if (!panel_items.getChildren().isEmpty()) {
            panelComponent.getChildren().add(panel_items);
        }

        // Inventory empty slots in bottom.
        if (
            storage.storage_id.equals(StorageConfig.inventory) &&
            config.getInventoryEmptyStyle() == StorageConfig.InventoryEmpty.BOTTOM &&
            (storage.getEmptySlotsCount() > 0 || config.showInventoryZeroSpaceLeft())
        ) {
            addFreeSlotsImageComponent();
        }

        // Overlay has items, render it.
        if (!panel_items.getChildren().isEmpty() || !panelComponent.getChildren().isEmpty()) {
            return super.render(graphics);
        }

        // Overlay empty, don't render it.
        return null;
    }

    private void addFreeSlotsImageComponent() {
        final int panel_items_width = panel_items.getBounds().width;
        final int items_free = storage.getEmptySlotsCount();;

        if (panel_items.getBounds().width > 0 && (panel_items_width != this.panel_items_width || items_free != this.items_free)) {
            final String free = String.format(LABEL_FREE, storage.getEmptySlotsCount());
            this.panel_items_width = panel_items_width;
            this.items_free = items_free;

            final BufferedImage free_image = new BufferedImage(panel_items.getBounds().width, 14, BufferedImage.TYPE_4BYTE_ABGR);
            final Graphics graphics = free_image.getGraphics();
            final FontMetrics font_metrics = graphics.getFontMetrics();
            graphics.setFont(FontManager.getRunescapeFont());

            // Shadow.
            graphics.setColor(Color.BLACK);
            graphics.drawString(free, ((free_image.getWidth() - font_metrics.stringWidth(free)) / 2) + 1, font_metrics.getAscent() + 1);

            // Label.
            graphics.setColor(color_gray);
            graphics.drawString(free, (free_image.getWidth() - font_metrics.stringWidth(free)) / 2, font_metrics.getAscent());

            graphics.dispose();
            panel_items_free = new ImageComponent(free_image);
        }

        if (panel_items_free != null) {
            panelComponent.getChildren().add(panel_items_free);
        }
    }

    private ImageComponent createInventoryItem() {
        final String free = String.valueOf(storage.getEmptySlotsCount());

        // Make copy of inventory icon.
        final BufferedImage inventory_image = new BufferedImage(this.inventory_image.getWidth(), this.inventory_image.getHeight(), this.inventory_image.getType());
        final Graphics graphics = inventory_image.getGraphics();
        graphics.drawImage(this.inventory_image, 0, 0, null);

        // Free slots count.
        final FontMetrics fm = graphics.getFontMetrics();
        graphics.setFont(FontManager.getRunescapeSmallFont());

        // Shadow.
        graphics.setColor(Color.BLACK);
        graphics.drawString(free, 1, fm.getAscent() - 2 + 1);

        // Yellow label.
        graphics.setColor(Color.YELLOW);
        graphics.drawString(free, 0, fm.getAscent() - 2);

        graphics.dispose();
        return new ImageComponent(inventory_image);
    }
}
