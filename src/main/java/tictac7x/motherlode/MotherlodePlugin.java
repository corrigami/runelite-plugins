package tictac7x.motherlode;

import javax.inject.Inject;

import net.runelite.api.Client;
import lombok.extern.slf4j.Slf4j;
import com.google.inject.Provides;
import net.runelite.api.events.*;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Motherlode Mine Improved",
	description = "Better indicators for ore veins",
	tags = { "motherlode", "prospector", "golden", "nugget" }
)
public class MotherlodePlugin extends Plugin {
	@Inject
	private Client client;

	@Inject
	private MotherlodeConfig config;

	@Inject
	private OverlayManager overlays;

	@Provides
	MotherlodeConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(MotherlodeConfig.class);
	}

	private Motherlode motherlode;
	private MotherlodeInventory inventory;
	private MotherlodeVeins veins;
	private MotherlodeSack sack;
	private MotherlodeSackWidget sack_widget;
	private MotherlodeVeinsOverlay veins_overlay;

	@Override
	protected void startUp() {
		if (motherlode == null) {
			motherlode = new Motherlode(client);
			inventory = new MotherlodeInventory();
			veins = new MotherlodeVeins(motherlode);
			sack = new MotherlodeSack(motherlode, inventory, client);

			veins_overlay = new MotherlodeVeinsOverlay(config, motherlode, veins, sack, inventory, client);
			sack_widget = new MotherlodeSackWidget(motherlode, sack, inventory);
		}

		overlays.add(veins_overlay);
		overlays.add(sack_widget);
	}

	@Override
	protected void shutDown() {
		overlays.remove(veins_overlay);
		overlays.remove(sack_widget);
	}

	@Subscribe
	public void onGameObjectSpawned(final GameObjectSpawned event) {
		veins_overlay.onTileObjectSpawned(event.getGameObject());
	}

	@Subscribe
	public void onGameObjectDespawned(final GameObjectDespawned event) {
		veins_overlay.onTileObjectDespawned(event.getGameObject());
	}

	@Subscribe
	public void onWallObjectSpawned(final WallObjectSpawned event) {
		veins.onTileObjectSpawned(event.getWallObject());
		veins_overlay.onTileObjectSpawned(event.getWallObject());
	}

	@Subscribe
	public void onWallObjectDespawned(final WallObjectDespawned event) {
		veins.onTileObjectDespawned(event.getWallObject());
		veins_overlay.onTileObjectDespawned(event.getWallObject());
	}

	@Subscribe
	public void onGameStateChanged(final GameStateChanged event) {
		motherlode.onGameStateChanged(event);
		veins_overlay.onGameStateChanged(event);
	}

	@Subscribe
	public void onGameTick(final GameTick event) {
		sack.onGameTick();
		veins.onGameTick();
	}

	@Subscribe
	public void onVarbitChanged(final VarbitChanged event) {
		sack.onVarbitChanged();
	}

	@Subscribe
	public void onWidgetLoaded(final WidgetLoaded event) {
		sack_widget.onWidgetLoaded(event);
	}

	@Subscribe
	public void onItemContainerChanged(final ItemContainerChanged event) {
		inventory.onItemContainerChanged(event);
	}
}
