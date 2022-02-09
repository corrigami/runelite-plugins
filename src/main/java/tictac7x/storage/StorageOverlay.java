package tictac7x.storage;

import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ComponentOrientation;
import net.runelite.client.ui.overlay.components.ImageComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import tictac7x.Overlay;

import java.awt.*;

public class StorageOverlay extends Overlay {
    private final Storage storage;

    private final PanelComponent panel = new PanelComponent();

    public StorageOverlay(final Storage storage) {
        this.storage = storage;

        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPosition(OverlayPosition.TOP_RIGHT);

        panel.setWrap(true);
        panel.setOrientation(ComponentOrientation.HORIZONTAL);
        panel.setBackgroundColor(null);

        panelComponent.setOrientation(ComponentOrientation.VERTICAL);
        panelComponent.setBorder(new Rectangle(0,0,0,0));
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        panelComponent.getChildren().clear();
        panel.getChildren().clear();

        // Storage overlay hidden.
        if (!storage.show()) return null;

        // Render storage visible items.
        for (final ImageComponent item : storage.getStorageImages()) {
            panel.getChildren().add(item);
        }

        // Overlay has items, render it.
        if (!panel.getChildren().isEmpty()) {
            panelComponent.getChildren().add(panel);
            return super.render(graphics);
        }

        // Overlay empty, don't render it.
        return null;
    }
}
