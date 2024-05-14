package tictac7x.motherlode;

import net.runelite.api.Item;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemID;
import net.runelite.api.events.ItemContainerChanged;

public class Inventory {
    private int payDirtCurrentlyInInventory = 0;
    private int otherItemsInInventory = 0;

    public void onItemContainerChanged(final ItemContainerChanged event) {
        if (event.getContainerId() != InventoryID.INVENTORY.getId()) return;

        int payDirtCurrentlyInInventory = 0;
        int otherItemsInInventory = 0;

        for (final Item item : event.getItemContainer().getItems()) {
            if (item.getId() < 0) continue;

            otherItemsInInventory += item.getId() == ItemID.PAYDIRT ? 0 : 1;
            payDirtCurrentlyInInventory += item.getId() == ItemID.PAYDIRT ? 1 : 0;
        }

        this.payDirtCurrentlyInInventory = payDirtCurrentlyInInventory;
        this.otherItemsInInventory = otherItemsInInventory;
    }

    public int getAmountOfPayDirtCurrentlyInInventory() {
        return payDirtCurrentlyInInventory;
    }

    public int getMaximumAmountOfPaydirtThatCanBeHold() {
        return 28 - otherItemsInInventory;
    }
}
