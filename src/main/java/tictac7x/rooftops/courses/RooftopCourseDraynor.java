package tictac7x.rooftops.courses;

import java.util.List;
import com.google.common.collect.ImmutableSet;

public class RooftopCourseDraynor extends Course {
    public RooftopCourseDraynor() {
        super(
            // Obstacles.
            List.of(
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

            ImmutableSet.of(
                //
                762,
                762,
                757, 756
                //
                //
                //
            ),
            ImmutableSet.of(
                //
                763,
                763,
                757
                //
                //
                //
            ),

            ImmutableSet.of()
        );
    }
}
