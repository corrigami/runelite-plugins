package tictac7x.balloon;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import com.google.inject.Provides;
import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.client.plugins.Plugin;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.WidgetLoaded;
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
	description = "Show amount of logs stashed in the balloon storage.",
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

	private BalloonOverlayLogs overlay_logs;
	private BalloonInfoboxLogs infobox_logs;
	private BalloonInfoboxLogs infobox_logs_oak;
	private BalloonInfoboxLogs infobox_logs_willow;
	private BalloonInfoboxLogs infobox_logs_yew;
	private BalloonInfoboxLogs infobox_logs_magic;

	@Override
	protected void startUp() {
		if (overlay_logs == null) {
			overlay_logs = new BalloonOverlayLogs(config, client, configs, items);

			infobox_logs = new BalloonInfoboxLogs(
				"logs",
				items.getImage(ItemID.LOGS),
				() -> config.getStyle() == BalloonConfig.style.INFOBOXES,
				() -> overlay_logs.getLogsCount(BalloonOverlayLogs.STORAGE_INDEX_LOGS),
				"Entrana / Taverley",
				this
			);

			infobox_logs_oak = new BalloonInfoboxLogs(
				"logs_oak",
				items.getImage(ItemID.OAK_LOGS),
				() -> config.getStyle() == BalloonConfig.style.INFOBOXES,
				() -> overlay_logs.getLogsCount(BalloonOverlayLogs.STORAGE_INDEX_LOGS_OAK),
				"Crafting Guild",
				this);

			infobox_logs_willow = new BalloonInfoboxLogs(
				"logs_willow",
				items.getImage(ItemID.WILLOW_LOGS),
				() -> config.getStyle() == BalloonConfig.style.INFOBOXES,
				() -> overlay_logs.getLogsCount(BalloonOverlayLogs.STORAGE_INDEX_LOGS_WILLOW),
				"Varrock",
				this);

			infobox_logs_yew = new BalloonInfoboxLogs(
				"logs_yew",
				items.getImage(ItemID.YEW_LOGS),
				() -> config.getStyle() == BalloonConfig.style.INFOBOXES,
				() -> overlay_logs.getLogsCount(BalloonOverlayLogs.STORAGE_INDEX_LOGS_YEW),
				"Castle Wars",
				this);

			infobox_logs_magic = new BalloonInfoboxLogs(
				"logs_magic",
				items.getImage(ItemID.MAGIC_LOGS),
				() -> config.getStyle() == BalloonConfig.style.INFOBOXES,
				() -> overlay_logs.getLogsCount(BalloonOverlayLogs.STORAGE_INDEX_LOGS_MAGIC),
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
		overlay_logs.onChatMessage(event);
	}

	@Subscribe
	public void onWidgetLoaded(final WidgetLoaded event) {
		client_thread.invokeLater(() -> overlay_logs.onWidgetLoaded(event));
	}

	@Provides
	BalloonConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(BalloonConfig.class);
	}
}
