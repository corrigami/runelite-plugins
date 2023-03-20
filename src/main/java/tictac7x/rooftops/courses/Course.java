package tictac7x.rooftops.courses;

import tictac7x.rooftops.MarkOfGrace;

import java.util.List;
import java.util.Set;

public abstract class Course {
    private final String id;
    private final Set<Integer> regions;
    private final List<Obstacle> obstacles;
    private final Set<MarkOfGrace> mark_of_graces;

    private int obstacle_index = 0;
    private boolean doing_obstacle;

    public Course(
        final String id,
        final Set<Integer> regions,
        final List<Obstacle> obstacles,
        final Set<MarkOfGrace> mark_of_graces
    ) {
        this.id = id;
        this.regions = regions;
        this.obstacles = obstacles;
        this.mark_of_graces = mark_of_graces;
    }

    public String getId() {
        return id;
    }

    public Set<Integer> getRegions() {
        return regions;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public Set<MarkOfGrace> getMarkOfGraces() {
        return mark_of_graces;
    }

    public Obstacle getNextObstacle() {
        return obstacles.get(obstacle_index);
    }

    public void startObstacle() {
        // Obstacle already started.
        if (doing_obstacle) return;

        doing_obstacle = true;
        obstacle_index = obstacle_index + 1 == obstacles.size()
            ? 0
            : obstacle_index + 1;
    }

    public void completeObstacle() {
        doing_obstacle = false;
    }

    public void reset() {
        doing_obstacle = false;
        obstacle_index = 0;
    }

    public boolean isDoingObstacle() {
        return doing_obstacle;
    }
}
