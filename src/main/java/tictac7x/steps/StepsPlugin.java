package tictac7x.steps;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Steps Measurements",
	description = "Measure how many steps were made and how much time did it take",
	tags = {
		"steps",
		"measure",
		"track",
		"time",
		"distance"
	}
)
public class StepsPlugin extends Plugin {
	@Inject
	private Client client;

	@Inject
	private OverlayManager overlays;

	@Inject
	private StepsConfig config;

	@Inject
	private StepsOverlay overlay;

	@Override
	protected void startUp() {
		overlays.add(overlay);
	}

	@Override
	protected void shutDown() {
		overlays.remove(overlay);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged) {
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN) {
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null);
		}
	}

	@Provides
	StepsConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(StepsConfig.class);
	}
}
