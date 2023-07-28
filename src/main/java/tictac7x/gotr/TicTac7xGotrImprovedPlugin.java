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
import net.runelite.api.events.WidgetLoaded;
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
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import javax.inject.Inject;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

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

	private Teleporters teleporters;
	private GreatGuardian great_guardian;
	private Guardians guardians;
	private Portal portal;
	private Overlay overlay;
	private Inventory inventory;

	@Provides
	TicTac7xGotrImprovedConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(TicTac7xGotrImprovedConfig.class);
	}

	@Override
	protected void startUp() {
		teleporters = new Teleporters();
		great_guardian = new GreatGuardian();
		guardians = new Guardians(client);
		portal = new Portal(client, notifier);
		inventory = new Inventory();

		overlay = new tictac7x.gotr.Overlay(client, items, outlines, config, teleporters, great_guardian, guardians, portal, inventory);
		overlays.add(overlay);
	}

	@Override
	protected void shutDown() {
		overlays.remove(overlay);
	}

	@Subscribe
	public void onItemContainerChanged(final ItemContainerChanged event) {
		inventory.onItemContainerChanged(event.getItemContainer());
	}

	@Subscribe
	public void onGameObjectSpawned(final GameObjectSpawned event) {
		teleporters.onGameObjectSpawned(event.getGameObject());
		portal.onGameObjectSpawned((event.getGameObject()));
	}

	@Subscribe
	public void onGameObjectDespawned(final GameObjectDespawned event) {
		portal.onGameObjectDespawned(event.getGameObject());
	}

	@Subscribe
	public void onChatMessage(final ChatMessage message) {
		message.setMessage(message.getMessage().replaceAll("</?col.*?>", ""));
		teleporters.onChatMessage(message);
		portal.onChatMessage(message);
	}

	@Subscribe
	public void onNpcSpawned(final NpcSpawned event) {
		great_guardian.onNpcSpawned(event.getNpc());
	}

	@Subscribe
	public void onNpcDespawned(final NpcDespawned event) {
		great_guardian.onNpcDespawned(event.getNpc());
	}

	@Subscribe
	public void onGameTick(final GameTick gametick) {
		teleporters.onGameTick();
		portal.onGameTick();
	}

	@Subscribe
	public void onGameStateChanged(final GameStateChanged event) {
		teleporters.onGameStateChanged(event.getGameState());
		great_guardian.onGameStateChanged(event.getGameState());
		portal.onGameStateChanged(event.getGameState());
	}
}

