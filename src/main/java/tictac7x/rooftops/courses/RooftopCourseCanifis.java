package tictac7x.rooftops.courses;

import java.util.Arrays;
import com.google.common.collect.ImmutableSet;
import tictac7x.rooftops.MarkOfGrace;

public class RooftopCourseCanifis extends Course {
    public RooftopCourseCanifis() {
        super(
            // Obstacles.
            Arrays.asList(
                14843,
                14844,
                14845,
                14848,
                14846,
                14894,
                14847,
                14897
            ),

            // Animations.
            ImmutableSet.of(
                1765,
                1995, 2586, 2588,
                2586, 2588,
                2585,
                2586, 2588,
                1955, 7132, 2588,
                2586, 2588,
                2586, 2588
            ),

            // Poses.
            ImmutableSet.of(
                //
                //
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
                //
                //
                //
                //
                //
                //
                //
            ),

            ImmutableSet.of(
                //
                new MarkOfGrace(3506, 3495, 14844),
                new MarkOfGrace(3501, 3505, 14845),
                new MarkOfGrace(3490, 3501, 14848),
                new MarkOfGrace(3478, 3496, 14846),
                new MarkOfGrace(3478, 3484, 14894)
                //
                //
            )
        );
    }
}
