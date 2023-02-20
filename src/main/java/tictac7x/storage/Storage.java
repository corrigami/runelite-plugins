package tictac7x.storage;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ComponentOrientation;
import net.runelite.client.ui.overlay.components.ImageComponent;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Storage extends OverlayPanel {
    private final String storage_id;
    private final int item_container_id;
    private final ClientThread client_thread;
    private final ConfigManager configs;
    private final ItemManager items;

    private final int PLACEHOLDER = 14401;
    private final List<ImageComponent> images = new ArrayList<>();
    private final JsonParser parser = new JsonParser();

    public Storage(final String storage_id, final InventoryID item_container_id, final ClientThread client_thread, final ConfigManager configs, final ItemManager items) {
        this.storage_id = storage_id;
        this.item_container_id = item_container_id.getId();
        this.client_thread = client_thread;
        this.configs = configs;
        this.items = items;

        // Overlay configuration.
        setPreferredPosition(OverlayPosition.BOTTOM_RIGHT);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        panelComponent.setWrap(true);
        panelComponent.setOrientation(ComponentOrientation.HORIZONTAL);

        // Generate images based on config.
        this.client_thread.invokeLater(() -> this.updateImages(configs.getConfiguration(StorageConfig.group, storage_id)));

    }

    public void onItemContainerChanged(final ItemContainerChanged event) {
        if (event.getContainerId() != this.item_container_id) return;

        final ItemContainer item_container = event.getItemContainer();
        final JsonObject json = new JsonObject();

        for (final Item item : item_container.getItems()) {
            // Empty item.
            if (item.getId() == -1 || item.getQuantity() == 0) continue;

            // Placeholder.
            if (items.getItemComposition(item.getId()).getPlaceholderTemplateId() == PLACEHOLDER) continue;

            // Save item.
            final String id = String.valueOf(item.getId());
            if (!json.has(id)) {
                json.addProperty(id, item_container.count(item.getId()));
            }
        }

        configs.setConfiguration(StorageConfig.group, this.storage_id, json.toString());
    }

    public void onConfigChanged(final ConfigChanged event) {
        if (
            !event.getGroup().equals(StorageConfig.group) |
            !event.getKey().equals(this.storage_id) && !event.getKey().equals(this.storage_id + "_whitelist")
        ) return;

        this.client_thread.invokeLater(() -> this.updateImages(configs.getConfiguration(StorageConfig.group, this.storage_id)));
    }

    private void updateImages(final String items) {
        // List of images to render.
        List<ImageComponent> images = new ArrayList<>();

        final JsonObject json = parser.parse(items).getAsJsonObject();
        for (final Map.Entry<String, JsonElement> entry : json.entrySet()) {
            final int item_id = Integer.parseInt(entry.getKey());
            final int item_quantity = entry.getValue().getAsInt();


            // Item not shown.
            if (!isWhitelisted(item_id)) continue;

            images.add(new ImageComponent(this.items.getImage(item_id, item_quantity, true)));
        }

        // Replace old images with new ones.
        this.images.clear();
        this.images.addAll(images);
    }

    private String[] getWhitelist() {
        String[] whitelist = new String[]{};
        try { whitelist = configs.getConfiguration(StorageConfig.group, this.storage_id + "_whitelist").split(",");
        } catch (final Exception ignored) {}

        return whitelist;
    }

    private boolean isWhitelisted(final int item_id) {
        final String[] whitelist = this.getWhitelist();
        final ItemComposition item = this.items.getItemComposition(item_id);

        // Whitelist not used.
        if (whitelist.length == 0 || whitelist.length == 1 && whitelist[0].equals("")) return true;

        // Check if whitelisted.
        for (final String name : whitelist) {
            if (item.getName().contains(name)) {
                return true;
            }
        }

        // Not whitelisted.
        return false;
    }

    @Override
    public Dimension render(final Graphics2D graphics) {
        if (this.images.size() == 0) return null;

        panelComponent.getChildren().clear();
        this.images.forEach(image -> panelComponent.getChildren().add(image));
        return super.render(graphics);
    }
}
