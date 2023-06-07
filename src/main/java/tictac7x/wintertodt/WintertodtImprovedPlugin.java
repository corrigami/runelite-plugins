package tictac7x.wintertodt;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.client.Notifier;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;



@Slf4j
@PluginDescriptor(
	name = "Wintertodt Improved",
	description = "Additional helpful information for Wintertodt",
	tags = {"wintertodt"}
)
public class WintertodtImprovedPlugin extends Plugin {
	private final String plugin_version = "v0.1";
	private final String plugin_message = "" +
		"<colHIGHLIGHT>Wintertodt Improved " + plugin_version + ":<br>" +
		"<colHIGHLIGHT>* Notifications when brazier is about to break next to you<br>" +
		"<colHIGHLIGHT>* Overlay to show how many logs are needed to complete goal";

	@Inject
	private Client client;

	@Inject
	private ConfigManager configs;

	@Inject
	private OverlayManager overlays;

	@Inject
	private WintertodtConfig config;

	@Inject
	private ChatMessageManager chat_messages;

	@Inject
	private Notifier notifier;

	private WintertodtOverlay wintertodt_overlay;

	private WintertodtPanel wintertodt_panel;

	@Provides
	WintertodtConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(WintertodtConfig.class);
	}

	@Override
	protected void startUp() {
		wintertodt_overlay = new WintertodtOverlay(client, notifier, config);
		wintertodt_panel = new WintertodtPanel(client, config);

		overlays.add(wintertodt_overlay);
		overlays.add(wintertodt_panel);
	}

	@Override
	protected void shutDown() {
		overlays.remove(wintertodt_overlay);
		overlays.remove(wintertodt_panel);
	}

	@Subscribe
	public void onGameObjectSpawned(final GameObjectSpawned event) {
		wintertodt_overlay.onGameObjectSpawned(event);
	}

	@Subscribe
	public void onGameObjectDespawned(final GameObjectDespawned event) {
		wintertodt_overlay.onGameObjectDespawned(event);
	}

	@Subscribe
	public void onWidgetLoaded(final WidgetLoaded event) {
		wintertodt_panel.onWidgetLoaded(event);
	}

	@Subscribe
	public void onItemContainerChanged(final ItemContainerChanged event) {
		wintertodt_panel.onItemContainerChanged(event);
	}

	@Subscribe
	public void onGameStateChanged(final GameStateChanged event) {
		wintertodt_overlay.onGameStateChanged(event);

		// Send message about plugin updates for once.
		if (event.getGameState() == GameState.LOGGED_IN && !config.getVersion().equals(plugin_version)) {
			configs.setConfiguration(WintertodtConfig.group, WintertodtConfig.version, plugin_version);
			chat_messages.queue(QueuedMessage.builder()
				.type(ChatMessageType.CONSOLE)
				.runeLiteFormattedMessage(plugin_message)
				.build()
			);
		}
	}
}

