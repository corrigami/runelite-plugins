package tictac7x.gotr.widgets;

import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.util.ImageUtil;
import tictac7x.gotr.store.Portal;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

public class InactivePortalWidget extends Overlay {
    private final int PORTAL_SPRITE_ID = 4368;
    private final int GOTR_WIDGET_GROUP = 746;
    private final int GOTR_WIDGET_CHILD = 2;
    private final int GOTR_WIDGET_ACTIVE_PORTAL_CHILD = 26;

    private final Client client;
    private final SpriteManager spriteManager;
    private final Portal portal;

    private final BufferedImage inactivePortalImage;

    public InactivePortalWidget(final Client client, final SpriteManager spriteManager, final Portal portal) {
        this.client = client;
        this.spriteManager = spriteManager;
        this.inactivePortalImage = getInactivePortalImage();
        this.portal = portal;
    }

    @Override
    public Dimension render(final Graphics2D graphics) {
        final Optional<Widget> gotrWidget = Optional.ofNullable(client.getWidget(GOTR_WIDGET_GROUP, GOTR_WIDGET_CHILD));
        final Optional<Widget> activePortalWidget = Optional.ofNullable(client.getWidget(GOTR_WIDGET_GROUP, GOTR_WIDGET_ACTIVE_PORTAL_CHILD));
        if (!gotrWidget.isPresent() || gotrWidget.get().isHidden()) return null;
        if (activePortalWidget.isPresent() && !activePortalWidget.get().isHidden()) return null;
        if (!portal.getTimeToPortal().isPresent()) return null;

        int x = gotrWidget.get().getRelativeX() + 189;
        int y = gotrWidget.get().getRelativeY() + 33;
        int width = 32;
        int height = 32;

        // Inactive portal image.
        try {
            final BufferedImage image = spriteManager.getSprite(PORTAL_SPRITE_ID, 0);
            graphics.drawImage(ImageUtil.grayscaleImage(image), x, y, width, height, null);
        } catch (final Exception ignored) {}

        // Inactive portal text.
        if (portal.getTimeToPortal().isPresent()) {
            final long minutes = Duration.between(Instant.now(), portal.getTimeToPortal().get()).getSeconds() / 60;
            final long seconds = Duration.between(Instant.now(), portal.getTimeToPortal().get()).getSeconds() % 60;
            final String time = minutes + ":" + String.format("%02d", seconds < 0 ? 0 : seconds);

            int textHeight = 24;
            final Rectangle rect = new Rectangle(x, y + height, width, textHeight);

            try {
                graphics.setFont(FontManager.getRunescapeFont());
                FontMetrics metrics = graphics.getFontMetrics();
                x = rect.x + (rect.width - metrics.stringWidth(time)) / 2;
                y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
                graphics.setColor(Color.BLACK);
                graphics.drawString(time, x + 1, y + 1);
                graphics.setColor(
                    minutes == 0 && seconds <= 10 ? Color.RED :
                    minutes == 0 && seconds <= 30 ? Color.ORANGE :
                    Color.WHITE
                );
                graphics.drawString(time, x, y);
            } catch (final Exception ignored) {}
        }

        return null;
    }

    private BufferedImage getInactivePortalImage() {
        return null;
    }
}
