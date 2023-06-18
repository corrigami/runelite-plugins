package tictac7x.gotr;

import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.time.Duration;
import java.time.Instant;

public class Panel extends OverlayPanel {
    private final TicTac7xGotrImprovedConfig config;
    private final Timer timer;
    private final Energy energy;
    private final Portal portal;
    private final PanelComponent panel = panelComponent;

    public Panel(final TicTac7xGotrImprovedConfig config, final Timer timer, final Energy energy, final Portal portal) {
        this.config = config;
        this.timer = timer;
        this.energy = energy;
        this.portal = portal;

        setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        panel.getChildren().clear();

        // Time to start.
        if (timer.getTime().isPresent()) {
            final long seconds = Duration.between(Instant.now(), timer.getTime().get()).getSeconds();

            if (seconds > 0) {
                panel.getChildren().add(LineComponent.builder()
                    .left("Time to start:")
                    .right(String.valueOf(seconds))
                    .build());
            }
        }

        // Time to portal.
        if (portal.getTimeToPortal().isPresent()) {
            final long seconds = Duration.between(Instant.now(), portal.getTimeToPortal().get()).getSeconds();

            panel.getChildren().add(LineComponent.builder()
                .left("Time to portal:")
                .right(seconds+"s")
                .rightColor(portal.getTimeToPortalColor(seconds, true))
                .build());
        }

        // Points.
        panel.getChildren().add(LineComponent.builder()
            .left("Points")
            .right(energy.getElementalEnergy() + "/" + energy.getCatalyticEnergy())
            .build());

        if (panel.getChildren().size() == 0) return null;
        return super.render(graphics);
    }
}
