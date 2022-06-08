package tictac7x.daily;

import net.runelite.api.Client;
import tictac7x.InfoBox;
import tictac7x.Overlay;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.function.Supplier;

public abstract class DailyInfobox extends InfoBox {
    protected final Client client;
    protected final DailyConfig config;
    protected final String id;
    protected final BufferedImage image;
    protected final TicTac7xDailyPlugin plugin;

    public DailyInfobox(final Client client, final DailyConfig config, final String id, final BufferedImage image, final TicTac7xDailyPlugin plugin) {
        super(id, image, null, null, null, null, plugin);
        this.client = client;
        this.config = config;
        this.id = id;
        this.image = image;
        this.plugin = plugin;
    }

    public abstract Supplier<Boolean> getRenderSupplier();

    public abstract Supplier<String> getTextSupplier();

    public abstract Supplier<String> getTooltipSupplier();

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
        return Overlay.color_red;
    }
}
