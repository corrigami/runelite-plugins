package tictac7x.daily;

import javax.inject.Inject;
import net.runelite.api.Client;
import lombok.extern.slf4j.Slf4j;
import com.google.inject.Provides;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Daily Tasks",
	description = "Reminds you to do your daily tasks",
	tags = { "daily","battlestaves","essence","ess","kingdom","battlestaff","sand","flax","bowstring","ogre","rantz","bone","bonemeal","slime","buckets","herb","boxes,nmz,dynamite,mith,grapple" }

)
public class TicTac7xDailyPlugin extends Plugin {
	@Inject
	private Client client;

	@Inject
	private DailyConfig config;

	@Provides
	DailyConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(DailyConfig.class);
	}

	@Override
	protected void startUp() {
	}

	@Override
	protected void shutDown() {
	}
}
