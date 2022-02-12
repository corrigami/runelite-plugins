package tictac7x.balloon;

import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.ItemID;
import lombok.extern.slf4j.Slf4j;
import com.google.inject.Provides;
import net.runelite.api.events.*;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.game.ItemManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;

@Slf4j
@PluginDescriptor(
	name = "Balloon Transport System",
	description = "Show amount of logs stored in the balloon transport system storages.",
	tags = { "balloon", "transport", "logs", "storage" }
)
public class TicTac7xBalloonPlugin extends Plugin {
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

	private BalloonStorage balloon_storage;
	private BalloonOverlayLogs overlay_logs;
	private BalloonInfoboxLogs infobox_logs;
	private BalloonInfoboxLogs infobox_logs_oak;
	private BalloonInfoboxLogs infobox_logs_willow;
	private BalloonInfoboxLogs infobox_logs_yew;
	private BalloonInfoboxLogs infobox_logs_magic;

	@Provides
	BalloonConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(BalloonConfig.class);
	}

	@Override
	protected void startUp() {
		if (overlay_logs == null) {
			balloon_storage = new BalloonStorage(config, client, configs);
			overlay_logs = new BalloonOverlayLogs(config, configs, items, balloon_storage);

			infobox_logs = new BalloonInfoboxLogs(
				"logs",
				items.getImage(ItemID.LOGS),
				() -> showInfobox(BalloonStorage.Logs.LOGS),
				() -> balloon_storage.getLogsCount(BalloonStorage.Logs.LOGS),
				"Entrana / Taverley",
				this
			);

			infobox_logs_oak = new BalloonInfoboxLogs(
				"logs_oak",
				items.getImage(ItemID.OAK_LOGS),
				() -> showInfobox(BalloonStorage.Logs.LOGS_OAK),
				() -> balloon_storage.getLogsCount(BalloonStorage.Logs.LOGS_OAK),
				"Crafting Guild",
				this);

			infobox_logs_willow = new BalloonInfoboxLogs(
				"logs_willow",
				items.getImage(ItemID.WILLOW_LOGS),
				() -> showInfobox(BalloonStorage.Logs.LOGS_WILLOW),
				() -> balloon_storage.getLogsCount(BalloonStorage.Logs.LOGS_WILLOW),
				"Varrock",
				this);

			infobox_logs_yew = new BalloonInfoboxLogs(
				"logs_yew",
				items.getImage(ItemID.YEW_LOGS),
				() -> showInfobox(BalloonStorage.Logs.LOGS_YEW),
				() -> balloon_storage.getLogsCount(BalloonStorage.Logs.LOGS_YEW),
				"Castle Wars",
				this);

			infobox_logs_magic = new BalloonInfoboxLogs(
				"logs_magic",
				items.getImage(ItemID.MAGIC_LOGS),
				() -> showInfobox(BalloonStorage.Logs.LOGS_MAGIC),
				() -> balloon_storage.getLogsCount(BalloonStorage.Logs.LOGS_MAGIC),
				"Grand Tree",
				this);
		}

		overlays.add(overlay_logs);
		infoboxes.addInfoBox(infobox_logs);
		infoboxes.addInfoBox(infobox_logs_oak);
		infoboxes.addInfoBox(infobox_logs_willow);
		infoboxes.addInfoBox(infobox_logs_yew);
		infoboxes.addInfoBox(infobox_logs_magic);
	}

	@Override
	protected void shutDown() {
		overlays.remove(overlay_logs);
		infoboxes.removeInfoBox(infobox_logs);
		infoboxes.removeInfoBox(infobox_logs_oak);
		infoboxes.removeInfoBox(infobox_logs_willow);
		infoboxes.removeInfoBox(infobox_logs_yew);
		infoboxes.removeInfoBox(infobox_logs_magic);
	}

	@Subscribe
	public void onChatMessage(final ChatMessage event) {
		client_thread.invokeLater(() -> balloon_storage.onChatMessage(event));
	}

	@Subscribe
	public void onWidgetLoaded(final WidgetLoaded event) {
		client_thread.invokeLater(() -> balloon_storage.onWidgetLoaded(event));
	}

	@Subscribe
	public void onGameObjectSpawned(final GameObjectSpawned event) {
		balloon_storage.onGameObjectSpawned(event);
	}

	@Subscribe
	public void onGameStateChanged(final GameStateChanged event) {
		balloon_storage.onGameStateChanged(event);
	}

	private boolean showInfobox(final BalloonStorage.Logs logs) {
		return config.getStyle() == BalloonConfig.style.INFOBOXES && balloon_storage.showLogs(logs);
	}
}
