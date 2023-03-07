package tictac7x.sulliuscep;

import tictac7x.Overlay;

import java.awt.Dimension;
import java.awt.Graphics2D;

import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

public class TarSwampWidget extends Overlay {
    private final SulliuscepConfig config;
    private final TarSwamp tar_swamp;
    private final PanelComponent panel = panelComponent;

    public TarSwampWidget(final SulliuscepConfig config, final TarSwamp tar_swamp) {
        this.config = config;
        this.tar_swamp = tar_swamp;

        setPosition(OverlayPosition.TOP_LEFT);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (config.showMudPitWidget() && tar_swamp.inRegion() && !tar_swamp.isPitFilled()) {
            panel.getChildren().clear();
            panel.setBackgroundColor(getPanelBackgroundColor(color_red));

            panel.getChildren().add(
                TitleComponent.builder()
                .text("MUD PIT EMPTY!")
                .build()
            );

            return super.render(graphics);
        }

        return null;
    }
}
