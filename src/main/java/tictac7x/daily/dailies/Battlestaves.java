package tictac7x.daily.dailies;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.Varbits;
import tictac7x.daily.TicTac7xDailyTasksConfig;
import tictac7x.daily.common.DailyInfobox;
import tictac7x.daily.TicTac7xDailyTasksPlugin;
import net.runelite.client.game.ItemManager;

public class Battlestaves extends DailyInfobox {
    private final String tooltip = "Buy %d battlestaves from Zaff at Varrock for %d,000 coins";

    public Battlestaves(final Client client, final TicTac7xDailyTasksConfig config, final ItemManager itemManager, final TicTac7xDailyTasksPlugin plugin) {
        super(TicTac7xDailyTasksConfig.battlestaves, itemManager.getImage(ItemID.BATTLESTAFF), client, config, plugin);
    }

    @Override
    public boolean isShowing() {
        return (
            config.showBattlestaves() &&
            !isDiaryCompleted(Varbits.DAILY_STAVES_COLLECTED)
        );
    }

    @Override
    public String getText() {
        return String.valueOf(getRemainingBattlestavesAmount());
    }

    @Override
    public String getTooltip() {
        return String.format(tooltip, getRemainingBattlestavesAmount(), getRemainingBattlestavesAmount() * 7);
    }

    private int getRemainingBattlestavesAmount() {
        final boolean easy   = isDiaryCompleted(Varbits.DIARY_VARROCK_EASY);
        final boolean medium = isDiaryCompleted(Varbits.DIARY_VARROCK_MEDIUM);
        final boolean hard   = isDiaryCompleted(Varbits.DIARY_VARROCK_HARD);
        final boolean elite  = isDiaryCompleted(Varbits.DIARY_VARROCK_ELITE);

        if (easy && medium && hard && elite) return 120;
        if (easy && medium && hard) return 60;
        if (easy && medium) return 30;
        if (easy) return 15;
        return 5;
    }
}
