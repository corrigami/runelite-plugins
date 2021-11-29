package tictac7x.storage;

import com.google.gson.JsonObject;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.widgets.WidgetInfo;

import javax.annotation.Nullable;

public class StorageManager {
    private final Client client;
    private final Storage inventory;
    private final Storage bank;

    @Nullable
    private Item[] inventory_items;

    public StorageManager(final Client client, final Storage inventory, final Storage bank) {
        this.client = client;
        this.inventory = inventory;
        this.bank = bank;
    }

    public void onItemContainerChanged(final ItemContainerChanged event) {
        // Inventory changed, Deposit Box is opened.
        if (event.getContainerId() == InventoryID.INVENTORY.getId() && client.getWidget(WidgetInfo.DEPOSIT_BOX_INVENTORY_ITEMS_CONTAINER) != null) {
            final JsonObject deposit = new JsonObject();
            final Item[] inventory_items = event.getItemContainer().getItems();

            if (this.inventory_items != null) {
                for (int i = 0; i < event.getItemContainer().size(); i++) {
                    final Item item_before = this.inventory_items[i];
                    final Item item_after = inventory_items[i];
                    final String id = String.valueOf(item_before.getId());

                    int quantity_after = 0;

                    // Item didn't change.
                    if (item_before.getId() == item_after.getId() && item_before.getQuantity() == item_after.getQuantity()) continue;

                    // Amount from stack deposited.
                    if (item_before.getId() != -1 && item_after.getId() != -1 && item_before.getId() == item_after.getId() && item_before.getQuantity() != item_after.getQuantity()) {
                        quantity_after = item_after.getQuantity();
                    }

                    // Multiple non-stack items with same IDs deposited.
                    if (deposit.has(id)) {
                        deposit.addProperty(id, deposit.get(id).getAsInt() + (item_before.getQuantity() - quantity_after));

                    // First time depositing item.
                    } else {
                        deposit.addProperty(id, item_before.getQuantity() - quantity_after);
                    }
                }

                bank.deposit(deposit);
            }
        }

        // Update inventory items after the deposit logic.
        if (event.getContainerId() == InventoryID.INVENTORY.getId()) {
            inventory_items = event.getItemContainer().getItems();
        }
    }
}
