package tictac7x.deposit_worn_items;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class TicTac7xDepositWornItemsPluginTest {
	public static void main(String[] args) throws Exception {
		ExternalPluginManager.loadBuiltin(TicTac7xDepositWornItemsPlugin.class);
		RuneLite.main(args);
	}
}