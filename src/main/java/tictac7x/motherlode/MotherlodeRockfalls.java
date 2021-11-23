package tictac7x.motherlode;

import com.google.common.collect.ImmutableSet;

import java.util.Arrays;
import java.util.Set;

public class MotherlodeRockfalls {
    private final Motherlode motherlode;

    private final Set<Rockfall> rockfalls = ImmutableSet.of(
        new Rockfall(3757, 5677, Arrays.asList(Sector.UPSTAIRS_3, Sector.UPSTAIRS_4, Sector.UPSTAIRS_5)),
        new Rockfall(3748, 5684, Arrays.asList(Sector.UPSTAIRS_1, Sector.UPSTAIRS_2))
    );

    public MotherlodeRockfalls(final Motherlode motherlode) {
        this.motherlode = motherlode;
    }

    public Set<Rockfall> getRockfalls() {
        return rockfalls;
    }
}
