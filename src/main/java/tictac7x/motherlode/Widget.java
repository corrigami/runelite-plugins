package tictac7x.motherlode;

import net.runelite.api.Client;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

public class Widget extends OverlayPanel {
    private final Client client;
    private final TicTac7xMotherlodeConfig config;
    private final Motherlode motherlode;
    private final Character character;

    private final int SACK_WIDGET_GROUP = 382;
    private final int SACK_WIDGET_CHILD = 0;

    public Widget(final Client client, final TicTac7xMotherlodeConfig config, final Motherlode motherlode, final Character character) {
        this.client = client;
        this.config = config;
        this.motherlode = motherlode;
        this.character = character;

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

    public void shutDown() {
        toggleOriginalWidget(false);
    }

    private void toggleOriginalWidget(final boolean hidden) {
        final net.runelite.api.widgets.Widget widget = client.getWidget(SACK_WIDGET_GROUP, SACK_WIDGET_CHILD);

        if (widget != null) {
            widget.setHidden(hidden);
        }
    }

    @Override
    public Dimension render(final Graphics2D graphics2D) {
        if (!character.isInMotherlode() || !config.showCustomSackWidget()) return null;

        panelComponent.getChildren().clear();

        setPreferredColor(
            motherlode.shouldStopMining() ? new Color(255,0,0, 70) :
            motherlode.sackCanBeMoreThanFull() ? new Color(255, 165, 0, 70) :
            motherlode.shouldDepositPaydirt() ? new Color(0, 255, 0, 70) :
            null
        );


        if (config.showGoldenNuggetsTotal() || config.showGoldenNuggetsSession()) {
            String nuggets = "";

            if (config.showGoldenNuggetsTotal()) {
                nuggets += motherlode.getGoldenNuggetsTotal();
            }

            if (config.showGoldenNuggetsSession()) {
                nuggets += config.showGoldenNuggetsTotal() ? " (+" + motherlode.getGoldenNuggetsSession() + ")" : motherlode.getGoldenNuggetsSession();
            }

            panelComponent.getChildren().add(LineComponent.builder()
                .left("Nuggets:")
                .right(nuggets).rightColor(Color.ORANGE)
                .build()
            );
        }

        if (config.showSackAndHopperPaydirt() || config.showSackSize()) {
            String paydirt = "";

            if (config.showSackAndHopperPaydirt()) {
                paydirt += motherlode.getDepositedPaydirt();

                if (config.showSackPaydirtFromInventory()) {
                    paydirt += " + " + motherlode.getInventoryPaydirt();
                }
            }

            if (config.showSackSize()) {
                if (config.showSackAndHopperPaydirt()) {
                    paydirt += " / ";
                }

                paydirt += motherlode.getSackSize();
            }

            panelComponent.getChildren().add(LineComponent.builder().left("Sack:").right(paydirt).build());
        }

        if (config.showSackDeposits()) {
            panelComponent.getChildren().add(LineComponent.builder()
                .left("Deposits:")
                .right(motherlode.getDepositsLeft() + "")
                .build()
            );
        }

        if (config.showSackNeeded()) {
            panelComponent.getChildren().add(LineComponent.builder()
                .left("Needed:")
                .right(motherlode.getNeededPaydirt() + "").rightColor(
                    (motherlode.getSpaceLeftToDeposit() == 0 || motherlode.getNeededPaydirt() == 0) ? Color.WHITE :
                    motherlode.getNeededPaydirt() < 0 ? Color.RED :
                    Color.GREEN )
                .build()
            );
        }

        return super.render(graphics2D);
    }
}
