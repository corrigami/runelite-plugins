package tictac7x.storage;

import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.client.game.ItemManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.callback.ClientThread;

import java.util.Objects;

public class StorageInventory extends Storage {
    private static final int INVENTORY_SIZE = 28;

    public StorageInventory(final ConfigManager configs, final StorageConfig config, final ItemManager items, final ClientThread client_thread, final InventoryID item_container_id, final String storage_id, final boolean whitelist_enabled, final boolean blacklist_enabled) {
        super(configs, config, items, client_thread, item_container_id, storage_id, whitelist_enabled, blacklist_enabled);
    }

    /**
     * If config changed, check if this storage white- or blacklist was changed.
     * @param event - ConfigChanged.
     */
    public void onConfigChanged(final ConfigChanged event) {
        if (!Objects.equals(event.getGroup(), StorageConfig.group)) return;

        // Inventory whitelist toggle not changed.
        if (!Objects.equals(event.getKey(), "inventory_whitelist_enabled")) {
            super.onConfigChanged(event);
            return;
        }

        // Inventory whitelist toggled.
        super.whitelist_enabled = Boolean.parseBoolean(event.getNewValue());

        // Only update whitelist if it was actually enabled.
        if (super.whitelist_enabled) super.loadWhitelist();
        super.updateStorageImages();
    }

    @Override
    int getStorageSize(final ItemContainer item_container) {
        return INVENTORY_SIZE;
    }
}
