package tictac7x.motherlode;

import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.config.ConfigManager;

public class Bank {
    private final ConfigManager configManager;
    private final TicTac7xMotherlodeConfig config;

    public Bank(final ConfigManager configManager, final TicTac7xMotherlodeConfig config) {
        this.configManager = configManager;
        this.config = config;
    }

    public int getGoldenNuggets() {
        return config.getBankGoldenNuggets();
    }

    public void onItemContainerChanged(final ItemContainerChanged event) {
        if (event.getContainerId() != InventoryID.BANK.getId()) return;

        final Item goldenNuggets = event.getItemContainer().getItem(ItemID.GOLDEN_NUGGET);
        configManager.setConfiguration(TicTac7xMotherlodeConfig.group, TicTac7xMotherlodeConfig.bank_golden_nuggets, goldenNuggets != null ? goldenNuggets.getQuantity() : 0);
    }
}
