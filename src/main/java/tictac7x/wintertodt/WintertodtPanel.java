package tictac7x.wintertodt;

import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemID;
import net.runelite.api.Varbits;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

public class WintertodtPanel extends OverlayPanel {
    private final Client client;
    private final WintertodtConfig config;

    private Widget widget_points = null;
    private int inventory_brumas = 0;

    private final static int WIDGET_POINTS_GROUP = 396;
    private final static int WIDGET_POINTS_CHILD = 6;

    public WintertodtPanel(final Client client, final WintertodtConfig config) {
        this.client = client;
        this.config = config;
        setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
    }

    public void onWidgetLoaded(final WidgetLoaded event) {
        if (event.getGroupId() == WIDGET_POINTS_GROUP) {
            widget_points = client.getWidget(WIDGET_POINTS_GROUP, WIDGET_POINTS_CHILD);
        }
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (widget_points == null || widget_points.isHidden() || !config.showGoalPanel()) return null;

        final int points = Integer.parseInt(widget_points.getText().replaceAll("Points<br>", ""));
        final int logs_needed = Math.max(0, (config.getGoal() - points) / (config.isFletchingForGoal() ? 25 : 10) - inventory_brumas);

        panelComponent.getChildren().clear();
        panelComponent.getChildren().add(LineComponent.builder()
            .left("Logs left:")
            .right(String.valueOf(logs_needed))
            .rightColor(logs_needed > 0 ? Color.green : Color.red)
            .build()
        );

        return super.render(graphics);
    }

    public void onItemContainerChanged(final ItemContainerChanged event) {
        if (event.getContainerId() != InventoryID.INVENTORY.getId()) return;

        final ItemContainer inventory = event.getItemContainer();
        inventory_brumas = inventory.count(ItemID.BRUMA_ROOT) + inventory.count(ItemID.BRUMA_KINDLING);
    }
}
