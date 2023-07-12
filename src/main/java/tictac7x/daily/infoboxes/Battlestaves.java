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
    public Supplier<Boolean> getRenderSupplier() {
        return () -> (
            config.showBattlestaves() &&
            client.getVarbitValue(Varbits.DAILY_STAVES_COLLECTED) == 0
        );
    }

    @Override
    public Supplier<String> getTextSupplier() {
        return () -> String.valueOf(getBattlestavesAmount());
    }

    @Override
    public Supplier<String> getTooltipSupplier() {
        return () -> String.format(tooltip, getBattlestavesAmount(), getBattlestavesAmount() * 7);
    }

    private int getBattlestavesAmount() {
        if (client.getVarbitValue(Varbits.DIARY_VARROCK_EASY) == 1) {
            if (client.getVarbitValue(Varbits.DIARY_VARROCK_MEDIUM) == 1) {
                if (client.getVarbitValue(Varbits.DIARY_VARROCK_HARD) == 1) {
                    if (client.getVarbitValue(Varbits.DIARY_VARROCK_ELITE) == 1) {
                        return 120;
                    } else return 60;
                } else return 30;
            } else return 15;
        } else return 5;
    }
}
