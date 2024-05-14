package tictac7x.motherlode;

import net.runelite.api.Client;
import net.runelite.api.Varbits;
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


public class Sack extends OverlayPanel {
    private final Client client;
    private TicTac7xMotherlodeConfig config;
    private Inventory inventory;

    private final Color defaultPanelColor;
    private final int SACK_SIZE_SMALL = 108;
    private final int SACK_SIZE_UPGRADED = 189;
    private final int ANIMATION_HOPPER_DEPOSIT = 832;
    private final int SACK_WIDGET_GROUP = 382;
    private final int SACK_WIDGET_CHILD = 0;

    public Sack(final Client client, final TicTac7xMotherlodeConfig config, final Inventory inventory) {
        this.client = client;
        this.config = config;
        this.inventory = inventory;
        this.defaultPanelColor = getPanelComponent().getBackgroundColor();

        setPosition(OverlayPosition.TOP_LEFT);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        toggleOriginalWidget(config.showCustomSackWidget());
    }

    private int getPaydirtInSackFromVarbit() {
        return client.getVarbitValue(Varbits.SACK_NUMBER);
    }

    private int getPaydirtInSack() {
        return getPaydirtInSackFromVarbit();
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

    @Override
    public Dimension render(final Graphics2D graphics2D) {
        if (!config.showCustomSackWidget()) return null;
        panelComponent.getChildren().clear();
        panelComponent.setBackgroundColor(getNeededPaydirt() < 0 ? new Color(255,0,0, 70) : getNeededPaydirt() == 0 ? new Color(0, 255, 0, 70) : defaultPanelColor);

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
                .left("Sack:").leftColor(Color.white)
                .right(sack)
                .build()
            );
        }

        panelComponent.getChildren().add(LineComponent.builder()
            .left("Deposits:").leftColor(Color.white)
            .right(getDepositsLeft() + "")
            .build()
        );

        panelComponent.getChildren().add(LineComponent.builder()
            .left("Needed:").leftColor(Color.white)
            .right(getNeededPaydirt() + "").rightColor(
                getNeededPaydirt() < 0 ? Color.RED :
                (getSpaceLeftInSack() < inventory.getMaximumAmountOfPaydirtThatCanBeHold()) ? Color.YELLOW :
                Color.GREEN )
            .build()
        );

        return super.render(graphics2D);
    }

    public void onWidgetLoaded(final WidgetLoaded event) {
        if (event.getGroupId() == SACK_WIDGET_GROUP) {
            toggleOriginalWidget(config.showCustomSackWidget());
        }
    }

    public void onConfigChanged(final ConfigChanged event) {
        if (event.getGroup().equals(TicTac7xMotherlodeConfig.group) && event.getKey().equals(TicTac7xMotherlodeConfig.sack_custom)) {
            toggleOriginalWidget(config.showCustomSackWidget());
        }
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
}
