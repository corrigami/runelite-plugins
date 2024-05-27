package tictac7x.rooftops;

import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.ItemID;
import net.runelite.api.Player;
import net.runelite.api.Skill;
import net.runelite.api.Tile;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.ItemDespawned;
import net.runelite.api.events.ItemSpawned;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.StatChanged;
import tictac7x.rooftops.courses.Course;
import tictac7x.rooftops.courses.Obstacle;
import tictac7x.rooftops.courses.RooftopCourseAlKharid;
import tictac7x.rooftops.courses.RooftopCourseArdougne;
import tictac7x.rooftops.courses.RooftopCourseCanifis;
import tictac7x.rooftops.courses.RooftopCourseDraynor;
import tictac7x.rooftops.courses.RooftopCourseFalador;
import tictac7x.rooftops.courses.RooftopCoursePollnivneach;
import tictac7x.rooftops.courses.RooftopCourseRellekka;
import tictac7x.rooftops.courses.RooftopCourseSeers;
import tictac7x.rooftops.courses.RooftopCourseVarrock;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class RooftopsCourseManager {
    private final Client client;

    private final Pattern lap_complete = Pattern.compile(".*lap count is:.*");

    private final Course[] courses = new Course[]{
        new RooftopCourseDraynor(),
        new RooftopCourseAlKharid(),
        new RooftopCourseVarrock(),
        new RooftopCourseCanifis(),
        new RooftopCourseFalador(),
        new RooftopCourseSeers(),
        new RooftopCoursePollnivneach(),
        new RooftopCourseRellekka(),
        new RooftopCourseArdougne(),
    };

    private final List<TileObject> obstacles = new ArrayList<>();
    private final List<Tile> marks_of_graces = new ArrayList<>();

    // Keep track of next obstacle id locally until obstacle is completed.
    private int[] next_obstacle_ids;

    //  n: gameticks to wait before starting an obstacle
    //  0: start the obstacle
    // -1: don't start an obstacle
    private int start_obstacle = -1;

    @Nullable
    private Course course;

    private boolean is_near_course;

    public RooftopsCourseManager(final Client client) {
        this.client = client;
    }

    public void onTileObjectSpawned(final TileObject object) {
        if (course == null) return;

        for (final Obstacle obstacle : course.getObstacles()) {
            if (obstacle.hasId(object.getId())) {
                obstacles.add(object);
            }
        }
    }

    public void onGameStateChanged(final GameStateChanged event) {
        // Clear previous obstacles objects (since they will spawn again).
        if (event.getGameState() == GameState.LOADING) {
            obstacles.clear();
            marks_of_graces.clear();
        }

        detectCourse();
    }

    public void onStatChanged(final StatChanged event) {
        if (course == null || event.getSkill() != Skill.AGILITY) return;
        completeObstacle();
    }

    public void onHitsplatApplied(final HitsplatApplied event) {
        if (course == null || event.getActor() != client.getLocalPlayer()) return;
        resetCourse();
    }

    public void onGameTick(final GameTick ignored) {
        checkNearCourse();
        checkStartObstacle();
    }

    public void onChatMessage(final ChatMessage event) {
        if (course == null || event.getType() != ChatMessageType.GAMEMESSAGE || !lap_complete.matcher(event.getMessage()).find()) return;
        resetCourse();
    }

    public void onItemSpawned(final ItemSpawned event) {
        if (event.getItem().getId() != ItemID.MARK_OF_GRACE) return;
        marks_of_graces.add(event.getTile());
    }

    public void onItemDespawned(final ItemDespawned event) {
        if (event.getItem().getId() != ItemID.MARK_OF_GRACE) return;
        marks_of_graces.remove(event.getTile());
    }

    @Nullable
    public Course getCourse() {
        return course;
    }

    public List<TileObject> getObstacles() {
        return obstacles;
    }

    public List<Tile> getMarksOfGraces() {
        return marks_of_graces;
    }

    public boolean isNearCourse() {
        return is_near_course;
    }

    public boolean isStoppingObstacle(final int obstacle_id) {
        if (course == null) return false;

        for (final Tile tile : marks_of_graces) {
            for (final MarkOfGrace mark : course.getMarkOfGraces()) {
                if (mark.obstacle == obstacle_id && mark.x == tile.getWorldLocation().getX() && mark.y == tile.getWorldLocation().getY()) {
                    return true;
                }
            }
        }

        return false;
    }

    @Nullable
    private TileObject getNextObstacle() {
        if (course == null) return null;

        for (final TileObject obstacle : obstacles) {
            if (course.getNextObstacle().hasId(obstacle.getId())) {
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

    private void startObstacle() {
        if (course == null) return;

        start_obstacle = -1;
        course.startObstacle();
    }

    private void completeObstacle() {
        if (this.course == null) return;
        course.completeObstacle();
        next_obstacle_ids = course.getNextObstacle().getIds();
    }

    private void detectCourse() {
        final int[] regions = client.getMapRegions();
        if (regions == null) return;

        for (final Course course : courses) {
            if (course.isNearRegion(regions)) {
                // If player is specifically in another course region, don't change the course.
                if (this.course != null && this.course.isInRegion(client.getLocalPlayer().getWorldLocation().getRegionID())) {
                    continue;
                }

                // New course found, reset previous.
                if (this.course != null && this.course != course) {
                    this.course.reset();
                }

                // New course found.
                if (this.course != course) {
                    this.next_obstacle_ids = course.getNextObstacle().getIds();
                }

                this.course = course;
                // Cant break here since some courses share regions and we need to check all of them.
            }
        }
    }

    private void checkNearCourse() {
        if (course == null) return;
        is_near_course = course.isInRegion(client.getLocalPlayer().getWorldLocation().getRegionID());
    }

    private void checkStartObstacle() {
        if (course == null) return;

        // Delay starting the obstacle.
        if (start_obstacle > 0) {
            start_obstacle--;
            return;
        }

        // Start obstacle.
        if (start_obstacle == 0) {
            startObstacle();
            return;
        }

        // Obstacle detected from further away.
        if (isNearObstacle()) {
            start_obstacle = 1;
        }
    }

    private void resetCourse() {
        if (course == null) return;
        course.reset();
        next_obstacle_ids = course.getNextObstacle().getIds();
    }
}
