package tictac7x.motherlode;

public class OreVein {
    public final int x;
    public final int y;
    public final Sector sector;
    public int ticks;
    public boolean depleted;

    public OreVein(final int x, final int y, final Sector sector, final boolean depleted) {
        this.x = x;
        this.y = y;
        this.sector = sector;
        this.depleted = depleted;
        this.ticks = 0;
    }

    public void onGameTick() {
        this.ticks++;
    }
}
