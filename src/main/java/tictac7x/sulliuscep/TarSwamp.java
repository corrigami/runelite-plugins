package tictac7x.sulliuscep;

import com.google.common.collect.ImmutableSet;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.VarbitChanged;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class TarSwamp {
    public static final int SULLIUSCEP_0 = 31420;
    public static final int SULLIUSCEP_1 = 31421;
    public static final int SULLIUSCEP_2 = 31422;
    public static final int SULLIUSCEP_3 = 31423;
    public static final int SULLIUSCEP_4 = 31424;
    public static final int SULLIUSCEP_5 = 31425;
    public static final int PIT = 31426;
    public static final int VINES = 30644;
    public static final int THICK_VINE = 30646;
    public static final int THICK_VINES = 30648;
    public static final int VARBIT_SULLIUSCEP = 5808;
    public static final int VARBIT_PIT_FILLED = 5809;

    private final Set<Integer> regions = ImmutableSet.of(14649, 14650, 14651, 14652);

    private final List<WorldLocationObstacle> obstacles = Arrays.asList(
        new WorldLocationObstacle(THICK_VINE, 3678, 3743, 1),
        new WorldLocationObstacle(THICK_VINES, 3669, 3746, 2),
        new WorldLocationObstacle(THICK_VINES, 3671, 3760, 2),
        new WorldLocationObstacle(THICK_VINES, 3672, 3764, 2),
        new WorldLocationObstacle(VINES, 3674, 3771, 3),
        new WorldLocationObstacle(THICK_VINES, 3666, 3788, 4),
        new WorldLocationObstacle(THICK_VINES, 3670, 3792, 4),
        new WorldLocationObstacle(THICK_VINE, 3672, 3801, 4)
    );

    private final List<WorldLocationObject> sulliusceps = Arrays.asList(
        new WorldLocationObject(SULLIUSCEP_0, 3683, 3758),
        new WorldLocationObject(SULLIUSCEP_1, 3678, 3733),
        new WorldLocationObject(SULLIUSCEP_2, 3683, 3775),
        new WorldLocationObject(SULLIUSCEP_3, 3663, 3781),
        new WorldLocationObject(SULLIUSCEP_4, 3663, 3802),
        new WorldLocationObject(SULLIUSCEP_5, 3678, 3806)
    );

    private final Client client;

    private boolean in_region = false;
    private boolean pit_filled = false;
    private int sulliuscep_active = 0;

    public TarSwamp(final Client client) {
        this.client = client;
    }

    public void onVarbitChanged() {
        pit_filled = (client.getVarbitValue(VARBIT_PIT_FILLED) == 1);
        sulliuscep_active = (client.getVarbitValue(VARBIT_SULLIUSCEP));
    }

    public void onGameStateChanged(final GameStateChanged event) {
        if (client.getMapRegions() == null) return;

        for (final int region : client.getMapRegions()) {
            if (regions.contains(region)) {
                in_region = true;
                return;
            }
        }

        in_region = false;
    }

    public List<WorldLocationObstacle> getObstacles() {
        return obstacles;
    }

    public List<WorldLocationObject> getSulliusceps() {
        return sulliusceps;
    }

    public int getSulliuscepActive() {
        return sulliuscep_active;
    }

    public boolean isSulliuscep(final int id) {
        return (
            id == SULLIUSCEP_0 ||
            id == SULLIUSCEP_1 ||
            id == SULLIUSCEP_2 ||
            id == SULLIUSCEP_3 ||
            id == SULLIUSCEP_4 ||
            id == SULLIUSCEP_5
        );
    }

    public boolean isPit(final int id) {
        return id == PIT;
    }

    public boolean isPitFilled() {
        return pit_filled;
    }

    public boolean inRegion() {
        return in_region;
    }
}
