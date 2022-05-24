package tictac7x.storage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;
import javax.annotation.Nullable;

import net.runelite.api.Client;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.components.ImageComponent;


public class StorageOverlayInventory extends StorageOverlay {
    private static final String LABEL_FREE = "%d free";

    private final BufferedImage inventory_image;

    @Nullable
    private ImageComponent panel_items_free = null;

    @Nullable
    private Integer panel_items_width = null;

    @Nullable
    private Integer items_free = null;

    public StorageOverlayInventory(
        final Client client,
        final ClientThread client_thread,
        final ConfigManager configs,
        final ItemManager items,
        final StorageConfig config,
        final StorageManager storages,
        final String config_id,
        final String storage_id,
        final @Nullable WidgetInfo widget_info
    ) {
        super(client, client_thread, configs, items, config, storages, config_id, storage_id, widget_info);
        this.inventory_image = ImageUtil.getResourceStreamFromClass(getClass(), "inventory.png");
    }

    @Override
    void renderBefore() {
        if (
            config.getInventoryEmptyStyle() == StorageConfig.InventoryEmpty.TOP &&
            (getEmpty() > 0 || config.showInventoryZeroSpaceLeft())
        ) {
            addFreeSlotsImageComponent();
        }
    }

    @Override
    void renderBeforeItems() {
        if (
            config.getInventoryEmptyStyle() == StorageConfig.InventoryEmpty.FIRST &&
            (getEmpty() > 0 || config.showInventoryZeroSpaceLeft())
        ) {
            panel_items.getChildren().add(createInventoryItem());
        }
    }

    @Override
    void renderAfterItems() {
        if (
            config.getInventoryEmptyStyle() == StorageConfig.InventoryEmpty.LAST &&
            (getEmpty() > 0 || config.showInventoryZeroSpaceLeft())
        ) {
            panel_items.getChildren().add(createInventoryItem());
        }
    }

    @Override
    void renderAfter() {
        if (
            config.getInventoryEmptyStyle() == StorageConfig.InventoryEmpty.BOTTOM &&
            (getEmpty() > 0 || config.showInventoryZeroSpaceLeft())
        ) {
            addFreeSlotsImageComponent();
        }
    }

    private void addFreeSlotsImageComponent() {
        final int panel_items_width = panel_items.getBounds().width;
        final int items_free = getEmpty();

        if (
            panel_items.getBounds().width > 0 && (
                this.panel_items_width == null ||
                panel_items_width != this.panel_items_width ||
                this.items_free == null ||
                items_free != this.items_free
            )
        ) {
            final String free = String.format(LABEL_FREE, getEmpty());
            this.panel_items_width = panel_items_width;
            this.items_free = items_free;

            final BufferedImage free_image = new BufferedImage(panel_items.getBounds().width, 14, BufferedImage.TYPE_4BYTE_ABGR);
            final Graphics graphics = free_image.getGraphics();
            final FontMetrics font_metrics = graphics.getFontMetrics();
            graphics.setFont(FontManager.getRunescapeFont());

            // Shadow.
            graphics.setColor(Color.BLACK);
            graphics.drawString(free, ((free_image.getWidth() - font_metrics.stringWidth(free)) / 2) + 1, font_metrics.getAscent() + 2);

            // Label.
            graphics.setColor(color_gray);
            graphics.drawString(free, (free_image.getWidth() - font_metrics.stringWidth(free)) / 2, font_metrics.getAscent() + 1);

            graphics.dispose();
            panel_items_free = new ImageComponent(free_image);
        }

        if (panel_items_free != null) {
            panelComponent.getChildren().add(panel_items_free);
        }
    }

    private ImageComponent createInventoryItem() {
        final String free = String.valueOf(getEmpty());

        // Make copy of inventory icon.
        final BufferedImage inventory_image = new BufferedImage(this.inventory_image.getWidth(), this.inventory_image.getHeight(), this.inventory_image.getType());
        final Graphics graphics = inventory_image.getGraphics();
        graphics.drawImage(this.inventory_image, 0, 0, null);

        // Free slots count.
        final FontMetrics fm = graphics.getFontMetrics();
        graphics.setFont(FontManager.getRunescapeSmallFont());

        // Shadow.
        graphics.setColor(Color.BLACK);
        graphics.drawString(free, 1, fm.getAscent());

        // Yellow label.
        graphics.setColor(Color.YELLOW);
        graphics.drawString(free, 0, fm.getAscent() - 1);

        graphics.dispose();
        return new ImageComponent(inventory_image);
    }
}
