package tictac7x.gotr.widgets;

import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.Overlay;
import tictac7x.gotr.TicTac7xGotrImprovedConfig;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnergyWidget extends Overlay {
    private final int GOTR_WIDGET_GROUP = 746;
    private final int GOTR_WIDGET_CHILD = 2;
    private final int GOTR_WIDGET_ELEMENTAL_ENERGY = 21;
    private final int GOTR_WIDGET_CATALYTIC_ENERGY = 24;

    private final Pattern regexElementalEnergy = Pattern.compile("Elemental<br>Energy: (?<energy>.+)");
    private final Pattern regexCatalyticEnergy = Pattern.compile("Catalytic<br>Energy: (?<energy>.+)");

    private final Client client;
    private final TicTac7xGotrImprovedConfig config;

    public EnergyWidget(final Client client, final TicTac7xGotrImprovedConfig config) {
        this.client = client;
        this.config = config;
    }

    @Override
    public Dimension render(final Graphics2D graphics) {
        final Optional<Widget> widgetGotr = Optional.ofNullable(client.getWidget(GOTR_WIDGET_GROUP, GOTR_WIDGET_CHILD));
        if (!widgetGotr.isPresent() || widgetGotr.get().isHidden()) return null;

        final Optional<Widget> widgetElementalEnergy = Optional.ofNullable(client.getWidget(GOTR_WIDGET_GROUP, GOTR_WIDGET_ELEMENTAL_ENERGY));
        final Optional<Widget> widgetCatalyticEnergy = Optional.ofNullable(client.getWidget(GOTR_WIDGET_GROUP, GOTR_WIDGET_CATALYTIC_ENERGY));
        if (!widgetElementalEnergy.isPresent() || !widgetCatalyticEnergy.isPresent()) return null;

        widgetElementalEnergy.get().setHidden(true);
        widgetCatalyticEnergy.get().setHidden(true);

        final Matcher matcherElementalEnergy = regexElementalEnergy.matcher(widgetElementalEnergy.get().getText());
        final Matcher matcherCatalyticEnergy = regexCatalyticEnergy.matcher(widgetCatalyticEnergy.get().getText());
        matcherElementalEnergy.find();
        matcherCatalyticEnergy.find();

        final double elementalEnergy = Integer.parseInt(matcherElementalEnergy.group("energy"));
        final double catalyticEnergy = Integer.parseInt(matcherCatalyticEnergy.group("energy"));

        int x = widgetGotr.get().getRelativeX();
        int y = widgetGotr.get().getRelativeY();

        drawCenteredString(graphics, "Elemental:", new Rectangle(x + 44, y + 47, 0, 0), config.getElementalColor());
        drawCenteredString(graphics, config.getElementalEnergy() + " + " + elementalEnergy / 100, new Rectangle(x + 44, y + 62, 0, 0), config.getElementalColor());

        drawCenteredString(graphics, "Catalytic:", new Rectangle(x + 124, y + 47, 0, 0), config.getCatalyticColor());
        drawCenteredString(graphics, config.getCatalyticEnergy() + " + " + catalyticEnergy / 100, new Rectangle(x + 124, y + 62, 0, 0), config.getCatalyticColor());

        return null;
    }

    public void drawCenteredString(final Graphics2D g, final String text, final Rectangle rect, final Color color) {
        g.setFont(FontManager.getRunescapeFont());
        final FontMetrics metrics = g.getFontMetrics();

        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.setColor(Color.BLACK);
        g.drawString(text, x + 1, y + 1);
        g.setColor(color);
        g.drawString(text, x, y);
    }
}
