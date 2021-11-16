package tictac7x.rooftops.courses;

import java.util.Arrays;
import tictac7x.rooftops.MarkOfGrace;
import com.google.common.collect.ImmutableSet;

public class RooftopCourseSeers extends Course {
    public RooftopCourseSeers() {
        super(
            // Obstacles.
            Arrays.asList(
                14927,
                14928,
                14932,
                14929,
                14930,
                14931
            ),

            // Animations.
            ImmutableSet.of(
                737, 1118,
                2586, 2588, 2586, 2588,
                //
                2585,
                2586, 2588,
                2586, 2588
            ),

            ImmutableSet.of(
                //
                //
                762
                //
                //
                //
            ),
            ImmutableSet.of(
                //
                //
                763
                //
                //
                //
            ),

            ImmutableSet.of(
                //
                new MarkOfGrace(2727, 3493, 14928),
                new MarkOfGrace(2706, 3493, 14932), new MarkOfGrace(2709, 3493, 14932),
                new MarkOfGrace(2712, 3481, 14929),
                //
                new MarkOfGrace(2698, 3465, 14931)
            )
        );
    }
}