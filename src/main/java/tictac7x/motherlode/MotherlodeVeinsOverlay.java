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
    private final MotherlodeInventory inventory;
    private final Client client;

    private final Set<TileObject> ore_veins = new HashSet<>();
    private final Set<TileObject> ore_veins_depleted = new HashSet<>();

    public MotherlodeVeinsOverlay(final MotherlodeConfig config, final Motherlode motherlode, final Client client) {
        this.config = config;
        this.motherlode = motherlode;
        this.inventory = motherlode.getInventory();
        this.veins = motherlode.getVeins();
        this.client = client;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    public void onTileObjectSpawned(final TileObject object) {
        if (!motherlode.inRegion()) return;

        if (veins.isOreVein(object)) {
            ore_veins.add(object);
        } else if (veins.isOreVeinDepleted(object)) {
            ore_veins_depleted.add(object);
        }
    }

    public void onTileObjectDespawned(final TileObject object) {
        if (!motherlode.inRegion()) return;

        if (veins.isOreVein(object)) {
            ore_veins.remove(object);
        } else if (veins.isOreVeinDepleted(object)) {
            ore_veins_depleted.remove(object);
        }
    }

    public void onGameStateChanged(final GameStateChanged event) {
        if (event.getGameState() == GameState.LOADING) {
            ore_veins.clear();
            ore_veins_depleted.clear();
        }
    }

    @Override
    public Dimension render(final Graphics2D graphics) {
        if (!motherlode.inRegion()) return null;

        final Player player = client.getLocalPlayer();
        if (player == null) return null;

        final int pay_dirt_needed = motherlode.getPayDirtNeeded();
        if (pay_dirt_needed == 0 && inventory.countPayDirt() != 0) return null;

        // Veins.
        for (final TileObject ore_vein : ore_veins) {
            final OreVein vein = veins.getOreVein(ore_vein);
            if (vein != null && motherlode.getPlayerSector() == vein.sector && player.getLocalLocation().distanceTo(ore_vein.getLocalLocation()) <= motherlode.getDrawDistance()) {
                renderPie(graphics, ore_vein, pay_dirt_needed > 0 ? config.getOreVeinsColor() : config.getOreVeinsStoppingColor(), 1, 150);
            }
        }

        // Depleted veins.
        for (final TileObject ore_vein_depleted : ore_veins_depleted) {
            final OreVein vein = veins.getOreVein(ore_vein_depleted);
            if (vein != null && motherlode.getPlayerSector() == vein.sector && player.getLocalLocation().distanceTo(ore_vein_depleted.getLocalLocation()) <= motherlode.getDrawDistance()) {
                renderPie(graphics, ore_vein_depleted, pay_dirt_needed > 0 ? config.getOreVeinsDepletedColor() : config.getOreVeinsStoppingColor(), veins.getDepletedOreVeinProgress(ore_vein_depleted), 150);
            }
        }

        return null;
    }
}
