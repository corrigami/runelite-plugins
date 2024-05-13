package tictac7x.motherlode;

import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.*;

import static tictac7x.motherlode.TicTac7xMotherlodePlugin.getWorldObjectKey;

public class Rockfalls extends Overlay {
    private final TicTac7xMotherlodeConfig config;
    private final Player player;

    private Map<String, Rockfall> rockfalls = new HashMap<>();
    private Set<GameObject> rockfallsGameObjects = new HashSet<>();

    public Rockfalls(final TicTac7xMotherlodeConfig config, final Player player) {
        this.config = config;
        this.player = player;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }


    public void onGameObjectSpawned(final GameObjectSpawned event) {
        if (!Rockfall.isRockfall(event.getGameObject())) return;

        updateRockfall(event.getGameObject());
        rockfallsGameObjects.add(event.getGameObject());
    }

    public void onGameObjectDespawned(final GameObjectDespawned event) {
        if (!Rockfall.isRockfall(event.getGameObject())) return;

        rockfallsGameObjects.remove(event.getGameObject());
    }

    public void onGameStateChanged(final GameStateChanged event) {
        if (event.getGameState() == GameState.LOADING) {
            rockfallsGameObjects.clear();
        }
    }

    private void updateRockfall(final GameObject gameObject) {
        final String key = getWorldObjectKey(gameObject);

        if (!rockfalls.containsKey(key)) {
            rockfalls.put(key, new Rockfall(gameObject.getWorldLocation().getX(), gameObject.getWorldLocation().getY()));
        }
    }

    @Nullable
    private Rockfall getRockfallFromGameObject(final GameObject gameObject) {
        for (final Rockfall rockfall : rockfalls.values()) {
            if (rockfall.x == gameObject.getWorldLocation().getX() && rockfall.y == gameObject.getWorldLocation().getY()) {
                return rockfall;
            }
        }

        return null;
    }

    @Override
    public Dimension render(final Graphics2D graphics2D) {
        if (config.getRockfallsColor().getAlpha() == 0) return null;

        for (final GameObject rockfallGameObject : rockfallsGameObjects) {
            final Rockfall rockfall = getRockfallFromGameObject(rockfallGameObject);
            if (rockfall == null || !rockfall.isRendering(config, player)) continue;

            renderTile(graphics2D, rockfallGameObject, rockfall.getTileColor(config));
        }

        return null;
    }

    private void renderTile(final Graphics2D graphics, final GameObject gameObject, final Color color) {
        try {
            // Area border.
            graphics.setColor(color);
            graphics.setStroke(new BasicStroke(1));
            graphics.draw(gameObject.getCanvasTilePoly());

            // Area fill.
            graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(color.getAlpha() - 20, 0)));
            graphics.fill(gameObject.getCanvasTilePoly());
        } catch (final Exception ignored) {}
    }
}
