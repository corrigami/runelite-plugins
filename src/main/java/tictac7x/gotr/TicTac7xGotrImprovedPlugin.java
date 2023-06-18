package tictac7x.gotr;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.widgets.Widget;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import javax.inject.Inject;

@Slf4j
@PluginDescriptor(
	name = "Guardians of the Rift Improved",
	description = "Show charges of various items",
	tags = {
		"gotr",
		"guardian",
		"rift",
		"abyssal",
	}
)
public class TicTac7xGotrImprovedPlugin extends Plugin {
	private final String plugin_version = "v0.1";
	private final String plugin_message = "" +
		"<colHIGHLIGHT>GOTR Improved " + plugin_version + ":<br>";

	@Inject
	private Client client;

	@Inject
	private ClientThread client_thread;

	@Inject
	private ItemManager items;

	@Inject
	private ConfigManager configs;

	@Inject
	private InfoBoxManager infoboxes;

	@Inject
	private OverlayManager overlays;

	@Inject
	private TicTac7xGotrImprovedConfig config;

	@Inject
	private ChatMessageManager chat_messages;

	@Inject
	private Notifier notifier;

	@Inject
	private ModelOutlineRenderer outlines;

	@Inject
	private SpriteManager sprites;

	private Timer timer;
	private Guardians guardians;
	private Tables tables;
	private Energy energy;
	private Portal portal;
	private Teleporters teleporters;
	private Inventory inventory;
	private Overlay overlay;

	@Provides
	TicTac7xGotrImprovedConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(TicTac7xGotrImprovedConfig.class);
	}

	@Override
	protected void startUp() {
		timer = new Timer(configs, config);
		guardians = new Guardians(client, configs);
		tables = new Tables();
		energy = new Energy(client, configs, config);
		portal = new Portal(client, notifier);
		teleporters = new Teleporters();
		inventory = new Inventory();

		overlay = new Overlay(client, outlines, sprites, items, config, tables, guardians, inventory, portal, teleporters, energy, timer);
		overlays.add(overlay);
	}

	@Override
	protected void shutDown() {
		overlays.remove(overlay);

		client_thread.invokeLater(() -> {
			final Widget widget_elemental_energy = client.getWidget(746, 21);
			if (widget_elemental_energy != null) widget_elemental_energy.setHidden(false);

			final Widget widget_catalytic_energy = client.getWidget(746, 24);
			if (widget_catalytic_energy != null) widget_catalytic_energy.setHidden(false);
		});
	}

	@Subscribe
	public void onItemContainerChanged(final ItemContainerChanged event) {
		inventory.onItemContainerChanged(event.getItemContainer());
	}

	@Subscribe
	public void onGameObjectSpawned(final GameObjectSpawned event) {
		guardians.onGameObjectSpawned(event.getGameObject());
		tables.onGameObjectSpawned(event.getGameObject());
		portal.onGameObjectSpawned(event.getGameObject());
		teleporters.onGameObjectSpawned(event.getGameObject());
	}

	@Subscribe
	public void onGameObjectDespawned(final GameObjectDespawned event) {
		guardians.onGameObjectDespawned(event.getGameObject());
		tables.onGameObjectDespawned(event.getGameObject());
		portal.onGameObjectDespawned(event.getGameObject());
		teleporters.onGameObjectDespawned(event.getGameObject());
	}

	@Subscribe
	public void onChatMessage(ChatMessage message) {
		message.setMessage(message.getMessage().replaceAll("</?col.*?>", ""));
		timer.onChatMesage(message);
		energy.onChatMessage(message);
		portal.onChatMessage(message);
	}

	@Subscribe
	public void onNpcSpawned(final NpcSpawned event) {
		guardians.onNpcSpawned(event.getNpc());
	}

	@Subscribe
	public void onNpcDespawned(final NpcDespawned event) {
		guardians.onNpcDespawned(event.getNpc());
	}

	@Subscribe
	public void onGameTick(final GameTick gametick) {
		guardians.onGameTick();
		energy.onGameTick();
		teleporters.onGameTick();
	}

	@Subscribe
	public void onGameStateChanged(final GameStateChanged event) {
		guardians.onGameStateChanged(event.getGameState());
		tables.onGameStateChanged(event.getGameState());
		portal.onGameStateChanged(event.getGameState());
		teleporters.onGameStateChanged(event.getGameState());

		// Send message about plugin updates for once.
		if (event.getGameState() == GameState.LOGGED_IN && !config.getVersion().equals(plugin_version)) {
			configs.setConfiguration(TicTac7xGotrImprovedConfig.group, TicTac7xGotrImprovedConfig.version, plugin_version);
			chat_messages.queue(QueuedMessage.builder()
					.type(ChatMessageType.CONSOLE)
					.runeLiteFormattedMessage(plugin_message)
					.build()
			);
		}
	}
}

