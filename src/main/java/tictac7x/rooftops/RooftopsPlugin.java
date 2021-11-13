package tictac7x.rooftops;

import tictac7x.rooftops.courses.Courses;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import com.google.inject.Provides;

import net.runelite.api.Client;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.api.events.StatChanged;
import net.runelite.api.events.ItemSpawned;
import net.runelite.api.events.ItemDespawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GroundObjectSpawned;
import net.runelite.api.events.DecorativeObjectSpawned;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Rooftop Agility Improved",
	description = "Improved clickboxes for rooftop agility courses",
	tags = { "roof", "rooftop", "agility", "mark", "grace", "graceful" }
)
public class RooftopsPlugin extends Plugin {
	@Inject
	private Client client;

	@Inject
	private OverlayManager overlays;

	@Inject
	private RooftopsConfig config;

	private RooftopsOverlay overlay;

//	private RooftopsOverylayDebug debug;

	private Courses course_manager;

	@Provides
	RooftopsConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(RooftopsConfig.class);
	}

	@Override
	protected void startUp() {
		if (overlay == null) {
			course_manager = new Courses(config, client);
			overlay = new RooftopsOverlay(course_manager, config, client);
//			debug = new RooftopsOverylayDebug(client, course_manager);
		}

		overlays.add(overlay);
//		overlays.add(debug);
	}

	@Override
	protected void shutDown() {
		overlays.remove(overlay);
//		overlays.remove(debug);
	}

	@Subscribe
	public void onGameObjectSpawned(final GameObjectSpawned event) {
		overlay.onTileObjectSpawned(event.getGameObject());
		course_manager.onTileObjectSpawned(event.getGameObject());
	}

	@Subscribe
	public void onGroundObjectSpawned(final GroundObjectSpawned event) {
		overlay.onTileObjectSpawned(event.getGroundObject());
		course_manager.onTileObjectSpawned(event.getGroundObject());
	}

	@Subscribe
	public void onDecorativeObjectSpawned(final DecorativeObjectSpawned event) {
		overlay.onTileObjectSpawned(event.getDecorativeObject());
		course_manager.onTileObjectSpawned(event.getDecorativeObject());
	}

	@Subscribe
	public void onItemSpawned(final ItemSpawned event) {
		overlay.onItemSpawned(event);
		course_manager.onItemSpawned(event);
	}

	@Subscribe
	public void onItemDespawned(final ItemDespawned event) {
		overlay.onItemDespawned(event);
		course_manager.onItemDespawned(event);
	}

	@Subscribe
	public void onMenuOptionClicked(final MenuOptionClicked event) {
		course_manager.onMenuOptionClicked(event);
	}

	@Subscribe
	public void onStatChanged(final StatChanged event) {
		course_manager.onStatChanged(event);
	}

	@Subscribe
	public void onGameStateChanged(final GameStateChanged event) {
		overlay.onGameStateChanged(event);
	}
}
