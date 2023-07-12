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
    public Supplier<Boolean> getRenderSupplier() {
        return () -> (
            config.showBowStrings() &&
            client.getVarbitValue(Varbits.DIARY_KANDARIN_EASY) == 1 &&
            client.getVarbitValue(Varbits.DAILY_FLAX_STATE) == 0
        );
    }

    @Override
    public Supplier<String> getTextSupplier() {
        return () -> String.valueOf(getBowStringsAmount());
    }

    @Override
    public Supplier<String> getTooltipSupplier() {
        return () -> String.format(tooltip, getBowStringsAmount());
    }

    private int getBowStringsAmount() {
        if (client.getVarbitValue(Varbits.DIARY_KANDARIN_EASY) == 1) {
            if (client.getVarbitValue(Varbits.DIARY_KANDARIN_MEDIUM) == 1) {
                if (client.getVarbitValue(Varbits.DIARY_KANDARIN_HARD) == 1) {
                    if (client.getVarbitValue(Varbits.DIARY_KANDARIN_ELITE) == 1) {
                        return 250;
                    } return 120;
                } return 60;
            } return 30;
        } return 0;
    }
}
