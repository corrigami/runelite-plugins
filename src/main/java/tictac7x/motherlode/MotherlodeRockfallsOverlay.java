package tictac7x.motherlode;

import tictac7x.Overlay;
import java.util.Set;
import java.util.HashSet;
import java.util.Optional;
import java.awt.Dimension;
import java.awt.Graphics2D;
import com.google.common.collect.ImmutableSet;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.TileObject;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

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
            rockfalls.clear();;
        }
    }

    @Override
    public Dimension render(final Graphics2D graphics) {
        if (!motherlode.inRegion()) return null;

        final Player player = client.getLocalPlayer();
        if (player == null) return null;

        // Rockfalls.
        for (final TileObject rockfall : rockfalls) {
            // Find if the rockfall is predefined/upstairs.
            final Optional<Rockfall> rockfall_predefined = motherlode.getRockfalls().getRockfalls().stream().filter(rock ->
                rockfall.getWorldLocation().getX() == rock.x &&
                rockfall.getWorldLocation().getY() == rock.y
            ).findAny();


            if (
                // Upstairs rockfall is rendered based on the sector.
                rockfall_predefined.isPresent() && rockfall_predefined.get().sectors.contains(motherlode.getPlayerSector()) ||

                // Downstairs rockfall is rendered based on the draw distance.
                !rockfall_predefined.isPresent() && motherlode.getPlayerSector() == Sector.DOWNSTAIRS && player.getLocalLocation().distanceTo(rockfall.getLocalLocation()) <= config.getDrawDistance()
            ) {
                renderTile(graphics, rockfall, config.getRockfallsColor());
            }
        }

        return null;
    }
}
