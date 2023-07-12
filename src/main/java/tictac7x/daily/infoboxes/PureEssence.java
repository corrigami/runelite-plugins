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
    public Supplier<Boolean> getRenderSupplier() {
        return () -> (
            config.showPureEssence() &&
            client.getVarbitValue(Varbits.DIARY_ARDOUGNE_EASY) == 1 &&
            client.getVarbitValue(Varbits.DIARY_ARDOUGNE_MEDIUM) == 1 &&
            client.getVarbitValue(Varbits.DAILY_ESSENCE_COLLECTED) == 0
        );
    }

    @Override
    public Supplier<String> getTextSupplier() {
        return () -> String.valueOf(getPureEssenceAmount());
    }

    @Override
    public Supplier<String> getTooltipSupplier() {
        return () -> String.format(tooltip, getPureEssenceAmount());
    }

    private int getPureEssenceAmount() {
        if (client.getVarbitValue(Varbits.DIARY_ARDOUGNE_MEDIUM) == 1) {
            if (client.getVarbitValue(Varbits.DIARY_ARDOUGNE_HARD) == 1) {
                if (client.getVarbitValue(Varbits.DIARY_ARDOUGNE_ELITE) == 1) {
                    return 250;
                } return 150;
            } return 100;
        } return 0;
    }
}
