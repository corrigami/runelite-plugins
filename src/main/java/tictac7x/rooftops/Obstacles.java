package tictac7x.rooftops;

import java.util.*;

import net.runelite.api.Player;
import net.runelite.api.Tile;
import net.runelite.api.TileObject;
import com.google.common.collect.ImmutableSet;

public class Obstacles {
    public enum Region {
        DRAYNOR,
        ALKHARID,
        VARROCK,
        CANIFIS,
        FALADOR,
        SEERS,
        POLLNIVNEACH
    }

    public static Region getRegion(final int[] regions) {
        for (final int region : regions) {
            if (DRAYNOR_REGIONS.contains(region)) {
                return Region.DRAYNOR;
            } else if (ALKHARID_REGIONS.contains(region)) {
                return Region.ALKHARID;
            } else if (VARROCK_REGIONS.contains(region)) {
                return Region.VARROCK;
            } else if (CANIFIS_REGIONS.contains(region)) {
                return Region.CANIFIS;
            } else if (FALADOR_REGIONS.contains(region)) {
                return Region.FALADOR;
            } else if (SEERS_REGIONS.contains(region)) {
                return Region.SEERS;
            } else if (POLLNIVNEACH_REGIONS.contains(region)) {
                return Region.POLLNIVNEACH;
            }
        }

        return null;
    }

    private static final int POSE_IDLE = 813;
    private static final int ANIMATION_IDLE = -1;

    public static List<Integer> getObstacles(final Region region) {
        switch (region) {
            case DRAYNOR:
                return DRAYNOR_OBSTACLES;
            case ALKHARID:
                return ALKHARID_OBSTACLES;
            case VARROCK:
                return VARROCK_OBSTACLES;
            case CANIFIS:
                return CANIFIS_OBSTACLES;
            case FALADOR:
                return FALADOR_OBSTACLES;
            case SEERS:
                return SEERS_OBSTACLES;
            case POLLNIVNEACH:
                return POLLNIVNEACH_OBSTACLES;
            default:
                return null;
        }
    }

