package tictac7x.daily.dailies;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.client.game.ItemManager;
import tictac7x.daily.TicTac7xDailyTasksConfig;
import tictac7x.daily.common.DailyInfobox;
import tictac7x.daily.TicTac7xDailyTasksPlugin;

public class ImplingJars extends DailyInfobox {
    private final int VARBIT_IMPLING_JARS_BOUGHT = 11769;
    private final String tooltip = "Buy %d impling jars from Elnock Inquisitor at Puro-Puro";

    public ImplingJars(final Client client, final TicTac7xDailyTasksConfig config, final ItemManager itemManager, final TicTac7xDailyTasksPlugin plugin) {
        super(TicTac7xDailyTasksConfig.impling_jars, itemManager.getImage(ItemID.IMPLING_JAR), client, config, plugin);
    }

    @Override
    public boolean isShowing() {
        return (
            config.showImplingJars() &&
            client.getVarbitValue(VARBIT_IMPLING_JARS_BOUGHT) < 10
        );
    }

    @Override
    public String getText() {
        return String.valueOf(getRemainingImplingJarsAmount());
    }

    @Override
    public String getTooltip() {
        return String.format(tooltip, getRemainingImplingJarsAmount());
    }

    private int getRemainingImplingJarsAmount() {
        return 10 - client.getVarbitValue(VARBIT_IMPLING_JARS_BOUGHT);
    }
}
