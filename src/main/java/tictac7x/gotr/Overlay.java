package tictac7x.gotr;

import net.runelite.api.Client;
import net.runelite.api.DynamicObject;
import net.runelite.api.GameObject;
import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.widgets.Widget;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;
import net.runelite.client.util.ImageUtil;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.Instant;

public class Overlay extends net.runelite.client.ui.overlay.Overlay {
    private final Client client;
    private final ModelOutlineRenderer outlines;
    private final SpriteManager sprites;
    private final ItemManager items;
    private final TicTac7xGotrImprovedConfig config;
    private final Tables tables;
    private final Guardians guardians;
    private final Inventory inventory;
    private final Portal portal;
    private final Teleporters teleporters;
    private final Energy energy;
    private final Timer timer;

    private BufferedImage portal_inactive_image;

    public Overlay(
            final Client client,
            final ModelOutlineRenderer outlines,
            final SpriteManager sprites,
            final ItemManager items,
            final TicTac7xGotrImprovedConfig config,
            final Tables tables,
            final Guardians guardians,
            final Inventory inventory,
            final Portal portal,
            final Teleporters teleporters,
            final Energy energy,
            final Timer timer
    ) {
        this.client = client;
        this.outlines = outlines;
        this.sprites = sprites;
        this.items = items;
        this.config = config;
        this.tables = tables;
        this.guardians = guardians;
        this.inventory = inventory;
        this.portal = portal;
        this.teleporters = teleporters;
        this.energy = energy;
        this.timer = timer;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.UNDER_WIDGETS);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        final Widget widget_gotr = client.getWidget(746, 4);

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
        if (portal.getTimeToPortal().isPresent() && (widget_portal_active == null || widget_portal_active.isHidden()) && widget_gotr != null && !widget_gotr.isHidden()) {
            if (portal_inactive_image == null) {
                int portal_sprite_id = 4368;
                portal_inactive_image = sprites.getSprite(portal_sprite_id, 0);
                if (portal_inactive_image != null) {
                    portal_inactive_image = ImageUtil.grayscaleImage(portal_inactive_image);
                }
            }

            final long seconds = Math.max(Duration.between(portal.getTimeToPortal().get(), Instant.now()).getSeconds(), 0);

            graphics.drawImage(portal_inactive_image, 194, 90, 32, 32, null);
            final Rectangle rectangle = new Rectangle(194, 120, 32, 32);
            drawCenteredString(graphics, seconds + "s", rectangle, config.showWidgetPortalCountdownColors() ? portal.getTimeToPortalColor(seconds) : Color.WHITE, FontManager.getRunescapeFont());
        }

        // Elemental energy widget.
        final Widget widget_elemental_energy = client.getWidget(746, 21);
        if (widget_elemental_energy != null) {
            widget_elemental_energy.setHidden(true);
            final Rectangle rectangle = new Rectangle(47, 95, 0, 24);
            drawCenteredString(graphics, "Elemental:", rectangle, config.showWidgetEnergyPointColors() ? config.getElementalColor() : Color.WHITE, FontManager.getRunescapeFont());

            final Rectangle rectangle1 = new Rectangle(47, 112, 0, 24);
            drawCenteredString(graphics, energy.getElementalEnergy(), rectangle1, config.showWidgetEnergyPointColors() ? config.getElementalColor() : Color.WHITE, FontManager.getRunescapeFont());
        }

        // Catalytic energy widget.
        final Widget widget_catalytic_energy = client.getWidget(746, 24);
        if (widget_catalytic_energy != null) {
            widget_catalytic_energy.setHidden(true);
            final Rectangle rectangle = new Rectangle(128, 95, 0, 24);
            drawCenteredString(graphics, "Catalytic:", rectangle, config.showWidgetEnergyPointColors() ? config.getCatalyticColor() : Color.WHITE, FontManager.getRunescapeFont());

            final Rectangle rectangle1 = new Rectangle(128, 112, 0, 24);
            drawCenteredString(graphics, energy.getCatalyticEnergy(), rectangle1, config.showWidgetEnergyPointColors() ? config.getCatalyticColor() : Color.WHITE, FontManager.getRunescapeFont());
        }

