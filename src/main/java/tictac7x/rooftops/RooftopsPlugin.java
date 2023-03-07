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
import net.runelite.api.events.HitsplatApplied;
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

	private RooftopsOverylayDebug overlay_debug;

	private Courses courses;

	@Provides
	RooftopsConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(RooftopsConfig.class);
	}

	@Override
	protected void startUp() {
		courses = new Courses(config, client);
		overlay = new RooftopsOverlay(config, client, courses);
		overlay_debug = new RooftopsOverylayDebug(client, config, courses);

		overlays.add(overlay);
		overlays.add(overlay_debug);
	}

	@Override
	protected void shutDown() {
		overlays.remove(overlay);
		overlays.remove(overlay_debug);
	}

	@Subscribe
	public void onGameObjectSpawned(final GameObjectSpawned event) {
		overlay.onTileObjectSpawned(event.getGameObject());
		courses.onTileObjectSpawned(event.getGameObject());
	}

	@Subscribe
	public void onGroundObjectSpawned(final GroundObjectSpawned event) {
		overlay.onTileObjectSpawned(event.getGroundObject());
		courses.onTileObjectSpawned(event.getGroundObject());
	}

	@Subscribe
	public void onDecorativeObjectSpawned(final DecorativeObjectSpawned event) {
		overlay.onTileObjectSpawned(event.getDecorativeObject());
		courses.onTileObjectSpawned(event.getDecorativeObject());
	}

	@Subscribe
	public void onItemSpawned(final ItemSpawned event) {
		overlay.onItemSpawned(event);
		courses.onItemSpawned(event);
	}

	@Subscribe
	public void onItemDespawned(final ItemDespawned event) {
		overlay.onItemDespawned(event);
		courses.onItemDespawned(event);
	}

	@Subscribe
	public void onMenuOptionClicked(final MenuOptionClicked event) {
		courses.onMenuOptionClicked(event);
	}

	@Subscribe
	public void onStatChanged(final StatChanged event) {
		courses.onStatChanged(event);
	}

	@Subscribe
	public void onHitsplatApplied(final HitsplatApplied event) {
		if (event.getActor() == client.getLocalPlayer()) {
			courses.onObstacleFailed();
		}
	}

	@Subscribe
	public void onGameStateChanged(final GameStateChanged event) {
		overlay.onGameStateChanged(event);
		courses.onGameStateChanged(event);
	}
}
