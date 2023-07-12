package tictac7x.daily.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.Varbits;
import net.runelite.client.game.ItemManager;
import tictac7x.daily.DailyConfig;
import tictac7x.daily.DailyInfobox;
import tictac7x.daily.TicTac7xDailyPlugin;

import java.util.function.Supplier;

public class OgreArrows extends DailyInfobox {
    private final String tooltip = "Collect %d ogre arrows from Rantz near Feldip Hills cave";

    public OgreArrows(final Client client, final DailyConfig config, final ItemManager items, final TicTac7xDailyPlugin plugin) {
        super(DailyConfig.ogre_arrows, items.getImage(ItemID.OGRE_ARROW, 1000, false), client, config, plugin);
    }

    @Override
    public Supplier<Boolean> getRenderSupplier() {
        return () -> (
            config.showOgreArrows() &&
            client.getVarbitValue(Varbits.DIARY_WESTERN_EASY) == 1 &&
            client.getVarbitValue(Varbits.DAILY_ARROWS_STATE) == 0
        );
    }

    @Override
    public Supplier<String> getTextSupplier() {
        return () -> String.valueOf(getOgreArrowsAmount());
    }

    @Override
    public Supplier<String> getTooltipSupplier() {
        return () -> String.format(tooltip, getOgreArrowsAmount());
    }

    private int getOgreArrowsAmount() {
        if (client.getVarbitValue(Varbits.DIARY_WESTERN_EASY) == 1) {
            if (client.getVarbitValue(Varbits.DIARY_WESTERN_MEDIUM) == 1) {
                if (client.getVarbitValue(Varbits.DIARY_WESTERN_HARD) == 1) {
                    if (client.getVarbitValue(Varbits.DIARY_WESTERN_ELITE) == 1) {
                        return 150;
                    } return 100;
                } return 50;
            } return 25;
        } return 0;
    }
}
