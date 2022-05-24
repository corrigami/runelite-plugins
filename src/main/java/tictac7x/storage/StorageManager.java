package tictac7x.storage;

import com.google.gson.*;
import lombok.NonNull;
import net.runelite.api.Item;
import net.runelite.api.Client;
import javax.annotation.Nullable;

import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.config.ConfigManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class StorageManager {
    private final Client client;
    private final ConfigManager configs;
    private final StorageConfig config;

    private JsonObject storages;

    private final Map<String, StorageOverlay> overlays = new HashMap<>();
    private final JsonParser json_parser = new JsonParser();

    public StorageManager(final Client client, final ConfigManager configs, final StorageConfig config) {
        this.client = client;
        this.configs = configs;
        this.config = config;

        this.loadStorages();
    }

    public void registerOverlay(final String storage_id, final StorageOverlay overlay) {
        overlays.put(storage_id, overlay);
        notifyOverlays(storage_id);
    }

    public void unregisterOverlay(final String storage_id) {
        overlays.remove(storage_id);
    }

    public void replace(final String storage_id, final JsonObject items, final int size, final int amount) {
        final JsonObject storage = new JsonObject();

        storage.add("items", items);
        storage.addProperty("size", size);
        storage.addProperty("amount", amount);
        storage.addProperty("empty", size - amount);

        this.storages.add(storage_id, storage);
        this.updateStorages();
        notifyOverlays(storage_id);
    }

    public void deposit(final String storage_id, final JsonObject items) {
//        JsonObject storage = this.storages.getAsJsonObject(String.valueOf(storage_id));
//        if (storage == null) storage = new JsonObject();
        updateStorages();
        notifyOverlays(storage_id);
    }

    private void loadStorages() {
        this.storages = json_parser.parse(config.getStorages()).getAsJsonObject();
    }

    private void updateStorages() {
        configs.setConfiguration(StorageConfig.group, StorageConfig.storages, this.storages.toString());
    }

    private void notifyOverlays(final String storage_id) {
        if (!overlays.containsKey(storage_id)) return;

        final JsonObject storage = storages.get(storage_id).getAsJsonObject();
        if (storage == null) return;

        final StorageOverlay overlay = overlays.get(storage_id);

        System.out.println("UPDATE OVERLAY " + storage_id + " " + storage.toString());
        overlay.update(storage);
    }

    public void onItemContainerChanged(final @Nullable ItemContainerChanged event) {
        if (event == null || event.getItemContainer() == null) return;

        final ItemContainer item_container = event.getItemContainer();
        final JsonObject items = new JsonObject();
        int items_amount = 0;

        for (final Item item : item_container.getItems()) {
            if (item == null || item.getId() < 0) continue;

            final String item_id = String.valueOf(item.getId());
            final int item_amount = items.has(item_id)
                    ? items.get(item_id).getAsInt() + item.getQuantity()
                    : item.getQuantity();

            items.addProperty(item_id, item_amount);
            items_amount++;
        }

        this.replace(String.valueOf(item_container.getId()), items, item_container.size(), items_amount);
    }
}
