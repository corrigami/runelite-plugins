package tictac7x.sulliuscep;

public class WorldLocationObstacle extends WorldLocationObject {
    public final int sulliuscep;

    public WorldLocationObstacle(final int id, final int x, final int y, final int sulliuscep) {
        super(id, x, y);
        this.sulliuscep = sulliuscep;
    }
}
