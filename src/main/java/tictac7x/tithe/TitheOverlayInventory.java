package tictac7x.tithe;

import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemID;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.ui.overlay.WidgetItemOverlay;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public class TitheOverlayInventory extends WidgetItemOverlay {
    private final TithePlugin plugin;
    private final TitheConfig config;

    public int fruits_inventory = 0;

    public TitheOverlayInventory(final TithePlugin plugin, final TitheConfig config) {
        this.plugin = plugin;
        this.config = config;
        showOnInventory();
    }

    public void onItemContainerChanged(final ItemContainerChanged event) {
        if (event.getContainerId() != InventoryID.INVENTORY.getId()) return;

        final ItemContainer inventory = event.getItemContainer();
        this.fruits_inventory = inventory.count(ItemID.GOLOVANOVA_FRUIT) + inventory.count(ItemID.BOLOGANO_FRUIT) + inventory.count(ItemID.LOGAVANO_FRUIT);
    }

    @Override
    public void renderItemOverlay(final Graphics2D graphics, int item_id, final WidgetItem widget_item) {
        if (!plugin.inTitheFarm() || config.getHighlightSeedsColor().getAlpha() == 0) return;

        switch (item_id) {
            case ItemID.GOLOVANOVA_SEED:
            case ItemID.BOLOGANO_SEED:
            case ItemID.LOGAVANO_SEED:
                graphics.setColor(config.getHighlightSeedsColor());
                graphics.fill(widget_item.getCanvasBounds());
                return;
            case ItemID.WATERING_CAN1:
            case ItemID.WATERING_CAN2:
            case ItemID.WATERING_CAN3:
            case ItemID.WATERING_CAN4:
            case ItemID.WATERING_CAN5:
            case ItemID.WATERING_CAN6:
            case ItemID.WATERING_CAN7:
            case ItemID.WATERING_CAN8:
            case ItemID.GRICOLLERS_CAN:
                graphics.setColor(config.getHighlightWateringCanColor());
                graphics.fill(widget_item.getCanvasBounds());
                return;
        }
    }
}