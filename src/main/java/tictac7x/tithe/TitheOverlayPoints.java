package tictac7x.tithe;

import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.ui.overlay.OverlayPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import net.runelite.api.Client;
import net.runelite.api.Varbits;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;

public class TitheOverlayPoints extends OverlayPanel {
    private final static int TITHE_FARM_POINTS = Varbits.TITHE_FARM_POINTS;
    private final static int TITHE_FARM_SACK = Varbits.TITHE_FARM_SACK_AMOUNT;

    private final TithePlugin plugin;
    private final TitheConfig config;
    private final Client client;

    public int points_total = 0;
    public int fruits_sack = 0;


    public TitheOverlayPoints(final TithePlugin plugin, final TitheConfig config, final Client client) {
        this.plugin = plugin;
        this.config = config;
        this.client = client;

        setPosition(OverlayPosition.TOP_LEFT);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
    }

    public void onWidgetLoaded(final WidgetLoaded event) {
        // Not tithe points widget.
        if (event.getGroupId() != WidgetInfo.TITHE_FARM.getGroupId()) return;

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
        switch (event.getVarbitId()) {
            case TITHE_FARM_POINTS:
                this.points_total = event.getValue();
                return;
            case TITHE_FARM_SACK:
                this.fruits_sack = event.getValue();
                return;
        }
    }

    @Override
    public Dimension render(final Graphics2D graphics) {
        if (!plugin.inTitheFarm() || !config.showCustomPoints()) return null;

        final int fruits = plugin.fruitsInInventory();
        final int points_earned = fruits / 3 + fruits / 100 * 2;

        panelComponent.getChildren().clear();

        // Points.
        panelComponent.getChildren().add(LineComponent.builder()
            .left("Points:").leftColor(new Color(200, 200, 200))
            .right(this.points_total + (points_earned > 0 ? " + " + points_earned : "")).rightColor(Color.green)
            .build()
        );

        return super.render(graphics);
    }
}
