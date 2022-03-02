package tictac7x.storage;

import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.*;
import net.runelite.client.util.ImageUtil;
import tictac7x.Overlay;

import java.awt.*;
import java.awt.image.BufferedImage;

public class StorageOverlay extends Overlay {
    private static final String LABEL_FREE = "%d free";
    private static final int PADDING_TOP = 5;
    private static final int PADDING_LEFT = 5;
    private static final int DENSITY_HORIZONTAL = 6;
    private static final int DENSITY_VERTICAL = 4;

    private final StorageConfig config;
    private final Storage storage;
    private final PanelComponent panel = new PanelComponent();
    private final BufferedImage inventory_image;

    public StorageOverlay(final StorageConfig config, final Storage storage) {
        this.config = config;
        this.storage = storage;
        this.inventory_image = ImageUtil.getResourceStreamFromClass(getClass(), "inventory.png");

        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPosition(OverlayPosition.TOP_RIGHT);
        makePanelResizeable(panelComponent, panel);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        panelComponent.getChildren().clear();
        panel.getChildren().clear();

        // Storage overlay hidden.
        if (!storage.show()) return null;

        // Storage overlay density.
        if (config.getOverlayDensity() == StorageConfig.InventoryDensity.REGULAR) {
            panel.setGap(new Point(DENSITY_HORIZONTAL, DENSITY_VERTICAL));
        } else {
            panel.setGap(new Point(0, 0));
        }

        // Inventory empty slots hidden or as items.
        if (storage.storage_id.equals(StorageConfig.inventory) && (
            config.getInventoryEmptySlots() == StorageConfig.InventoryEmpty.HIDDEN ||
            config.getInventoryEmptySlots() == StorageConfig.InventoryEmpty.FIRST ||
            config.getInventoryEmptySlots() == StorageConfig.InventoryEmpty.LAST)
        ) {
            panelComponent.setGap(new Point(0, 0));
            panelComponent.setBorder(new Rectangle(PADDING_LEFT, PADDING_TOP, 0, PADDING_TOP));
        }

        // Inventory empty slots in top.
        if (storage.storage_id.equals(StorageConfig.inventory) && config.getInventoryEmptySlots() == StorageConfig.InventoryEmpty.TOP) {
            panelComponent.getChildren().add(createEmptySlotsComponent());
            panelComponent.setBorder(new Rectangle(PADDING_LEFT, PADDING_TOP, 0, 0));
            panelComponent.setGap(new Point(PADDING_LEFT, PADDING_TOP));
        }

        // Inventory empty slots as first item.
        if (
            storage.storage_id.equals(StorageConfig.inventory) &&
            config.getInventoryEmptySlots() == StorageConfig.InventoryEmpty.FIRST &&
            storage.getEmptySlotsCount() > 0
        ) {
            panel.getChildren().add(createInventoryItem());
        }

        // Render storage visible items.
        for (final ImageComponent item : storage.getStorageImages()) {
            panel.getChildren().add(item);
        }

        // Inventory empty slots as last item.
        if (
            storage.storage_id.equals(StorageConfig.inventory) &&
            config.getInventoryEmptySlots() == StorageConfig.InventoryEmpty.LAST &&
            storage.getEmptySlotsCount() > 0
        ) {
            panel.getChildren().add(createInventoryItem());
        }

        if (!panel.getChildren().isEmpty()) {
            panelComponent.getChildren().add(panel);
        }

        // Inventory empty slots in bottom.
        if (storage.storage_id.equals(StorageConfig.inventory) && config.getInventoryEmptySlots() == StorageConfig.InventoryEmpty.BOTTOM) {
            panelComponent.getChildren().add(createEmptySlotsComponent());
            panelComponent.setBorder(new Rectangle(PADDING_LEFT, PADDING_TOP, 0, 10));
            panelComponent.setGap(new Point(0, 0));
        }

        // Overlay has items, render it.
        if (!panel.getChildren().isEmpty() || !panelComponent.getChildren().isEmpty()) {
            return super.render(graphics);
        }

        // Overlay empty, don't render it.
        return null;
    }

    private TitleComponent createEmptySlotsComponent() {
        return TitleComponent
            .builder()
            .text(String.format(LABEL_FREE, storage.getEmptySlotsCount()))
            .color(color_gray)
            .build();
    }

    private ImageComponent createInventoryItem() {
        // Make copy of inventory icon.
        final BufferedImage inventory_image = new BufferedImage(this.inventory_image.getWidth(), this.inventory_image.getHeight(), this.inventory_image.getType());
        final Graphics g = inventory_image.getGraphics();
        g.drawImage(this.inventory_image, 0, 0, null);

        // Free slots count.
        final FontMetrics fm = g.getFontMetrics();
        g.setFont(FontManager.getRunescapeSmallFont());

        // Shadow.
        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(storage.getEmptySlotsCount()), 1, fm.getAscent() - 3 + 1);

        // Yellow label.
        g.setColor(Color.YELLOW);
        g.drawString(String.valueOf(storage.getEmptySlotsCount()), 0, fm.getAscent() - 3);

        g.dispose();
        return new ImageComponent(inventory_image);
    }
}
