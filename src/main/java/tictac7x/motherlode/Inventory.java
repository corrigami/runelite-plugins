package tictac7x.motherlode;

import net.runelite.api.Item;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemID;
import net.runelite.api.events.ItemContainerChanged;

public class Inventory {
    private int paydirt = 0;
    private int otherItems = 0;

    public void onItemContainerChanged(final ItemContainerChanged event) {
        if (event.getContainerId() != InventoryID.INVENTORY.getId()) return;

        int paydirt = 0;
        int otherItems = 0;

        for (final Item item : event.getItemContainer().getItems()) {
            if (item.getId() >= 0) {
                otherItems += item.getId() == ItemID.PAYDIRT ? 0 : 1;
                paydirt += item.getId() == ItemID.PAYDIRT ? 1 : 0;
            }
        }

        this.paydirt = paydirt;
        this.otherItems = otherItems;
    }

    public int getPaydirt() {
        return paydirt;
    }

    public int getMaximumAvailablePayDirt() {
        return 28 - otherItems;
    }
}
