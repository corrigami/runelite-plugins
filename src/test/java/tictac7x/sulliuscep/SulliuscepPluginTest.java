package tictac7x.sulliuscep;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class SulliuscepPluginTest {
	public static void main(String[] args) throws Exception {
		ExternalPluginManager.loadBuiltin(SulliuscepPlugin.class);
		RuneLite.main(args);
	}
}