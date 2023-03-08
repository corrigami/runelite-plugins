package tictac7x.balloon;

import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBox;

import java.awt.Color;

public class BalloonInfoBox extends InfoBox {
    private final ConfigManager configs;
    private final BalloonConfig config;
    private final Balloon balloon;

    private final String logs_id;
    private final String tooltip;

    public BalloonInfoBox(final int item_id, final String logs_id, final String tooltip, final ConfigManager configs, final BalloonConfig config, final ItemManager items, final Balloon balloon, final Plugin plugin) {
        super(items.getImage(item_id), plugin);
        this.configs = configs;
        this.config = config;
        this.balloon = balloon;

        this.logs_id = logs_id;
        this.tooltip = tooltip;
    }

    @Override
    public String getName() {
        return super.getName() + "_" + this.logs_id;
    }

    @Override
    public String getTooltip() {
        return this.tooltip;
    }

    @Override
    public String getText() {
        return String.valueOf(this.getCount());
    }

    @Override
    public Color getTextColor() {
        return this.getCount() > 0 ? Color.lightGray : Color.red;
    }

    @Override
    public boolean render() {
        return
            config.show() == BalloonConfig.Show.ALL_THE_TIME ||
            config.show() == BalloonConfig.Show.NEAR_THE_BALLOON && balloon.isVisible();
    }

    private int getCount() {
        return Integer.parseInt(configs.getConfiguration(BalloonConfig.group, this.logs_id));
    }
}
