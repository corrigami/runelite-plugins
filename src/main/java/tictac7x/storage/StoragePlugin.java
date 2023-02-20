package tictac7x.storage;

import javax.inject.Inject;
import net.runelite.api.Client;
import lombok.extern.slf4j.Slf4j;
import com.google.inject.Provides;
import net.runelite.api.InventoryID;
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
	description = "Show overlays of inventory and bank",
	tags = { "storage", "bank", "inventory", "item" }
)
public class StoragePlugin extends Plugin {
	@Inject
	private Client client;

	@Inject
	private StorageConfig config;

	@Inject
	private ClientThread client_thread;

	@Inject
	private ConfigManager configs;

	@Inject
	private ItemManager items;

	@Inject
	private OverlayManager overlays;

	@Provides
	StorageConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(StorageConfig.class);
	}

	private Storage[] storages;

	@Override
	protected void startUp() {
		storages = new Storage[]{
			new StorageInventory(StorageConfig.inventory, InventoryID.INVENTORY, WidgetInfo.INVENTORY, client, client_thread, configs, config, items),
			new Storage(StorageConfig.bank, InventoryID.BANK, WidgetInfo.BANK_CONTAINER, client, client_thread, configs, config, items)
		};

		for (final Storage storage : storages) {
			overlays.add(storage);
		}
	}

	@Override
	protected void shutDown() {
		for (final Storage storage : storages) {
			overlays.remove(storage);
		}
	}

	@Subscribe
	public void onItemContainerChanged(final ItemContainerChanged event) {
		for (final Storage storage : storages) {
			storage.onItemContainerChanged(event);
		}
	}

	@Subscribe
	public void onConfigChanged(final ConfigChanged event) {
		for (final Storage storage : storages) {
			storage.onConfigChanged(event);
		}
	}
}
