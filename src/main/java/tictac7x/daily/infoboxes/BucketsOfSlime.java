package tictac7x.daily.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.Varbits;
import net.runelite.client.game.ItemManager;
import tictac7x.daily.DailyConfig;
import tictac7x.daily.DailyInfobox;
import tictac7x.daily.TicTac7xDailyPlugin;

import java.util.function.Supplier;

public class BucketsOfSlime extends DailyInfobox {
    private final String tooltip = "Exchange %d bones to buckets of slime and bonemeal from Robin at Porty Phasmatys";

    public BucketsOfSlime(final Client client, final DailyConfig config, final ItemManager items, final TicTac7xDailyPlugin plugin) {
        super(DailyConfig.buckets_of_slime, items.getImage(ItemID.BUCKET_OF_SLIME), client, config, plugin);
    }

    @Override
    public Supplier<Boolean> getRenderSupplier() {
        return () -> (
            config.showBucketsOfSlime() &&
            client.getVarbitValue(Varbits.DIARY_MORYTANIA_EASY) == 1 &&
            client.getVarbitValue(Varbits.DIARY_MORYTANIA_MEDIUM) == 1 &&
            getBucketsOfSlimeAmount() > 0
        );
    }

    @Override
    public Supplier<String> getTextSupplier() {
        return () -> String.valueOf(getBucketsOfSlimeAmount());
    }

    @Override
    public Supplier<String> getTooltipSupplier() {
        return () -> String.format(tooltip, getBucketsOfSlimeAmount());
    }

    private int getBucketsOfSlimeAmount() {
        final int buckets_of_slime;

        if (client.getVarbitValue(Varbits.DIARY_MORYTANIA_MEDIUM) == 1) {
            if (client.getVarbitValue(Varbits.DIARY_MORYTANIA_HARD) == 1) {
                if (client.getVarbitValue(Varbits.DIARY_MORYTANIA_ELITE) == 1) {
                    buckets_of_slime = 39;
                } else buckets_of_slime = 26;
            } else buckets_of_slime = 13;
        } else buckets_of_slime = 0;

        return buckets_of_slime - client.getVarbitValue(Varbits.DAILY_BONEMEAL_STATE);
    }
}
