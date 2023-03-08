package tictac7x.balloon;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;

import javax.inject.Inject;

@Slf4j
@PluginDescriptor(
	name = "Balloon Transport System",
	description = "Show amount of logs stored in the balloon transport system storages.",
	tags = { "balloon", "transport", "logs", "storage" }
)
public class BalloonPlugin extends Plugin {
	@Inject
	private Client client;

	@Inject
	private OverlayManager overlays;

	@Inject
	private ConfigManager configs;

	@Inject
	private InfoBoxManager infoboxes;

	@Inject
	private ClientThread client_thread;

	@Inject
	private ItemManager items;

	@Inject
	private BalloonConfig config;

	private Balloon balloon;
	private BalloonStorage balloon_storage;
	private BalloonInfoBox[] balloon_infoboxes;

	@Provides
	BalloonConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(BalloonConfig.class);
	}

	@Override
	protected void startUp() {
		balloon = new Balloon();
		balloon_storage = new BalloonStorage(client, client_thread, configs);
		balloon_infoboxes = new BalloonInfoBox[]{
			new BalloonInfoBox(ItemID.LOGS, BalloonConfig.logs_regular, "Regular logs - Entrana / Taverley", configs, config, items, balloon, this),
			new BalloonInfoBox(ItemID.OAK_LOGS, BalloonConfig.logs_oak, "Oak logs - Crafting Guild", configs, config, items, balloon, this),
			new BalloonInfoBox(ItemID.WILLOW_LOGS, BalloonConfig.logs_willow, "Willow logs - Varrock", configs, config, items, balloon, this),
			new BalloonInfoBox(ItemID.YEW_LOGS, BalloonConfig.logs_yew, "Yew logs - Castle Wars", configs, config, items, balloon, this),
			new BalloonInfoBox(ItemID.MAGIC_LOGS, BalloonConfig.logs_magic, "Magic logs - Grand Tree", configs, config, items, balloon, this),
		};

		for (final BalloonInfoBox infobox : balloon_infoboxes) {
			infoboxes.addInfoBox(infobox);
		}
	}

	@Override
	protected void shutDown() {
		for (final BalloonInfoBox infobox : balloon_infoboxes) {
			infoboxes.removeInfoBox(infobox);
		}
	}

	@Subscribe
	public void onChatMessage(final ChatMessage event) {
		balloon_storage.onChatMessage(event);
	}

	@Subscribe
	public void onWidgetLoaded(final WidgetLoaded event) {
		balloon_storage.onWidgetLoaded(event);
	}

	@Subscribe
	public void onGameObjectSpawned(final GameObjectSpawned event) {
		balloon.onGameObjectSpawned(event);
	}

	@Subscribe
	public void onGameStateChanged(final GameStateChanged event) {
		balloon.onGameStateChanged(event);
	}

	@Subscribe
	public void onConfigChanged(final ConfigChanged event) {
		for (final BalloonInfoBox infobox : balloon_infoboxes) {
			infobox.onConfigChanged(event);
		}
	}
}
