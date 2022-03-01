package tictac7x;

import java.awt.Color;
import java.util.function.Supplier;
import java.awt.image.BufferedImage;
import net.runelite.client.plugins.Plugin;

public class InfoBox extends net.runelite.client.ui.overlay.infobox.InfoBox {
    private final String id;
    private final Supplier<Boolean> supplier_render;
    private final Supplier<String> supplier_text;
    private final Supplier<String> supplier_tooltip;
    private final Supplier<Color> supplier_color;

    public InfoBox(final String id, final BufferedImage image, final Supplier<Boolean> supplier_render, final Supplier<String> supplier_text, final Supplier<String> supplier_tooltip, final Supplier<Color> supplier_color, final Plugin plugin) {
        super(image, plugin);
        this.id = id;
        this.supplier_render = supplier_render;
        this.supplier_text = supplier_text;
        this.supplier_tooltip = supplier_tooltip;
        this.supplier_color = supplier_color;
    }

    @Override
    public String getName() {
        return super.getName() + "_" + id;
    }

    @Override
    public boolean render() {
        return supplier_render.get();
    }

    @Override
    public String getText() {
        return supplier_text.get();
    }

    @Override
    public Color getTextColor() {
        return supplier_color.get();
    }

    @Override
    public String getTooltip() {
        return supplier_tooltip.get();
    }
}
