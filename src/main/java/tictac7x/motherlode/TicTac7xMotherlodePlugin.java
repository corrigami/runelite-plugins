package tictac7x.motherlode;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import com.google.inject.Provides;
import net.runelite.api.Client;
import net.runelite.api.events.*;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Motherlode Mine Improved",
	description = "Better indicators for ore veins",
	tags = { "motherlode", "prospector", "golden", "nugget" }
)
public class TicTac7xMotherlodePlugin extends Plugin {
	@Inject
	private Client client;

	@Inject
	private MotherlodeConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private Player player;

	@Inject
	private Inventory inventory;

	@Inject
	private OreVeins oreVeins;

	@Provides
	MotherlodeConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(MotherlodeConfig.class);
	}

	@Override
	protected void startUp() {
		overlayManager.add(oreVeins);
	}

	@Override
	protected void shutDown() {
		overlayManager.remove(oreVeins);
	}

	@Subscribe
	public void onGameStateChanged(final GameStateChanged event) {
		player.onGameStateChanged(event);
		oreVeins.onGameStateChanged(event);
	}

	@Subscribe
	public void onItemContainerChanged(final ItemContainerChanged event) {
		if (!player.isInMotherlode()) return;

		inventory.onItemContainerChanged(event);
	}

	@Subscribe
	public void onWallObjectSpawned(final WallObjectSpawned event) {
		if (!player.isInMotherlode()) return;

		oreVeins.onWallObjectSpawned(event);
	}

	@Subscribe
	public void onWallObjectDespawned(final WallObjectDespawned event) {
		if (!player.isInMotherlode()) return;

		oreVeins.onWallObjectDespawned(event);
	}

	@Subscribe
	public void onAnimationChanged(final AnimationChanged event) {
		if (!player.isInMotherlode()) return;

		oreVeins.onAnimationChanged(event);
	}

	@Subscribe
	public void onGameTick(final GameTick event) {
		oreVeins.onGameTick();
	}
}
