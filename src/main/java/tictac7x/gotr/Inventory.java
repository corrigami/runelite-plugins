package tictac7x.gotr;

import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemID;

public class Inventory {
    private boolean has_uncharged_cell;
    private boolean has_charged_cell;
    private boolean has_elemental_stones;
    private boolean has_catalytical_stones;

    public boolean hasUnchargedCell() {
        return has_uncharged_cell;
    }

    public boolean hasChargedCell() {
        return has_charged_cell;
    }

    public boolean hasElementalStones() {
        return has_elemental_stones;
    }

    public boolean hasCatalyticalStones() {
        return has_catalytical_stones;
    }

    public void onItemContainerChanged(final ItemContainer inventory) {
        if (inventory.getId() != InventoryID.INVENTORY.getId()) return;

        has_charged_cell = inventory.contains(ItemID.WEAK_CELL) || inventory.contains(ItemID.MEDIUM_CELL) || inventory.contains(ItemID.STRONG_CELL) || inventory.contains(ItemID.OVERCHARGED_CELL);

        has_uncharged_cell = inventory.contains(ItemID.UNCHARGED_CELL);

        has_elemental_stones = inventory.contains(ItemID.ELEMENTAL_GUARDIAN_STONE) || inventory.contains(ItemID.POLYELEMENTAL_GUARDIAN_STONE);

        has_catalytical_stones = inventory.contains(ItemID.CATALYTIC_GUARDIAN_STONE);
    }
}
