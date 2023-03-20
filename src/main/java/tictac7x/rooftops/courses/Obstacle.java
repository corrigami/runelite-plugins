package tictac7x.rooftops.courses;

import net.runelite.api.coords.WorldPoint;

import java.util.ArrayList;
import java.util.List;

public class Obstacle {
    public final int id;
    public final List<WorldPoint> locations;

    public Obstacle(final int id, final int plane, final int[][] locations) {
        this.id = id;
        this.locations = new ArrayList<>();
        for (final int[] location : locations) {
            this.locations.add(new WorldPoint(location[0], location[1], plane));
        }
    }
}
