package tictac7x.daily.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.Varbits;
import net.runelite.client.game.ItemManager;
import tictac7x.daily.DailyConfig;
import tictac7x.daily.DailyInfobox;
import tictac7x.daily.TicTac7xDailyPlugin;

public class Dynamite extends DailyInfobox {
    private final String tooltip = "Claim %d dynamite from Thirus at Lovakengj";

    public Dynamite(final Client client, final DailyConfig config, final ItemManager items, final TicTac7xDailyPlugin plugin) {
        super(DailyConfig.dynamite, items.getImage(ItemID.DYNAMITE), client, config, plugin);
    }

    @Override
    public boolean isShowing() {
        return (
            config.showDynamite() &&
            plugin.isCompleted(Varbits.DIARY_KOUREND_EASY) &&
            plugin.isCompleted(Varbits.DIARY_KOUREND_MEDIUM) &&
            !plugin.isCompleted(Varbits.DAILY_DYNAMITE_COLLECTED)
        );
    }

    @Override
    public String getText() {
        return String.valueOf(getDynamiteAmount());
    }

    @Override
    public String getTooltip() {
        return String.format(tooltip, getDynamiteAmount());
    }

    private int getDynamiteAmount() {
        final boolean easy   = plugin.isCompleted(Varbits.DIARY_KOUREND_EASY);
        final boolean medium = plugin.isCompleted(Varbits.DIARY_KOUREND_MEDIUM);
        final boolean hard   = plugin.isCompleted(Varbits.DIARY_KOUREND_HARD);
        final boolean elite  = plugin.isCompleted(Varbits.DIARY_KOUREND_ELITE);

        if (easy && medium && hard && elite) return 80;
        if (easy && medium && hard) return 40;
        if (easy && medium) return 20;
        if (easy) return 0;
        return 0;
    }
}
