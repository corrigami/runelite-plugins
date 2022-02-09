package tictac7x.ground;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class GroundPluginTest {
	public static void main(String[] args) throws Exception {
		ExternalPluginManager.loadBuiltin(GroundPluginTicTac7x.class);
		RuneLite.main(args);
	}
}