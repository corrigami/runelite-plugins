package tictac7x.rooftops;

import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import tictac7x.Overlay;

import java.awt.*;

public class OverylayDebug extends Overlay {
    private final PanelComponent panel = new PanelComponent();

    public OverylayDebug() {
        setPosition(OverlayPosition.TOP_LEFT);

    }

    @Override
    public Dimension render(Graphics2D graphics) {
        panel.getChildren().clear();

        String visited = "";

        for (final int v : RooftopsOverlay.obstacles_visited) {
            visited += v + ", ";
        }

        panel.getChildren().add(LineComponent.builder().left("XP:").right(RooftopsOverlay.xp_drop ? "true" : "false").rightColor(RooftopsOverlay.xp_drop ? Color.green : color_red).build());
        panel.getChildren().add(LineComponent.builder().left("GLOBAL:").right(RooftopsOverlay.doing_obstacle ? "true" : "false").rightColor(RooftopsOverlay.doing_obstacle ? color_green : color_red).build());
        panel.getChildren().add(LineComponent.builder().left("LOCAL:").right(RooftopsOverlay.doing_obstacle ? "true" : "false").rightColor(RooftopsOverlay.doing_obstacle ? color_green : color_red).build());
        panel.getChildren().add(LineComponent.builder().left("CLICKED:").right(RooftopsOverlay.obstacle_clicked != null ? RooftopsOverlay.obstacle_clicked.getId() + "" : "-").build());
        panel.getChildren().add(LineComponent.builder().left("NEXT").right(RooftopsOverlay.obstacle_next.isPresent() ? RooftopsOverlay.obstacle_next.get() + "" : "-").build());
        panel.getChildren().add(LineComponent.builder().left("VISITED").right(visited).build());
        panel.getChildren().add(LineComponent.builder().left("MARK").right(RooftopsOverlay.mark_of_grace != null ? RooftopsOverlay.mark_of_grace.getWorldLocation().getX() + " " + RooftopsOverlay.mark_of_grace.getWorldLocation().getY() : "-").build());

        return panel.render(graphics);
    }
}
