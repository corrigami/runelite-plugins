package tictac7x.storage;

import javax.inject.Inject;
import net.runelite.api.Client;
import lombok.extern.slf4j.Slf4j;
import com.google.inject.Provides;
import net.runelite.api.InventoryID;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Storage",
	description = "Show overlays of various storages and searchable panel",
	tags = { "storage", "bank", "inventory" }
)
public class StoragePlugin extends Plugin {
	@Inject
	private Client client;

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

	private StorageOverlayBank overlay_bank;

	@Override
	protected void startUp() {
		overlay_bank = new StorageOverlayBank(config, configs, items, InventoryID.BANK, "bank");
		overlays.add(overlay_bank);
	}

	@Override
	protected void shutDown() {
		overlays.remove(overlay_bank);
	}

	@Subscribe
	public void onItemContainerChanged(final ItemContainerChanged event) {
		overlay_bank.onItemContainerChanged(event);
	}
}
