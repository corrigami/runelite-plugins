package tictac7x.rooftops;

import tictac7x.Overlay;
import java.awt.Dimension;
import java.awt.Graphics2D;
import net.runelite.api.Tile;
import javax.annotation.Nullable;
import net.runelite.api.events.ItemDespawned;
import net.runelite.api.events.ItemSpawned;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

public class RooftopsOverlayMarks extends Overlay {
    private static final int MARK_OF_GRACE = 11849;

    private final RooftopsConfig config;

    @Nullable
    private Tile mark_of_grace = null;

    public RooftopsOverlayMarks(final RooftopsConfig config) {
        this.config = config;
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
            renderItem(graphics, mark_of_grace, config.getMarkOfGrace(), 2, 25);
        }

        return null;
    }

    @Nullable
    public Tile getMarkOfGrace() {
        return mark_of_grace;
    }
}
