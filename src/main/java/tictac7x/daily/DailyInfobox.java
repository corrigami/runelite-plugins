package tictac7x.daily;

import net.runelite.api.Client;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.ui.overlay.infobox.InfoBox;

import java.awt.Color;
import java.awt.image.BufferedImage;

public abstract class DailyInfobox extends InfoBox {
    protected final String id;
    protected final Client client;
    protected final DailyConfig config;
    protected final TicTac7xDailyPlugin plugin;

    public DailyInfobox(final String id, final BufferedImage image, final Client client, final DailyConfig config, final TicTac7xDailyPlugin plugin) {
        super(image, plugin);
        this.id = id;
        this.client = client;
        this.config = config;
        this.plugin = plugin;
    }

    abstract public boolean isShowing();

    abstract public String getText();

    abstract public String getTooltip();

    @Override
    public String getName() {
        return super.getName() + "_" + this.id;
    }

    @Override
    public boolean render() {
        return isShowing();
    }

    @Override
    public Color getTextColor() {
        return Color.red;
    }

    protected void onConfigChanged(final ConfigChanged ignored) {}

    protected void onGameTick() {}

    protected void onVarbitChanged(final VarbitChanged event) {}
}
