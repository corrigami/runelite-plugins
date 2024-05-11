package tictac7x.motherlode;

import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.TileObject;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ProgressPieComponent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;

public class MotherlodeVeinsOverlay extends Overlay {
    private final MotherlodeConfig config;
    private final Motherlode motherlode;
    private final MotherlodeVeins veins;
    private final Inventory inventory;
    private final MotherlodeSack sack;
    private final Client client;

    private final Set<TileObject> ore_veins = new HashSet<>();

    public MotherlodeVeinsOverlay(final MotherlodeConfig config, final Motherlode motherlode, final Client client) {
        this.config = config;
        this.motherlode = motherlode;
        this.inventory = motherlode.getInventory();
        this.sack = motherlode.getSack();
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
            clear();
        }
    }

    public void clear() {
        ore_veins.clear();
    }

    @Override
    public Dimension render(final Graphics2D graphics) {
        if (!motherlode.inRegion()) return null;

        final Player player = client.getLocalPlayer();
        if (player == null) return null;

        final int pay_dirt_needed = motherlode.getPayDirtNeeded();
        final int inventory_pay_dirt = inventory.getAmountOfPayDirtCurrentlyInInventory();

        // Inventory is full of pay-dirt.
        if (pay_dirt_needed == 0 && inventory.getAmountOfItemsInInventory() == inventory.getSize()) return null;

        // Sack should be emptied first.
        if (sack.shouldBeEmptied()) return null;

        // Veins.
        for (final TileObject ore_vein : ore_veins) {
            final OreVein vein = veins.getOreVein(ore_vein);
            if (vein == null || config.upstairsOnly() && vein.sector == Sector.DOWNSTAIRS) continue;

            // Depleted ore vein.
            if (vein.isDepleted()) {
                if (motherlode.getPlayerSectors().contains(vein.sector) && player.getLocalLocation().distanceTo(ore_vein.getLocalLocation()) <= config.getDrawDistance()) {
                    // Can't be mined.
                    if (pay_dirt_needed == 0 && inventory.getAmountOfItemsInInventory() != inventory.getSize() || pay_dirt_needed < 0) {
                        renderPie(graphics, ore_vein, config.getOreVeinsStoppingColor(), veins.getOreVeinProgress(ore_vein));

                    // Can be mined.
                    } else if (motherlode.needToMine()) {
                        renderPie(graphics, ore_vein, config.getOreVeinsDepletedColor(), veins.getOreVeinProgress(ore_vein));
                    }
                }

            // Available ore vein.
            } else {
                if (motherlode.getPlayerSectors().contains(vein.sector) && player.getLocalLocation().distanceTo(ore_vein.getLocalLocation()) <= config.getDrawDistance()) {
                    // Can't be mined.
                    if (pay_dirt_needed == 0 && inventory.getAmountOfItemsInInventory() != inventory.getSize() || pay_dirt_needed < 0) {
                        renderPie(graphics, ore_vein, config.getOreVeinsStoppingColor(), 1);

                    // Can be mined.
                    } else if (motherlode.needToMine()) {
                        renderPie(graphics, ore_vein, config.getOreVeinsColor(), 1);
                    }
                }
            }
        }

        return null;
    }

    private void renderPie(final Graphics2D graphics, final TileObject object, final Color color, final float progress) {
        if (color.getAlpha() == 0) return;

        try {
            final ProgressPieComponent progressPieComponent = new ProgressPieComponent();
            progressPieComponent.setPosition(object.getCanvasLocation(150));
            progressPieComponent.setProgress(-progress);
            progressPieComponent.setBorderColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.min(255, color.getAlpha() + 20)));
            progressPieComponent.setFill(color);
            progressPieComponent.render(graphics);
        } catch (final Exception ignored) {}
    }
}
