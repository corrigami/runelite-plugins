package tictac7x.rooftops;

import net.runelite.api.Item;
import net.runelite.api.Tile;
import net.runelite.api.TileItem;
import net.runelite.api.TileObject;
import net.runelite.api.events.ItemDespawned;
import net.runelite.api.events.ItemSpawned;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import tictac7x.Overlay;

import javax.annotation.Nullable;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class RooftopsOverlayMarks extends Overlay {
    private static final int MARK_OF_GRACE = 11849;
    @Nullable
    private Tile mark_of_grace = null;

    public RooftopsOverlayMarks() {
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    public void onItemSpawned(final ItemSpawned item) {
        if (item.getItem().getId() == MARK_OF_GRACE) {
            mark_of_grace = item.getTile();
        }
    }

    public void onItemDespawned(final ItemDespawned item) {
        if (item.getItem().getId() == MARK_OF_GRACE) {
            mark_of_grace = null;
        }
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (mark_of_grace != null) {
            renderItem(graphics, mark_of_grace, color_green, 2, 25);
        }

        return null;
    }

    @Nullable
    public Tile getMarkOfGrace() {
        return mark_of_grace;
    }
}
