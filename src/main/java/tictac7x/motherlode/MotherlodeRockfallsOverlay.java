package tictac7x.motherlode;

import com.google.common.collect.ImmutableSet;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.TileObject;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MotherlodeRockfallsOverlay extends Overlay {
    private final MotherlodeConfig config;
    private final Motherlode motherlode;
    private final Client client;

    private final Set<Integer> ROCKFALLS = ImmutableSet.of(26679, 26680);
    private final Set<TileObject> rockfalls = new HashSet<>();

    public MotherlodeRockfallsOverlay(final MotherlodeConfig config, final Motherlode motherlode, final Client client) {
        this.config = config;
        this.motherlode = motherlode;
        this.client = client;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    public void onTileObjectSpawned(final TileObject object) {
        if (motherlode.inRegion() && ROCKFALLS.contains(object.getId())) {
            rockfalls.add(object);
        }
    }

    public void onTileObjectDespawned(final TileObject object) {
        if (motherlode.inRegion() && ROCKFALLS.contains(object.getId())) {
            rockfalls.remove(object);
        }
    }

    public void onGameStateChanged(final GameStateChanged event) {
        if (event.getGameState() == GameState.LOADING) {
            clear();
        }
    }

    public void clear() {
        rockfalls.clear();
    }

    @Override
    public Dimension render(final Graphics2D graphics) {
        if (!motherlode.inRegion()) return null;

        final Player player = client.getLocalPlayer();
        if (player == null || motherlode.getSack().shouldBeEmptied()) return null;

        // Rockfalls.
        for (final TileObject rockfall : rockfalls) {
            // Find if the rockfall is predefined/upstairs.
            final Optional<Rockfall> rockfall_predefined = motherlode.getRockfalls().getRockfalls().stream().filter(rock ->
                rockfall.getWorldLocation().getX() == rock.x &&
                rockfall.getWorldLocation().getY() == rock.y
            ).findAny();

            if (config.upstairsOnly() && !rockfall_predefined.isPresent()) continue;


            // Upstairs rockfall is rendered based on the sector.
            if (rockfall_predefined.isPresent() && motherlode.getPlayerSectors() != null) {
                for (final Sector sector : rockfall_predefined.get().sectors) {
                    if (motherlode.getPlayerSectors().contains(sector)) {
                        renderTile(graphics, rockfall, config.getRockfallsColor());
                        break;
                    }
                }

            // Downstairs rockfall is rendered based on the draw distance.
            } else if (!rockfall_predefined.isPresent() && motherlode.isDownStairs() && player.getLocalLocation().distanceTo(rockfall.getLocalLocation()) <= config.getDrawDistance()) {
                renderTile(graphics, rockfall, config.getRockfallsColor());
            }
        }

        return null;
    }

    private void renderTile(final Graphics2D graphics, final TileObject tile_object, final Color color) {
        if (color.getAlpha() == 0) return;

        try {
            // Area border.
            graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.min(255, color.getAlpha() + 20)));
            graphics.setStroke(new BasicStroke(1));
            graphics.draw(tile_object.getCanvasTilePoly());

            // Area fill.
            graphics.setColor(color);
            graphics.fill(tile_object.getCanvasTilePoly());
        } catch (final Exception ignored) {}
    }
}
