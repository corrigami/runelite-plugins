package tictac7x.daily.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.Varbits;
import net.runelite.client.game.ItemManager;
import tictac7x.daily.DailyConfig;
import tictac7x.daily.DailyInfobox;
import tictac7x.daily.TicTac7xDailyPlugin;

public class HerbBoxes extends DailyInfobox {
    private final String tooltip = "Buy %d herb boxes from Nightmare Zone rewards shop.";
    private final int herb_boxes_daily = 15;

    public HerbBoxes(final Client client, final DailyConfig config, final ItemManager items, final TicTac7xDailyPlugin plugin) {
        super(DailyConfig.herb_boxes, items.getImage(ItemID.HERB_BOX), client, config, plugin);
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
        return herb_boxes_daily - client.getVarbitValue(Varbits.DAILY_HERB_BOXES_COLLECTED);
    }
}
