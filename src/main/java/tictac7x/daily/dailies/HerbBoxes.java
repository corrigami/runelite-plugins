package tictac7x.daily.dailies;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.Varbits;
import net.runelite.client.game.ItemManager;
import tictac7x.daily.TicTac7xDailyTasksConfig;
import tictac7x.daily.common.DailyInfobox;
import tictac7x.daily.TicTac7xDailyTasksPlugin;

public class HerbBoxes extends DailyInfobox {
    private final int HERB_BOXES_DAILY = 15;
    private final String tooltip = "Buy %d herb boxes from Nightmare Zone rewards shop.";

    public HerbBoxes(final Client client, final TicTac7xDailyTasksConfig config, final ItemManager itemManager, final TicTac7xDailyTasksPlugin plugin) {
        super(TicTac7xDailyTasksConfig.herb_boxes, itemManager.getImage(ItemID.HERB_BOX), client, config, plugin);
    }

    @Override
    public boolean isShowing() {
        return (
            config.showHerbBoxes() &&
            client.getVarbitValue(Varbits.ACCOUNT_TYPE) == 0 && // 0 - REGULAR ACCOUNT
            getHerbBoxesAmount() > 0
        );
    }

    @Override
    public String getText() {
        return String.valueOf(getHerbBoxesAmount());
    }

    @Override
    public String getTooltip() {
        return String.format(tooltip, getHerbBoxesAmount());
    }

    private int getHerbBoxesAmount() {
        return HERB_BOXES_DAILY - client.getVarbitValue(Varbits.DAILY_HERB_BOXES_COLLECTED);
    }
}
