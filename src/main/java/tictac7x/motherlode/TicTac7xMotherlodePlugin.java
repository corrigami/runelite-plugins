package tictac7x.motherlode;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import com.google.inject.Provides;
import net.runelite.api.Client;
import net.runelite.api.TileObject;
import net.runelite.api.WallObject;
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
	private TicTac7xMotherlodeConfig config;

	@Inject
	private OverlayManager overlayManager;

	private Player player;
	private Inventory inventory;
	private OreVeins oreVeins;
	private Rockfalls rockfalls;

	@Provides
	TicTac7xMotherlodeConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(TicTac7xMotherlodeConfig.class);
	}

	@Override
	protected void startUp() {
		player = new Player(client);
		inventory = new Inventory();
		oreVeins = new OreVeins(config, player);
		rockfalls = new Rockfalls(config, player);

		overlayManager.add(oreVeins);
		overlayManager.add(rockfalls);
	}

	@Override
	protected void shutDown() {
		overlayManager.remove(oreVeins);
		overlayManager.remove(rockfalls);
	}

	@Subscribe
	public void onGameStateChanged(final GameStateChanged event) {
		player.onGameStateChanged(event);
		oreVeins.onGameStateChanged(event);
		rockfalls.onGameStateChanged(event);
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
	public void onGameObjectSpawned(final GameObjectSpawned event) {
		if (!player.isInMotherlode()) return;

		rockfalls.onGameObjectSpawned(event);
	}

	@Subscribe
	public void onGameObjectDespawned(final GameObjectDespawned event) {
		if (!player.isInMotherlode()) return;

		rockfalls.onGameObjectDespawned(event);
	}

	@Subscribe
	public void onGameTick(final GameTick event) {
		if (!player.isInMotherlode()) return;

		player.onGameTick();
		oreVeins.onGameTick();
	}

	public static String getWorldObjectKey(final TileObject tileObject) {
		return tileObject.getWorldLocation().getX() + "_" + tileObject.getWorldLocation().getY();
	}
}
