package tictac7x.daily.dailies;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.Varbits;
import net.runelite.client.game.ItemManager;
import tictac7x.daily.TicTac7xDailyTasksConfig;
import tictac7x.daily.TicTac7xDailyTasksPlugin;
import tictac7x.daily.common.DailyInfobox;

public class ExplorersRingAlchemy extends DailyInfobox {
    private final String tooltip = "You have %d alchemy uses left on your Explorers ring";

    public ExplorersRingAlchemy(final Client client, final TicTac7xDailyTasksConfig config, final ItemManager itemManager, final TicTac7xDailyTasksPlugin plugin) {
        super(TicTac7xDailyTasksConfig.explorers_ring_alchemy, itemManager.getImage(getPlayerExplorerRingId(client)), client, config, plugin);
    }

    static private int getPlayerExplorerRingId(final Client client) {
        final boolean easy   = client.getVarbitValue(Varbits.DIARY_LUMBRIDGE_EASY) == 1;
        final boolean medium = client.getVarbitValue(Varbits.DIARY_LUMBRIDGE_MEDIUM) == 1;
        final boolean hard   = client.getVarbitValue(Varbits.DIARY_LUMBRIDGE_HARD) == 1;
        final boolean elite  = client.getVarbitValue(Varbits.DIARY_LUMBRIDGE_ELITE) == 1;

        if (easy && medium && hard && elite) return ItemID.EXPLORERS_RING_4;
        if (easy && medium && hard) return ItemID.EXPLORERS_RING_3;
        if (easy && medium) return ItemID.EXPLORERS_RING_2;
        return ItemID.EXPLORERS_RING_1;
    }

    @Override
    public boolean isShowing() {
        return (
            config.showExplorersRingAlchemy() &&
            isDiaryCompleted(Varbits.DIARY_LUMBRIDGE_EASY) &&
            getRemainingAlchemyUses() > 0
        );
    }

    @Override
    public String getText() {
        return String.valueOf(getRemainingAlchemyUses());
    }

    @Override
    public String getTooltip() {
        return String.format(tooltip, getRemainingAlchemyUses());
    }

    private int getRemainingAlchemyUses() {
        return 30 - client.getVarbitValue(Varbits.EXPLORER_RING_ALCHS);
    }
}
