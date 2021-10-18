package tictac7x.balloon;

import java.util.function.Supplier;
import java.awt.Color;
import java.awt.image.BufferedImage;
import net.runelite.client.ui.overlay.infobox.InfoBox;

public class BalloonInfoboxLogs extends InfoBox {
    private final String name;
    private final Supplier<Boolean> render_check;
    private final Supplier<Integer> logs_count;
    private final String tooltip;

    public BalloonInfoboxLogs(final String name, final BufferedImage image, final Supplier<Boolean> render_check, final Supplier<Integer> logs_count, final String tooltip, final BalloonPlugin plugin) {
        super(image, plugin);
        this.name = name;
        this.render_check = render_check;
        this.logs_count = logs_count;
        this.tooltip = tooltip;
    }

    @Override
    public String getName() {
        return super.getName() + "_" + name;
    }

    @Override
    public boolean render() {
        return render_check.get();
    }

    @Override
    public String getText() {
        return String.valueOf(logs_count.get());
    }

    @Override
    public Color getTextColor() {
        return logs_count.get() == 0 ? Color.red : null;
    }

    @Override
    public String getTooltip() {
        return tooltip;
    }
}
