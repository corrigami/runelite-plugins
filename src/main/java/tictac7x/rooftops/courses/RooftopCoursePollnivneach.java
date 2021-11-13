package tictac7x.rooftops.courses;

import tictac7x.rooftops.MarkOfGrace;

import java.util.List;

import com.google.common.collect.ImmutableSet;

public class RooftopCoursePollnivneach extends Course {
    public RooftopCoursePollnivneach() {
        super(
            // Obstacles.
            List.of(
                14935,
                14936,
                14937,
                14938,
                14939,
                14940,
                14941,
                14944,
                14945
            ),

            // Animations.
            ImmutableSet.of(
                2583, 2588,
                1995, 1603, 2588,
                1995, 1118, 2588,
                2585,
                1122, 1124, 2588,
                828,
                742, 743,
                1603,
                741, 2586, 2588
            ),

            ImmutableSet.of(
                //
                //
                //
                //
                //
                //
                745, 744, 745
                //
                //
            ),
            ImmutableSet.of(
                //
                //
                //
                //
                //
                //
                745
                //
                //
            ),

            ImmutableSet.of(
                //
                new MarkOfGrace(3346, 2968, 14936),
                new MarkOfGrace(3353, 2975, 14937),
                //
                //
                //
                new MarkOfGrace(3359, 2983, 14941),
                new MarkOfGrace(3362, 2993, 14944),
                new MarkOfGrace(3357, 3002, 14945)
            )
        );
    }
}