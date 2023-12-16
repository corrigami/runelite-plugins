package tictac7x.gotr;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
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
import tictac7x.gotr.overlays.GuardiansOverlay;
import tictac7x.gotr.overlays.PortalOverlay;

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
	private ConfigManager configManager;

	@Inject
	private InfoBoxManager infoboxes;

	@Inject
	private OverlayManager overlayManager;

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
	private Energy energy;
	private Notifications notifications;

	private PortalOverlay portalOverlayOverlay;
	private GuardiansOverlay guardiansOverlay;

	@Provides
	TicTac7xGotrImprovedConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(TicTac7xGotrImprovedConfig.class);
	}

	@Override
	protected void startUp() {
		notifications = new Notifications(notifier, config);
		teleporters = new Teleporters();
		great_guardian = new GreatGuardian();
		guardians = new Guardians(client);
		portal = new Portal(client, notifications);
		inventory = new Inventory();
		energy = new Energy(configManager);

		portalOverlayOverlay = new PortalOverlay(client, portal);
		guardiansOverlay = new GuardiansOverlay(outlines, config, guardians);

//		overlay = new tictac7x.gotr.Overlay(client, items, outlines, config, teleporters, great_guardian, guardians, portal, inventory);
//		overlays.add(overlay);

		overlayManager.add(portalOverlayOverlay);
		overlayManager.add(guardiansOverlay);
	}

	@Override
	protected void shutDown() {
//		overlays.remove(overlay);
		overlayManager.remove(portalOverlayOverlay);
		overlayManager.remove(guardiansOverlay);
	}

	@Subscribe
	public void onItemContainerChanged(final ItemContainerChanged event) {
		inventory.onItemContainerChanged(event.getItemContainer());
	}

	@Subscribe
	public void onGameObjectSpawned(final GameObjectSpawned event) {
		teleporters.onGameObjectSpawned(event.getGameObject());
		portalOverlayOverlay.onGameObjectSpawned(event.getGameObject());
		guardiansOverlay.onGameObjectSpawned(event.getGameObject());
	}

	@Subscribe
	public void onGameObjectDespawned(final GameObjectDespawned event) {
		portalOverlayOverlay.onGameObjectDespawned(event.getGameObject());
		guardiansOverlay.onGameObjectDespawned(event.getGameObject());
	}

	@Subscribe
	public void onChatMessage(final ChatMessage message) {
		message.setMessage(message.getMessage().replaceAll("</?col.*?>", ""));

		teleporters.onChatMessage(message);
		energy.onChatMessage(message);
		notifications.onChatMessage(message);

		System.out.println(message.getMessage());
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
		guardians.onGameTick();
	}

	@Subscribe
	public void onGameStateChanged(final GameStateChanged event) {
		teleporters.onGameStateChanged(event.getGameState());
		great_guardian.onGameStateChanged(event.getGameState());
		portalOverlayOverlay.onGameStateChanged(event.getGameState());
		guardiansOverlay.onGameStateChanged(event.getGameState());
	}
}

