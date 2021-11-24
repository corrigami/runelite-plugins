package tictac7x.motherlode;

import java.util.List;
import javax.annotation.Nullable;

public class Rockfall {
    public final int x;
    public final int y;
    public final List<Sector> sectors;

    public Rockfall(final int x, final int y, @Nullable final List<Sector> sectors) {
        this.x = x;
        this.y = y;
        this.sectors = sectors;
    }
}
