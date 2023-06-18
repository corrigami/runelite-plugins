package tictac7x.gotr;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class TicTac7xGotrImprovedPluginTest {
	public static void main(String[] args) throws Exception {
		ExternalPluginManager.loadBuiltin(TicTac7xGotrImprovedPlugin.class);
		RuneLite.main(args);
	}
}