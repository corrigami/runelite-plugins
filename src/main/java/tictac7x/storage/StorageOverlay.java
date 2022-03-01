package tictac7x.storage;

import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.*;
import net.runelite.client.util.ImageUtil;
import tictac7x.Overlay;

import java.awt.*;

public class StorageOverlay extends Overlay {
    private static final String label_empty_slots = "%d free";

    private final StorageConfig config;
    private final Storage storage;
    private final PanelComponent panel = new PanelComponent();
    private final ImageComponent icon_inventory;

    public StorageOverlay(final StorageConfig config, final Storage storage) {
        this.config = config;
        this.storage = storage;
        this.icon_inventory = new ImageComponent(ImageUtil.getResourceStreamFromClass(getClass(), "inventory.png"));

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
        if (config.getOverlayDensity() == StorageConfig.OverlayDensity.REGULAR) {
            panel.setGap(new Point(6, 4));
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
            panelComponent.setBorder(new Rectangle(0, 0, 0, 0));
        }

        // Inventory empty slots in top.
        if (storage.storage_id.equals(StorageConfig.inventory) && config.getInventoryEmptySlots() == StorageConfig.InventoryEmpty.TOP) {
            panelComponent.getChildren().add(createEmptySlotsComponent());
            panelComponent.setBorder(new Rectangle(0, 5, 0, 0));
            panelComponent.setGap(new Point(5, 5));
        }

        // Inventory empty slots as first item.
        if (storage.storage_id.equals(StorageConfig.inventory) && config.getInventoryEmptySlots() == StorageConfig.InventoryEmpty.FIRST && icon_inventory != null) {
            panel.getChildren().add(icon_inventory);
        }

        // Render storage visible items.
        for (final ImageComponent item : storage.getStorageImages()) {
            panel.getChildren().add(item);
        }

        // Inventory empty slots as last item.
        if (storage.storage_id.equals(StorageConfig.inventory) && config.getInventoryEmptySlots() == StorageConfig.InventoryEmpty.LAST && icon_inventory != null) {
            panel.getChildren().add(icon_inventory);
        }

        if (!panel.getChildren().isEmpty()) {
            panelComponent.getChildren().add(panel);
        }

        // Inventory empty slots in bottom.
        if (storage.storage_id.equals(StorageConfig.inventory) && config.getInventoryEmptySlots() == StorageConfig.InventoryEmpty.BOTTOM) {
            panelComponent.getChildren().add(createEmptySlotsComponent());
            panelComponent.setBorder(new Rectangle(0, 0, 0, 10));
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
            .text(String.format(label_empty_slots, storage.getEmptySlotsCount()))
            .color(color_gray)
            .build();
    }
}
