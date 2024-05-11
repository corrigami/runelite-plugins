package tictac7x.motherlode;

import net.runelite.api.Client;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;

import javax.annotation.Nullable;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class MotherlodeSackWidget extends OverlayPanel {
    private final MotherlodeConfig config;
    private final Motherlode motherlode;
    private final Client client;
    private final MotherlodeSack sack;
    private final Inventory inventory;
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
        panelComponent.setBorder(new Rectangle(0, 0, 0, 0));
    }

    public void loadNativeWidget() {
        final Widget widget = client.getWidget(WidgetInfo.MOTHERLODE_MINE);

        if (widget != null) {
            this.widget = widget;
            updateMotherlodeNativeWidget(config.showSackPaydirt() || config.showSackNeeded() || config.showSackDeposits());
        }
    }

    public void onWidgetLoaded(final WidgetLoaded event) {
        if (event.getGroupId() == WidgetInfo.MOTHERLODE_MINE.getGroupId()) {
            loadNativeWidget();
        }
    }

    public void onConfigChanged(final ConfigChanged event) {
        if (event.getGroup().equals(MotherlodeConfig.group)) {
            updateMotherlodeNativeWidget(config.showSackPaydirt() || config.showSackNeeded() || config.showSackDeposits());
        }
    }

    public void updateMotherlodeNativeWidget(final boolean hidden) {
        if (widget != null) widget.setHidden(hidden);
    }

    private int getTotalPayDirtCount() {
        return inventory.getAmountOfPayDirtCurrentlyInInventory() + sack.countPayDirt();
    }

    /**
     * Calculate whether the amount of total pay-dirt is perfect.
     * @param pay_dirt_needed - Needed pay dirt.
     * @return true if total pay-dirt is perfect.
     */
    private boolean isPayDirtTotalPerfect(final int pay_dirt_needed) {
        return (
            this.getTotalPayDirtCount() == sack.getSize() && inventory.getAmountOfPayDirtCurrentlyInInventory() != 0 ||
            sack.countPayDirt() == sack.getSize() && inventory.getAmountOfPayDirtCurrentlyInInventory() == inventory.getSize() ||
            sack.countPayDirt() == sack.getSize() && pay_dirt_needed == 0
        );
    }

    @Override
    public Dimension render(final Graphics2D graphics) {
        if (!motherlode.inRegion() || !config.showSackDeposits() && !config.showSackNeeded() && !config.showSackPaydirt()) return null;
        final int pay_dirt_needed = motherlode.getPayDirtNeeded();
        final int deposits_left = motherlode.getDepositsLeft();

        panelComponent.getChildren().clear();
        panel.getChildren().clear();

        // Panel background color.
        final Color color_background =
            (this.isPayDirtTotalPerfect(pay_dirt_needed) || pay_dirt_needed == 0 && inventory.getAmountOfPayDirtCurrentlyInInventory() > 0) ? Color.green :
            (sack.isFull() || pay_dirt_needed < 0) ? Color.red :
            (sack.shouldBeEmptied()) ? Color.orange :
            null;
        panel.setBackgroundColor(color_background == null ? null : new Color(color_background.getRed(), color_background.getGreen(), color_background.getBlue(), 80));

        // Sack Pay-dirt count.
        if (config.showSackPaydirt()) {
            panel.getChildren().add(LineComponent.builder()
                .left("Sack:").leftColor(color_background != null ? Color.white : Color.lightGray)
                .right(String.valueOf(sack.countPayDirt())).rightColor(Color.white)
                .build()
            );
        }

        // Inventory deposits left.
        if (config.showSackDeposits()) {
            panel.getChildren().add(LineComponent.builder()
                .left("Deposits:").leftColor(color_background != null ? Color.white : Color.lightGray)
                .right(String.valueOf(deposits_left)).rightColor(Color.white)
                .build()
            );
        }

        // Pay-dirt needed to mine.
        if (config.showSackNeeded()) {
            final Color color_needed = (color_background != null || pay_dirt_needed == 0) ? Color.white : Color.green;
            panel.getChildren().add(LineComponent.builder()
                .left("Needed:").leftColor(Color.white)
                .right(String.valueOf(pay_dirt_needed)).rightColor(color_needed)
                .build()
            );
        }

        panelComponent.getChildren().add(panel);
        return super.render(graphics);
    }
}
