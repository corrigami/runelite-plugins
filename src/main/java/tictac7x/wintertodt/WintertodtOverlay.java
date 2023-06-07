package tictac7x.wintertodt;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.Notifier;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WintertodtOverlay extends Overlay {
    private final static int BURNING_BRAZIER = 29314;
    private final static int FALLING_SNOW = 26690;

    private final Client client;
    private final Notifier notifier;
    private final WintertodtConfig config;

    private final List<GameObject> burning_braziers = new ArrayList<>();
    private final List<GameObject> breaking_braziers = new ArrayList<>();
    private final List<GameObject> falling_snows = new ArrayList<>();

    public WintertodtOverlay(final Client client, final Notifier notifier, final WintertodtConfig config) {
        this.client = client;
        this.notifier = notifier;
        this.config = config;

        setLayer(OverlayLayer.ABOVE_SCENE);
        setPosition(OverlayPosition.DYNAMIC);
    }

    public void onGameObjectSpawned(final GameObjectSpawned event) {
        switch (event.getGameObject().getId()) {
            case BURNING_BRAZIER:
                burning_braziers.add(event.getGameObject());
                return;

            case FALLING_SNOW:
                final GameObject falling_snow = event.getGameObject();
                falling_snows.add(falling_snow);

                if (config.notifyFallingSnow() && client.getLocalPlayer().getWorldLocation().distanceTo(event.getGameObject().getWorldLocation()) == 0) {
                    notifier.notify("Snow is about to fall on you!");
                    return;
                }

                final int x = falling_snow.getWorldLocation().getX();
                final int y = falling_snow.getWorldLocation().getY();

                if (x == 1621 && y == 3997 || x == 1639 && y == 3997 || x == 1621 && y == 4015 || x == 1639 && y == 4015) {
                    final Optional<GameObject> breaking_brazier = burning_braziers.stream().filter(brazier -> falling_snow.getWorldLocation().distanceTo(brazier.getWorldLocation()) == 1).findAny();
                    if (!breaking_brazier.isPresent()) return;

                    // Collect all breaking braziers for highlights.
                    breaking_braziers.add(breaking_brazier.get());

                    // Notify only about the breaking brazier that the player is standing next to.
                    if (
                        config.notifyBreakingBrazier() &&
                        client.getLocalPlayer().getWorldLocation().distanceTo(breaking_brazier.get().getWorldLocation()) <= 2
                    ) {
                        notifier.notify("Brazier is about to break!");
                    }
                }
        }
    }

    public void onGameObjectDespawned(final GameObjectDespawned event) {
        switch (event.getGameObject().getId()) {
            case BURNING_BRAZIER:
                burning_braziers.remove(event.getGameObject());
                breaking_braziers.remove(event.getGameObject());
                return;

            case FALLING_SNOW:
                falling_snows.remove(event.getGameObject());
        }
    }

    public void onGameStateChanged(final GameStateChanged event) {
        if (event.getGameState() == GameState.LOADING) {
            burning_braziers.clear();
            breaking_braziers.clear();
            falling_snows.clear();
        }
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (config.highlightBreakingBrazier().getAlpha() != 0) {
            for (final GameObject breaking_brazier : breaking_braziers) {
                renderShape(graphics, breaking_brazier.getCanvasTilePoly(), config.highlightBreakingBrazier());
            }
        }

        if (config.highlightFallingSnow().getAlpha() != 0) {
            for (final GameObject falling_snow : falling_snows) {
                renderShape(graphics, falling_snow.getCanvasTilePoly(), config.highlightBreakingBrazier());
            }
        }

        return null;
    }

    private void renderShape(final Graphics2D graphics, final Shape shape, final Color color) {
        try {
            // Area border.
            graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(0, Math.min(255, color.getAlpha() + 20))));
            graphics.setStroke(new BasicStroke(1));
            graphics.draw(shape);

            // Area fill.
            graphics.setColor(color);
            graphics.fill(shape);
        } catch (final Exception ignored) {}
    }
}
