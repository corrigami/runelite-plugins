package tictac7x.rooftops;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.DecorativeObjectSpawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.GroundObjectSpawned;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.ItemDespawned;
import net.runelite.api.events.ItemSpawned;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.StatChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;

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

	private RooftopsCoursesManager coursesManager;

	private RooftopsOverlay overlayRooftops;

	@Provides
	RooftopsConfig provideConfig(final ConfigManager configManager) {
		return configManager.getConfig(RooftopsConfig.class);
	}

	@Override
	protected void startUp() {
		coursesManager = new RooftopsCoursesManager(client);
		overlayRooftops = new RooftopsOverlay(config, coursesManager);
		overlays.add(overlayRooftops);
	}

	@Override
	protected void shutDown() {
		overlays.remove(overlayRooftops);
	}

	@Subscribe
	public void onGameObjectSpawned(final GameObjectSpawned event) {
		coursesManager.onTileObjectSpawned(event.getGameObject());
	}

	@Subscribe
	public void onGroundObjectSpawned(final GroundObjectSpawned event) {
		coursesManager.onTileObjectSpawned(event.getGroundObject());
	}

	@Subscribe
	public void onDecorativeObjectSpawned(final DecorativeObjectSpawned event) {
		coursesManager.onTileObjectSpawned(event.getDecorativeObject());
	}

	@Subscribe
	public void onItemSpawned(final ItemSpawned event) {
		coursesManager.onItemSpawned(event);
	}

	@Subscribe
	public void onItemDespawned(final ItemDespawned event) {
		coursesManager.onItemDespawned(event);
	}

	@Subscribe
	public void onChatMessage(final ChatMessage event) {
		coursesManager.onChatMessage(event);
	}

	@Subscribe
	public void onStatChanged(final StatChanged event) {
		coursesManager.onStatChanged(event);
	}

	@Subscribe
	public void onHitsplatApplied(final HitsplatApplied event) {
		coursesManager.onHitsplatApplied(event);
	}

	@Subscribe
	public void onGameTick(final GameTick gametick) {
		coursesManager.onGameTick(gametick);
	}

	@Subscribe
	public void onGameStateChanged(final GameStateChanged event) {
		coursesManager.onGameStateChanged(event);
	}

	@Subscribe
	public void onMenuOptionClicked(final MenuOptionClicked event) {
		coursesManager.onMenuOptionClicked(event);
	}
}
