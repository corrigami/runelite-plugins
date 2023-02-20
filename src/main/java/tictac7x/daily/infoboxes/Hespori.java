package tictac7x.daily.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.Varbits;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.game.ItemManager;
import tictac7x.daily.DailyConfig;
import tictac7x.daily.DailyInfobox;
import tictac7x.daily.TicTac7xDailyPlugin;

import java.util.function.Supplier;

public class Hespori extends DailyInfobox {
    private final String tooltip_not_planted = "Hespori seed has not been planted yet";
    private final String tooltip_ready_to_fight = "Hespori is ready to be fought";
    private final String tooltip_ready_to_harvest = "Hespori is ready to be harvested";

    public Hespori(final Client client, final DailyConfig config, final ItemManager items, final TicTac7xDailyPlugin plugin) {
        super(DailyConfig.bow_strings_id, items.getImage(ItemID.HESPORI_SEED), client, config, plugin);
    }

    @Override
    public Supplier<Boolean> getRenderSupplier() {
        return () -> (
            config.showHespori() && (
                hesporiNotPlanted() ||
                hesporiReadyToFight() ||
                hesporiReadyToHarvest()
            )
        );
    }

    @Override
    public Supplier<String> getTooltipSupplier() {
        return () -> hesporiNotPlanted() ? tooltip_not_planted : hesporiReadyToFight() ? tooltip_ready_to_fight : hesporiReadyToHarvest() ? tooltip_ready_to_harvest : null;
    }

    private boolean hesporiNotPlanted() {
        return client.getVarbitValue(Varbits.FARMING_7908) == 3;
    }

    private boolean hesporiReadyToFight() {
        return client.getVarbitValue(Varbits.FARMING_7908) == 7;
    }

    private boolean hesporiReadyToHarvest() {
        return client.getVarbitValue(Varbits.FARMING_7908) == 8;
    }

    @Override
    public void onVarbitChanged(VarbitChanged event) {
        if (event.getVarbitId() != Varbits.FARMING_7908) return;

        if (config.getHesporiPlantedDate() == null) {
        }
    }
}
