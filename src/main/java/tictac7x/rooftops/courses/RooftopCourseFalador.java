package tictac7x.rooftops.courses;

import java.util.Arrays;
import com.google.common.collect.ImmutableSet;

public class RooftopCourseFalador extends Course {
    public RooftopCourseFalador() {
        super(
            // Obstacles.
                Arrays.asList(
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
            ),

            // Animations.
            ImmutableSet.of(
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
            ),

            // Poses.
            ImmutableSet.of(
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
            ),

            // Idles.
            ImmutableSet.of(
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
            ),

            ImmutableSet.of()
        );
    }
}
