package tictac7x.storage;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.events.ConfigChanged;

import java.util.Arrays;
import java.util.Objects;

public class Storage {
    private final ConfigManager configs;
    private final String storage_id;
    private final int item_container_id;
    private final boolean whitelist_enabled, blacklist_enabled;

    private JsonObject storage, whitelist, blacklist;

    public Storage(final ConfigManager configs, final InventoryID item_container_id, final String storage_id, final boolean whitelist, final boolean blacklist) {
        this.configs = configs;
        this.storage_id = storage_id;
        this.item_container_id = item_container_id.getId();
        this.whitelist_enabled = whitelist;
        this.blacklist_enabled = blacklist;

        this.storage = loadStorage();
        this.whitelist = loadWhitelist();
        this.blacklist = loadBlacklist();
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
     * Get whitelist json from config.
     */
    private JsonObject loadWhitelist() {
        return getListFromString(configs.getConfiguration(StorageConfig.group, getWhitelistID()));
    }

    /**
     * Get blacklist json from config.
     */
    private JsonObject loadBlacklist() {
        return getListFromString(configs.getConfiguration(StorageConfig.group, getBlacklistID()));
    }

    /**
     * Get json from white/blacklist comma separated string.
     * @param value - Comma separated string.
     * @return json of listed elements.
     */
    private JsonObject getListFromString(final String value) {
        final JsonObject object = new JsonObject();
        if (value == null) return object;

        for (String item : value.split(",")) {
            item = item.trim();
            object.addProperty(item, true);
        }

        return object;
    }

    public boolean isWhitelistEnabled() {
        return whitelist_enabled;
    }

    public boolean isBlacklistEnabled() {
        return blacklist_enabled;
    }

    public JsonObject getWhitelist() {
        return whitelist;
    }

    public JsonObject getBlacklist() {
        return blacklist;
    }

    public JsonObject getStorage() {
        return storage;
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
                this.whitelist = getListFromString(event.getNewValue());

                // Update blacklist.
            } else if (Objects.equals(event.getKey(), getBlacklistID())) {
                this.blacklist = getListFromString(event.getNewValue());
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
            });

            this.storage = storage;
            saveStorageToConfig();
        }
    }
}
