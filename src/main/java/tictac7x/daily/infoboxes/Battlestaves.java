package tictac7x.daily.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.Varbits;
import tictac7x.daily.DailyConfig;
import tictac7x.daily.DailyInfobox;
import tictac7x.daily.TicTac7xDailyPlugin;
import net.runelite.client.game.ItemManager;

import java.util.function.Supplier;

public class Battlestaves extends DailyInfobox {
    private final String tooltip = "Buy %d battlestaves from Zaff at Varrock for %d,000 coins";

    public Battlestaves(final Client client, final DailyConfig config, final ItemManager items, final TicTac7xDailyPlugin plugin) {
        super(DailyConfig.battlestaves, items.getImage(ItemID.BATTLESTAFF), client, config, plugin);
    }

    @Override
    public boolean isShowing() {
        return (
            config.showBattlestaves() &&
            !plugin.isCompleted(Varbits.DAILY_STAVES_COLLECTED)
        );
    }

    @Override
    public String getText() {
        return String.valueOf(getBattlestavesAmount());
    }

    @Override
    public String getTooltip() {
        return String.format(tooltip, getBattlestavesAmount(), getBattlestavesAmount() * 7);
    }

    private int getBattlestavesAmount() {
        final boolean easy   = plugin.isCompleted(Varbits.DIARY_VARROCK_EASY);
        final boolean medium = plugin.isCompleted(Varbits.DIARY_VARROCK_MEDIUM);
        final boolean hard   = plugin.isCompleted(Varbits.DIARY_VARROCK_HARD);
        final boolean elite  = plugin.isCompleted(Varbits.DIARY_VARROCK_ELITE);

        if (easy && medium && hard && elite) return 120;
        if (easy && medium && hard) return 60;
        if (easy && medium) return 30;
        if (easy) return 15;
        return 5;
    }
}
