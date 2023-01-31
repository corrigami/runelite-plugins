package tictac7x.daily;

import net.runelite.api.Client;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBox;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.function.Supplier;

public abstract class DailyInfobox extends InfoBox {
    protected final Client client;
    protected final DailyConfig config;
    protected final String id;
    protected final BufferedImage image;
    protected final Plugin plugin;

    public DailyInfobox(final Client client, final DailyConfig config, final String id, final BufferedImage image, final Plugin plugin) {
        super(image, plugin);
        this.client = client;
        this.config = config;
        this.id = id;
        this.image = image;
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return super.getName() + "_" + this.id;
    }

    public Supplier<Boolean> getRenderSupplier() {
        return () -> false;
    }

    public Supplier<String> getTextSupplier() {
        return () -> null;
    };

    public Supplier<String> getTooltipSupplier() {
        return () -> null;
    };

    @Override
    public boolean render() {
        return getRenderSupplier().get();
    }

    @Override
    public String getText() {
        return getTextSupplier().get();
    }

    @Override
    public String getTooltip() {
        return getTooltipSupplier().get();
    }

    @Override
    public Color getTextColor() {
        return Color.red;
    }

    public void onConfigChanged(final ConfigChanged event) {}

    public void onGameTick() {}
}
