package tictac7x.storage;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class StoragePluginTest {
	public static void main(String[] args) throws Exception {
		ExternalPluginManager.loadBuiltin(StoragePlugin.class);
		RuneLite.main(args);
	}
}