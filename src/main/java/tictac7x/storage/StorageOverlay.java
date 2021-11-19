package tictac7x.storage;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemID;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.config.ConfigManager;
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
    private final StorageConfig config;
    private final ConfigManager configs;
    private final ItemManager items;
    private final InventoryID item_container;
    private final String storage_key;
    private final boolean white_list;
    private final boolean black_list;

    private final PanelComponent panel = new PanelComponent();

    private JsonObject storage;

    public StorageOverlay(final StorageConfig config, final ConfigManager configs, final ItemManager items, final InventoryID item_container, final String storage_key, final boolean white_list, final boolean black_list) {
        this.config = config;
        this.configs = configs;
        this.items = items;
        this.item_container = item_container;
        this.storage_key = storage_key;
        this.white_list = white_list;
        this.black_list = black_list;

        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPosition(OverlayPosition.TOP_RIGHT);

        panel.setWrap(true);
        panel.setOrientation(ComponentOrientation.HORIZONTAL);
        panel.setBackgroundColor(null);

        panelComponent.setOrientation(ComponentOrientation.VERTICAL);
        panelComponent.setBorder(new Rectangle(0,0,0,0));

        this.storage = loadStorage();
    }

    private JsonObject loadStorage() {
        try {
            final JsonParser parser = new JsonParser();
            return parser.parse(getStorage()).getAsJsonObject();
        } catch (final Exception exception) {
            return new JsonObject();
        }
    }

    private String getStorage() {
        return configs.getConfiguration(StorageConfig.group, storage_key);
    }

    private String getWhitelist() {
        return configs.getConfiguration(StorageConfig.group, storage_key + "_whitelist");
    }

    private String getBlacklist() {
        return configs.getConfiguration(StorageConfig.group, storage_key + "_blacklist");
    }

    public void onItemContainerChanged(final ItemContainerChanged event) {
        final ItemContainer item_container = event.getItemContainer();

        if (item_container != null && item_container.getId() == this.item_container.getId()) {
            final JsonObject storage = new JsonObject();

            for (final Item item : item_container.getItems()) {
                if (item != null && item.getId() != -1) {
                    final String id = String.valueOf(item.getId());
                    if (storage.has(id)) {
                        storage.addProperty(id, storage.get(id).getAsInt() + item.getQuantity());
                    } else {
                        storage.addProperty(id, item.getQuantity());
                    }
                }
            }

            this.storage = storage;
            configs.setConfiguration(StorageConfig.group, storage_key, storage.toString());
        }
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        panelComponent.getChildren().clear();
        panel.getChildren().clear();

        for (final Map.Entry<String, JsonElement> item : storage.entrySet()) {
            final int id = Integer.parseInt(item.getKey());
            final String name = items.getItemComposition(id).getName();
            final int quantity = item.getValue().getAsInt();

            // Placeholder check.
            if (quantity < 1) {
                continue;

            // Whitelist check.
            } else if (white_list && getWhitelist() != null && !getWhitelist().contains(name)) {
                continue;

            // Blacklist check.
            } else if (black_list && getBlacklist() != null && getBlacklist().contains(name)) {
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