    private static final Set<Integer> DRAYNOR_REGIONS = ImmutableSet.of(
        12082, 12083, 12337, 12338, 12339, 12593, 12594, 12595
    );
    private static final List<Integer> DRAYNOR_OBSTACLES = Arrays.asList(11404,
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

    private static final Set<Integer> ALKHARID_REGIONS = ImmutableSet.of(
        12848, 12849, 12850, 13104, 13105, 13106, 13361, 13362
    );
    private static final List<Integer> ALKHARID_OBSTACLES = List.of(
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

    private static final Set<Integer> VARROCK_REGIONS = ImmutableSet.of(
        12596, 12597, 12598, 12852, 12583, 12584
    );
    private static final List<Integer> VARROCK_OBSTACLES = List.of(
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

    private static final Set<Integer> CANIFIS_REGIONS = ImmutableSet.of(
        13621, 13622, 13623, 13877, 13878, 13879, 14133, 14134, 14135, 14390, 14391
    );
    private static final List<Integer> CANIFIS_OBSTACLES = List.of(
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

    private static final Set<Integer> FALADOR_REGIONS = ImmutableSet.of(
        11827, 11828, 11829, 12083, 12084, 12085, 12339, 12340
    );
    private static final List<Integer> FALADOR_OBSTACLES = List.of(
        14898,
        14899,
        14901,
        14903,
        14904,
        14905,
        14911,
        14919,
        14920,
        14921,
        14922,
        14924,
        14925
    );
    private static final Set<Integer> FALADOR_ANIMATIONS = ImmutableSet.of(
        828,
        //
        1118, 1120,
        741,
        741,
        //
        7134,
        1603,
        1603,
        1603,
        1603,
        1603,
        1603, 2586, 2588
    );
    private static final Set<Integer> FALADOR_POSES = ImmutableSet.of(
        //
        762,
        //
        //
        //
        762,
        763
        //
        //
        //
        //
        //
        //
    );
    private static final Set<Integer> FALADOR_IDLES = ImmutableSet.of(
        //
        763,
        //
        //
        //
        763,
        763
        //
        //
        //
        //
        //
        //
    );

    private static final Set<Integer> SEERS_REGIONS = ImmutableSet.of(
        10549, 10550, 10551, 10805, 10806, 10807, 11061, 11062, 11063
    );
    private static final Map<String, Integer> SEERS_MARKS = new HashMap<String, Integer>(){{
        //
        put("2727_3493", 14928);
        put("2706_3493", 12932); put("2709_3493", 14932);
        put("2712_3481", 14929);
        //
        put("2698_3465", 14931);
    }};
    private static final List<Integer> SEERS_OBSTACLES = List.of(
        14927,
        14928,
        14932,
        14929,
        14930,
        14931
    );
    private static final Set<Integer> SEERS_ANIMATIONS = ImmutableSet.of(
        737, 1118,
        2586, 2588, 2586, 2588,
        //
        2585,
        2586, 2588,
        2586, 2588
    );
    private static final Set<Integer> SEERS_POSES = ImmutableSet.of(
        //
        //
        762
        //
        //
        //
    );
    private static final Set<Integer> SEERS_IDLES = ImmutableSet.of(
        //
        //
        763
        //
        //
        //
    );

    private static final Set<Integer> POLLNIVNEACH_REGIONS = ImmutableSet.of(
        13101, 13102, 13103, 13358, 13359, 13613, 13614, 13615
    );
    private static final Map<String, Integer> POLLNIVNEACH_MARKS = new HashMap<String, Integer>(){{
        put("3359_2983", 14941);
        put("3357_3002", 14945);
    }};
    private static final List<Integer> POLLNIVNEACH_OBSTACLES = List.of(
        14935,
        14936,
        14937,
        14938,
        14939,
        14940,
        14941,
        14944,
        14945
    );
    private static final Set<Integer> POLLNIVNEACH_ANIMATIONS = ImmutableSet.of(
        2583, 2588,
        1995, 1603, 2588,
        1995, 1118, 2588,
        2585,
        1122, 1124, 2588,
        828,
        742, 743,
        1603,
        741, 2586, 2588
    );
    private static final Set<Integer> POLLNIVNEACH_POSES = ImmutableSet.of(
        //
        //
        //
        //
        //
        //
        745, 744, 745
        //
        //
    );
    private static final Set<Integer> POLLNIVNEACH_IDLES = ImmutableSet.of(
        //
        //
        //
        //
        //
        //
        745
        //
        //
    );

    public static boolean isObstacle(final TileObject tile) {
        final int obstacle = tile.getId();
        return (false
            || DRAYNOR_OBSTACLES.contains(obstacle)
            || ALKHARID_OBSTACLES.contains(obstacle)
            || VARROCK_OBSTACLES.contains(obstacle)
            || CANIFIS_OBSTACLES.contains(obstacle)
            || FALADOR_OBSTACLES.contains(obstacle)
            || SEERS_OBSTACLES.contains(obstacle)
            || POLLNIVNEACH_OBSTACLES.contains(obstacle)
        );
    }

    public static boolean isDoingObstacle(final Region region, final Player player, final TileObject obstacle) {
        if (player != null) {
            final int animation = player.getAnimation();
            final int pose = player.getPoseAnimation();
            final int idle = player.getIdlePoseAnimation();

            final Integer distance_world;
            if (obstacle != null) {
                distance_world = player.getWorldLocation().distanceTo(obstacle.getWorldLocation());
            } else {
                distance_world = null;
            }

            return (
                obstacle != null && distance_world == Integer.MAX_VALUE && idle == POSE_IDLE && animation != ANIMATION_IDLE

                || region == Region.DRAYNOR && (
                    DRAYNOR_ANIMATIONS.contains(animation)
                    || DRAYNOR_POSES.contains(pose)
                    || DRAYNOR_IDLES.contains(idle)
                )

                || region == Region.ALKHARID && (
                    ALKHARID_ANIMATIONS.contains(animation)
                    || ALKHARID_POSES.contains(pose)
                    || ALKHARID_IDLES.contains(idle)
                )

                || region == Region.VARROCK && (
                    VARROCK_ANIMATIONS.contains(animation)
                    || VARROCK_POSES.contains(pose)
                    || VARROCK_IDLES.contains(idle)
                )

                || region == Region.CANIFIS && (
                    CANIFIS_ANIMATIONS.contains(animation)
                    || CANIFIS_POSES.contains(pose)
                    || CANIFIS_IDLES.contains(idle)
                )

                || region == Region.FALADOR && (
                    FALADOR_ANIMATIONS.contains(animation)
                    || FALADOR_POSES.contains(pose)
                    || FALADOR_IDLES.contains(idle)
                )

                || region == Region.SEERS && (
                    SEERS_ANIMATIONS.contains(animation)
                    || SEERS_POSES.contains(pose)
                    || SEERS_IDLES.contains(idle)
                )

                || region == Region.POLLNIVNEACH && (
                    POLLNIVNEACH_ANIMATIONS.contains(animation)
                    || POLLNIVNEACH_POSES.contains(pose)
                    || POLLNIVNEACH_IDLES.contains(idle)
                )
            );
        }

        return false;
    }

    public static Optional<Integer> getMarkObstacle(final Region region, final Tile tile) {
        final String location = tile.getWorldLocation().getX() + "_" + tile.getWorldLocation().getY();

        if (region == Region.SEERS && SEERS_MARKS.containsKey(location)) {
            return Optional.of(SEERS_MARKS.get(location));
        } else if (region == Region.POLLNIVNEACH && POLLNIVNEACH_MARKS.containsKey(location)) {
            return Optional.of(POLLNIVNEACH_MARKS.get(location));
        }

        return Optional.empty();
    }
}
