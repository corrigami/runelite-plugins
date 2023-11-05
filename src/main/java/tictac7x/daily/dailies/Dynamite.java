package tictac7x.daily.dailies;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.Varbits;
import net.runelite.client.game.ItemManager;
import tictac7x.daily.TicTac7xDailyTasksConfig;
import tictac7x.daily.common.DailyInfobox;
import tictac7x.daily.TicTac7xDailyTasksPlugin;

public class Dynamite extends DailyInfobox {
    private final String tooltip = "Claim %d dynamite from Thirus at Lovakengj";

    public Dynamite(final Client client, final TicTac7xDailyTasksConfig config, final ItemManager itemManager, final TicTac7xDailyTasksPlugin plugin) {
        super(TicTac7xDailyTasksConfig.dynamite, itemManager.getImage(ItemID.DYNAMITE), client, config, plugin);
    }

    @Override
    public boolean isShowing() {
        return (
            config.showDynamite() &&
            isDiaryCompleted(Varbits.DIARY_KOUREND_EASY) &&
            isDiaryCompleted(Varbits.DIARY_KOUREND_MEDIUM) &&
            !isDiaryCompleted(Varbits.DAILY_DYNAMITE_COLLECTED)
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
        final boolean easy   = isDiaryCompleted(Varbits.DIARY_KOUREND_EASY);
        final boolean medium = isDiaryCompleted(Varbits.DIARY_KOUREND_MEDIUM);
        final boolean hard   = isDiaryCompleted(Varbits.DIARY_KOUREND_HARD);
        final boolean elite  = isDiaryCompleted(Varbits.DIARY_KOUREND_ELITE);

        if (easy && medium && hard && elite) return 80;
        if (easy && medium && hard) return 40;
        if (easy && medium) return 20;
        if (easy) return 0;
        return 0;
    }
}
