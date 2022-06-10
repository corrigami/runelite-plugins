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
        super(client, config, DailyConfig.pure_essence_id, items.getImage(ItemID.BOW_STRING), plugin);
    }

    @Override
    public Supplier<Boolean> getRenderSupplier() {
        return () -> (
                config.showBowStrings() &&
                        client.getVarbitValue(Varbits.DIARY_KANDARIN_EASY) == 1 &&
                        client.getVarbitValue(Varbits.DAILY_FLAX_STATE) == 0 &&
                        getFlaxAmount() > 0
        );
    }

    @Override
    public Supplier<String> getTextSupplier() {
        return () -> String.valueOf(getFlaxAmount());
    }

    @Override
    public Supplier<String> getTooltipSupplier() {
        return () -> String.format(tooltip, getFlaxAmount());
    }

    private int getFlaxAmount() {
        int flax;

        if (client.getVarbitValue(Varbits.DIARY_KANDARIN_EASY) == 1) {
            if (client.getVarbitValue(Varbits.DIARY_KANDARIN_MEDIUM) == 1) {
                if (client.getVarbitValue(Varbits.DIARY_KANDARIN_HARD) == 1) {
                    if (client.getVarbitValue(Varbits.DIARY_KANDARIN_ELITE) == 1) {
                        flax = 250;
                    } else flax = 120;
                } else flax = 60;
            } else flax = 30;
        } else flax = 0;
        return flax;
    }
}
