package tictac7x.balloon;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class BalloonPluginTest {
	public static void main(String[] args) throws Exception {
		ExternalPluginManager.loadBuiltin(tictac7x.balloon.BalloonPlugin.class);
		RuneLite.main(args);
	}
}