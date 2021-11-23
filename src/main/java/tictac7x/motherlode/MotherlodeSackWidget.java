package tictac7x.motherlode;

import net.runelite.api.events.WidgetLoaded;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import tictac7x.Overlay;

import java.awt.*;


public class MotherlodeSackWidget extends Overlay {
    private final Motherlode motherlode;
    private final MotherlodeSack sack;
    private final MotherlodeInventory inventory;
    private final PanelComponent panel = new PanelComponent();

    public MotherlodeSackWidget(final Motherlode motherlode) {
        this.motherlode = motherlode;
        this.sack = motherlode.getSack();
        this.inventory = motherlode.getInventory();

        setPosition(OverlayPosition.TOP_LEFT);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        panelComponent.setBorder(new Rectangle(0,0,0,0));
    }

    public void onWidgetLoaded(final WidgetLoaded event) {

    }

    private int getTotalPayDirtCount() {
        return inventory.countPayDirt() + sack.countPayDirt();
    }

    private boolean isPayDirtTotalPerfect(final int pay_dirt_needed) {
        return (
            this.getTotalPayDirtCount() == sack.getSize() && inventory.countPayDirt() != 0 ||
            sack.countPayDirt() == sack.getSize() && inventory.countPayDirt() == inventory.getSize() ||
            sack.countPayDirt() == sack.getSize() && pay_dirt_needed == 0
        );
    }

    @Override
    public Dimension render(final Graphics2D graphics) {
        if (!motherlode.inRegion()) return null;
        final int pay_dirt_needed = motherlode.getPayDirtNeeded();

        panelComponent.getChildren().clear();
        panel.getChildren().clear();

        // Panel background color.
        final Color color_background =
            (this.isPayDirtTotalPerfect(pay_dirt_needed)) ? color_green :
            (sack.isFull() || pay_dirt_needed < 0) ? color_red :
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
