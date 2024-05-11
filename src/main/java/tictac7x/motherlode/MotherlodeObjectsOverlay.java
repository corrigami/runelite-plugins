package tictac7x.motherlode;

import net.runelite.api.TileObject;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;

public class MotherlodeObjectsOverlay extends Overlay {
    private final MotherlodeConfig config;
    private final Motherlode motherlode;
    private final Inventory inventory;
    private final MotherlodeSack motherlode_sack;

    private final int
        LADDER_UP = 19045,
        LADDER_DOWN = 19044,
        CRATE = 357,
        CRATE_X = 3752,
        CRATE_Y = 5674,
        HOPPER = 26674,
        BROKEN_STRUT = 26670,
        SACK = 26688;

    private TileObject
        ladder_up,
        ladder_down,
        crate,
        hopper,
        sack;

    private final Set<TileObject> broken_struts = new HashSet<>();

    public MotherlodeObjectsOverlay(final MotherlodeConfig config, final Motherlode motherlode) {
        this.config = config;
        this.motherlode = motherlode;
        this.inventory = motherlode.getInventory();
        this.motherlode_sack = motherlode.getSack();

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    public void onTileObjectSpawned(final TileObject object) {
        final int id = object.getId();

        if (id == LADDER_UP) {
            ladder_up = object;
        } else if (id == LADDER_DOWN) {
            ladder_down = object;
        } else if (id == CRATE && object.getWorldLocation().getX() == CRATE_X && object.getWorldLocation().getY() == CRATE_Y) {
            crate = object;
        } else if (id == HOPPER) {
            hopper = object;
        } else if (id == BROKEN_STRUT) {
            broken_struts.add(object);
        } else if (id == SACK) {
            sack = object;
        }
    }

    public void onTileObjectDespawned(final TileObject object) {
        final int id = object.getId();

        if (id == BROKEN_STRUT) {
            broken_struts.remove(object);
        }
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!motherlode.inRegion()) return null;
        final int pay_dirt_needed = motherlode.getPayDirtNeeded();

        if (ladder_down != null && motherlode.isDownStairs()) {
            if (motherlode.needToMine()) {
                renderClickbox(graphics, ladder_down, config.getOreVeinsColor());
            } else if (motherlode.needToDepositPayDirt()) {
                renderClickbox(graphics, ladder_down, config.getOreVeinsDepletedColor());
            }
        }

        if (ladder_up != null && (motherlode.needToDepositPayDirt() || pay_dirt_needed < 0) && motherlode.isUpstairs()) {
            if (pay_dirt_needed == 0) {
                renderClickbox(graphics, ladder_up, config.getOreVeinsColor());
            } else if (pay_dirt_needed < 0) {
                renderClickbox(graphics, ladder_up, config.getOreVeinsStoppingColor());
            }
        } else if (ladder_up != null && pay_dirt_needed > 0 && motherlode.isUpstairs()) {
            renderClickbox(graphics, ladder_up, config.getOreVeinsDepletedColor());
        }

        if (crate != null && pay_dirt_needed == 0 && inventory.getAmountOfPayDirtCurrentlyInInventory() > 0 && motherlode_sack.isFull() && motherlode.isDownStairs() && broken_struts.size() == 2) {
            renderClickbox(graphics, crate, config.getOreVeinsColor());
        }

        if (hopper != null && inventory.getAmountOfPayDirtCurrentlyInInventory() > 0) {
            if (pay_dirt_needed < 0) {
                renderClickbox(graphics, hopper, config.getOreVeinsStoppingColor());
            } else if (pay_dirt_needed == 0) {
                if (motherlode.isDownStairs()) {
                    renderClickbox(graphics, hopper, config.getOreVeinsColor());
                } else {
                    renderClickbox(graphics, hopper, config.getOreVeinsDepletedColor());
                }
            }
        }

        if (sack != null && motherlode_sack.shouldBeEmptied()) {
            if (motherlode.isDownStairs()) {
                renderClickbox(graphics, sack, config.getOreVeinsColor());
            } else {
                renderClickbox(graphics, sack, config.getOreVeinsDepletedColor());
            }
        }

        if (motherlode_sack.isFull() && !motherlode_sack.isFullActual() && motherlode.isDownStairs()) {
            for (final TileObject broken_strut : broken_struts) {
                if (broken_struts.size() == 1) {
                    renderClickbox(graphics, broken_strut, config.getOreVeinsDepletedColor());
                } else {
                    renderClickbox(graphics, broken_strut, config.getOreVeinsStoppingColor());
                }
            }
        }

        return null;
    }

    private void renderClickbox(final Graphics2D graphics, final TileObject tile_object, final Color color) {
        if (color.getAlpha() == 0) return;

        try {
            // Area border.
            graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.min(255, color.getAlpha() + 20)));
            graphics.setStroke(new BasicStroke(1));
            graphics.draw(tile_object.getClickbox());

            // Area fill.
            graphics.setColor(color);
            graphics.fill(tile_object.getClickbox());
        } catch (final Exception ignored) {}
    }
}
