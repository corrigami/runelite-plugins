package tictac7x.rooftops;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.runelite.api.Player;
import net.runelite.api.TileObject;

import java.util.Set;

public class Obstacles {
    public static final Set<Integer> OBSTACLES_ANIMATIONS = ImmutableSet.of(737, 1118, 2585, 2586, 2588);
    public static final Set<Integer> OBSTACLES_POSES = ImmutableSet.of(762);
    private static final Set<Integer> OBSTACLES_SEERS = ImmutableSet.of(14927, 14928, 14929, 14930, 14931, 14932);

    public static boolean isObstacle(final TileObject tile) {
        final int id = tile.getId();
        return OBSTACLES_SEERS.contains(id);
    }

    public static boolean isDoingObstacle(final Player player) {
        return player != null && (
            OBSTACLES_ANIMATIONS.contains(player.getAnimation()) ||
            OBSTACLES_POSES.contains(player.getPoseAnimation())
        );
    }
}
