package tictac7x.gotr.store;

import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemID;
import tictac7x.gotr.types.Teleporter;

import java.util.Optional;

public class Inventory {
    private Optional<ItemContainer> inventory = Optional.empty();

    public void onItemContainerChanged(final ItemContainer itemContainer) {
        if (itemContainer.getId() == InventoryID.INVENTORY.getId()) {
            inventory = Optional.of(itemContainer);
        }
    }

    public boolean hasTeleporterTalisman(final Teleporter teleporter) {
        return inventory.isPresent() && inventory.get().contains(teleporter.talismanItemId);
    }

    public boolean hasEssence() {
        return inventory.isPresent() && (
            inventory.get().contains(ItemID.ELEMENTAL_GUARDIAN_STONE) ||
            inventory.get().contains(ItemID.CATALYTIC_GUARDIAN_STONE) ||
            inventory.get().contains(ItemID.POLYELEMENTAL_GUARDIAN_STONE)
        );
    }

    public boolean hasElementalEssence() {
        return inventory.isPresent() && inventory.get().contains(ItemID.ELEMENTAL_GUARDIAN_STONE);
    }

    public boolean hasCell() {
        return inventory.isPresent() && (inventory.get().contains(ItemID.WEAK_CELL) || inventory.get().contains(ItemID.MEDIUM_CELL) || inventory.get().contains(ItemID.STRONG_CELL) || inventory.get().contains(ItemID.OVERCHARGED_CELL));
    }
}
