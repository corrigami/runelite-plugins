package tictac7x.rooftops.courses;

import tictac7x.Overlay;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import javax.annotation.Nullable;

import net.runelite.api.Skill;
import net.runelite.api.ItemID;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.GameState;
import net.runelite.api.TileObject;
import net.runelite.api.MenuAction;
import tictac7x.rooftops.MarkOfGrace;
import tictac7x.rooftops.RooftopsConfig;
import net.runelite.api.events.StatChanged;
import net.runelite.api.events.ItemSpawned;
import net.runelite.api.events.ItemDespawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.MenuOptionClicked;

public class Courses extends Overlay {
    private final RooftopsConfig config;
    private final Client client;

    private final RooftopCourseDraynor rooftop_draynor = new RooftopCourseDraynor();
    private final RooftopCourseAlKharid rooftop_alkharid = new RooftopCourseAlKharid();
    private final RooftopCourseVarrock rooftop_varrock = new RooftopCourseVarrock();
    private final RooftopCourseCanifis rooftop_canifis = new RooftopCourseCanifis();
    private final RooftopCourseFalador rooftop_falador = new RooftopCourseFalador();
    private final RooftopCourseSeers rooftop_seers = new RooftopCourseSeers();
    private final RooftopCoursePollnivneach rooftop_pollnivneach = new RooftopCoursePollnivneach();
    private final RooftopCourseRellekka rooftop_rellekka = new RooftopCourseRellekka();
    private final RooftopCourseArdougne rooftop_ardougne = new RooftopCourseArdougne();

    private final List<TileObject> obstacles = new ArrayList<>();
    private final List<Integer> obstacles_visited = new ArrayList<>();
    private final List<MarkOfGrace> mark_of_graces = new ArrayList<>();

    private static final int POSE_IDLE = 813;
    private static final int ANIMATION_IDLE = -1;

    @Nullable
    private Course course = null;

    @Nullable
    private Integer obstacle_next = null;

    @Nullable
    private TileObject obstacle_clicked = null;

    private boolean doing_obstacle = false;
    private boolean xp_drop = true;

    public Courses(final RooftopsConfig config, final Client client) {
        this.config = config;
        this.client = client;
    }

    @Nullable
    public Course getCourseBasedOnObstacle(final TileObject object) {
        if (object == null) return null;
        final int obstacle = object.getId();

        // Check from current course first.
        if (course != null && course.getObstacles().contains(obstacle)) {
            return course;

        // Check from all other courses.
        } else if (rooftop_draynor.getObstacles().contains(obstacle)) {
            return rooftop_draynor;
        } else if (rooftop_alkharid.getObstacles().contains(obstacle)) {
            return rooftop_alkharid;
        } else if (rooftop_varrock.getObstacles().contains(obstacle)) {
            return rooftop_varrock;
        } else if (rooftop_canifis.getObstacles().contains(obstacle)) {
            return rooftop_canifis;
        } else if (rooftop_falador.getObstacles().contains(obstacle)) {
            return rooftop_falador;
        } else if (rooftop_seers.getObstacles().contains(obstacle)) {
            return rooftop_seers;
        } else if (rooftop_pollnivneach.getObstacles().contains(obstacle)) {
            return rooftop_pollnivneach;
        } else if (rooftop_rellekka.getObstacles().contains(obstacle)) {
            return rooftop_rellekka;
        } else if (rooftop_ardougne.getObstacles().contains(obstacle)) {
            return rooftop_ardougne;
        }

        return null;
    }

    public void onTileObjectSpawned(final TileObject object) {
        final Course course = getCourseBasedOnObstacle(object);

        // Course found.
        if (course != null) {

            // Course changed.
            if (course != this.course) {
                this.course = course;
                this.obstacles_visited.clear();
                this.obstacle_next = null;
                this.obstacle_clicked = null;
            }

            obstacles.add(object);
        }
    }

