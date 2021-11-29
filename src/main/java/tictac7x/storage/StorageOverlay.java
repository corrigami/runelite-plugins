package tictac7x.storage;

import com.google.gson.JsonElement;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ComponentOrientation;
import net.runelite.client.ui.overlay.components.ImageComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import tictac7x.Overlay;

import java.awt.*;
import java.util.Map;

public class StorageOverlay extends Overlay {
    private final Storage storage;
    private final ItemManager items;

    private final PanelComponent panel = new PanelComponent();

    public StorageOverlay(final Storage storage, final ItemManager items) {
        this.storage = storage;
        this.items = items;

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

        for (final Map.Entry<String, JsonElement> item : storage.getStorage().entrySet()) {
            final int id = Integer.parseInt(item.getKey());
            final String name = items.getItemComposition(id).getName();
            final int quantity = item.getValue().getAsInt();

            // Placeholder check.
            if (quantity < 1) {
                continue;

            // Whitelist check.
            } else if (storage.isWhitelistEnabled() && !storage.getWhitelist().has(name)) {
                continue;

            // Blacklist check.
            } else if (storage.isBlacklistEnabled() && storage.getBlacklist().has(name)) {
                continue;
            }

            panel.getChildren().add(new ImageComponent(items.getImage(id, quantity, true)));
        }

        if (!panel.getChildren().isEmpty()) {
            panelComponent.getChildren().add(panel);
            return super.render(graphics);
        } else {
            return null;
        }
    }
}
