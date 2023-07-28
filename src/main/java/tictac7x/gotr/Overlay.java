package tictac7x.gotr;

import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class Overlay extends net.runelite.client.ui.overlay.Overlay {
    private final Client client;
    private final ItemManager items;
    private final ModelOutlineRenderer outlines;
    private final TicTac7xGotrImprovedConfig config;
    private final Teleporters teleporters;
    private final GreatGuardian great_guardian;
    private final Guardians guardians;
    private final Portal portal;
    private final Inventory inventory;

    public Overlay(
        final Client client,
        final ItemManager items,
        final ModelOutlineRenderer outlines,
        final TicTac7xGotrImprovedConfig config,
        final Teleporters teleporters,
        final GreatGuardian great_guardian,
        final Guardians guardians,
        final Portal portal,
        final Inventory inventory
    ) {
        this.client = client;
        this.items = items;
        this.outlines = outlines;
        this.config = config;
        this.teleporters = teleporters;
        this.great_guardian = great_guardian;
        this.guardians = guardians;
        this.portal = portal;
        this.inventory = inventory;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.UNDER_WIDGETS);
    }

    @Override
    public Dimension render(final Graphics2D graphics) {
        drawTeleporters(graphics);
        drawGreatGuardian();
        drawPortal(graphics);

        return null;
    }

    private void drawPortal(final Graphics2D graphics) {
        if (portal.getPortal().isPresent() && portal.getTimeLeft().isPresent()) {
            final long seconds = Duration.between(Instant.now(), portal.getTimeLeft().get()).getSeconds();
            if (seconds < 0) return;

            final long milliseconds = Duration.between(Instant.now(), portal.getTimeLeft().get()).getNano() / 1_000_000 % 1000 / 100;
            final String time = seconds + "." + milliseconds;
            final Point location =  Perspective.getCanvasTextLocation(client, graphics, portal.getPortal().get().getLocalLocation(), time, 120);

            drawCenteredString(graphics, time, location);
            client.setHintArrow(portal.getPortal().get().getLocalLocation());
        } else {
            client.clearHintArrow();
        }
    }

    private void drawGreatGuardian() {
        if (great_guardian.getGreatGuardian().isPresent() && inventory.hasEssence()) {
            drawOutline(great_guardian.getGreatGuardian().get(), inventory.hasElementalEssence() ? config.getElementalColor() : config.getCatalyticColor());
        }
    }

    private void drawTeleporters(final Graphics2D graphics) {
        for (final Map.Entry<GameObject, Teleporter> entry : teleporters.getTeleporters().entrySet()) {
            final GameObject game_object = entry.getKey();
            final Teleporter teleporter = entry.getValue();
            final boolean has_talisman = inventory.hasTeleporterTalisman(teleporter);
            final boolean is_active = ((DynamicObject) game_object.getRenderable()).getAnimation().getId() == Teleporter.ACTIVE_ANIMATION_ID;

            if (is_active || has_talisman) {
                // Outline.
                drawOutline(game_object, teleporter.isElemental() ? config.getElementalColor() : config.getCatalyticColor());

                // Image.
                final BufferedImage rune_image = items.getImage(is_active ? teleporter.rune_id : teleporter.talisman_id);
                drawImage(graphics, game_object, rune_image);

                // Remaining time.
                if (is_active && teleporters.getTimeLeft().isPresent()) {
                    final long seconds = Duration.between(Instant.now(), teleporters.getTimeLeft().get()).getSeconds();
                    if (seconds < 0) break;

                    final long milliseconds = Duration.between(Instant.now(), teleporters.getTimeLeft().get()).getNano() / 1_000_000 % 1000 / 100;
                    final String time = seconds % 60 + "." + milliseconds;

                    graphics.setFont(FontManager.getRunescapeSmallFont());
                    final Point rune_location = Perspective.getCanvasImageLocation(client, game_object.getLocalLocation(), rune_image, 500);
                    final Rectangle rectangle = new Rectangle(rune_location.getX() + 16, rune_location.getY() - 18, 0, 24);

                    drawCenteredString(graphics, time, rectangle, Color.WHITE, FontManager.getRunescapeSmallFont());
                }
            }
        }
    }

    private void drawImage(final Graphics2D graphics, final GameObject object, final BufferedImage image) {
        try {
            OverlayUtil.renderImageLocation(client, graphics, object.getLocalLocation(), image, 500);
        } catch (final Exception ignored) {}
    }

    private void drawOutline(final GameObject object, final Color color) {
        try {
            outlines.drawOutline(object, 2, color, 2);
        } catch (final Exception ignored) {}
    }

    private void drawOutline(final NPC npc, final Color color) {
        try {
            outlines.drawOutline(npc, 2, color, 2);
        } catch (final Exception ignored) {}
    }

    private void drawCenteredString(final Graphics2D graphics, final String text, final Point location) {
        try {
            OverlayUtil.renderTextLocation(graphics, location, text, Color.WHITE);
        } catch (final Exception ignored) {}
    }

    private void drawCenteredString(final Graphics graphics, final String text, final Rectangle rectangle, final Color color, final Font font) {
        graphics.setFont(font);
        final FontMetrics metrics = graphics.getFontMetrics();

        final int x = rectangle.x + (rectangle.width - metrics.stringWidth(text)) / 2;
        final int y = rectangle.y + ((rectangle.height - metrics.getHeight()) / 2) + metrics.getAscent();

        graphics.setColor(Color.BLACK);
        graphics.drawString(text, x + 1, y + 1);

        graphics.setColor(color);
        graphics.drawString(text, x, y);
    }
}
