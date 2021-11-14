package tictac7x.sulliuscep;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import com.google.inject.Provides;

import net.runelite.api.Client;
import net.runelite.client.plugins.Plugin;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Sulliuscep run",
	description = "Helpful clickboxes for sulliuscep run",
	tags = { "fossil", "sullius", "sulliuscep", "numulite", "tar", "swamp" }
)
public class SulliuscepPlugin extends Plugin {
	@Inject
	private Client client;

	@Inject
	private SulliuscepConfig config;

	@Inject
	private OverlayManager overlays;

	private SulliuscepOverlay sulliuscep_overlay;

	@Provides
	SulliuscepConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(SulliuscepConfig.class);
	}

	@Override
	protected void startUp() {
		if (sulliuscep_overlay == null) {
			sulliuscep_overlay = new SulliuscepOverlay(client, config);
		}

		overlays.add(sulliuscep_overlay);
	}

	@Override
	protected void shutDown() {
		overlays.remove(sulliuscep_overlay);
	}

	@Subscribe
	public void onGameObjectSpawned(final GameObjectSpawned event) {
		sulliuscep_overlay.onGameObjectSpawned(event.getGameObject());
	}

	@Subscribe
	public void onGameObjectDespawned(final GameObjectDespawned event) {
		sulliuscep_overlay.onGameObjectDespawned(event.getGameObject());
	}

	@Subscribe
	public void onVarbitChanged(final VarbitChanged event) {
		sulliuscep_overlay.onVarbitChanged(event);
	}

	@Subscribe
	public void onGameStateChanged(final GameStateChanged event) {
		sulliuscep_overlay.onGameStateChanged(event);
	}
}
