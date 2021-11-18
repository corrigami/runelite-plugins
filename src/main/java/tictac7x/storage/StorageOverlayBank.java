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

public class StorageOverlayBank extends Overlay {
    private final StorageConfig config;
    private final ConfigManager configs;
    private final ItemManager items;
    private final InventoryID item_container;
    private final String storage_key;
    private JsonObject storage;

    private final PanelComponent panel = new PanelComponent();

    public StorageOverlayBank(final StorageConfig config, final ConfigManager configs, final ItemManager items, final InventoryID item_container, final String storage_key) {
        this.config = config;
        this.configs = configs;
        this.items = items;
        this.item_container = item_container;
        this.storage_key = storage_key;

        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPosition(OverlayPosition.TOP_RIGHT);

        panel.setWrap(true);
        panel.setOrientation(ComponentOrientation.HORIZONTAL);
        panel.setBackgroundColor(null);

        panelComponent.setOrientation(ComponentOrientation.VERTICAL);
        panelComponent.setBorder(new Rectangle(0,0,0,0));

        this.storage = loadStorage(storage_key);
    }

    private JsonObject loadStorage(final String storage_key) {
        try {
            final JsonParser parser = new JsonParser();
            return parser.parse(getStorage(storage_key)).getAsJsonObject();
        } catch (final Exception exception) {
            return new JsonObject();
        }
    }

    private String getStorage(final String storage_key) {
        return configs.getConfiguration(StorageConfig.group, storage_key);
    }

    public void onItemContainerChanged(final ItemContainerChanged event) {
        final ItemContainer item_container = event.getItemContainer();

        if (item_container != null && item_container.getId() == this.item_container.getId()) {
            final JsonObject storage = new JsonObject();

            for (final Item item : item_container.getItems()) {
                if (item != null && item.getId() != -1) {
                    storage.addProperty(String.valueOf(item.getId()), item.getQuantity());
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
            if (config.getBankWhitelist().contains(items.getItemComposition(Integer.parseInt(item.getKey())).getName())) {
                panel.getChildren().add(new ImageComponent(items.getImage(Integer.parseInt(item.getKey()), item.getValue().getAsInt(), item.getValue().getAsInt() > 1)));
            }
        }

        panelComponent.getChildren().add(panel);

        return super.render(graphics);
    }
}
