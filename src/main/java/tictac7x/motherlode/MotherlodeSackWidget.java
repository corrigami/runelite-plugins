package tictac7x.motherlode;

import tictac7x.Overlay;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;

import javax.annotation.Nullable;

public class MotherlodeSackWidget extends Overlay {
    private final MotherlodeConfig config;
    private final Motherlode motherlode;
    private final Client client;
    private final MotherlodeSack sack;
    private final MotherlodeInventory inventory;
    private final PanelComponent panel = new PanelComponent();

    @Nullable
    private Widget widget = null;

    public MotherlodeSackWidget(final MotherlodeConfig config, final Motherlode motherlode, final Client client) {
        this.config = config;
        this.motherlode = motherlode;
        this.client = client;
        this.sack = motherlode.getSack();
        this.inventory = motherlode.getInventory();

        setPosition(OverlayPosition.TOP_LEFT);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        panelComponent.setBorder(new Rectangle(0,0,0,0));
    }

    public void loadNativeWidget() {
        final Widget widget = client.getWidget(WidgetInfo.MOTHERLODE_MINE);

        if (widget != null) {
            this.widget = widget;
            updateMotherlodeNativeWidget(config.showCustomSackWidget());
        }
    }

    public void onWidgetLoaded(final WidgetLoaded event) {
        if (event.getGroupId() == WidgetInfo.MOTHERLODE_MINE.getGroupId()) {
            loadNativeWidget();
        }
    }

    public void onConfigChanged(final ConfigChanged event) {
        if (event.getGroup().equals(MotherlodeConfig.group) && event.getKey().equals(MotherlodeConfig.custom_sack_widget)) {
            updateMotherlodeNativeWidget(config.showCustomSackWidget());
        }
    }

    public void updateMotherlodeNativeWidget(final boolean hidden) {
        if (widget != null) widget.setHidden(hidden);
    }

    private int getTotalPayDirtCount() {
        return inventory.countPayDirt() + sack.countPayDirt();
    }

    /**
     * Calculate whether the amount of total pay-dirt is perfect.
     * @param pay_dirt_needed - Needed pay dirt.
     * @return true if total pay-dirt is perfect.
     */
    private boolean isPayDirtTotalPerfect(final int pay_dirt_needed) {
        return (
            this.getTotalPayDirtCount() == sack.getSize() && inventory.countPayDirt() != 0 ||
            sack.countPayDirt() == sack.getSize() && inventory.countPayDirt() == inventory.getSize() ||
            sack.countPayDirt() == sack.getSize() && pay_dirt_needed == 0
        );
    }

    @Override
    public Dimension render(final Graphics2D graphics) {
        if (!motherlode.inRegion() || !config.showCustomSackWidget()) return null;
        final int pay_dirt_needed = motherlode.getPayDirtNeeded();

        panelComponent.getChildren().clear();
        panel.getChildren().clear();

        // Panel background color.
        final Color color_background =
            (this.isPayDirtTotalPerfect(pay_dirt_needed) || pay_dirt_needed == 0 && inventory.countPayDirt() > 0) ? color_green :
            (sack.isFull() || pay_dirt_needed < 0) ? color_red :
            (sack.shouldBeEmptied()) ? color_yellow :
            null;
        panel.setBackgroundColor(getPanelBackgroundColor(color_background));

        // Sack Pay-dirt count.
        panel.getChildren().add(LineComponent.builder()
            .left("Sack:").leftColor(color_background != null ? color_white : color_orange)
            .right(String.valueOf(sack.countPayDirt())).rightColor(color_white)
            .build()
        );

        // Pay-dirt needed to mine.
        final Color color_needed = (color_background != null || pay_dirt_needed == 0) ? color_white : color_green;
        panel.getChildren().add(LineComponent.builder()
            .left("Needed:").leftColor(color_background != null ? color_white : color_orange)
            .right(String.valueOf(pay_dirt_needed)).rightColor(color_needed)
            .build()
        );

        panelComponent.getChildren().add(panel);
        return super.render(graphics);
    }
}
