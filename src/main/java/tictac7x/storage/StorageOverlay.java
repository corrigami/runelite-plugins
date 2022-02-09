package tictac7x.storage;

import com.google.gson.JsonElement;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemID;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.*;
import net.runelite.client.ui.overlay.components.ComponentOrientation;
import net.runelite.client.ui.overlay.components.TextComponent;
import net.runelite.client.ui.overlay.infobox.InfoBox;
import net.runelite.client.util.ImageUtil;
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

        // Storage overlay hidden.
        if (!storage.show()) return null;

        // Whitelist check.
        for (final String whitelist : storage.getWhitelist()) {
            for (final Map.Entry<String, JsonElement> item : storage.getStorage().entrySet()) {
                final int id = Integer.parseInt(item.getKey());
                final String name = items.getItemComposition(id).getName();
                final int quantity = item.getValue().getAsInt();

                // Placeholder check.
                if (quantity < 1 || name == null) continue;

                // Blacklist check.
                if (storage.isBlacklistEnabled() && storage.getBlacklist() != null) {
                    boolean blacklisted = false;

                    for (final String blacklist : storage.getBlacklist()) {
                        if (blacklist == null || blacklist.length() == 0) continue;

                        // Item blacklisted.
                        if (name.contains(blacklist)) {
                            blacklisted = true;
                            break;
                        }
                    }

                    if (blacklisted) continue;
                }

                // Whitelist disabled or item whitelisted.
                if (!storage.isWhitelistEnabled() || whitelist != null && name.contains(whitelist)) {
                    panel.getChildren().add(new ImageComponent(items.getImage(id, quantity, true)));
                }
            }
        }

        // TODO - Inventory free space with quantity.
//        if (storage.getItemContainerID() == InventoryID.INVENTORY.getId()) {
//            panel.getChildren().add(new ImageComponent(ImageUtil.loadImageResource(getClass(), "inventory.png")));
//        }

        // Show panel.
        if (!panel.getChildren().isEmpty()) {
            panelComponent.getChildren().add(panel);
            return super.render(graphics);
        }

        return null;
    }
}
