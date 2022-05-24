package tictac7x.storage;

import javax.inject.Inject;
import net.runelite.api.Client;
import lombok.extern.slf4j.Slf4j;
import com.google.inject.Provides;
import net.runelite.api.InventoryID;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.game.ItemManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Storage",
	description = "Show overlays of various storages and searchable panel",
	tags = { "storage", "bank", "inventory" }
)
public class TicTac7xStoragePlugin extends Plugin {
	public static final String INVENTORY = "93";
	public static final String BANK = "95";
	public static final String GLOBAL = "global";

	@Inject
	private Client client;

	@Inject
	private ClientThread client_thread;

	@Inject
	private StorageConfig config;

	@Inject
	private ConfigManager configs;

	@Inject
	private OverlayManager overlays;

	@Inject
	private ItemManager items;

	@Provides
	StorageConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(StorageConfig.class);
	}

	private Storage bank;
	private Storage inventory;
	private StorageManager storage_manager;
	private StorageOverlay overlay_bank;
	private StorageOverlay overlay_inventory;
	private StorageOverlay overlay_global;
	private FossilsStorage storage_fossils;

	@Override
	protected void startUp() {
		if (bank == null) {
			storage_manager = new StorageManager(client, configs, config);
			storage_fossils = new FossilsStorage(client, storage_manager);

			overlay_bank = new StorageOverlay(client, client_thread, configs, items, config, storage_manager, "bank", BANK, WidgetInfo.BANK_CONTAINER);
			overlay_inventory = new StorageOverlayInventory(client, client_thread, configs, items, config, storage_manager, "inventory", INVENTORY, WidgetInfo.INVENTORY);
			overlay_global = new StorageOverlay(client, client_thread, configs, items, config, storage_manager, GLOBAL, GLOBAL, null);
		}

//		overlays.add(overlay_bank);
		overlays.add(overlay_inventory);

//		storage_manager.registerOverlay(BANK, overlay_bank);
		storage_manager.registerOverlay(INVENTORY, overlay_inventory);
//		storage_manager.registerOverlay(GLOBAL, overlay_global);
	}

	@Override
	protected void shutDown() {
//		storage_manager.unregisterOverlay(BANK);
		storage_manager.unregisterOverlay(INVENTORY);

//		overlays.remove(overlay_bank);
		overlays.remove(overlay_inventory);
	}

	@Subscribe
	public void onItemContainerChanged(final ItemContainerChanged event) {
		storage_manager.onItemContainerChanged(event);
	}

	@Subscribe
	public void onConfigChanged(final ConfigChanged event) {
//		bank.onConfigChanged(event);
//		inventory.onConfigChanged(event);
	}

	@Subscribe
	public void onVarbitChanged(final VarbitChanged event) {
		storage_fossils.onVarbitChanged(event);
	}
}
