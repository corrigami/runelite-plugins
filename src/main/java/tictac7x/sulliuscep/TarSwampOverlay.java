package tictac7x.sulliuscep;

import tictac7x.Overlay;

import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.annotation.Nullable;

import net.runelite.api.Client;
import net.runelite.api.ObjectID;
import net.runelite.api.GameState;
import net.runelite.api.GameObject;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;


public class TarSwampOverlay extends Overlay {
    private final SulliuscepConfig config;
    private final TarSwamp tar_swamp;

    private final Set<GameObject> obstacles = new HashSet<>();
    private final Set<GameObject> sulliusceps = new HashSet<>();
    private Optional<GameObject> pit = Optional.empty();

    public TarSwampOverlay(final SulliuscepConfig config, final TarSwamp tar_swamp) {
        this.config = config;
        this.tar_swamp = tar_swamp;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    public void onGameObjectSpawned(final GameObject game_object) {
        if (!tar_swamp.inRegion()) return;
        final int id = game_object.getId();

        // Sulliuscep.
        if (tar_swamp.isSulliuscep(id)) {
            sulliusceps.add(game_object);

        // Pit.
        } else if (tar_swamp.isPit(id)) {
            pit = Optional.of(game_object);

        // Obstacle.
        } else {
            tar_swamp.getObstacles().stream().filter(obstacle ->
                game_object.getId() == obstacle.id &&
                game_object.getWorldLocation().getX() == obstacle.x &&
                game_object.getWorldLocation().getY() == obstacle.y
            ).findFirst().ifPresent((obstacle -> {
                obstacles.add(game_object);
            }));
        }
    }

    public void onGameObjectDespawned(final GameObject game_object) {
        if (!tar_swamp.inRegion()) return;
        final int id = game_object.getId();

        // Sulliuscep.
        if (tar_swamp.isSulliuscep(id)) {
            sulliusceps.remove(game_object);

        // Pit.
        } else if (tar_swamp.isPit(id)) {
            pit = Optional.empty();

        // Obstacle.
        } else {
            obstacles.remove(game_object);
        }
    }

    public void onGameStateChanged(final GameStateChanged event) {
        if (tar_swamp.inRegion() && event.getGameState() == GameState.LOADING) {
            obstacles.clear();
            sulliusceps.clear();
            pit = Optional.empty();
        }
    }

    private Optional<GameObject> getSulliuscep() {
        final WorldLocationObject world_location_sulliuscep = tar_swamp.getSulliusceps().get(tar_swamp.getSulliuscepActive());

        return sulliusceps.stream().filter(sulliuscep_game_object ->
            sulliuscep_game_object.getWorldLocation().getX() == world_location_sulliuscep.x &&
            sulliuscep_game_object.getWorldLocation().getY() == world_location_sulliuscep.y
        ).findFirst();
    }

    private Set<GameObject> getObstacles() {
        switch (config.highlightObstacles()) {
            case NONE:
                return new HashSet<>();

            default:
            case ALL:
                return obstacles;

            case NEXT:
                final Set<GameObject> obstacles_next = new HashSet<>();

                for (final GameObject obstacle : obstacles) {
                    tar_swamp.getObstacles().stream().filter(world_location_obstacle ->
                         obstacle.getWorldLocation().getX() == world_location_obstacle.x &&
                         obstacle.getWorldLocation().getY() == world_location_obstacle.y &&
                         tar_swamp.getSulliuscepActive()    == world_location_obstacle.sulliuscep
                    ).findFirst().ifPresent(obs -> obstacles_next.add(obstacle));
                }

                return obstacles_next;
        }
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!tar_swamp.inRegion()) return null;

        // Sulliuscep.
        final Optional<GameObject> sulliuscep = getSulliuscep();
        if (config.highlightSulliuscep() && sulliuscep.isPresent()) {
            renderClickbox(graphics, sulliuscep.get(), config.getSulliuscepColor());
        }

        // Mud pit.
        if (config.highlightMudPit() && pit.isPresent() && !tar_swamp.isPitFilled()) {
            renderClickbox(graphics, pit.get(), config.getMudPitColor());
        }

        // Obstacles.
        for (final GameObject obstacle : getObstacles()) {
            renderClickbox(graphics, obstacle, config.getObstaclesColor());
        }

        return null;
    }
}
