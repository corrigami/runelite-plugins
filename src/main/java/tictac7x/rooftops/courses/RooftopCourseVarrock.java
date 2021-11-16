package tictac7x.rooftops.courses;

import java.util.Arrays;
import com.google.common.collect.ImmutableSet;

public class RooftopCourseVarrock extends Course {
    public RooftopCourseVarrock() {
        super(
            // Obstacles.
            Arrays.asList(
                14412,
                14413,
                14414,
                14832,
                14833,
                14834,
                14835,
                14836,
                14841
            ),

            // Animations.
            ImmutableSet.of(
                828, 2585,
                741, 741,
                2586, 2588,
                1995, 1122, 1124, 753,
                2585,
                1995, 4789, 2585,
                2586, 2588,
                1603,
                741, 2586, 2588
            ),

            ImmutableSet.of(
                //
                //
                //
                757, 756
                //
                //
                //
                //
                //
            ),
            ImmutableSet.of(
                //
                //
                //
                757, 757
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
