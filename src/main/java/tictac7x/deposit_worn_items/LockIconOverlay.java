package tictac7x.deposit_worn_items;

import net.runelite.api.Client;
import net.runelite.api.SpriteID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.util.ImageUtil;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class LockIconOverlay extends Overlay {
    @Inject
    private Client client;

    @Inject
    private TicTac7xDepositWornItemsConfig config;

    @Inject
    private SpriteManager spriteManager;

    private BufferedImage lockImage = null;

    @Override
    public Dimension render(final Graphics2D graphics2D) {
        if (config.isDepositWornItemsEnabled()) return null;
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPosition(OverlayPosition.DYNAMIC);

        if (lockImage == null) {
            lockImage = spriteManager.getSprite(SpriteID.BANK_PLACEHOLDERS_LOCK, 0);
            lockImage = ImageUtil.resizeImage(lockImage, 12, 14);
            lockImage = ImageUtil.alphaOffset(lockImage, -50);
        }

        @Nullable
        final Widget widgetBankDepositWornItems = client.getWidget(12, 44);

        @Nullable
        final Widget widgetDepositBoxDepositWornItems = client.getWidget(192, 6);

        if (widgetBankDepositWornItems != null) {
            graphics2D.drawImage(
                    lockImage,
                widgetBankDepositWornItems.getCanvasLocation().getX() - 1,
                widgetBankDepositWornItems.getCanvasLocation().getY() - lockImage.getHeight(),
                null
            );
        }

        if (widgetDepositBoxDepositWornItems != null) {
            graphics2D.drawImage(
                    lockImage,
                widgetDepositBoxDepositWornItems.getCanvasLocation().getX() - 1,
                widgetDepositBoxDepositWornItems.getCanvasLocation().getY() - lockImage.getHeight(),
                null
            );
        }

        return null;
    }
}
