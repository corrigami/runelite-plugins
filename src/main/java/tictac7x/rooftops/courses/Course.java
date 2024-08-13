package tictac7x.rooftops.courses;

import tictac7x.rooftops.MarkOfGrace;

public abstract class Course {
    public final String id;
    public final int[] regions;
    public final Obstacle[] obstacles;
    public final MarkOfGrace[] markOfGraces;

    private int obstacleIndex = 0;
    private boolean doingObstacle;

    public Course(
        final String id,
        final int[] regions,
        final Obstacle[] obstacles,
        final MarkOfGrace[] markOfGraces
    ) {
        this.id = id;
        this.regions = regions;
        this.obstacles = obstacles;
        this.markOfGraces = markOfGraces;
    }

    public Obstacle getNextObstacle() {
        return obstacles[obstacleIndex];
    }

    public void startObstacle() {
        // Obstacle already started.
        if (doingObstacle) return;

        doingObstacle = true;
        obstacleIndex = obstacleIndex + 1 == obstacles.length
            ? 0
            : obstacleIndex + 1;
    }

    public void completeObstacle() {
        doingObstacle = false;
    }

    public void completeCourse() {
        doingObstacle = false;
        obstacleIndex = 0;
    }

    public boolean isDoingObstacle() {
        return doingObstacle;
    }

    public boolean isNearRegion(final int region) {
        for (final int courseRegion : this.regions) {
            if (courseRegion == region) {
                return true;
            }
        }

        return false;
    }
}
