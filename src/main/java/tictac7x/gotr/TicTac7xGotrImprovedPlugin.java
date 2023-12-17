package tictac7x.gotr;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
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
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;
import tictac7x.gotr.overlays.BarrierOverlay;
import tictac7x.gotr.overlays.GreatGuardianOverlay;
import tictac7x.gotr.overlays.GuardiansOverlay;
import tictac7x.gotr.overlays.PortalOverlay;
import tictac7x.gotr.overlays.TeleportersOverlay;
import tictac7x.gotr.overlays.UnchargedCellsBenchOverlay;
import tictac7x.gotr.store.Barrier;
import tictac7x.gotr.store.Energy;
import tictac7x.gotr.store.Guardians;
import tictac7x.gotr.store.Inventory;
import tictac7x.gotr.store.Portal;
import tictac7x.gotr.store.Teleporters;
import tictac7x.gotr.widgets.InactivePortalWidget;

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
	private final String pluginVersion = "v0.1";
	private final String pluginMessage = "" +
		"<colHIGHLIGHT>GOTR Improved " + pluginVersion + ":<br>";

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private ItemManager itemManager;

	@Inject
	private ConfigManager configManager;

	@Inject
	private InfoBoxManager infoBoxManager;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private TicTac7xGotrImprovedConfig config;

	@Inject
	private ChatMessageManager chatMessageManager;

	@Inject
	private Notifier notifier;

	@Inject
	private ModelOutlineRenderer modelOutlineRenderer;

	@Inject
	private SpriteManager spriteManager;

	private Teleporters teleporters;
	private GreatGuardianOverlay greatGuardianOverlay;
	private Guardians guardians;
	private Portal portal;
	private Inventory inventory;
	private Energy energy;
	private Barrier barrier;
	private Notifications notifications;

	private PortalOverlay portalOverlayOverlay;
	private GuardiansOverlay guardiansOverlay;
	private TeleportersOverlay teleportersOverlay;
	private BarrierOverlay barrierOverlay;
	private UnchargedCellsBenchOverlay unchargedCellsBenchOverlay;

	private InactivePortalWidget inactivePortalWidget;

	@Provides
	TicTac7xGotrImprovedConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(TicTac7xGotrImprovedConfig.class);
	}

	@Override
	protected void startUp() {
		notifications = new Notifications(client, notifier, config);
		teleporters = new Teleporters();
		guardians = new Guardians(client);
		portal = new Portal(client, notifications);
		inventory = new Inventory();
		energy = new Energy(configManager);
		barrier = new Barrier(notifications);

		portalOverlayOverlay = new PortalOverlay(client, portal);
		guardiansOverlay = new GuardiansOverlay(modelOutlineRenderer, config, guardians, inventory);
		greatGuardianOverlay = new GreatGuardianOverlay(client, itemManager, modelOutlineRenderer, config, inventory);
		teleportersOverlay = new TeleportersOverlay(client, itemManager, modelOutlineRenderer, config, teleporters, inventory);
		barrierOverlay = new BarrierOverlay(client, modelOutlineRenderer, barrier);
		unchargedCellsBenchOverlay = new UnchargedCellsBenchOverlay(client, modelOutlineRenderer, config, inventory);

		inactivePortalWidget = new InactivePortalWidget(client, spriteManager, portal);

		overlayManager.add(portalOverlayOverlay);
		overlayManager.add(guardiansOverlay);
		overlayManager.add(greatGuardianOverlay);
		overlayManager.add(teleportersOverlay);
		overlayManager.add(barrierOverlay);
		overlayManager.add(unchargedCellsBenchOverlay);
		overlayManager.add(inactivePortalWidget);
	}

	@Override
	protected void shutDown() {
		overlayManager.remove(portalOverlayOverlay);
		overlayManager.remove(guardiansOverlay);
		overlayManager.remove(greatGuardianOverlay);
		overlayManager.remove(teleportersOverlay);
		overlayManager.remove(barrierOverlay);
		overlayManager.remove(unchargedCellsBenchOverlay);
		overlayManager.remove(inactivePortalWidget);
	}

	@Subscribe
	public void onItemContainerChanged(final ItemContainerChanged event) {
		inventory.onItemContainerChanged(event.getItemContainer());
	}

	@Subscribe
	public void onGameObjectSpawned(final GameObjectSpawned event) {
		final GameObject gameObject = event.getGameObject();

		teleporters.onGameObjectSpawned(gameObject);
		portalOverlayOverlay.onGameObjectSpawned(gameObject);
		guardiansOverlay.onGameObjectSpawned(gameObject);
		barrier.onGameObjectSpawned(gameObject);
		barrierOverlay.onGameObjectSpawned(gameObject);
		unchargedCellsBenchOverlay.onGameObjectSpawned(gameObject);
	}

	@Subscribe
	public void onGameObjectDespawned(final GameObjectDespawned event) {
		final GameObject gameObject = event.getGameObject();

		portalOverlayOverlay.onGameObjectDespawned(gameObject);
		guardiansOverlay.onGameObjectDespawned(gameObject);
		barrier.onGameObjectDespawned(gameObject);
		barrierOverlay.onGameObjectDespawned(gameObject);
		unchargedCellsBenchOverlay.onGameObjectDespawned(gameObject);
	}

	@Subscribe
	public void onChatMessage(final ChatMessage message) {
		message.setMessage(message.getMessage().replaceAll("</?col.*?>", ""));

		teleporters.onChatMessage(message);
		energy.onChatMessage(message);
		notifications.onChatMessage(message);
		portal.onChatMessage(message);
	}

	@Subscribe
	public void onNpcSpawned(final NpcSpawned event) {
		greatGuardianOverlay.onNpcSpawned(event.getNpc());
	}

	@Subscribe
	public void onNpcDespawned(final NpcDespawned event) {
		greatGuardianOverlay.onNpcDespawned(event.getNpc());
	}

	@Subscribe
	public void onGameTick(final GameTick ignored) {
		teleporters.onGameTick();
		portal.onGameTick();
		guardians.onGameTick();
		barrier.onGameTick();
	}

	@Subscribe
	public void onGameStateChanged(final GameStateChanged event) {
		final GameState gameState = event.getGameState();

		teleporters.onGameStateChanged(gameState);
		greatGuardianOverlay.onGameStateChanged(gameState);
		portalOverlayOverlay.onGameStateChanged(gameState);
		guardiansOverlay.onGameStateChanged(gameState);
		barrierOverlay.onGameStateChanged(gameState);
		unchargedCellsBenchOverlay.onGameStateChanged(gameState);
	}
}

