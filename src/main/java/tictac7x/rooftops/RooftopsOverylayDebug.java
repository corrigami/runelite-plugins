package tictac7x.rooftops;

import tictac7x.Overlay;
import tictac7x.rooftops.courses.Courses;

import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Color;
import java.util.Objects;

import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;

public class RooftopsOverylayDebug extends Overlay {
    private final Client client;
    private final Courses course_manager;
    private final PanelComponent panel = new PanelComponent();

    // For debugging and finding out course animations.
    private static int animation;
    private static int pose;
    private static int idle;
    private static Integer obstacle;

    public RooftopsOverylayDebug(final Client client, final Courses course_manager) {
        this.client = client;
        this.course_manager = course_manager;
        setPosition(OverlayPosition.TOP_LEFT);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        panel.getChildren().clear();

        String visited = "";
        String mark = "";

        for (final int v : course_manager.getObstaclesVisited()) {
            visited += v + ", ";
        }
        if (visited.length() > 0) {
            visited = visited.substring(0, visited.length() - 2);
        } else {
            visited = "-";
        }
        for (final MarkOfGrace m : course_manager.getMarkOfGraces()) {
            mark += m.x + "_" + m.y + ", ";
        }
        if (mark.length() > 0) {
            mark = mark.substring(0, mark.length() - 2);
        } else {
            mark = "-";
        }

        panel.getChildren().add(LineComponent.builder().left("XP:").right(course_manager.isXpDrop() ? "true" : "false").rightColor(course_manager.isXpDrop() ? Color.green : color_red).build());
        panel.getChildren().add(LineComponent.builder().left("ANIMATING:").right(course_manager.isDoingObstacle() ? "true" : "false").rightColor(course_manager.isDoingObstacle() ? color_green : color_red).build());
        panel.getChildren().add(LineComponent.builder().left("CLICKED:").right(course_manager.getObstacleClicked() != null ? course_manager.getObstacleClicked() + "" : "-").build());
        panel.getChildren().add(LineComponent.builder().left("NEXT").right(course_manager.getObstacleNext() != null ? course_manager.getObstacleNext() + "" : "-").build());
        panel.getChildren().add(LineComponent.builder().left("VISITED").right(visited).build());
        panel.getChildren().add(LineComponent.builder().left("MARK").right(mark).build());

        final Player player = client.getLocalPlayer();
        if (player != null) {
            // For debugging and finding out course animations.
            if (!Objects.equals(obstacle, course_manager.getObstacleClicked())) {
                obstacle = course_manager.getObstacleClicked();
                if (obstacle != null) {
                    System.out.println();
                    System.out.println("OBSTACLE: " + obstacle);
                }
            }
            if (player.getAnimation() != animation) {
                animation = player.getAnimation();

                if (animation != -1) {
                    System.out.println("Animation: " + animation);
                }
            }
            if (player.getPoseAnimation() != pose) {
                pose = player.getPoseAnimation();

                if (pose != 813 && pose != 1205 && pose != 1206 && pose != 1207 && pose != 1208 && pose != 1209 && pose != 1210) {
                    System.out.println("Pose: " + pose);
                }
            }
            if (player.getIdlePoseAnimation() != idle) {
                idle = player.getIdlePoseAnimation();

                if (idle != 813) {
                    System.out.println("Idle: " + idle);
                }
            }
        }

        return panel.render(graphics);
    }
}
