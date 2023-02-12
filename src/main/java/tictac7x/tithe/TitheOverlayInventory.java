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
    public int seeds_inventory = 0;

    public TitheOverlayInventory(final TithePlugin plugin, final TitheConfig config) {
        this.plugin = plugin;
        this.config = config;
        showOnInventory();
    }

    public void onItemContainerChanged(final ItemContainerChanged event) {
        if (event.getContainerId() == InventoryID.INVENTORY.getId()) {
            final ItemContainer inventory = event.getItemContainer();
            this.fruits_inventory = inventory.count(ItemID.GOLOVANOVA_FRUIT) + inventory.count(ItemID.BOLOGANO_FRUIT) + inventory.count(ItemID.LOGAVANO_FRUIT);
            this.seeds_inventory = inventory.count(ItemID.GOLOVANOVA_SEED) + inventory.count(ItemID.BOLOGANO_SEED) + inventory.count(ItemID.LOGAVANO_SEED);
        }
    }

    @Override
    public void renderItemOverlay(final Graphics2D graphics, int item_id, final WidgetItem widget_item) {
        if (!plugin.inTitheFarm()) return;

        switch (item_id) {
            case ItemID.GOLOVANOVA_SEED:
            case ItemID.BOLOGANO_SEED:
            case ItemID.LOGAVANO_SEED:
                if (config.getHighlightSeedsColor().getAlpha() > 0) {
                    final Rectangle rectangle = widget_item.getCanvasBounds();
                    graphics.setColor(config.getHighlightSeedsColor());
                    graphics.fill(rectangle);
                }
                return;
            case ItemID.FARMERS_STRAWHAT:
            case ItemID.FARMERS_JACKET:
            case ItemID.FARMERS_BORO_TROUSERS:
            case ItemID.FARMERS_BOOTS:
            case ItemID.FARMERS_SHIRT:
                if (config.getHighlightFarmersOutfitColor().getAlpha() > 0 && this.seeds_inventory == 0 && plugin.nonBlightedPlants() == 0) {
                    final Rectangle rectangle = widget_item.getCanvasBounds();
                    graphics.setColor(config.getHighlightFarmersOutfitColor());
                    graphics.fill(rectangle);
                }
                return;
        }
    }
}