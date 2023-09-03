package tictac7x.daily.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.Varbits;
import net.runelite.client.game.ItemManager;
import tictac7x.daily.DailyConfig;
import tictac7x.daily.DailyInfobox;
import tictac7x.daily.TicTac7xDailyPlugin;

import java.util.function.Supplier;

public class BowStrings extends DailyInfobox {
    private final String tooltip = "Exchange flax to %d bow strings from the Flax Keeper at Seers Village";

    public BowStrings(final Client client, final DailyConfig config, final ItemManager items, final TicTac7xDailyPlugin plugin) {
        super(DailyConfig.bow_strings, items.getImage(ItemID.BOW_STRING), client, config, plugin);
    }

    @Override
    public boolean isShowing() {
        return (
            config.showBowStrings() &&
            plugin.isCompleted(Varbits.DIARY_KANDARIN_EASY) &&
            !plugin.isCompleted(Varbits.DAILY_FLAX_STATE)
        );
    }

    @Override
    public String getText() {
        return String.valueOf(getBowStringsAmount());
    }

    @Override
    public String getTooltip() {
        return String.format(tooltip, getBowStringsAmount());
    }

    private int getBowStringsAmount() {
        final boolean easy   = plugin.isCompleted(Varbits.DIARY_KANDARIN_EASY);
        final boolean medium = plugin.isCompleted(Varbits.DIARY_KANDARIN_MEDIUM);
        final boolean hard   = plugin.isCompleted(Varbits.DIARY_KANDARIN_HARD);
        final boolean elite  = plugin.isCompleted(Varbits.DIARY_KANDARIN_ELITE);

        if (easy && medium && hard && elite) return 250;
        if (easy && medium && hard) return 120;
        if (easy && medium) return 60;
        if (easy) return 30;
        return 0;
    }
}
