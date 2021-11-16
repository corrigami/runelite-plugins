package tictac7x.rooftops.courses;

import com.google.common.collect.ImmutableSet;
import tictac7x.rooftops.MarkOfGrace;

import java.util.Arrays;

public class RooftopCourseRellekka extends Course {
    public RooftopCourseRellekka() {
        super(
            // Obstacles.
            Arrays.asList(
                14946,
                14947,
                14987,
                14990,
                14991,
                14992,
                14994
            ),

            // Animations.
            ImmutableSet.of(
                828,
                1995, 1603,
                //
                752,
                1995, 1603,
                //
                2586, 2588
            ),

            ImmutableSet.of(
                //
                //
                762,
                755, 754, 762,
                //
                762
                //
            ),

            ImmutableSet.of(
                //
                //
                763,
                755, 763,
                //
                763
                //
            ),

            ImmutableSet.of(
                //
                new MarkOfGrace(2623, 3675, 14947),
                //
                //
                new MarkOfGrace(2642, 3651, 14991)
            )
        );
    }
}