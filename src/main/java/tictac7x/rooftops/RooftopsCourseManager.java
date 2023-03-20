package tictac7x.rooftops;

import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.Skill;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.StatChanged;
import tictac7x.rooftops.courses.Course;
import tictac7x.rooftops.courses.Obstacle;
import tictac7x.rooftops.courses.RooftopCourseSeers;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RooftopsCourseManager {
    private final Client client;

    private final Pattern lap_complete = Pattern.compile(".*lap count is:.*");

    private final Course[] courses = new Course[]{
//        new RooftopCourseAlKharid(),
//        new RooftopCourseVarrock(),
        new RooftopCourseSeers(),
//        new RooftopCoursePollnivneach(),
//        new RooftopCourseArdougne(),
//        new RooftopCourseCanifis(),
//        new RooftopCourseDraynor(),
//        new RooftopCourseRellekka(),
//        new RooftopCourseFalador(),
    };

    private final List<TileObject> obstacles = new ArrayList<>();

    private boolean obstacle_clicked;

    // Keep track of next obstacle id locally until obstacle is completed.
    private int next_obstacle_id;

    //  n: gameticks to wait before starting an obstacle
    //  0: start the obstacle
    // -1: don't start an obstacle
    private int start_obstacle = -1;

    @Nullable
    private Course course;

    public RooftopsCourseManager(final Client client) {
        this.client = client;
    }

    public void onTileObjectSpawned(final TileObject object) {
        if (course == null) return;

        for (final Obstacle obstacle : course.getObstacles()) {
            if (object.getId() == obstacle.id) {
                obstacles.add(object);
            }
        }
    }

    public void onGameStateChanged(final GameStateChanged event) {
        final int[] regions = client.getMapRegions();
        if (regions == null) return;

        if (event.getGameState() == GameState.LOADING) {
            obstacles.clear();
        }

        for (final Course course : courses) {
            for (final int region : regions) {
                if (course.getRegions().contains(region)) {
                    // New course found, reset previous.
                    if (this.course != null && this.course != course) {
                        this.course.reset();
                    }

                    // New course found.
                    this.course = course;
                    this.next_obstacle_id = course.getNextObstacle().id;
                    return;
                }
            }
        }

        // Course not found, reset previous.
        if (this.course != null) {
            this.course.reset();
            this.course = null;
        }
    }

    public void onMenuOptionClicked(final MenuOptionClicked event) {
        // Click on interface or item in invetory doesn't stop from doing the obstacle.
        if (event.getId() == 1 && event.getItemId() == -1 || event.getItemId() != -1 && !event.getMenuTarget().contains("->")) return;
        if (course == null) return;

        // Next obstacle clicked.
        if (event.getId() == next_obstacle_id) {
            obstacle_clicked = true;

            // Mark obstacle to be started.
            if (isNearObstacle()) {
                start_obstacle = 1;
            }

        // Some action happened that is stopping us from doing an obstacle.
        } else {
            obstacle_clicked = false;
        }
    }

    public void onStatChanged(final StatChanged event) {
        if (course == null || event.getSkill() != Skill.AGILITY) return;

        // Obstacle completed, find next one.
        course.completeObstacle();
        next_obstacle_id = course.getNextObstacle().id;
    }

    public void onGameTick(final GameTick gametick) {
        if (course == null) return;

        // Delay starting the obstacle.
        if (start_obstacle > 0) {
            start_obstacle--;
            return;
        }

        // Start obstacle.
        if (start_obstacle == 0) {
            start_obstacle = -1;
            obstacle_clicked = false;
            course.startObstacle();
            return;
        }

        // Obstacle detected from further away.
        if (obstacle_clicked && isNearObstacle()) {
            start_obstacle = 1;
        }
    }

    public void onChatMessage(final ChatMessage event) {
        if (course != null && event.getType() == ChatMessageType.GAMEMESSAGE && lap_complete.matcher(event.getMessage()).find()) {
            course.reset();
        }
    }

    @Nullable
    public Course getCourse() {
        return course;
    }

    public List<TileObject> getObstacles() {
        return obstacles;
    }

    @Nullable
    private TileObject getNextObstacle() {
        if (course == null) return null;

        for (final TileObject obstacle : obstacles) {
            if (obstacle.getId() == course.getNextObstacle().id) {
                return obstacle;
            }
        }

        return null;
    }

    private boolean isNearObstacle() {
        if (course == null) return false;

        final TileObject obstacle = getNextObstacle();
        if (obstacle == null) return false;

        final Player player = client.getLocalPlayer();
        for (final WorldPoint obstacle_point : course.getNextObstacle().locations) {
            if (player.getWorldLocation().distanceTo(obstacle_point) <= 1) {
                return true;
            }
        }

        return false;
    }
}
