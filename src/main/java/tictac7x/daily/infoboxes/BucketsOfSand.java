package tictac7x.daily.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.Quest;
import net.runelite.api.QuestState;
import net.runelite.api.Varbits;
import net.runelite.api.vars.AccountType;
import net.runelite.client.game.ItemManager;
import tictac7x.daily.DailyConfig;
import tictac7x.daily.DailyInfobox;
import tictac7x.daily.TicTac7xDailyPlugin;

import java.util.function.Supplier;

public class BucketsOfSand extends DailyInfobox {
    private final String tooltip = "Collect %d buckets of sand from Bert at Yanille";
    private final Quest quest_the_hand_in_the_sand = Quest.THE_HAND_IN_THE_SAND;
    private final int amount_buckets_of_sand = 84;

    public BucketsOfSand(final Client client, final DailyConfig config, final ItemManager items, final TicTac7xDailyPlugin plugin) {
        super(DailyConfig.buckets_of_sand, items.getImage(ItemID.BUCKET_OF_SAND), client, config, plugin);
    }

    @Override
    public Supplier<Boolean> getRenderSupplier() {
        return () -> (
            config.showBucketsOfSand() &&
            client.getAccountType() != AccountType.ULTIMATE_IRONMAN &&
            quest_the_hand_in_the_sand.getState(client) == QuestState.FINISHED &&
            client.getVarbitValue(Varbits.DAILY_SAND_COLLECTED) == 0
        );
    }

    @Override
    public Supplier<String> getTextSupplier() {
        return () -> String.valueOf(amount_buckets_of_sand);
    }

    @Override
    public Supplier<String> getTooltipSupplier() {
        return () -> String.format(tooltip, amount_buckets_of_sand);
    }
}
