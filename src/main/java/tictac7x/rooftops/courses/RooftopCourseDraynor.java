package tictac7x.rooftops.courses;

import java.util.Arrays;
import com.google.common.collect.ImmutableSet;
import tictac7x.rooftops.MarkOfGrace;

public class RooftopCourseDraynor extends Course {
    public RooftopCourseDraynor() {
        super(
            // Obstacles.
                Arrays.asList(
                11404,
                11405,
                11406,
                11430,
                11630,
                11631,
                11632
            ),

            // Animations.
            ImmutableSet.of(
                828,
                //
                //
                753, 759,
                2585,
                2586, 2588,
                2586, 2588, 2586, 2588
            ),

            // Poses.
            ImmutableSet.of(
                //
                762,
                762,
                757, 756
                //
                //
                //
            ),

            // Idles.
            ImmutableSet.of(
                //
                763,
                763,
                757
                //
                //
                //
            ),

            ImmutableSet.of(
                //
                new MarkOfGrace(3098, 3281, 11405),
                new MarkOfGrace(3089, 3274, 11406),
                new MarkOfGrace(3094, 3266, 11430),
                //
                //
                new MarkOfGrace(3100, 3257, 11632), new MarkOfGrace(3097, 3259, 11632)
            )
        );
    }
}
