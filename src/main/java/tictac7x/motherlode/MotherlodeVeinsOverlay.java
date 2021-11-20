package tictac7x.motherlode;

import net.runelite.api.*;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import tictac7x.Overlay;

import java.awt.Graphics2D;
import java.awt.Dimension;
import java.util.HashSet;
import java.util.Set;

public class MotherlodeVeinsOverlay extends Overlay {
    private final MotherlodeConfig config;
    private final Motherlode motherlode;
    private final MotherlodeVeins veins;
    private final MotherlodeSack sack;
    private final MotherlodeInventory inventory;
    private final Client client;

    private final int VEIN_DRAW_DISTANCE = 4000;

    private final Set<TileObject> ore_veins = new HashSet<>();
    private final Set<TileObject> ore_veins_depleted = new HashSet<>();
    private int player_x = 0;
    private int player_y = 0;
    private Sector player_sector = null;

    public MotherlodeVeinsOverlay(final MotherlodeConfig config, final Motherlode motherlode, final MotherlodeVeins veins, final MotherlodeSack sack, final MotherlodeInventory inventory, final Client client) {
        this.config = config;
        this.motherlode = motherlode;
        this.veins = veins;
        this.sack = sack;
        this.inventory = inventory;
        this.client = client;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    public void onTileObjectSpawned(final TileObject object) {
        if (veins.isOreVein(object)) {
            ore_veins.add(object);
        } else if (veins.isOreVeinDepleted(object)) {
            ore_veins_depleted.add(object);
        }
    }

    public void onTileObjectDespawned(final TileObject object) {
        if (veins.isOreVein(object)) {
            ore_veins.remove(object);
        } else if (veins.isOreVeinDepleted(object)) {
            ore_veins_depleted.remove(object);
        }
    }

    public void onGameStateChanged(final GameStateChanged event) {
        if (event.getGameState() == GameState.LOADING) {
            ore_veins.clear();
        }
    }

    @Override
    public Dimension render(final Graphics2D graphics) {
        if (!motherlode.inRegion()) return null;

        final Player player = client.getLocalPlayer();
        if (player == null) return null;

        final int player_x = player.getWorldLocation().getX();
        final int player_y = player.getWorldLocation().getY();
        final int pay_dirt_needed = motherlode.getPayDirtNeeded(sack, inventory);

        if (this.player_x != player_x || this.player_y != player_y) {
            player_sector = motherlode.getVeinSector(player_x, player_y);
        }

        // Veins.
        for (final TileObject ore_vein : ore_veins) {
            final OreVein vein = veins.getOreVein(ore_vein);
            if (vein.sector == player_sector && player.getLocalLocation().distanceTo(ore_vein.getLocalLocation()) <= VEIN_DRAW_DISTANCE) {
                renderPie(graphics, ore_vein, pay_dirt_needed > 0 ? config.getOreVeinsColor() : pay_dirt_needed == 0 ? null : config.getOreVeinsStoppingColor(), 1, pie_fill_alpha, 150);
            }
        }

        // Depleted veins.
        for (final TileObject ore_vein_depleted : ore_veins_depleted) {
            final OreVein vein = veins.getOreVein(ore_vein_depleted);
            if (vein.sector == player_sector && player.getLocalLocation().distanceTo(ore_vein_depleted.getLocalLocation()) <= VEIN_DRAW_DISTANCE) {
                renderPie(graphics, ore_vein_depleted, pay_dirt_needed > 0 ? config.getOreVeinsDepletedColor() : pay_dirt_needed == 0 ? null : config.getOreVeinsStoppingColor(), veins.getDepletedOreVeinProgress(ore_vein_depleted), pie_fill_alpha, 150);
            }
        }

        return null;
    }
}
