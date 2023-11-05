package tictac7x.daily.dailies;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.Varbits;
import net.runelite.client.game.ItemManager;
import tictac7x.daily.TicTac7xDailyTasksConfig;
import tictac7x.daily.common.DailyInfobox;
import tictac7x.daily.TicTac7xDailyTasksPlugin;

public class BowStrings extends DailyInfobox {
    private final String tooltip = "Exchange flax to %d bow strings from the Flax Keeper at Seers Village";

    public BowStrings(final Client client, final TicTac7xDailyTasksConfig config, final ItemManager itemManager, final TicTac7xDailyTasksPlugin plugin) {
        super(TicTac7xDailyTasksConfig.bow_strings, itemManager.getImage(ItemID.BOW_STRING), client, config, plugin);
    }

    @Override
    public boolean isShowing() {
        return (
            config.showBowStrings() &&
            isDiaryCompleted(Varbits.DIARY_KANDARIN_EASY) &&
            client.getVarbitValue(Varbits.DAILY_FLAX_STATE) == 0
        );
    }

    @Override
    public String getText() {
        return String.valueOf(getRemainingBowStringsAmount());
    }

    @Override
    public String getTooltip() {
        return String.format(tooltip, getRemainingBowStringsAmount());
    }

    private int getRemainingBowStringsAmount() {
        final boolean easy   = isDiaryCompleted(Varbits.DIARY_KANDARIN_EASY);
        final boolean medium = isDiaryCompleted(Varbits.DIARY_KANDARIN_MEDIUM);
        final boolean hard   = isDiaryCompleted(Varbits.DIARY_KANDARIN_HARD);
        final boolean elite  = isDiaryCompleted(Varbits.DIARY_KANDARIN_ELITE);

        if (easy && medium && hard && elite) return 250;
        if (easy && medium && hard) return 120;
        if (easy && medium) return 60;
        if (easy) return 30;
        return 0;
    }
}
