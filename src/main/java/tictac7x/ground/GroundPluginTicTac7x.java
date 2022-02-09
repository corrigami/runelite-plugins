package tictac7x.ground;

import javax.inject.Inject;
import net.runelite.api.Client;
import lombok.extern.slf4j.Slf4j;
import com.google.inject.Provides;
import net.runelite.api.events.ItemDespawned;
import net.runelite.api.events.ItemSpawned;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Ground Items Improved",
	description = "Improved ground items and timers",
	tags = { "ground,items"	}
)
public class GroundPluginTicTac7x extends Plugin {
	@Inject
	private Client client;

	@Inject
	private GroundConfig config;

	@Inject
	private OverlayManager overlays;

	@Provides
	GroundConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(GroundConfig.class);
	}

	private GroundOverlay overlay_ground;

	@Override
	protected void startUp() {
		if (overlay_ground == null) {
			overlay_ground = new GroundOverlay(client);
		}

		overlays.add(overlay_ground);
	}

	@Override
	protected void shutDown() {
		overlays.remove(overlay_ground);
	}

	@Subscribe
	public void onItemSpawned(final ItemSpawned event) {
		overlay_ground.onItemSpawned(event);
	}

	@Subscribe
	public void onItemDespawned(final ItemDespawned event) {
		overlay_ground.onItemDespawned(event);
	}
}
