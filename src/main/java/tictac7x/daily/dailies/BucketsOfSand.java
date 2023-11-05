package tictac7x.daily.dailies;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.Quest;
import net.runelite.api.QuestState;
import net.runelite.api.Varbits;
import net.runelite.client.game.ItemManager;
import tictac7x.daily.TicTac7xDailyTasksConfig;
import tictac7x.daily.common.DailyInfobox;
import tictac7x.daily.TicTac7xDailyTasksPlugin;

public class BucketsOfSand extends DailyInfobox {
    private final int AMOUNT_BUCKETS_OF_SAND = 84;
    private final String tooltip = "Collect %d buckets of sand from Bert at Yanille";

    public BucketsOfSand(final Client client, final TicTac7xDailyTasksConfig config, final ItemManager itemManager, final TicTac7xDailyTasksPlugin plugin) {
        super(TicTac7xDailyTasksConfig.buckets_of_sand, itemManager.getImage(ItemID.BUCKET_OF_SAND), client, config, plugin);
    }

    @Override
    public boolean isShowing() {
        return (
            config.showBucketsOfSand() &&
            client.getVarbitValue(Varbits.ACCOUNT_TYPE) != 2 && // 2 - ULTIMATE IRONMAN
            Quest.THE_HAND_IN_THE_SAND.getState(client) == QuestState.FINISHED &&
            !isDiaryCompleted(Varbits.DAILY_SAND_COLLECTED)
        );
    }

    @Override
    public String getText() {
        return String.valueOf(AMOUNT_BUCKETS_OF_SAND);
    }

    @Override
    public String getTooltip() {
        return String.format(tooltip, AMOUNT_BUCKETS_OF_SAND);
    }
}
