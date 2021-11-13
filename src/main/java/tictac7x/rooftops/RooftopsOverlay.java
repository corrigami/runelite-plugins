package tictac7x.rooftops;

import tictac7x.Overlay;
import tictac7x.rooftops.courses.Courses;

import java.util.List;
import java.util.ArrayList;

import java.awt.Shape;
import java.awt.Dimension;
import java.awt.Graphics2D;

import net.runelite.api.Tile;
import net.runelite.api.Model;
import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.Player;
import net.runelite.api.GameState;
import net.runelite.api.GameObject;
import net.runelite.api.TileObject;
import net.runelite.api.GroundObject;
import net.runelite.api.DecorativeObject;
import net.runelite.api.events.ItemSpawned;
import net.runelite.api.events.ItemDespawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

public class RooftopsOverlay extends Overlay {
    private final Courses course_manager;
    private final RooftopsConfig config;
    private final Client client;

    private final List<TileObject> obstacles = new ArrayList<>();
    private final List<Tile> mark_of_graces = new ArrayList<>();

    public RooftopsOverlay(final Courses course_manager, final RooftopsConfig config, final Client client) {
        this.course_manager = course_manager;
        this.config = config;
        this.client = client;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    public void onGameStateChanged(final GameStateChanged event) {
        if (event.getGameState() == GameState.LOADING) {
            obstacles.clear();
            mark_of_graces.clear();
        }
    }

    public void onTileObjectSpawned(final TileObject object) {
        if (course_manager.getCourseBasedOnObstacle(object) != null) {
            obstacles.add(object);
        }
    }

    public void onItemSpawned(final ItemSpawned event) {
        if (event.getItem().getId() == ItemID.MARK_OF_GRACE) {
            mark_of_graces.add(event.getTile());
        }
    }

    public void onItemDespawned(final ItemDespawned event) {
        if (event.getItem().getId() == ItemID.MARK_OF_GRACE) {
            mark_of_graces.remove(event.getTile());
        }
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        final Player player = client.getLocalPlayer();
        if (player == null) return null;

        // Main cycle to observe for obstacle changes.
        course_manager.cycle(player);

        // Mark of graces.
        if (config.showMarkOfGrace()) {
            for (final Tile mark_of_grace : mark_of_graces) {
                renderItem(graphics, mark_of_grace, config.getMarkOfGraceColor(), clickbox_stroke_width, 25);
            }
        }

        // Obstacles.
        for (final TileObject obstacle : obstacles) {
            // Same plane obstacles check.
            if (!config.highlightAllObstacles() && obstacle.getPlane() != client.getPlane()) continue;

            final Shape clickbox = RooftopsPerspective.getClickbox(client,
                obstacle instanceof GameObject
                    ? (Model) ((GameObject) obstacle).getRenderable() :
                obstacle instanceof GroundObject
                    ? (Model) ((GroundObject) obstacle).getRenderable() :
                obstacle instanceof DecorativeObject
                    ? (Model) ((DecorativeObject) obstacle).getRenderable() :
                null, 0, obstacle.getLocalLocation(), obstacle.getPlane() - client.getPlane()
            );

            if (clickbox != null) {
                renderShape(graphics, clickbox, course_manager.getObstacleColor(obstacle));
            }
        }

        return null;
    }
}
