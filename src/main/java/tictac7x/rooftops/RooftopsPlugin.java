package tictac7x.rooftops;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
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

	private RooftopsOverlayObstacles overlay_obstacles;
	private RooftopsOverlayMarks overlay_marks;

	@Provides
	RooftopsConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(RooftopsConfig.class);
	}

	@Override
	protected void startUp() {
		if (overlay_obstacles == null) {
			overlay_marks = new RooftopsOverlayMarks();
			overlay_obstacles = new RooftopsOverlayObstacles(client, overlay_marks);
		}

		overlays.add(overlay_obstacles);
		overlays.add(overlay_marks);
	}

	@Override
	protected void shutDown() {
		overlays.remove(overlay_obstacles);
		overlays.remove(overlay_marks);
	}

	@Subscribe
	public void onGameObjectSpawned(final GameObjectSpawned event) {
		overlay_obstacles.onTileObjectSpawned(event.getGameObject());
	}

	@Subscribe
	public void onGameObjectDespawned(final GameObjectDespawned event) {
		overlay_obstacles.onTileObjectDespawned(event.getGameObject());
	}

	@Subscribe
	public void onGroundObjectSpawned(final GroundObjectSpawned event) {
		overlay_obstacles.onTileObjectSpawned(event.getGroundObject());
	}

	@Subscribe
	public void onGroundObjectDespawned(final GroundObjectDespawned event) {
		overlay_obstacles.onTileObjectDespawned(event.getGroundObject());
	}

	@Subscribe
	public void onWallObjectSpawned(final WallObjectSpawned event) {
		overlay_obstacles.onTileObjectSpawned(event.getWallObject());
	}

	@Subscribe
	public void onWallObjectDespawned(final WallObjectDespawned event) {
		overlay_obstacles.onTileObjectDespawned(event.getWallObject());
	}

	@Subscribe
	public void onDecorativeObjectSpawned(final DecorativeObjectSpawned event) {
		overlay_obstacles.onTileObjectSpawned(event.getDecorativeObject());
	}

	@Subscribe
	public void onDecorativeObjectDespawned(final DecorativeObjectDespawned event) {
		overlay_obstacles.onTileObjectDespawned(event.getDecorativeObject());
	}

	@Subscribe
	public void onItemSpawned(final ItemSpawned event) {
		overlay_marks.onItemSpawned(event);
	}

	@Subscribe
	public void onItemDespawned(final ItemDespawned event) {
		overlay_marks.onItemDespawned(event);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event) {
		if (event.getGameState() == GameState.LOADING) {
			overlay_obstacles.clear();
		}
	}
}
