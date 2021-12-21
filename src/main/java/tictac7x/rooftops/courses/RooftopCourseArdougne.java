package tictac7x.rooftops.courses;

import com.google.common.collect.ImmutableSet;

import java.util.Arrays;

public class RooftopCourseArdougne extends Course {
    public RooftopCourseArdougne() {
        super(
            // Obstacles.
            Arrays.asList(
                15608,
                15609,
                26635,
                15610,
                15611,
                28912,
                15612
            ),

            // Animations.
            ImmutableSet.of(
                737, 2588,
                2586, 2588, 2586, 2588, 2583, 2588,
                //
                2586, 2588,
                7133, 2588,
                753, 759,
                2586, 2588, 741, 741, 2586, 2588
            ),

            // Poses.
            ImmutableSet.of(
                //
                //
                762,
                //
                //
                757, 756, 757
                //
            ),

            // Idles.
            ImmutableSet.of(
                //
                //
                765,
                //
                //
                757
                //
            ),

            ImmutableSet.of()
        );
    }
}
