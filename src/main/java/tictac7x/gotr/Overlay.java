package tictac7x.gotr;

import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.widgets.Widget;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;
import net.runelite.client.util.ImageUtil;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.Instant;

public class Overlay extends net.runelite.client.ui.overlay.Overlay {
    private final Client client;
    private final ModelOutlineRenderer outlines;
    private final SpriteManager sprites;
    private final TicTac7xGotrImprovedConfig config;
    private final Tables tables;
    private final Guardians guardians;
    private final Inventory inventory;
    private final Portal portal;
    private final Energy energy;

    private BufferedImage portal_inactive_image;

    public Overlay(final Client client, final ModelOutlineRenderer outlines, final SpriteManager sprites, final TicTac7xGotrImprovedConfig config, final Tables tables, final Guardians guardians, final Inventory inventory, final Portal portal, final Energy energy) {
        this.client = client;
        this.outlines = outlines;
        this.sprites = sprites;
        this.config = config;
        this.tables = tables;
        this.guardians = guardians;
        this.inventory = inventory;
        this.portal = portal;
        this.energy = energy;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.UNDER_WIDGETS);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        // Essence guardians.
        if (guardians.getGuardiansCurrent() < guardians.getGuardiansTotal()) {
            outlines.drawOutline(guardians.getCatalyticGuardian(), 2, inventory.hasChargedCell() ? config.getCatalyticColor() : Color.RED, 2);
            outlines.drawOutline(guardians.getElementalGuardian(), 2, inventory.hasChargedCell() ? config.getElementalColor() : Color.RED, 2);
        }

        // Uncharged cells table.
        if (!inventory.hasUnchargedCell()) {
            outlines.drawOutline(tables.getUnchargedCellsTable(), 2, Color.RED, 2);
        }

        // Great guardian.
        if (inventory.hasElementalStones() || inventory.hasCatalyticalStones()) {
            outlines.drawOutline(guardians.getGreatGuardian(), 2, inventory.hasElementalStones() ? config.getElementalColor() : config.getCatalyticColor(), 2);
        }

        // Active portal.
        if (portal.getPortal().isPresent() && portal.getPortalTimeLeft().isPresent()) {
            final long seconds = Duration.between(Instant.now(), portal.getPortalTimeLeft().get()).getSeconds();
            final long milliseconds = Duration.between(Instant.now(), portal.getPortalTimeLeft().get()).getNano() / 1_000_000 % 1000 / 100;

            if (milliseconds >= 0) {
                final String time = seconds + "." + milliseconds;
                final Point location =  Perspective.getCanvasTextLocation(client, graphics, portal.getPortal().get().getLocalLocation(), time, 120);
                OverlayUtil.renderTextLocation(graphics, location, time, Color.WHITE);
            }
        }

        final Widget widget_portal_active = client.getWidget(746, 26);

        // Inactive portal countdown.
        if (portal.getTimeToPortal().isPresent() && (widget_portal_active == null || widget_portal_active.isHidden())) {
            if (portal_inactive_image == null) {
                int portal_sprite_id = 4368;
                portal_inactive_image = sprites.getSprite(portal_sprite_id, 0);
                if (portal_inactive_image != null) {
                    portal_inactive_image = ImageUtil.grayscaleImage(portal_inactive_image);
                }
            }

            final long seconds = Math.max(Duration.between(Instant.now(), portal.getTimeToPortal().get()).getSeconds(), 0);
            final String time = seconds / 60 + ":" + String.format("%02d", seconds % 60);

            graphics.drawImage(portal_inactive_image, 194, 90, 32, 32, null);
            final Rectangle rectangle = new Rectangle(194, 120, 32, 32);
            TicTac7xGotrImprovedPlugin.drawCenteredString(graphics, time, rectangle, portal.getTimeToPortalColor(seconds, false));
        }

        // Elemental energy widget.
        final Widget widget_elemental_energy = client.getWidget(746, 21);
        if (widget_elemental_energy != null) {
            widget_elemental_energy.setHidden(true);
            final Rectangle rectangle = new Rectangle(47, 95, 0, 24);
            TicTac7xGotrImprovedPlugin.drawCenteredString(graphics, "Elemental:", rectangle, config.getElementalColor());

            final Rectangle rectangle1 = new Rectangle(47, 112, 0, 24);
            TicTac7xGotrImprovedPlugin.drawCenteredString(graphics, String.valueOf(energy.getElementalEnergy()), rectangle1, config.getElementalColor());
        }

        // Catalytic energy widget.
        final Widget widget_catalytic_energy = client.getWidget(746, 24);
        if (widget_catalytic_energy != null) {
            widget_catalytic_energy.setHidden(true);
            final Rectangle rectangle = new Rectangle(128, 95, 0, 24);
            TicTac7xGotrImprovedPlugin.drawCenteredString(graphics, "Catalytic:", rectangle, config.getCatalyticColor());

            final Rectangle rectangle1 = new Rectangle(128, 112, 0, 24);
            TicTac7xGotrImprovedPlugin.drawCenteredString(graphics, String.valueOf(energy.getCatalyticEnergy()), rectangle1, config.getCatalyticColor());
        }

        return null;
    }


}
