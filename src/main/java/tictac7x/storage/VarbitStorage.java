package tictac7x.storage;

import com.google.gson.JsonObject;
import net.runelite.api.Client;
import net.runelite.api.events.VarbitChanged;

public class VarbitStorage {
    private final Client client;
    private final StorageManager storages;
    private final String storage_id;
    private final int size;
    private final int varbit_index;
    private final int[][] varbits_items; // [[varbit, item_id]]

    public VarbitStorage(
        final Client client,
        final StorageManager storages,
        final String storage_id,
        final int size,
        final int varbit_index,
        final int[][] varbits_items
    ) {
        this.client = client;
        this.storages = storages;
        this.storage_id = storage_id;
        this.size = size;
        this.varbit_index = varbit_index;
        this.varbits_items = varbits_items;
    }

    public void onVarbitChanged(final VarbitChanged event) {
//        if (event.getIndex() != varbit_index) return;

        final JsonObject items = new JsonObject();
        int amount = 0;

        for (final int[] varbit_item : varbits_items) {
            final int varbit = varbit_item[0];
            final String item_id = String.valueOf(varbit_item[1]);
            final int item_count = client.getVarbitValue(varbit);

            if (item_count > 0) {
                items.addProperty(item_id, client.getVarbitValue(varbit));
                amount++;
            }
        }

        storages.replace(storage_id, items, size, amount);
    }
}
