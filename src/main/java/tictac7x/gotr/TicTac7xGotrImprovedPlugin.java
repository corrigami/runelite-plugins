package tictac7x.gotr;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.GraphicChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import javax.inject.Inject;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

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
	private Inventory inventory;
	private Panel panel;
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
		inventory = new Inventory();

		panel = new Panel(config, timer, energy, portal);
		overlay = new Overlay(client, outlines, sprites, config, tables, guardians, inventory, portal, energy);

		overlays.add(panel);
		overlays.add(overlay);
	}

	@Override
	protected void shutDown() {
		overlays.remove(panel);
		overlays.remove(overlay);
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
	}

	@Subscribe
	public void onGameObjectDespawned(final GameObjectDespawned event) {
		guardians.onGameObjectDespawned(event.getGameObject());
		tables.onGameObjectDespawned(event.getGameObject());
		portal.onGameObjectDespawned(event.getGameObject());
	}

	@Subscribe
	public void onChatMessage(ChatMessage message) {
		message.setMessage(message.getMessage().replaceAll("</?col.*?>", ""));
		timer.onChatMesage(message);
		energy.onChatMessage(message);
		portal.onChatMessage(message);

		System.out.println("MESSAGE | " +
			"type: " + message.getType().name() +
			", message: " + message.getMessage().replaceAll("</?col.*?>", "") +
			", sender: " + message.getSender()
		);
	}

	@Subscribe
	public void onAnimationChanged(final AnimationChanged event) {
		if (event.getActor() == client.getLocalPlayer()) {
			System.out.println("ANIMATION | " +
				"id: " + event.getActor().getAnimation()
			);
		}
	}

	@Subscribe
	public void onGraphicChanged(final GraphicChanged event) {
		if (event.getActor() == client.getLocalPlayer()) {
			System.out.println("GRAPHIC | " +
				"id: " + event.getActor().getGraphic()
			);
		}
	}

	@Subscribe
	public void onConfigChanged(final ConfigChanged event) {
		if (event.getGroup().equals(TicTac7xGotrImprovedConfig.group)) {
			System.out.println("CONFIG | " +
				"key: " + event.getKey() +
				", old value: " + event.getOldValue() +
				", new value: " + event.getNewValue()
			);
		}
	}

	@Subscribe
	public void onWidgetLoaded(final WidgetLoaded event) {
		System.out.println("WIDGET | " +
			"group: " + event.getGroupId()
		);
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
	}

	@Subscribe
	public void onGameStateChanged(final GameStateChanged event) {
		guardians.onGameStateChanged(event.getGameState());
		tables.onGameStateChanged(event.getGameState());
		portal.onGameStateChanged(event.getGameState());

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

	static public void drawCenteredString(Graphics g, String text, Rectangle rect, final Color color) {
		g.setFont(FontManager.getRunescapeFont());
		FontMetrics metrics = g.getFontMetrics();
		int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
		int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
		g.setColor(Color.BLACK);
		g.drawString(text, x + 1, y + 1);
		g.setColor(color);
		g.drawString(text, x, y);
	}
}

