package tictac7x.ground;

import net.runelite.api.Client;
import net.runelite.api.Item;
import net.runelite.api.Tile;
import net.runelite.api.TileItem;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ItemDespawned;
import net.runelite.api.events.ItemSpawned;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import tictac7x.Overlay;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GroundOverlay extends Overlay {
    private Client client;
    private List<Tile> items = new ArrayList<>();

    public GroundOverlay(final Client client) {
        this.client = client;
        setPosition(OverlayPosition.DYNAMIC);
    }

    public void onItemSpawned(final ItemSpawned event) {
        items.add(event.getTile());
    }

    public void onItemDespawned(final ItemDespawned event) {
        items.remove(event.getTile());
    }

    @Override
    public Dimension render(final Graphics2D graphics) {
        for (final Tile item : items) {
            renderTile(client, graphics, item, color_gray);
            renderPie(client, graphics, item.getWorldLocation(), color_orange, 0.5f, item.getItemLayer().getHeight());
        }

        return null;
    }
}
