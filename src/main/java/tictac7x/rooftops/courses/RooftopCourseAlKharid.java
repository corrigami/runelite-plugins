package tictac7x.rooftops.courses;

import java.util.List;
import com.google.common.collect.ImmutableSet;

public class RooftopCourseAlKharid extends Course {
    public RooftopCourseAlKharid() {
        super(
            // Obstacles.
            List.of(
                11633,
                14398,
                14402,
                14403,
                14404,
                11634,
                14409,
                14399
            ),

            // Animations.
            ImmutableSet.of(
                828,
                //
                1995, 751,
                2586, 1601, 1602,
                1122, 1124, 2588,
                828,
                //
                2586
            ),

            ImmutableSet.of(
                //
                762,
                //
                //
                //
                //
                762
                //
            ),
            ImmutableSet.of(
                //
                763,
                //
                //
                //
                //
                763
                //
            ),

            ImmutableSet.of()
        );
    }
}
