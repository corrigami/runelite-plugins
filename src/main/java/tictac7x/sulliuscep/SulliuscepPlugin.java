package tictac7x.sulliuscep;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import com.google.inject.Provides;

import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
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
	private ClientThread client_thread;

	@Inject
	private SulliuscepConfig config;

	@Inject
	private OverlayManager overlays;

	private TarSwamp tar_swamp;
	private TarSwampOverlay tar_swamp_overlay;
	private TarSwampWidget tar_swamp_widget;

	@Provides
	SulliuscepConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(SulliuscepConfig.class);
	}

	@Override
	protected void startUp() {
		tar_swamp = new TarSwamp(client);
		tar_swamp_overlay = new TarSwampOverlay(config, tar_swamp);
		tar_swamp_widget = new TarSwampWidget(config, tar_swamp);

		overlays.add(tar_swamp_overlay);
		overlays.add(tar_swamp_widget);
	}

	@Override
	protected void shutDown() {
		overlays.remove(tar_swamp_overlay);
		overlays.remove(tar_swamp_widget);
	}

	@Subscribe
	public void onGameObjectSpawned(final GameObjectSpawned event) {
		tar_swamp_overlay.onGameObjectSpawned(event.getGameObject());
	}

	@Subscribe
	public void onGameObjectDespawned(final GameObjectDespawned event) {
		tar_swamp_overlay.onGameObjectDespawned(event.getGameObject());
	}

	@Subscribe
	public void onVarbitChanged(final VarbitChanged event) {
		tar_swamp.onVarbitChanged();
	}

	@Subscribe
	public void onGameStateChanged(final GameStateChanged event) {
		tar_swamp.onGameStateChanged(event);
		tar_swamp_overlay.onGameStateChanged(event);
	}
}
