package tictac7x.motherlode;

import net.runelite.api.Client;
import net.runelite.api.Varbits;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;


public class Sack extends OverlayPanel {
    private final Client client;
    private final TicTac7xMotherlodeConfig config;
    private final Bank bank;
    private final Inventory inventory;
    private final Hopper hopper;

    private final int SACK_SIZE_SMALL = 108;
    private final int SACK_SIZE_UPGRADED = 189;

    private final int SACK_WIDGET_GROUP = 382;
    private final int SACK_WIDGET_CHILD = 0;

    public Sack(final Client client, final TicTac7xMotherlodeConfig config, final Bank bank, final Inventory inventory, final Hopper hopper) {
        this.client = client;
        this.config = config;
        this.bank = bank;
        this.inventory = inventory;
        this.hopper = hopper;

        setPosition(OverlayPosition.TOP_LEFT);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        toggleOriginalWidget(config.showCustomSackWidget());
    }

    // On first widget load.
    public void onWidgetLoaded(final WidgetLoaded event) {
        if (event.getGroupId() == SACK_WIDGET_GROUP) {
            toggleOriginalWidget(config.showCustomSackWidget());
        }
    }

    // Widget needs to be toggled based on config changed.
    public void onConfigChanged(final ConfigChanged event) {
        if (event.getGroup().equals(TicTac7xMotherlodeConfig.group) && event.getKey().equals(TicTac7xMotherlodeConfig.sack_custom)) {
            toggleOriginalWidget(config.showCustomSackWidget());
        }
    }





    private int getPaydirtInSackFromVarbit() {
        return client.getVarbitValue(Varbits.SACK_NUMBER);
    }

    private int getPaydirtInSack() {
        return getPaydirtInSackFromVarbit() + hopper.getPaydirt();
    }

    private boolean isSackUpgraded() {
        return client.getVarbitValue(Varbits.SACK_UPGRADED) == 1;
    }

    private int getSackSize() {
        return isSackUpgraded() ? SACK_SIZE_UPGRADED : SACK_SIZE_SMALL;
    }

    private int getSpaceLeftInSack() {
        return getSackSize() - getPaydirtInSack();
    }

    public int getNeededPaydirt() {
        return Math.min(getSackSize() - getPaydirtInSack(), inventory.getMaximumAmountOfPaydirtThatCanBeHold()) - inventory.getAmountOfPayDirtCurrentlyInInventory();
    }

    private int getDepositsLeft() {
        return inventory.getMaximumAmountOfPaydirtThatCanBeHold() == 0 ? 0 : (int) Math.ceil((double) getSpaceLeftInSack() / inventory.getMaximumAmountOfPaydirtThatCanBeHold());
    }

    public boolean isAdditionalPaydirtNotNeeded() {
        return getSpaceLeftInSack() - inventory.getAmountOfPayDirtCurrentlyInInventory() < 0;
    }

    private void toggleOriginalWidget(final boolean hidden) {
        final Widget widget = client.getWidget(SACK_WIDGET_GROUP, SACK_WIDGET_CHILD);

        if (widget != null) {
            widget.setHidden(hidden);
        }
    }

    public void shutDown() {
        toggleOriginalWidget(false);
    }

    @Override
    public Dimension render(final Graphics2D graphics2D) {
        if (!config.showCustomSackWidget()) return null;
        panelComponent.getChildren().clear();

        setPreferredColor(
            (getSpaceLeftInSack() == 0 || getNeededPaydirt() < 0) ? new Color(255,0,0, 70) :
            getNeededPaydirt() == 0 && inventory.getAmountOfPayDirtCurrentlyInInventory() > 0 ? new Color(0, 255, 0, 70) :
            null
        );

        panelComponent.getChildren().add(LineComponent.builder()
            .left("Nuggets:")
            .right(bank.getGoldenNuggets() + "").rightColor(new Color(255, 180, 0))
            .bounds(new Rectangle(10, 10, 10, 10))
            .build()
        );

        if (config.showSackPaydirt() || config.showSackSize()) {
            String sack = "";

            if (config.showSackPaydirt()) {
                sack += getPaydirtInSack();

                if (config.showSackPaydirtFromInventory()) {
                    sack += " + " + inventory.getAmountOfPayDirtCurrentlyInInventory();
                }
            }

            if (config.showSackSize()) {
                if (config.showSackPaydirt()) {
                    sack += " / ";
                }

                sack += getSackSize();
            }

            panelComponent.getChildren().add(LineComponent.builder()
                .left("Sack:")
                .right(sack)
                .build()
            );
        }

        panelComponent.getChildren().add(LineComponent.builder()
            .left("Deposits:")
            .right(getDepositsLeft() + "")
            .build()
        );

        panelComponent.getChildren().add(LineComponent.builder()
            .left("Needed:")
            .right(getNeededPaydirt() + "").rightColor(
                getSpaceLeftInSack() == 0 ? Color.WHITE :
                getNeededPaydirt() < 0 ? Color.RED :
                (getSpaceLeftInSack() < inventory.getMaximumAmountOfPaydirtThatCanBeHold()) ? Color.YELLOW :
                Color.GREEN )
            .build()
        );

        return super.render(graphics2D);
    }
}
