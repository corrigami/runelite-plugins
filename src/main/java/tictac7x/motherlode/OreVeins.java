package tictac7x.motherlode;

import net.runelite.api.*;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.WallObjectDespawned;
import net.runelite.api.events.WallObjectSpawned;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ProgressPieComponent;

import java.awt.*;
import java.util.*;

import static tictac7x.motherlode.Orientation.*;

public class OreVeins extends Overlay {
    private final TicTac7xMotherlodeConfig config;
    private final Player player;

    public OreVeins(final TicTac7xMotherlodeConfig config, final Player player) {
        this.config = config;
        this.player = player;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    public Map<String, OreVein> oreVeins = new HashMap<>();
    public Set<WallObject> oreVeinsWallObjects = new HashSet<>();

    public void onWallObjectSpawned(final WallObjectSpawned event) {
        final WallObject wallObject = event.getWallObject();
        final boolean isOreVein = OreVein.isOreVein(wallObject);
        final boolean isDepletedOreVein = OreVein.isDepletedOreVein(wallObject);
        if (!isOreVein && !isDepletedOreVein) return;

        updateOreVein(wallObject, isDepletedOreVein);
        oreVeinsWallObjects.add(event.getWallObject());
    }

    public void onWallObjectDespawned(final WallObjectDespawned event) {
        final WallObject wallObject = event.getWallObject();
        final boolean isOreVein = OreVein.isOreVein(wallObject);
        final boolean isDepletedOreVein = OreVein.isDepletedOreVein(wallObject);
        if (!isOreVein && !isDepletedOreVein) return;

        updateOreVein(wallObject, isDepletedOreVein);
        oreVeinsWallObjects.remove(event.getWallObject());
    }

    public void onGameStateChanged(final GameStateChanged event) {
        if (event.getGameState() == GameState.LOADING) {
            oreVeinsWallObjects.clear();
        }
    }

    public void onAnimationChanged(final AnimationChanged event) {
        if (!isMiningAnimation(event)) return;
        setOreVeinMinedFromAnimation(event);
    }

    public void onGameTick() {
        for (final OreVein oreVein : oreVeins.values()) {
            oreVein.onGameTick();
        }
    }

    private void updateOreVein(final WallObject wallObject, final boolean isDepleted) {
        final String key = getOreVeinKey(wallObject);

        if (oreVeins.containsKey(key)) {
            oreVeins.get(key).setDepleted(isDepleted);
        } else {
            oreVeins.put(key, new OreVein(
                wallObject.getWorldLocation().getX(),
                wallObject.getWorldLocation().getY(),
                isDepleted,
                config
            ));
        }
    }

    private void setOreVeinMinedFromAnimation(final AnimationChanged event) {
        final Actor actor = event.getActor();
        final int x = actor.getWorldLocation().getX();
        final int y = actor.getWorldLocation().getY();
        final Orientation orientation = getOrientationSimplified(actor.getOrientation());

        // Find correct ore vein based on actor orientation when mining.
        for (final OreVein oreVein : oreVeins.values()) {
            if (
                orientation == SOUTH && x == oreVein.x && y == oreVein.y + 1 ||
                orientation == WEST && x == oreVein.x + 1 && y == oreVein.y ||
                orientation == EAST && x == oreVein.x - 1 && y == oreVein.y ||
                orientation == NORTH && x == oreVein.x && y == oreVein.y - 1
            ) {
                oreVein.setMined();
            }
        }
    }

    private Optional<OreVein> getOreVeinFromWallObject(final WallObject wallObject) {
        if (!oreVeins.containsKey(getOreVeinKey(wallObject))) return Optional.empty();

        return Optional.of(oreVeins.get(getOreVeinKey(wallObject)));
    }

    @Override
    public Dimension render(final Graphics2D graphics2D) {
        for (final WallObject wallObject : oreVeinsWallObjects) {
            final Optional<OreVein> oreVein = getOreVeinFromWallObject(wallObject);
            if (!oreVein.isPresent()) continue;

            if (player.getSectors().contains(oreVein.get().sector)) {
                renderPie(graphics2D, wallObject, oreVein.get().getPieColor(), oreVein.get().getPieProgress());
            }
        }

        return null;
    }

    private void renderPie(final Graphics2D graphics, final WallObject object, final Color color, final float progress) {
        try {
            final ProgressPieComponent progressPieComponent = new ProgressPieComponent();
            progressPieComponent.setPosition(object.getCanvasLocation(160));
            progressPieComponent.setProgress(progress);
            progressPieComponent.setBorderColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 255));
            progressPieComponent.setFill(new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(color.getAlpha() - 20, 0)));
            progressPieComponent.render(graphics);
        } catch (final Exception ignored) {}
    }

    private String getOreVeinKey(final WallObject wallObject) {
        return wallObject.getWorldLocation().getX() + "_" + wallObject.getWorldLocation().getY();
    }

    private boolean isMiningAnimation(final AnimationChanged event) {
        switch (event.getActor().getAnimation()) {
            case AnimationID.MINING_MOTHERLODE_BRONZE:
            case AnimationID.MINING_MOTHERLODE_IRON:
            case AnimationID.MINING_MOTHERLODE_STEEL:
            case AnimationID.MINING_MOTHERLODE_BLACK:
            case AnimationID.MINING_MOTHERLODE_MITHRIL:
            case AnimationID.MINING_MOTHERLODE_ADAMANT:
            case AnimationID.MINING_MOTHERLODE_RUNE:
            case AnimationID.MINING_MOTHERLODE_DRAGON:
            case AnimationID.MINING_MOTHERLODE_DRAGON_OR:
            case AnimationID.MINING_MOTHERLODE_DRAGON_UPGRADED:
            case AnimationID.MINING_MOTHERLODE_DRAGON_OR_TRAILBLAZER:
            case AnimationID.MINING_MOTHERLODE_3A:
            case AnimationID.MINING_MOTHERLODE_CRYSTAL:
            case AnimationID.MINING_MOTHERLODE_INFERNAL:
            case AnimationID.MINING_MOTHERLODE_GILDED:
            case AnimationID.MINING_MOTHERLODE_TRAILBLAZER:
                return true;
            default:
                return false;
        }
    }

    private Orientation getOrientationSimplified(final int orientation) {
        if (orientation >= 0 && orientation < 256) {
            return SOUTH;
        }

        if (orientation >= 256 && orientation < 768) {
            return WEST;
        }

        if (orientation >= 768 && orientation < 1280) {
            return NORTH;
        }

        return EAST;
    }
}