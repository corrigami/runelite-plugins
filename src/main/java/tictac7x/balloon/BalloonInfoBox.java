package tictac7x.balloon;

import net.runelite.client.config.ConfigManager;
import net.runelite.client.events.ConfigChanged;
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
    private boolean render_recently = false;

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
            config.show() == BalloonConfig.Show.NEAR_THE_BALLOON && balloon.isVisible() ||
            config.show() == BalloonConfig.Show.RECENTLY_USED && this.render_recently;
    }

    private int getCount() {
        return Integer.parseInt(configs.getConfiguration(BalloonConfig.group, this.logs_id));
    }

    // Logs count changed and infoboxes are shown based on recently used.
    public void onConfigChanged(final ConfigChanged event) {
        if (
            event.getGroup().equals(BalloonConfig.group) &&
            event.getKey().equals(this.logs_id) &&
            config.show() == BalloonConfig.Show.RECENTLY_USED
        ) {
            // Start showing infobox.
            this.render_recently = true;

            new Thread(() -> {
                try {
                    // Hide the infobox after specified time.
                    Thread.sleep(60L * config.showRecentlyUsedForMinutes() * 1000);
                    this.render_recently = false;
                } catch (final Exception ignored) {}
            }).start();
        }
    }
}
