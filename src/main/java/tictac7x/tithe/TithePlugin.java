package tictac7x.tithe;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.Client;
import lombok.extern.slf4j.Slf4j;
import com.google.inject.Provides;
import net.runelite.api.GameState;
import net.runelite.api.events.*;
import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.Plugin;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.game.ItemManager;
import com.google.common.collect.ImmutableSet;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Tithe Farm Improved",
	description = "Improve overall experience for Tithe farm",
	tags = { "tithe", "farm" },
	conflicts = "Tithe Farm"
)
public class TithePlugin extends Plugin {
	private boolean in_tithe_farm = false;
	public final Map<LocalPoint, TithePlant> plants = new HashMap<>();

	@Inject
	private TitheConfig config;

	@Inject
	private Client client;

	@Inject
	private ClientThread client_thread;

	@Inject
	private OverlayManager overlays;

	@Inject
	private ItemManager items;

	@Inject
	private ConfigManager configs;

	@Provides
	TitheConfig provideConfig(final ConfigManager configs) {
		return configs.getConfig(tictac7x.tithe.TitheConfig.class);
	}

	private TitheOverlayPlants    overlay_plants;
	private TitheOverlayPoints    overlay_points;
	private TitheOverlayPatches   overlay_patches;

	@Override
	protected void startUp() {
		overlay_points = new TitheOverlayPoints(this, config, client);
		overlay_patches = new TitheOverlayPatches(this, config, client);
		overlay_plants = new TitheOverlayPlants(this, config, client);

		overlays.add(overlay_points);
		overlays.add(overlay_patches);
		overlays.add(overlay_plants);

		overlay_points.startUp();
	}

	@Override
	protected void shutDown() {
		overlay_points.shutDown();

		overlays.remove(overlay_points);
		overlays.remove(overlay_patches);
		overlays.remove(overlay_plants);
	}

	@Subscribe
	public void onGameObjectSpawned(final GameObjectSpawned event) {
		overlay_plants.onGameObjectSpawned(event.getGameObject());
	}

	@Subscribe
	public void onItemContainerChanged(final ItemContainerChanged event) {
		overlay_points.onItemContainerChanged(event);
	}

	@Subscribe
	public void onVarbitChanged(final VarbitChanged event) {
		overlay_points.onVarbitChanged(event);
	}

	@Subscribe
	public void onGameTick(final GameTick event) {
		overlay_plants.onGameTick();
	}

	@Subscribe
	public void onWidgetLoaded(final WidgetLoaded event) {
		if (event.getGroupId() == WidgetInfo.TITHE_FARM.getGroupId()) {
			this.in_tithe_farm = true;
		}

		overlay_points.onWidgetLoaded(event);
	}

	@Subscribe
	public void onConfigChanged(final ConfigChanged event) {
		overlay_points.onConfigChanged(event);
	}

	@Subscribe
	public void onGameStateChanged(final GameStateChanged event) {
		if (event.getGameState() != GameState.LOADING) return;

		// When entering tithe farm, this check fails first time, since the widget is loaded later.
		// This check is needed for when loading happens while in the tithe farm.
		// Or when you leave from the farm.
		final Widget widget_tithe = client.getWidget(WidgetInfo.TITHE_FARM);
		this.in_tithe_farm = widget_tithe != null;

		if (!this.in_tithe_farm) {
			this.plants.clear();
		}
	}

	public boolean inTitheFarm() {
		return in_tithe_farm;
	}
}
