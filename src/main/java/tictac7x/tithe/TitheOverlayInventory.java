package tictac7x.tithe;

import net.runelite.api.ItemID;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.ui.overlay.WidgetItemOverlay;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public class TitheOverlayInventory extends WidgetItemOverlay {
    private final TithePlugin plugin;
    private final TitheConfig config;

    public TitheOverlayInventory(final TithePlugin plugin, final TitheConfig config) {
        this.plugin = plugin;
        this.config = config;
        showOnInventory();
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
                if (config.getHighlightFarmersOutfitColor().getAlpha() > 0 && plugin.seeds_inventory == 0 && plugin.plants.values().stream().allMatch(plant -> plant.isBlighted())) {
                    final Rectangle rectangle = widget_item.getCanvasBounds();
                    graphics.setColor(config.getHighlightFarmersOutfitColor());
                    graphics.fill(rectangle);
                }
                return;
        }
    }
}