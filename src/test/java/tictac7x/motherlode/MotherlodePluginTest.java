package tictac7x.motherlode;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class MotherlodePluginTest {
	public static void main(String[] args) throws Exception {
		ExternalPluginManager.loadBuiltin(MotherlodeImprovedPlugin.class);
		RuneLite.main(args);
	}
}