    public void onItemSpawned(final ItemSpawned item) {
        if (item.getItem().getId() == ItemID.MARK_OF_GRACE && course != null) {
            final Optional<MarkOfGrace> mark_of_grace = course.getMarkOfGraces().stream().filter(
                    m ->
                            m.x == item.getTile().getWorldLocation().getX() &&
                                    m.y == item.getTile().getWorldLocation().getY()

                    // Mark of grace hardcoded.
            ).findFirst();

            // Mark of grace predefined.
            if (mark_of_grace.isPresent()) {
                mark_of_graces.add(mark_of_grace.get());

            // Create mark of grace dynamically.
            } else {
                int distance_min = Integer.MAX_VALUE;
                TileObject mark_of_grace_obstacle = null;

                for (final TileObject obstacle : obstacles) {
                    final int distance = item.getTile().getWorldLocation().distanceTo(obstacle.getWorldLocation());

                    // Distance smaller than from previously found obstacle.
                    if (distance < distance_min) {
                        distance_min = distance;
                        mark_of_grace_obstacle = obstacle;
                    }
                }

                // Obstacle was found, create MarkOfGrace object dynamically.
                if (mark_of_grace_obstacle != null) {
                    mark_of_graces.add(new MarkOfGrace(
                        item.getTile().getWorldLocation().getX(),
                        item.getTile().getWorldLocation().getY(),
                        mark_of_grace_obstacle.getId()
                    ));
                }
            }
        }
    }

    /**
     * Ground item despawned.
     * @param item - Deespawned item object.
     */
    public void onItemDespawned(final ItemDespawned item) {
        if (item.getItem().getId() == ItemID.MARK_OF_GRACE) {
            mark_of_graces.stream().filter(
                mark_of_grace ->
                    mark_of_grace.x == item.getTile().getWorldLocation().getX() &&
                    mark_of_grace.y == item.getTile().getWorldLocation().getY()
            ).findFirst().ifPresent(mark_of_graces::remove);
        }
    }

    /**
     * Click listener. Check if clicked object was an obstacle.
     * @param menu_option_clicked - Clicked menu option.
     */
    public void onMenuOptionClicked(final MenuOptionClicked menu_option_clicked) {
        if (menu_option_clicked.getMenuAction() == MenuAction.GAME_OBJECT_FIRST_OPTION) {
            final int x = menu_option_clicked.getParam0();
            final int y = menu_option_clicked.getParam1();
            final int id = menu_option_clicked.getId();
            final TileObject object = findTileObject(client, x, y, id);

            if (object != null && getCourseBasedOnObstacle(object) != null && !obstacles_visited.contains(object.getId())) {
                obstacle_clicked = object;
            }
        }
    }

    /**
     * Stat changed listener. Check for agility drops to mark end of an obstacle.
     * @param stat_changed - Changed stat.
     */
    public void onStatChanged(final StatChanged stat_changed) {
        if (stat_changed.getSkill() == Skill.AGILITY) {
            xp_drop = true;
        }
    }

    public void onObstacleFailed() {
        if (course == null) return;

        this.xp_drop = true;
        this.obstacles_visited.clear();
        this.obstacle_next = course.getObstacles().get(0);
    }

    public void onGameStateChanged(final GameStateChanged event) {
        if (event.getGameState() == GameState.LOADING) {
            obstacles.clear();
            mark_of_graces.clear();
        }
    }

    /**
     * Get next obstacle based on the visited obstacles or player distance from obstacles.
     * @param player - Local player.
     * @return id of next obstacle if one exists.
     */
    @Nullable
    private Integer getObstacleNext(final Player player) {
        @Nullable
        Integer obstacle_next = null;

        // First obstacle in session.
        if (!obstacles.isEmpty() && obstacles_visited.isEmpty()) {
            int distance_min = Integer.MAX_VALUE;

            for (final TileObject obstacle : obstacles) {
                final int distance = player.getWorldLocation().distanceTo(obstacle.getWorldLocation());

                if (distance < distance_min) {
                    obstacle_next = obstacle.getId();
                    distance_min = distance;
                }
            }

        // Find next obstacle based on previous.
        } else if (course != null && !obstacles_visited.isEmpty()) {
            final int previous_obstacle_id = obstacles_visited.get(obstacles_visited.size() - 1);
            final int obstacle_index = course.getObstacles().indexOf(previous_obstacle_id);

            // Previous obstacle was last.
            if (obstacle_index + 1 >= course.getObstacles().size()) {
                obstacle_next = course.getObstacles().get(0);
                obstacles_visited.clear();

                // Next obstacle is available.
            } else {
                obstacle_next = course.getObstacles().get(obstacle_index + 1);
            }
        }

        return obstacle_next;
    }

