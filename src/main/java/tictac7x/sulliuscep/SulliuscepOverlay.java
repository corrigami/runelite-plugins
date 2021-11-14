package tictac7x.sulliuscep;

import tictac7x.Overlay;

import java.util.Set;
import java.util.List;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ArrayList;

import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.annotation.Nullable;

import net.runelite.api.Client;
import net.runelite.api.ObjectID;
import net.runelite.api.GameState;
import net.runelite.api.GameObject;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;


public class SulliuscepOverlay extends Overlay {
    private final Client client;
    private final SulliuscepConfig config;

    private static final int SULLIUSCEP_0 = 31420;
    private static final int SULLIUSCEP_1 = 31421;
    private static final int SULLIUSCEP_2 = 31422;
    private static final int SULLIUSCEP_3 = 31423;
    private static final int SULLIUSCEP_4 = 31424;
    private static final int SULLIUSCEP_5 = 31425;
    private static final int MUD_PIT = 31426;
    private static final int VINES = 30644;
    private static final int THICK_VINE = 30646;
    private static final int THICK_VINES = 30648;
    private static final int VARBIT_SULLIUSCEP = 5808;
    private static final int VARBIT_PIT_FILLED = 5809;

    private final List<WorldLocationObstacle> obstacles_all = Arrays.asList(
        new WorldLocationObstacle(THICK_VINE, 3678, 3743, 1),
        new WorldLocationObstacle(THICK_VINES, 3669, 3746, 2),
        new WorldLocationObstacle(THICK_VINES, 3671, 3760, 2),
        new WorldLocationObstacle(THICK_VINES, 3672, 3764, 2),
        new WorldLocationObstacle(VINES, 3674, 3771, 3),
        new WorldLocationObstacle(THICK_VINES, 3666, 3788, 4),
        new WorldLocationObstacle(THICK_VINES, 3670, 3792, 4),
        new WorldLocationObstacle(THICK_VINE, 3672, 3801, 4)
    );

    private final List<WorldLocationObject> sulliusceps_all = Arrays.asList(
        new WorldLocationObject(SULLIUSCEP_0, 3683, 3758),
        new WorldLocationObject(SULLIUSCEP_1, 3678, 3733),
        new WorldLocationObject(SULLIUSCEP_2, 3683, 3775),
        new WorldLocationObject(SULLIUSCEP_3, 3663, 3781),
        new WorldLocationObject(SULLIUSCEP_4, 3663, 3802),
        new WorldLocationObject(SULLIUSCEP_5, 3678, 3806)
    );

    private final Set<GameObject> obstacles = new HashSet<>();
    private final Set<GameObject> sulliusceps = new HashSet<>();

    @Nullable
    private GameObject pit;

    private boolean pit_filled = false;
    private int sulliuscep_active = 0;

    public SulliuscepOverlay(final Client client, final SulliuscepConfig config) {
        this.client = client;
        this.config = config;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    public void onGameObjectSpawned(final GameObject game_object) {
        final int id = game_object.getId();

        // Sulliuscep.
        if (id == SULLIUSCEP_0 || id == SULLIUSCEP_1 || id == SULLIUSCEP_2 || id == SULLIUSCEP_3 || id == SULLIUSCEP_4 || id == SULLIUSCEP_5) {
            sulliusceps.add(game_object);

        // Mud pit.
        } else if (id == MUD_PIT) {
            pit = game_object;

        // Obstacle.
        } else {
            obstacles_all.stream().filter(obstacle ->
                game_object.getId() == obstacle.id &&
                game_object.getWorldLocation().getX() == obstacle.x &&
                game_object.getWorldLocation().getY() == obstacle.y
            ).findFirst().ifPresent((obstacle -> {
                obstacles.add(game_object);
            }));
        }
    }

    public void onGameObjectDespawned(final GameObject game_object) {
        final int id = game_object.getId();

        // Sulliuscep.
        if (id == SULLIUSCEP_0 || id == SULLIUSCEP_1 || id == SULLIUSCEP_2 || id == SULLIUSCEP_3 || id == SULLIUSCEP_4 || id == SULLIUSCEP_5) {
            sulliusceps.remove(game_object);

        // Mud pit.
        } else if (id == ObjectID.MUD_PIT) {
            pit = null;

        // Obstacle.
        } else {
            obstacles.remove(game_object);
        }
    }

    public void onVarbitChanged(final VarbitChanged event) {
        pit_filled = (client.getVarbitValue(VARBIT_PIT_FILLED) == 1);
        sulliuscep_active = (client.getVarbitValue(VARBIT_SULLIUSCEP));
    }

    public void onGameStateChanged(final GameStateChanged event) {
        if (event.getGameState() == GameState.LOADING) {
            obstacles.clear();
            sulliusceps.clear();
            pit = null;
        }
    }

    @Nullable
    private GameObject getMudPit() {
        return (pit != null && !pit_filled) ? pit : null;
    }

    @Nullable
    private GameObject getSulliuscep() {
        final WorldLocationObject world_location_sulliuscep = sulliusceps_all.get(sulliuscep_active);

        return sulliusceps.stream().filter(sulliuscep_game_object ->
            sulliuscep_game_object.getWorldLocation().getX() == world_location_sulliuscep.x &&
            sulliuscep_game_object.getWorldLocation().getY() == world_location_sulliuscep.y
        ).findFirst().orElse(null);
    }

    private List<GameObject> getObstacles() {
        List<GameObject> obstacles_next = new ArrayList<>();

        for (final GameObject obstacle : obstacles) {
            obstacles_all.stream().filter(obs ->
                obs.x == obstacle.getWorldLocation().getX() &&
                obs.y == obstacle.getWorldLocation().getY() &&
                obs.sulliuscep == sulliuscep_active
            ).findFirst().ifPresent(obs -> obstacles_next.add(obstacle));
        }

        return obstacles_next;
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        // Sulliuscep.
        final GameObject sulliuscep = getSulliuscep();
        if (config.highlightSulliuscep() && sulliuscep != null) {
            renderClickbox(graphics, sulliuscep, config.getSulliuscepColor());
        }

        // Mud pit.
        final GameObject mud_pit = getMudPit();
        if (config.highlightMudPit() && mud_pit != null) {
            renderClickbox(graphics, mud_pit, config.getMudPitColor());
        }

        // Obstacles.
        if (config.highlightObstacles()) {
            for (final GameObject obstacle : getObstacles()) {
                renderClickbox(graphics, obstacle, config.getObstaclesColor());
            }
        }

        return null;
    }
}
