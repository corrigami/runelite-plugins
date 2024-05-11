package tictac7x.motherlode;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import com.google.inject.Provides;
import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.WallObjectSpawned;
import net.runelite.api.events.WallObjectDespawned;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GroundObjectSpawned;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.GroundObjectDespawned;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Motherlode Mine Improved",
	description = "Better indicators for ore veins",
	tags = { "motherlode", "prospector", "golden", "nugget" }
)
public class TicTac7xMotherlodePlugin extends Plugin {
	@Inject
	private Client client;

	@Inject
	private MotherlodeConfig config;

	@Inject
	private OverlayManager overlays;

	@Inject
	private ClientThread client_thread;

	@Provides
	MotherlodeConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(MotherlodeConfig.class);
	}

	private Motherlode motherlode;
	private Inventory inventory;
	private MotherlodeSack sack;
	private MotherlodeVeins veins;
	private MotherlodeSackWidget widget_sack;
	private MotherlodeVeinsOverlay overlay_veins;
	private MotherlodeRockfallsOverlay overlay_rockfalls;
	private MotherlodeObjectsOverlay overlay_objects;

	@Override
	protected void startUp() {
		if (motherlode == null) {
			motherlode = new Motherlode(client, config);
			inventory = motherlode.getInventory();
			sack = motherlode.getSack();
			veins = motherlode.getVeins();

			overlay_veins = new MotherlodeVeinsOverlay(config, motherlode, client);
			widget_sack = new MotherlodeSackWidget(config, motherlode, client);
			overlay_rockfalls = new MotherlodeRockfallsOverlay(config, motherlode, client);
			overlay_objects = new MotherlodeObjectsOverlay(config, motherlode);
		}

		client_thread.invokeLater(() -> {
			widget_sack.loadNativeWidget();
			motherlode.updatePayDirtNeeded();
		});

		overlays.add(overlay_veins);
		overlays.add(overlay_rockfalls);
		overlays.add(overlay_objects);
		overlays.add(widget_sack);
	}

	@Override
	protected void shutDown() {
		overlay_veins.clear();
		widget_sack.updateMotherlodeNativeWidget(false);
		overlay_rockfalls.clear();

		overlays.remove(overlay_veins);
		overlays.remove(overlay_rockfalls);
		overlays.remove(overlay_objects);
		overlays.remove(widget_sack);
	}

	@Subscribe
	public void onGameObjectSpawned(final GameObjectSpawned event) {
		overlay_veins.onTileObjectSpawned(event.getGameObject());
		overlay_rockfalls.onTileObjectSpawned(event.getGameObject());
		overlay_objects.onTileObjectSpawned(event.getGameObject());
	}

	@Subscribe
	public void onGameObjectDespawned(final GameObjectDespawned event) {
		overlay_veins.onTileObjectDespawned(event.getGameObject());
		overlay_rockfalls.onTileObjectDespawned(event.getGameObject());
		overlay_objects.onTileObjectDespawned(event.getGameObject());
	}

	@Subscribe
	public void onGroundObjectSpawned(final GroundObjectSpawned event) {
		overlay_objects.onTileObjectSpawned(event.getGroundObject());
	}

	@Subscribe
	public void onGroundObjectDespawned(final GroundObjectDespawned event) {
		overlay_objects.onTileObjectDespawned(event.getGroundObject());
	}

	@Subscribe
	public void onWallObjectSpawned(final WallObjectSpawned event) {
		veins.onTileObjectSpawned(event.getWallObject());
		overlay_veins.onTileObjectSpawned(event.getWallObject());
	}

	@Subscribe
	public void onWallObjectDespawned(final WallObjectDespawned event) {
		veins.onTileObjectDespawned(event.getWallObject());
		overlay_veins.onTileObjectDespawned(event.getWallObject());
	}

	@Subscribe
	public void onGameStateChanged(final GameStateChanged event) {
		motherlode.onGameStateChanged(event);
		overlay_veins.onGameStateChanged(event);
		overlay_rockfalls.onGameStateChanged(event);
	}

	@Subscribe
	public void onGameTick(final GameTick event) {
		motherlode.onGameTick();
		sack.onGameTick();
		veins.onGameTick();
	}

	@Subscribe
	public void onVarbitChanged(final VarbitChanged event) {
		sack.onVarbitChanged();
	}

	@Subscribe
	public void onWidgetLoaded(final WidgetLoaded event) {
		client_thread.invokeLater(() -> widget_sack.onWidgetLoaded(event));
	}

	@Subscribe
	public void onItemContainerChanged(final ItemContainerChanged event) {
		inventory.onItemContainerChanged(event);
	}

	@Subscribe
	public void onConfigChanged (final ConfigChanged event) {
		motherlode.onConfigChanged(event);
		widget_sack.onConfigChanged(event);
	}
}
