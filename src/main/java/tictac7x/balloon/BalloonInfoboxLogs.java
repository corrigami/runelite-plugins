package tictac7x.balloon;

import tictac7x.InfoBox;
import tictac7x.Overlay;
import java.util.function.Supplier;
import java.awt.image.BufferedImage;

public class BalloonInfoboxLogs extends InfoBox {
    public BalloonInfoboxLogs(final String id, final BufferedImage image, final Supplier<Boolean> supplier_render, final Supplier<Integer> supplier_logs, final String tooltip, final TicTac7xBalloonPlugin plugin) {
        super(
            id,
            image,
            supplier_render,
            () -> String.valueOf(supplier_logs.get()),
            () -> tooltip,
            () -> supplier_logs.get() == 0 ? Overlay.color_red : null,
            plugin
        );
    }
}
