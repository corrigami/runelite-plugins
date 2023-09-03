package tictac7x.daily.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.Varbits;
import net.runelite.client.game.ItemManager;
import tictac7x.daily.DailyConfig;
import tictac7x.daily.DailyInfobox;
import tictac7x.daily.TicTac7xDailyPlugin;

public class OgreArrows extends DailyInfobox {
    private final String tooltip = "Collect %d ogre arrows from Rantz near Feldip Hills cave";

    public OgreArrows(final Client client, final DailyConfig config, final ItemManager items, final TicTac7xDailyPlugin plugin) {
        super(DailyConfig.ogre_arrows, items.getImage(ItemID.OGRE_ARROW, 1000, false), client, config, plugin);
    }

    @Override
    public boolean isShowing() {
        return (
            config.showOgreArrows() &&
            plugin.isCompleted(Varbits.DIARY_WESTERN_EASY) &&
            !plugin.isCompleted(Varbits.DAILY_ARROWS_STATE)
        );
    }

    @Override
    public String getText() {
        return String.valueOf(getOgreArrowsAmount());
    }

    @Override
    public String getTooltip() {
        return String.format(tooltip, getOgreArrowsAmount());
    }

    private int getOgreArrowsAmount() {
        final boolean easy   = plugin.isCompleted(Varbits.DIARY_WESTERN_EASY);
        final boolean medium = plugin.isCompleted(Varbits.DIARY_WESTERN_MEDIUM);
        final boolean hard   = plugin.isCompleted(Varbits.DIARY_WESTERN_HARD);
        final boolean elite  = plugin.isCompleted(Varbits.DIARY_WESTERN_ELITE);

        if (easy && medium && hard && elite) return 150;
        if (easy && medium && hard) return 100;
        if (easy && medium) return 50;
        if (easy) return 25;
        return 0;
    }
}
