package tictac7x.daily.dailies;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.Varbits;
import net.runelite.client.game.ItemManager;
import tictac7x.daily.TicTac7xDailyTasksConfig;
import tictac7x.daily.common.DailyInfobox;
import tictac7x.daily.TicTac7xDailyTasksPlugin;

public class PureEssence extends DailyInfobox {
    private final String tooltip = "Collect %d pure essence from Wizard Cromperty at East-Ardougne";

    public PureEssence(final Client client, final TicTac7xDailyTasksConfig config, final ItemManager itemManager, final TicTac7xDailyTasksPlugin plugin) {
        super(TicTac7xDailyTasksConfig.pure_essence, itemManager.getImage(ItemID.PURE_ESSENCE), client, config, plugin);
    }

    @Override
    public boolean isShowing() {
        return (
            config.showPureEssence() &&
            isDiaryCompleted(Varbits.DIARY_ARDOUGNE_EASY) &&
            isDiaryCompleted(Varbits.DIARY_ARDOUGNE_MEDIUM) &&
            !isDiaryCompleted(Varbits.DAILY_ESSENCE_COLLECTED)
        );
    }

    @Override
    public String getText() {
        return String.valueOf(getPureEssenceAmount());
    }

    @Override
    public String getTooltip() {
        return String.format(tooltip, getPureEssenceAmount());
    }

    private int getPureEssenceAmount() {
        final boolean easy   = isDiaryCompleted(Varbits.DIARY_ARDOUGNE_EASY);
        final boolean medium = isDiaryCompleted(Varbits.DIARY_ARDOUGNE_MEDIUM);
        final boolean hard   = isDiaryCompleted(Varbits.DIARY_ARDOUGNE_HARD);
        final boolean elite  = isDiaryCompleted(Varbits.DIARY_ARDOUGNE_ELITE);

        if (easy && medium && hard && elite) return 250;
        if (easy && medium && hard) return 150;
        if (easy && medium) return 100;
        if (easy) return 0;
        return 0;
    }
}