    /**
     * Get obstacle color based on the status of the obstacle.
     * Red - Not to be used obstacle to prevent missing mark of grace.
     * Green - Next obstacle.
     * Yellow - Temporarily unavailable obstacle.
     * @param obstacle - Obstacle to check status for.
     * @return color of obstacle.
     */
    public Color getObstacleColor(final TileObject obstacle) {
        final int id = obstacle.getId();
        final Color color;

        if (getMarkOfGracesObstacles().contains(id)) {
            color = config.getObstacleStopColor();
        } else if (!doing_obstacle && xp_drop && obstacle_next != null && obstacle_next == id) {
            color = config.getObstacleNextColor();
        } else if (obstacle_next != null && obstacle_next == id) {
            color = config.getObstacleNextUnavailableColor();
        } else {
            color = config.getObstacleUnavailableColor();
        }

        return color;
    }

    /**
     * @return list of obstacles IDs that should not be used to prevent missing mark of graces.
     */
    private List<Integer> getMarkOfGracesObstacles() {
        final List<Integer> mark_of_graces_obstacles = new ArrayList<>();

        for (final MarkOfGrace mark_of_grace : mark_of_graces) {
            mark_of_graces_obstacles.add(mark_of_grace.obstacle);
        }

        return mark_of_graces_obstacles;
    }

    /**
     * Main cycle to determine if currently doing or started an obstacle.
     * @param player - Local player.
     */
    public void cycle(final Player player) {
        final boolean doing_obstacle = isDoingObstacle(course, player, obstacle_clicked);

        // First time finding next obstacle.
        if (obstacle_next == null) {
            obstacle_next = getObstacleNext(player);

        // Obstacle started, find next one.
        } else if (!this.doing_obstacle && this.xp_drop && doing_obstacle && obstacle_clicked != null && !obstacles_visited.contains(obstacle_clicked.getId())) {
            obstacles_visited.add(obstacle_clicked.getId());
            obstacle_next = getObstacleNext(player);
            this.doing_obstacle = true;
            this.xp_drop = false;
            obstacle_clicked = null;
        } else {
            this.doing_obstacle = doing_obstacle;
        }
    }

    public boolean isDoingObstacle(final Course course, final Player player, final TileObject obstacle_clicked) {
        if (player != null) {
            final int animation = player.getAnimation();
            final int pose = player.getPoseAnimation();
            final int idle = player.getIdlePoseAnimation();

            Integer distance_world = null;
            Integer distance_local = null;

            if (obstacle_clicked != null) {
                distance_world = player.getWorldLocation().distanceTo(obstacle_clicked.getWorldLocation());
                distance_local = player.getLocalLocation().distanceTo(obstacle_clicked.getLocalLocation());
            }

            return (
                obstacle_clicked != null && distance_world == Integer.MAX_VALUE && idle == POSE_IDLE && animation != ANIMATION_IDLE ||
                obstacle_clicked != null && distance_local < 128 && idle == POSE_IDLE && animation != ANIMATION_IDLE ||
                course != null && (
                    course.getAnimations().contains(animation) ||
                    course.getPoses().contains(pose) ||
                    course.getIdles().contains(idle)
                )
            );
        }

        return false;
    }

    public List<Integer> getObstaclesVisited() {
        return obstacles_visited;
    }

    public boolean isDoingObstacle() {
        return doing_obstacle;
    }

    public boolean isXpDrop() {
        return xp_drop;
    }

    public Integer getObstacleClicked() {
        return obstacle_clicked != null ? obstacle_clicked.getId() : null;
    }

    public Integer getObstacleNext() {
        return obstacle_next;
    }

    public List<MarkOfGrace> getMarkOfGraces() {
        return mark_of_graces;
    }

    public List<MarkOfGrace> getMarkOfGracesPredefined() {
        if (course != null) {
            return new ArrayList<>(course.getMarkOfGraces());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        return null;
    }
}
