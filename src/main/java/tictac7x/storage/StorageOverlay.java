package tictac7x.storage;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import tictac7x.Overlay;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ImageComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;

import javax.annotation.Nullable;

public class StorageOverlay extends Overlay {
    private static final int PADDING = 10;
    private static final int DENSITY_HORIZONTAL = 6;
    private static final int DENSITY_VERTICAL = 4;

    private final Client client;
    private final ClientThread client_thread;
    private final ConfigManager configs;
    private final ItemManager items;
    protected final StorageConfig config;
    private final StorageManager storages;
    private final String config_id;
    private final String storage_id;

    final PanelComponent panel_items = new PanelComponent();
    private final List<ImageComponent> images = new ArrayList<>();
    private int empty = 0;

    @Nullable
    Widget widget = null;

    @Nullable
    final WidgetInfo widget_info;

    public StorageOverlay(
        final Client client,
        final ClientThread client_thread,
        final ConfigManager configs,
        final ItemManager items,
        final StorageConfig config,
        final StorageManager storages,
        final String config_id,
        final String storage_id,
        final @Nullable WidgetInfo widget_info
    ) {
        this.client = client;
        this.client_thread = client_thread;
        this.configs = configs;
        this.items = items;
        this.config = config;
        this.storages = storages;
        this.config_id = config_id;
        this.widget_info = widget_info;
        this.storage_id = String.valueOf(storage_id);

        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPosition(OverlayPosition.TOP_RIGHT);
        makePanelResizeable(panelComponent, panel_items);

        panel_items.setBorder(new Rectangle(PADDING, 0, PADDING - 4, 0));
        panelComponent.setBorder(new Rectangle(0, PADDING, 0, PADDING));
        panelComponent.setGap(new Point(PADDING, PADDING));
    }

    public void update(final JsonObject storage) {
        this.empty = storage.get("empty").getAsInt();

        // Images need to be created on the client thread.
        client_thread.invoke(() -> {
            // Clear old images.
            images.clear();

            boolean whitelist_enabled = false;
            final String[] whitelista = new String[]{};

            // Order by whitelist.
            if (whitelist_enabled) {
                for (final String whitelist : whitelista) {
                    if (whitelist.length() > 0) {
                        updateStorageImagesBasedOnWhitelist(storage, whitelist);
                    }
                }

            // Order by item container.
            } else {
                updateStorageImagesBasedOnWhitelist(storage, null);
            }
        });
    }

    public int getEmpty() {
        return empty;
    }

    private void updateStorageImagesBasedOnWhitelist(final JsonObject storage, @Nullable final String whitelist) {
        for (final Map.Entry<String, JsonElement> item : storage.get("items").getAsJsonObject().entrySet()) {
            final int id = Integer.parseInt(item.getKey());
            final String name = items.getItemComposition(id).getName();
            final int quantity = item.getValue().getAsInt();

            // Placeholder check.
            if (quantity < 1 || name == null) continue;

//            // Blacklist check.
//            if (blacklist_enabled && blacklist != null) {
//                boolean blacklisted = false;
//
//                for (final String blacklist : blacklist) {
//                    if (blacklist == null || blacklist.length() == 0) continue;
//
//                    // Item blacklisted.
//                    if (name.contains(blacklist)) {
//                        blacklisted = true;
//                        break;
//                    }
//                }
//
//                if (blacklisted) continue;
//            }

            // Whitelist disabled or item whitelisted.
//            if (!whitelist_enabled || whitelist != null && name.contains(whitelist)) {
                images.add(new ImageComponent(items.getImage(id, quantity, true)));
//            }
        }
    }

    private boolean isHidden() {
        return configs.getConfiguration(StorageConfig.group, config_id + "_show").equals("false");
    }

    private boolean hideWithOverlay() {
        return configs.getConfiguration(StorageConfig.group, config_id + "_hide").equals("true");
    }

    @Override
    public Dimension render(final Graphics2D graphics) {
        panelComponent.getChildren().clear();
        panel_items.getChildren().clear();

        // Storage overlay hidden.
        if (isHidden()) return null;

        // Original widget visible.
        updateWidget();
        if (hideWithOverlay() && widget != null && !widget.isHidden()) return null;

        // Storage overlay density.
        final boolean densityCompact = config.getOverlayDensity() == StorageConfig.InventoryDensity.COMPACT;
        panel_items.setGap(new Point(densityCompact ? 0 : DENSITY_HORIZONTAL, densityCompact ? 0 : DENSITY_VERTICAL));

        // Render section before items.
        renderBefore();

        // Render pre-items.
        renderBeforeItems();

        // Render storage visible items.
        for (final ImageComponent item : images) {
            panel_items.getChildren().add(item);
        }

        // Render after-items.
        renderAfterItems();

        // Add items grid if there is something to render.
        if (!panel_items.getChildren().isEmpty()) {
            panelComponent.getChildren().add(panel_items);
        }

        // Render section after items.
        renderAfter();

        // Overlay has items, render it.
        if (!panel_items.getChildren().isEmpty() || !panelComponent.getChildren().isEmpty()) {
            return super.render(graphics);
        }

        // Overlay empty, don't render it.
        return null;
    }

    private void updateWidget() {
        if (this.widget == null) {
            this.widget = client.getWidget(widget_info);
        }
    }

    void renderBefore() {}

    void renderBeforeItems() {}

    void renderAfter() {}

    void renderAfterItems() {}
}
