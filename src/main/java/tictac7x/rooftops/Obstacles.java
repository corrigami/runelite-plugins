package tictac7x.rooftops;

import java.util.Set;
import net.runelite.api.Player;
import net.runelite.api.TileObject;
import com.google.common.collect.ImmutableSet;

public class Obstacles {
    private static final Set<Integer> DRAYNOR_OBSTACLES = ImmutableSet.of(
        11404,
        11405,
        11406,
        11430,
        11630,
        11631,
        11632
    );
    private static final Set<Integer> DRAYNOR_ANIMATIONS = ImmutableSet.of(
        828,
        //
        //
        753, 759,
        2585,
        2586, 2588,
        2586, 2588, 2586, 2588
    );
    private static final Set<Integer> DRAYNOR_POSES = ImmutableSet.of(
        //
        762,
        762,
        757, 756
        //
        //
        //
    );
    private static final Set<Integer> DRAYNOR_IDLES = ImmutableSet.of(
        //
        763,
        763,
        757
        //
        //
        //
    );

    private static final Set<Integer> ALKHARID_OBSTACLES = ImmutableSet.of(
        11633,
        14398,
        14402,
        14403,
        14404,
        11634,
        14409,
        14399
    );
    private static final Set<Integer> ALKHARID_ANIMATIONS = ImmutableSet.of(
        828,
        //
        1995, 751,
        2586, 1601, 1602,
        1122, 1124, 2588,
        828,
        //
        2586
    );
    private static final Set<Integer> ALKHARID_POSES = ImmutableSet.of(
        //
        762,
        //
        //
        //
        //
        762
        //
    );
    private static final Set<Integer> ALKHARID_IDLES = ImmutableSet.of(
        //
        763,
        //
        //
        //
        //
        763
        //
    );

    private static final Set<Integer> VARROCK_OBSTACLES = ImmutableSet.of(
        14412,
        14413,
        14414,
        14832,
        14833,
        14834,
        14835,
        14836,
        14841
    );
    private static final Set<Integer> VARROCK_ANIMATIONS = ImmutableSet.of(
        828, 2585,
        741, 741,
        2586, 2588,
        1995, 1122, 1124, 753,
        2585,
        1995, 4789, 2585,
        2586, 2588,
        1603,
        741, 2586, 2588
    );
    private static final Set<Integer> VARROCK_POSES = ImmutableSet.of(
        //
        //
        //
        757, 756
        //
        //
        //
        //
        //
    );
    private static final Set<Integer> VARROCK_IDLES = ImmutableSet.of(
        //
        //
        //
        757, 757
        //
        //
        //
        //
        //
    );

    private static final Set<Integer> CANIFIS_OBSTACLES = ImmutableSet.of(
        14843,
        14844,
        14845,
        14848,
        14846,
        14894,
        14847,
        14897
    );
    private static final Set<Integer> CANIFIS_ANIMATIONS = ImmutableSet.of(
        1765,
        1995, 2586, 2588,
        2586, 2588,
        2585,
        2586, 2588,
        1955, 7132, 2588,
        2586, 2588,
        2586, 2588
    );
    private static final Set<Integer> CANIFIS_POSES = ImmutableSet.of(
        //
        //
        //
        //
        //
        //
        //
        //
    );
    private static final Set<Integer> CANIFIS_IDLES = ImmutableSet.of(
        //
        //
        //
        //
        //
        //
        //
        //
    );

    private static final Set<Integer> SEERS_OBSTACLES = ImmutableSet.of(14927, 14928, 14929, 14930, 14931, 14932);
    private static final Set<Integer> SEERS_ANIMATIONS = ImmutableSet.of(737, 1118, 2585, 2586, 2588);
    private static final Set<Integer> SEERS_POSES = ImmutableSet.of(762);

    public static boolean isObstacle(final TileObject tile) {
        final int obstacle = tile.getId();
        return (false
            || DRAYNOR_OBSTACLES.contains(obstacle)
            || ALKHARID_OBSTACLES.contains(obstacle)
            || SEERS_OBSTACLES.contains(obstacle)
            || VARROCK_OBSTACLES.contains(obstacle)
        );
    }

    public static boolean isDoingObstacle(final Player player) {
        if (player != null) {
            final int animation = player.getAnimation();
            final int pose = player.getPoseAnimation();
            final int idle = player.getIdlePoseAnimation();

            return (false
                || DRAYNOR_ANIMATIONS.contains(animation)
                || DRAYNOR_POSES.contains(pose)
                || DRAYNOR_IDLES.contains(idle)
                || ALKHARID_ANIMATIONS.contains(animation)
                || ALKHARID_POSES.contains(pose)
                || ALKHARID_IDLES.contains(idle)
                || VARROCK_ANIMATIONS.contains(animation)
                || VARROCK_POSES.contains(pose)
                || VARROCK_IDLES.contains(idle)
                || SEERS_ANIMATIONS.contains(player.getAnimation())
                || SEERS_POSES.contains(player.getPoseAnimation())
            );
        }

        return false;
    }
}
