package tictac7x.motherlode;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import com.google.inject.Provides;
import net.runelite.api.Client;
import net.runelite.api.TileObject;
import net.runelite.api.events.*;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import tictac7x.motherlode.oreveins.OreVeins;
import tictac7x.motherlode.rockfalls.Rockfalls;

@Slf4j
@PluginDescriptor(
	name = "Motherlode Mine Improved",
	description = "Better indicators for ore veins",
	tags = { "motherlode", "prospector", "golden", "nugget" },
	conflicts = "Motherlode Mine"
)
public class TicTac7xMotherlodePlugin extends Plugin {
	@Inject
	private Client client;

	@Inject
	private ConfigManager configManager;

	@Inject
	private TicTac7xMotherlodeConfig config;

	@Inject
	private OverlayManager overlayManager;

	private Player player;
	private Bank bank;
	private Inventory inventory;
	private Hopper hopper;
	private OreVeins oreVeins;
	private Rockfalls rockfalls;
	private Sack sack;

	@Provides
	TicTac7xMotherlodeConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(TicTac7xMotherlodeConfig.class);
	}

	@Override
	protected void startUp() {
		player = new Player(client);
		bank = new Bank(configManager, config);
		inventory = new Inventory();
		hopper = new Hopper(client, inventory);
		sack = new Sack(client, config, bank, inventory, hopper);
		oreVeins = new OreVeins(config, player, sack);
		rockfalls = new Rockfalls(config, player);

		overlayManager.add(oreVeins);
		overlayManager.add(rockfalls);
		overlayManager.add(sack);
	}

	@Override
	protected void shutDown() {
		sack.shutDown();
		overlayManager.remove(oreVeins);
		overlayManager.remove(rockfalls);
		overlayManager.remove(sack);
	}

	@Subscribe
	public void onGameStateChanged(final GameStateChanged event) {
		oreVeins.onGameStateChanged(event);
		rockfalls.onGameStateChanged(event);
	}

	@Subscribe
	public void onItemContainerChanged(final ItemContainerChanged event) {
		if (!player.isInMotherlode()) return;
		inventory.onItemContainerChanged(event);
		bank.onItemContainerChanged(event);
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
		hopper.onAnimationChanged(event);
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
		player.checkIsInMotherlode();
		if (!player.isInMotherlode()) return;

		player.onGameTick();
		oreVeins.onGameTick();
	}

	@Subscribe
	public void onConfigChanged(final ConfigChanged event) {
		if (!player.isInMotherlode()) return;
		sack.onConfigChanged(event);
	}

	@Subscribe
	public void onWidgetLoaded(final WidgetLoaded event) {
		if (!player.isInMotherlode()) return;
		sack.onWidgetLoaded(event);
	}

	@Subscribe
	public void onVarbitChanged(final VarbitChanged event) {
		if (!player.isInMotherlode()) return;
		hopper.onVarbitChanged(event);
	}

	public static String getWorldObjectKey(final TileObject tileObject) {
		return tileObject.getWorldLocation().getX() + "_" + tileObject.getWorldLocation().getY();
	}
}
