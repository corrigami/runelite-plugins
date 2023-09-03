package tictac7x.daily.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.Varbits;
import net.runelite.client.game.ItemManager;
import tictac7x.daily.DailyConfig;
import tictac7x.daily.DailyInfobox;
import tictac7x.daily.TicTac7xDailyPlugin;

import java.util.function.Supplier;

public class PureEssence extends DailyInfobox {
    private final String tooltip = "Collect %d pure essence from Wizard Cromperty at East-Ardougne";

    public PureEssence(final Client client, final DailyConfig config, final ItemManager items, final TicTac7xDailyPlugin plugin) {
        super(DailyConfig.pure_essence, items.getImage(ItemID.PURE_ESSENCE), client, config, plugin);
    }

    @Override
    public boolean isShowing() {
        return (
            config.showPureEssence() &&
            plugin.isCompleted(Varbits.DIARY_ARDOUGNE_EASY) &&
            plugin.isCompleted(Varbits.DIARY_ARDOUGNE_MEDIUM) &&
            !plugin.isCompleted(Varbits.DAILY_ESSENCE_COLLECTED)
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
        final boolean easy   = plugin.isCompleted(Varbits.DIARY_ARDOUGNE_EASY);
        final boolean medium = plugin.isCompleted(Varbits.DIARY_ARDOUGNE_MEDIUM);
        final boolean hard   = plugin.isCompleted(Varbits.DIARY_ARDOUGNE_HARD);
        final boolean elite  = plugin.isCompleted(Varbits.DIARY_ARDOUGNE_ELITE);

        if (easy && medium && hard && elite) return 250;
        if (easy && medium && hard) return 150;
        if (easy && medium) return 100;
        if (easy) return 0;
        return 0;
    }
}
