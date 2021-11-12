package tictac7x.rooftops;

import javax.inject.Inject;
import net.runelite.api.Client;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GameState;
import com.google.inject.Provides;
import net.runelite.api.events.*;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Rooftops",
	description = "Rooftops",
	tags = { "rooftops" }
)
public class RooftopsPlugin extends Plugin {
	@Inject
	private Client client;

	@Inject
	private OverlayManager overlays;

	@Inject
	private RooftopsConfig config;

	private RooftopsOverlay overlay;

	private OverylayDebug debug;

	@Provides
	RooftopsConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(RooftopsConfig.class);
	}

	@Override
	protected void startUp() {
		if (overlay == null) {
			overlay = new RooftopsOverlay(config, client);
			debug = new OverylayDebug();
		}

		overlays.add(overlay);
		overlays.add(debug);
	}

	@Override
	protected void shutDown() {
		overlays.remove(overlay);
		overlays.remove(debug);
	}

	@Subscribe
	public void onGameObjectSpawned(final GameObjectSpawned event) {
		overlay.onTileObjectSpawned(event.getGameObject());
	}

	@Subscribe
	public void onGameObjectDespawned(final GameObjectDespawned event) {
		overlay.onTileObjectDespawned(event.getGameObject());
	}

	@Subscribe
	public void onGroundObjectSpawned(final GroundObjectSpawned event) {
		overlay.onTileObjectSpawned(event.getGroundObject());
	}

	@Subscribe
	public void onGroundObjectDespawned(final GroundObjectDespawned event) {
		overlay.onTileObjectDespawned(event.getGroundObject());
	}

	@Subscribe
	public void onDecorativeObjectSpawned(final DecorativeObjectSpawned event) {
		overlay.onTileObjectSpawned(event.getDecorativeObject());
	}

	@Subscribe
	public void onDecorativeObjectDespawned(final DecorativeObjectDespawned event) {
		overlay.onTileObjectDespawned(event.getDecorativeObject());
	}

	@Subscribe
	public void onItemSpawned(final ItemSpawned event) {
		overlay.onItemSpawned(event);
	}

	@Subscribe
	public void onItemDespawned(final ItemDespawned event) {
		overlay.onItemDespawned(event);
	}

	@Subscribe
	public void onMenuOptionClicked(final MenuOptionClicked menu_option_clicked) {
		overlay.onMenuOptionClicked(menu_option_clicked);
	}

	@Subscribe
	public void onStatChanged(final StatChanged stat_changed) {
		overlay.onStatChanged(stat_changed);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event) {
		if (event.getGameState() == GameState.LOADING) {
			overlay.clear();
		}
	}
}
