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

	@Inject
	private RooftopsCourseManager courseManager;

	@Inject
	private RooftopsOverlay overlayRooftops;

	@Provides
	RooftopsConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(RooftopsConfig.class);
	}

	@Override
	protected void startUp() {
		overlays.add(overlayRooftops);
	}

	@Override
	protected void shutDown() {
		overlays.remove(overlayRooftops);
	}

	@Subscribe
	public void onGameObjectSpawned(final GameObjectSpawned event) {
		courseManager.onTileObjectSpawned(event.getGameObject());
	}

	@Subscribe
	public void onGroundObjectSpawned(final GroundObjectSpawned event) {
		courseManager.onTileObjectSpawned(event.getGroundObject());
	}

	@Subscribe
	public void onDecorativeObjectSpawned(final DecorativeObjectSpawned event) {
		courseManager.onTileObjectSpawned(event.getDecorativeObject());
	}

	@Subscribe
	public void onItemSpawned(final ItemSpawned event) {
		courseManager.onItemSpawned(event);
	}

	@Subscribe
	public void onItemDespawned(final ItemDespawned event) {
		courseManager.onItemDespawned(event);
	}

	@Subscribe
	public void onChatMessage(final ChatMessage event) {
		courseManager.onChatMessage(event);
	}

	@Subscribe
	public void onStatChanged(final StatChanged event) {
		courseManager.onStatChanged(event);
	}

	@Subscribe
	public void onHitsplatApplied(final HitsplatApplied event) {
		courseManager.onHitsplatApplied(event);
	}

	@Subscribe
	public void onGameTick(final GameTick gametick) {
		courseManager.onGameTick(gametick);
	}

	@Subscribe
	public void onGameStateChanged(final GameStateChanged event) {
		courseManager.onGameStateChanged(event);
	}

	@Subscribe
	public void onMenuOptionClicked(final MenuOptionClicked event) {
		courseManager.onMenuOptionClicked(event);
	}
}
