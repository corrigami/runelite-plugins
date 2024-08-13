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
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RooftopsCourseManager {
    @Inject
    private Client client;

    private final Pattern regexLapComplete = Pattern.compile(".*lap count is:.*");

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

    private final List<TileObject> obstaclesTileObjects = new ArrayList<>();
    private final List<Tile> marksOfGraces = new ArrayList<>();
    @Nullable private Course course;
    private int lastMenuOptionClickedId;

    public void onTileObjectSpawned(final TileObject tileObject) {
        detectCourse();
        if (course == null) return;

        for (final Obstacle obstacle : course.obstacles) {
            if (obstacle.hasId(tileObject.getId())) {
                obstaclesTileObjects.add(tileObject);
            }
        }
    }

    public void onGameStateChanged(final GameStateChanged event) {
        // Clear previous obstacles objects (since they will spawn again).
        if (event.getGameState() == GameState.LOADING) {
            obstaclesTileObjects.clear();
            marksOfGraces.clear();
            course = null;
        }
    }

    public void onStatChanged(final StatChanged event) {
        if (course == null || event.getSkill() != Skill.AGILITY) return;
        completeObstacle();
    }

    public void onHitsplatApplied(final HitsplatApplied event) {
        if (course == null || event.getActor() != client.getLocalPlayer()) return;
        completeCourse();
    }

    public void onGameTick(final GameTick ignored) {
        checkStartObstacle();
    }

    public void onChatMessage(final ChatMessage event) {
        if (course == null || event.getType() != ChatMessageType.GAMEMESSAGE || !regexLapComplete.matcher(event.getMessage()).find()) return;
        completeCourse();
    }

    public void onItemSpawned(final ItemSpawned event) {
        if (event.getItem().getId() != ItemID.MARK_OF_GRACE) return;
        marksOfGraces.add(event.getTile());
    }

    public void onItemDespawned(final ItemDespawned event) {
        if (event.getItem().getId() != ItemID.MARK_OF_GRACE) return;
        marksOfGraces.remove(event.getTile());
    }

    @Nullable
    public Course getCourse() {
        return course;
    }

    public List<TileObject> getObstaclesTileObjects() {
        return obstaclesTileObjects;
    }

    public List<Tile> getMarksOfGraces() {
        return marksOfGraces;
    }

    public boolean isStoppingObstacle(final int obstacle_id) {
        if (course == null) return false;

        for (final Tile tile : marksOfGraces) {
            for (final MarkOfGrace mark : course.marksOfGraces) {
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

        for (final TileObject obstacle : obstaclesTileObjects) {
            if (course.getNextObstacle().hasId(obstacle.getId())) {
                return obstacle;
            }
        }

        return null;
    }

    private boolean isNearNextObstacle() {
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
        course.startObstacle();
        lastMenuOptionClickedId = -1;
    }

    private void completeObstacle() {
        if (this.course == null) return;
        course.completeObstacle();
    }

    private void detectCourse() {
        if (course != null || client.getLocalPlayer() == null || client.getLocalPlayer().getWorldLocation() == null) return;

        for (final Course course : courses) {
            if (course.isNearRegion(client.getLocalPlayer().getWorldLocation().getRegionID())) {
                if (course == this.course) return;

                // New course found, complete previous.
                if (this.course != null) {
                    completeCourse();
                }

                this.course = course;
                return;
            }
        }

        this.course = null;
    }

    private void checkStartObstacle() {
        if (course == null || course.isDoingObstacle()) return;

        // Start obstacle.
        if (isNearNextObstacle() && course.getNextObstacle().hasId(lastMenuOptionClickedId)) {
            startObstacle();
        }
    }

    private void completeCourse() {
        if (course == null) return;
        course.completeCourse();
    }

    public void onMenuOptionClicked(final MenuOptionClicked event) {
        lastMenuOptionClickedId = event.getId();
    }
}
