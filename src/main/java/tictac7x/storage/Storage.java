package tictac7x.storage;

import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.Objects;
import java.util.ArrayList;
import javax.annotation.Nullable;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.client.game.ItemManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.ui.overlay.components.ImageComponent;

public class Storage {
    private final static int inventory_size = 28;

    private final ConfigManager configs;
    private final ItemManager items;
    private final ClientThread client_thread;
    public final String storage_id;
    private final int item_container_id;
    private final boolean whitelist_enabled, blacklist_enabled;
    private int empty_slots_count = 0;

    private JsonObject storage;
    private String[] whitelist, blacklist;
    private final List<ImageComponent> images = new ArrayList<>();

    public Storage(final ConfigManager configs, final ItemManager items, final ClientThread client_thread, final InventoryID item_container_id, final String storage_id, final boolean whitelist, final boolean blacklist) {
        this.configs = configs;
        this.items = items;
        this.client_thread = client_thread;
        this.storage_id = storage_id;
        this.item_container_id = item_container_id.getId();
        this.whitelist_enabled = whitelist;
        this.blacklist_enabled = blacklist;

        this.storage = loadStorage();
        this.whitelist = loadWhitelist();
        this.blacklist = loadBlacklist();
        this.updateStorageImages();
    }

    public List<ImageComponent> getStorageImages() {
        return images;
    }

    private void updateStorageImages() {
        // Images need to be created on the client thread.
        client_thread.invoke(() -> {
            // Clear old images.
            images.clear();

            // Order by whitelist.
            if (whitelist_enabled) {
                for (final String whitelist : whitelist) {
                    if (whitelist.length() > 0) {
                        updateStorageImagesBasedOnWhitelist(whitelist);
                    }
                }

            // Order by item container.
            } else {
                updateStorageImagesBasedOnWhitelist(null);
            }
        });
    }

    private void updateStorageImagesBasedOnWhitelist(@Nullable final String whitelist) {
        for (final Map.Entry<String, JsonElement> item : storage.entrySet()) {
            final int id = Integer.parseInt(item.getKey());
            final String name = items.getItemComposition(id).getName();
            final int quantity = item.getValue().getAsInt();

            // Placeholder check.
            if (quantity < 1 || name == null) continue;

            // Blacklist check.
            if (blacklist_enabled && blacklist != null) {
                boolean blacklisted = false;

                for (final String blacklist : blacklist) {
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
            if (!whitelist_enabled || whitelist != null && name.contains(whitelist)) {
                images.add(new ImageComponent(items.getImage(id, quantity, true)));
            }
        }
    }

    public boolean show() {
        return configs.getConfiguration(StorageConfig.group, storage_id + "_show").equals("true");
    }

    private String getWhitelistID() {
        return storage_id + "_whitelist";
    }

    private String getBlacklistID() {
        return storage_id + "_blacklist";
    }

    /**
     * Save storage json to config.
     */
    private void saveStorageToConfig() {
        configs.setConfiguration(StorageConfig.group, storage_id, storage.toString());
    }

    /**
     * Get storage from config. Needed on first-run to restore state from previous session.
     * @return json of storage.
     */
    private JsonObject loadStorage() {
        try {
            final JsonParser parser = new JsonParser();
            return parser.parse(configs.getConfiguration(StorageConfig.group, storage_id)).getAsJsonObject();
        } catch (final Exception exception) {
            return new JsonObject();
        }
    }

    /**
     * Get comma separated whitelist from config.
     */
    private String[] loadWhitelist() {
        final String whitelist = configs.getConfiguration(StorageConfig.group, getWhitelistID());

        if (whitelist != null) {
            return whitelist.split(",");
        } else {
            return new String[]{};
        }
    }

    /**
     * Get comma separated blacklist from config.
     */
    private String[] loadBlacklist() {
        final String blacklist = configs.getConfiguration(StorageConfig.group, getBlacklistID());

        if (blacklist != null) {
            return blacklist.split(",");
        } else {
            return new String[]{};
        }
    }

    /**
     * Dynamically deposit items stored in json to the storage.
     * For example needed when depositing to bank with deposit box without opening the actual bank interface.
     * @param deposit - json of items to deposit.
     */
    public void deposit(final JsonObject deposit) {
        if (deposit != null) {
            for (final String id : deposit.keySet()) {
                if (storage.has(id)) {
                    storage.addProperty(id, storage.get(id).getAsInt() + deposit.get(id).getAsInt());
                } else {
                    storage.addProperty(id, deposit.get(id).getAsInt());
                }
            }
            saveStorageToConfig();
            updateStorageImages();
        }
    }

    /**
     * If config changed, check if this storage white- or blacklist was changed.
     * @param event - ConfigChanged.
     */
    public void onConfigChanged(final ConfigChanged event) {
        if (Objects.equals(event.getGroup(), StorageConfig.group)) {
            // Update whitelist.
            if (Objects.equals(event.getKey(), getWhitelistID())) {
                this.whitelist = event.getNewValue().split(",");
                this.updateStorageImages();

                // Update blacklist.
            } else if (Objects.equals(event.getKey(), getBlacklistID())) {
                this.blacklist = event.getNewValue().split(",");
                this.updateStorageImages();
            }
        }
    }

    /**
     * If item container changed, check if this item container belongs to this storage to update the storage and config.
     * @param event - ItemContainerChanged.
     */
    public void onItemContainerChanged(final ItemContainerChanged event) {
        final ItemContainer item_container = event.getItemContainer();

        if (item_container != null && item_container.getId() == item_container_id) {
            empty_slots_count = storage_id.equals(StorageConfig.inventory) ? inventory_size : item_container.size();
            final JsonObject storage = new JsonObject();

            Arrays.stream(item_container.getItems()).filter(
                    item -> item != null && item.getId() != -1
            ).forEach(item -> {
                final String id = String.valueOf(item.getId());
                if (storage.has(id)) {
                    storage.addProperty(id, storage.get(id).getAsInt() + item.getQuantity());
                } else {
                    storage.addProperty(id, item.getQuantity());
                }
                empty_slots_count -= 1;
            });

            this.storage = storage;
            saveStorageToConfig();
            updateStorageImages();
        }
    }

    public int getEmptySlotsCount() {
        return empty_slots_count;
    }
}
