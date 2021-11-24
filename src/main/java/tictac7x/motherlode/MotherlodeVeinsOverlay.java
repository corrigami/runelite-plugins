package tictac7x.motherlode;

import tictac7x.Overlay;
import java.util.Set;
import java.util.HashSet;
import java.awt.Dimension;
import java.awt.Graphics2D;
import net.runelite.api.Client;
import net.runelite.api.TileObject;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

public class MotherlodeVeinsOverlay extends Overlay {
    private final MotherlodeConfig config;
    private final Motherlode motherlode;
    private final MotherlodeVeins veins;
    private final MotherlodeInventory inventory;
    private final Client client;

    private final Set<TileObject> ore_veins = new HashSet<>();

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
        if (motherlode.inRegion() && (veins.isOreVein(object) || veins.isOreVeinDepleted(object))) {
            ore_veins.add(object);
        }
    }

    public void onTileObjectDespawned(final TileObject object) {
        if (motherlode.inRegion() && (veins.isOreVein(object) || veins.isOreVeinDepleted(object))) {
            ore_veins.remove(object);
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

        final int pay_dirt_needed = motherlode.getPayDirtNeeded();
        final int inventory_pay_dirt = inventory.countPayDirt();

        // Inventory is full of pay-dirt.
        if (pay_dirt_needed == 0 && inventory_pay_dirt != 0) return null;

        // Veins.
        for (final TileObject ore_vein : ore_veins) {
            final OreVein vein = veins.getOreVein(ore_vein);
            if (vein == null) continue;

            // Depleted ore vein.
            if (vein.isDepleted()) {
                if (motherlode.getPlayerSector() == vein.sector && player.getLocalLocation().distanceTo(ore_vein.getLocalLocation()) <= config.getDrawDistance()) {
                    renderPie(graphics, ore_vein, pay_dirt_needed >= 0 ? config.getOreVeinsDepletedColor() : config.getOreVeinsStoppingColor(), veins.getOreVeinProgress(ore_vein), 150);
                }

            // Available ore vein.
            } else {
                if (motherlode.getPlayerSector() == vein.sector && player.getLocalLocation().distanceTo(ore_vein.getLocalLocation()) <= config.getDrawDistance()) {
                    renderPie(graphics, ore_vein, pay_dirt_needed >= 0 ? config.getOreVeinsColor() : config.getOreVeinsStoppingColor(), 1, 150);
                }
            }
        }

        return null;
    }
}
