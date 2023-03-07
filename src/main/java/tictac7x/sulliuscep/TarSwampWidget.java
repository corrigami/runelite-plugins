package tictac7x.sulliuscep;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

public class TarSwampWidget extends OverlayPanel {
    private final SulliuscepConfig config;
    private final TarSwamp tar_swamp;

    public TarSwampWidget(final SulliuscepConfig config, final TarSwamp tar_swamp) {
        this.config = config;
        this.tar_swamp = tar_swamp;

        setPosition(OverlayPosition.TOP_LEFT);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (config.showMudPitWidget() && tar_swamp.inRegion() && !tar_swamp.isPitFilled()) {
            panelComponent.getChildren().clear();
            panelComponent.setBackgroundColor(config.getMudPitColor());

            panelComponent.getChildren().add(
                TitleComponent.builder()
                .text("MUD PIT EMPTY!")
                .build()
            );

            return super.render(graphics);
        }

        return null;
    }
}
