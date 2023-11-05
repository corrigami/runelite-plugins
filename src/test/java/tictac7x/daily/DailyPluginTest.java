package tictac7x.daily;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class DailyPluginTest {
	public static void main(String[] args) throws Exception {
		ExternalPluginManager.loadBuiltin(TicTac7xDailyTasksPlugin.class);
		RuneLite.main(args);
	}
}