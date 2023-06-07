package tictac7x.wintertodt;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class WintertodtPluginTest {
	public static void main(String[] args) throws Exception {
		ExternalPluginManager.loadBuiltin(TicTac7xWintertodtPlugin.class);
		RuneLite.main(args);
	}
}