        // Time to start.
        if (timer.getTime().isPresent() && (widget_gotr == null || widget_gotr.isHidden())) {
            final long seconds = Duration.between(Instant.now(), timer.getTime().get()).getSeconds();

            if (seconds > 0) {
                final Rectangle rectangle = new Rectangle(88, 50, 0, 24);
                drawCenteredString(graphics, "Game starting in:", rectangle, Color.WHITE, FontManager.getRunescapeFont());

                final String time = seconds / 60 + ":" + String.format("%02d", seconds % 60);
                final Rectangle rectangle1 = new Rectangle(88, 67, 0, 24);
                drawCenteredString(graphics, time, rectangle1, Color.WHITE, FontManager.getRunescapeFont());
            }
        }

        // Teleporters.
        if (teleporters.getTeleportersTimeLeft().isPresent()) {

            for (final GameObject teleporter : teleporters.getTeleporters()) {
                final int id = teleporter.getId();

                if (
                    ((DynamicObject) (teleporter.getRenderable())).getAnimation().getId() == 9363
                    || id == ObjectID.GUARDIAN_OF_AIR    && inventory.hasAirTalisman()
                    || id == ObjectID.GUARDIAN_OF_MIND   && inventory.hasMindTalisman()
                    || id == ObjectID.GUARDIAN_OF_WATER  && inventory.hasWaterTalisman()
                    || id == ObjectID.GUARDIAN_OF_EARTH  && inventory.hasEarthTalismanalisman()
                    || id == ObjectID.GUARDIAN_OF_FIRE   && inventory.hasFireTalisman()
                    || id == ObjectID.GUARDIAN_OF_BODY   && inventory.hasBodyTalisman()
                    || id == ObjectID.GUARDIAN_OF_COSMIC && inventory.hasCosmicTalisman()
                    || id == ObjectID.GUARDIAN_OF_CHAOS  && inventory.hasChaosTalisman()
                    || id == ObjectID.GUARDIAN_OF_NATURE && inventory.hasNatureTalisman()
                    || id == ObjectID.GUARDIAN_OF_LAW    && inventory.hasLawTalisman()
                    || id == ObjectID.GUARDIAN_OF_DEATH  && inventory.hasDeathTalisman()
                    || id == ObjectID.GUARDIAN_OF_BLOOD  && inventory.hasBloodTalisman()
                ) {
                    outlines.drawOutline(teleporter, 2, teleporters.isElementalTeleporter(teleporter.getId()) ? config.getElementalColor() : config.getCatalyticColor(), 2);

                    final BufferedImage rune_image = items.getImage(
                        id == ObjectID.GUARDIAN_OF_AIR ? ItemID.AIR_RUNE :
                        id == ObjectID.GUARDIAN_OF_MIND ? ItemID.MIND_RUNE :
                        id == ObjectID.GUARDIAN_OF_WATER ? ItemID.WATER_RUNE :
                        id == ObjectID.GUARDIAN_OF_EARTH ? ItemID.EARTH_RUNE :
                        id == ObjectID.GUARDIAN_OF_FIRE ? ItemID.FIRE_RUNE :
                        id == ObjectID.GUARDIAN_OF_BODY ? ItemID.BODY_RUNE :
                        id == ObjectID.GUARDIAN_OF_COSMIC ? ItemID.COSMIC_RUNE :
                        id == ObjectID.GUARDIAN_OF_CHAOS ? ItemID.CHAOS_RUNE :
                        id == ObjectID.GUARDIAN_OF_NATURE ? ItemID.NATURE_RUNE :
                        id == ObjectID.GUARDIAN_OF_LAW ? ItemID.LAW_RUNE :
                        id == ObjectID.GUARDIAN_OF_DEATH ? ItemID.DEATH_RUNE :
                        ItemID.BLOOD_RUNE
                    );
                    OverlayUtil.renderImageLocation(client, graphics, teleporter.getLocalLocation(), rune_image, 500);

                    final long seconds = Duration.between(Instant.now(), teleporters.getTeleportersTimeLeft().get()).getSeconds();

                    if (seconds >= 0) {
                        final long milliseconds = Duration.between(Instant.now(), teleporters.getTeleportersTimeLeft().get()).getNano() / 1_000_000 % 1000 / 100;
                        final String time = seconds % 60 + "." + milliseconds;

                        graphics.setFont(FontManager.getRunescapeSmallFont());
                        final Point rune_location = Perspective.getCanvasImageLocation(client, teleporter.getLocalLocation(), rune_image, 500);
                        final Rectangle rectangle = new Rectangle(rune_location.getX() + 16, rune_location.getY() - 18, 0, 24);

                        drawCenteredString(graphics, time, rectangle, Color.WHITE, FontManager.getRunescapeSmallFont());
                    }
                }
            }
        }

        return null;
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
