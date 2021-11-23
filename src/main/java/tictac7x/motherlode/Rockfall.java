package tictac7x.motherlode;

import javax.annotation.Nullable;
import java.util.List;

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
