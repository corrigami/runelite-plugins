package tictac7x.tithe;

import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.client.events.ConfigChanged;
import tictac7x.Overlay;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import net.runelite.api.ItemID;
import net.runelite.api.Client;
import net.runelite.api.Varbits;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;

public class TitheOverlayPoints extends Overlay {
    private final TithePlugin plugin;
    private final TitheConfig config;
    private final Client client;

    private final static int TITHE_FARM_POINTS = Varbits.TITHE_FARM_POINTS;
    private final static int TITHE_FARM_SACK = Varbits.TITHE_FARM_SACK_AMOUNT;
    private final static int TITHE_FARM_SACK_TOTAL = 100;
    private final static int TITHE_FARM_POINTS_BREAK = 74;

    public TitheOverlayPoints(final TithePlugin plugin, final TitheConfig config, final Client client) {
        this.plugin = plugin;
        this.config = config;
        this.client = client;

        setPosition(OverlayPosition.TOP_LEFT);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
    }

    public void onWidgetLoaded(final WidgetLoaded event) {
        // Not tithe points widget.
        if (!plugin.inTitheFarm() || event.getGroupId() != WidgetInfo.TITHE_FARM.getGroupId()) return;

        if (config.showCustomPoints()) {
            this.hideNativePoints();
        } else {
            this.showNativePoints();
        }
    }

    public void onConfigChanged(final ConfigChanged event) {
        // Wrong config changed.
        if (!plugin.inTitheFarm() || !event.getGroup().equals(TitheConfig.group) || !event.getKey().equals(TitheConfig.points)) return;

        // Correct config changed.
        this.checkWidget();
    }

    public void showNativePoints() {
        final Widget widget_tithe = client.getWidget(WidgetInfo.TITHE_FARM);
        if (widget_tithe != null) widget_tithe.setHidden(false);
    }

    public void hideNativePoints() {
        final Widget widget_tithe = client.getWidget(WidgetInfo.TITHE_FARM);
        if (widget_tithe != null) widget_tithe.setHidden(true);
    }

    public void startUp() {
        this.checkWidget();
    }

    public void shutDown() {
        showNativePoints();
    }

    private void checkWidget() {
        if (config.showCustomPoints()) {
            this.hideNativePoints();
        } else {
            this.showNativePoints();
        }
    }

    public void onVarbitChanged(final VarbitChanged event) {
        if (!plugin.inTitheFarm()) return;

        switch (event.getVarbitId()) {
            case TITHE_FARM_POINTS:
                plugin.points_total = event.getValue();
                return;
            case TITHE_FARM_SACK:
                plugin.fruits_sack = event.getValue();
                return;
        }
    }

    public void onItemContainerChanged(final ItemContainerChanged event) {
        if (event.getContainerId() == InventoryID.INVENTORY.getId()) {
            final ItemContainer inventory = event.getItemContainer();
            plugin.fruits_inventory = inventory.count(ItemID.GOLOVANOVA_FRUIT) + inventory.count(ItemID.BOLOGANO_FRUIT) + inventory.count(ItemID.LOGAVANO_FRUIT);
            plugin.seeds_inventory = inventory.count(ItemID.GOLOVANOVA_SEED) + inventory.count(ItemID.BOLOGANO_SEED) + inventory.count(ItemID.LOGAVANO_SEED);
        }
    }

    @Override
    public Dimension render(final Graphics2D graphics) {
        if (!plugin.inTitheFarm() || !config.showCustomPoints()) return null;

        final int fruits = plugin.fruits_sack + plugin.fruits_inventory;
        final int fruits_possible = fruits + plugin.seeds_inventory + (int) plugin.plants.values().stream().filter(plant -> !plant.isBlighted()).count();
        final int points_earned = Math.max(0, fruits - TITHE_FARM_POINTS_BREAK);

        panelComponent.getChildren().clear();

        // Total points.
        panelComponent.getChildren().add(LineComponent.builder()
            .left("Total Points:").leftColor(color_gray)
            .right(String.valueOf(plugin.points_total)).rightColor(color_orange)
            .build()
        );

        // Points earned.
        panelComponent.getChildren().add(LineComponent.builder()
            .left("Points earned:").leftColor(color_gray)
            .right(String.valueOf(points_earned)).rightColor(fruits_possible == TITHE_FARM_SACK_TOTAL ? Color.green : fruits_possible <= TITHE_FARM_POINTS_BREAK ? Color.red : Color.yellow)
            .build()
        );

        return super.render(graphics);
    }
}
