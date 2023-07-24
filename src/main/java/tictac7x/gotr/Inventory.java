package tictac7x.gotr;

import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;

import java.util.Arrays;
import java.util.Optional;

public class Inventory {
    private Optional<ItemContainer> inventory = Optional.empty();

    public void onItemContainerChanged(final ItemContainer item_container) {
        if (item_container.getId() == InventoryID.INVENTORY.getId()) {
            inventory = Optional.of(item_container);
        }
    }

    public boolean hasTeleporterTalisman(final Teleporter teleporter) {
        return inventory.isPresent() && Arrays.stream(inventory.get().getItems()).anyMatch(item -> item.getId() == teleporter.talisman_id);
    }
}
