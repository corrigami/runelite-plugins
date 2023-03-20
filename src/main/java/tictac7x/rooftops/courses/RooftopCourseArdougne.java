package tictac7x.rooftops.courses;

import tictac7x.rooftops.MarkOfGrace;

public class RooftopCourseArdougne extends Course {
    public RooftopCourseArdougne() {
        super("Ardougne",

            // Regions.
            new int[]{},

            // Obstacles.
            // TODO - need to find all planes and coordinates of the obstacles.
            new Obstacle[]{
                new Obstacle(15608, 0, new int[][]{}),
                new Obstacle(15609, 0, new int[][]{}),
                new Obstacle(26635, 0, new int[][]{}),
                new Obstacle(15610, 0, new int[][]{}),
                new Obstacle(15611, 0, new int[][]{}),
                new Obstacle(28912, 0, new int[][]{}),
                new Obstacle(15612, 0, new int[][]{})
            },

            new MarkOfGrace[]{}
        );
    }
}
