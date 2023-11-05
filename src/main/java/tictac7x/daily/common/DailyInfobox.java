package tictac7x.daily.common;

import net.runelite.api.Client;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.ui.overlay.infobox.InfoBox;
import tictac7x.daily.TicTac7xDailyTasksConfig;
import tictac7x.daily.TicTac7xDailyTasksPlugin;

import java.awt.Color;
import java.awt.image.BufferedImage;

public abstract class DailyInfobox extends InfoBox {
    protected final String id;
    protected final Client client;
    protected final TicTac7xDailyTasksConfig config;
    protected final TicTac7xDailyTasksPlugin plugin;

    public DailyInfobox(final String id, final BufferedImage image, final Client client, final TicTac7xDailyTasksConfig config, final TicTac7xDailyTasksPlugin plugin) {
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

    public void onVarbitChanged(final VarbitChanged event) {}

    public boolean isDiaryCompleted(final int diary) {
        return client.getVarbitValue(diary) == 1;
    }
}
