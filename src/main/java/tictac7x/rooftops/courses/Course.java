package tictac7x.rooftops.courses;

import tictac7x.rooftops.MarkOfGrace;

import java.util.Set;
import java.util.List;

public abstract class Course {
    private final List<Integer> obstacles;
    private final Set<Integer> animations;
    private final Set<Integer> poses;
    private final Set<Integer> idles;
    private final Set<MarkOfGrace> mark_of_graces;

    public Course(
        final List<Integer> obstacles,
        final Set<Integer> animations,
        final Set<Integer> poses,
        final Set<Integer> idles,
        final Set<MarkOfGrace> mark_of_graces
    ) {
        this.obstacles = obstacles;
        this.animations = animations;
        this.poses = poses;
        this.idles = idles;
        this.mark_of_graces = mark_of_graces;
    }

    public List<Integer> getObstacles() {
        return obstacles;
    }

    public Set<Integer> getAnimations() {
        return animations;
    }

    public Set<Integer> getPoses() {
        return poses;
    }

    public Set<Integer> getIdles() {
        return idles;
    }

    public Set<MarkOfGrace> getMarkOfGraces() {
        return mark_of_graces;
    }
}
