package tictac7x.storage;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemID;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
import tictac7x.storage.panel.StoragePanel;

import javax.annotation.Nullable;
import javax.inject.Inject;

@Slf4j
@PluginDescriptor(
	name = "Storage",
	description = "Show overlays of inventory and bank",
	tags = { "storage", "bank", "inventory", "item" }
)
public class TicTac7xStoragePlugin extends Plugin {
	private String plugin_version = "v0.5";
	private String plugin_message = "" +
		"<colHIGHLIGHT>Storage " + plugin_version + ":<br>" +
		"<colHIGHLIGHT>* Searchable panel with bank items (can be disabled in the settings)";

	@Inject
	private Client client;

	@Inject
	private ClientToolbar client_toolbar;

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

	@Inject
	private ChatMessageManager chat_messages;

	@Provides
	StorageConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(StorageConfig.class);
	}

	private Storage[] storages;

	private StoragePanel storage_panel;

	@Nullable
	private NavigationButton navigation_button;

	@Override
	protected void startUp() {
		storages = new Storage[]{
			new StorageInventory(StorageConfig.inventory, InventoryID.INVENTORY, WidgetInfo.INVENTORY, client, client_thread, configs, config, items),
			new Storage(StorageConfig.bank, InventoryID.BANK, WidgetInfo.BANK_CONTAINER, client, client_thread, configs, config, items)
		};

		for (final Storage storage : storages) {
			overlays.add(storage);
		}

		// Panel
		storage_panel = new StoragePanel(client_thread, items, config);
		updateNavigationButton();
	}

	@Override
	protected void shutDown() {
		for (final Storage storage : storages) {
			overlays.remove(storage);
		}

		if (config.showPanel()) {
			client_toolbar.removeNavigation(navigation_button);
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
		if (!event.getGroup().equals(StorageConfig.group)) return;

		// Toggle panel.
		if (event.getKey().equals(StorageConfig.panel) || event.getKey().equals(StorageConfig.panel_priority)) {
			updateNavigationButton();
			return;
		}

		// Update list of items in the panel.
		storage_panel.onConfigChanged(event);

		for (final Storage storage : storages) {
			storage.onConfigChanged(event);
		}
	}

	@Subscribe
	public void onGameStateChanged(final GameStateChanged event) {
		// Plugin update message.
		if (event.getGameState() == GameState.LOGGED_IN && !config.getVersion().equals(plugin_version)) {
			configs.setConfiguration(StorageConfig.group, StorageConfig.version, plugin_version);
			chat_messages.queue(QueuedMessage.builder()
				.type(ChatMessageType.CONSOLE)
				.runeLiteFormattedMessage(plugin_message)
				.build()
			);
		}
	}

	private void updateNavigationButton() {
		if (navigation_button != null) {
			client_toolbar.removeNavigation(navigation_button);
		}

		if (config.showPanel()) {
			// Invoke on client thread otherwise icon is not showing on the sidebar.
			client_thread.invokeLater(() -> {
				navigation_button = NavigationButton.builder()
						.tooltip("Storage")
						.icon(items.getImage(ItemID.CHEST))
						.priority(config.getPanelPriority())
						.panel(storage_panel)
						.build();
				client_toolbar.addNavigation(navigation_button);
			});
		}
	}
}
