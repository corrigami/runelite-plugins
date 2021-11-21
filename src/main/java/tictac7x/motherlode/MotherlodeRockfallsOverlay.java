package tictac7x.motherlode;

import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.TileObject;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import tictac7x.Overlay;

import java.awt.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MotherlodeRockfallsOverlay extends Overlay {
    private final MotherlodeConfig config;
    private final Motherlode motherlode;
    private final Client client;

    private final int ROCKFALL = 26679;

    private final Set<TileObject> rockfalls = new HashSet<>();

    public MotherlodeRockfallsOverlay(final MotherlodeConfig config, final Motherlode motherlode, final Client client) {
        this.config = config;
        this.motherlode = motherlode;
        this.client = client;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    public void onTileObjectSpawned(final TileObject object) {
        if (motherlode.inRegion() && object.getId() == ROCKFALL) {
            rockfalls.add(object);
        }
    }

    public void onTileObjectDespawned(final TileObject object) {
        if (motherlode.inRegion() && object.getId() == ROCKFALL) {
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

        for (final TileObject rockfall : rockfalls) {
            final Optional<Rockfall> rockfall_predefined = motherlode.getRockfalls().getRockfalls().stream().filter(rock ->
                rockfall.getWorldLocation().getX() == rock.x &&
                rockfall.getWorldLocation().getY() == rock.y
            ).findAny();

            if (
                rockfall_predefined.isPresent() && rockfall_predefined.get().sectors.contains(motherlode.getPlayerSector()) ||
                motherlode.getPlayerSector() == Sector.DOWNSTAIRS && player.getLocalLocation().distanceTo(rockfall.getLocalLocation()) <= motherlode.getDrawDistance()
            ) {
                renderTile(graphics, rockfall, config.getRockfallsColor());
            }
        }

        return null;
    }
}
