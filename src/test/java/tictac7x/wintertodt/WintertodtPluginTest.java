package tictac7x.wintertodt;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class WintertodtPluginTest {
	public static void main(String[] args) throws Exception {
		ExternalPluginManager.loadBuiltin(WintertodtImprovedPlugin.class);
		RuneLite.main(args);
	}
}