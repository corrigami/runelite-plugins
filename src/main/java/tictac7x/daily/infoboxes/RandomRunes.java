package tictac7x.daily.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.Varbits;
import net.runelite.client.game.ItemManager;
import tictac7x.daily.DailyConfig;
import tictac7x.daily.DailyInfobox;
import tictac7x.daily.TicTac7xDailyPlugin;

import java.util.Random;

public class RandomRunes extends DailyInfobox {
    private final String tooltip = "Claim %d random runes from Lundail at Mage Arena bank";

    public RandomRunes(final Client client, final DailyConfig config, final ItemManager items, final TicTac7xDailyPlugin plugin) {
        super(DailyConfig.random_runes, items.getImage(getRandomRuneId()), client, config, plugin);
    }

    static private int getRandomRuneId() {
        final int[] rune_ids = new int[]{
            ItemID.FIRE_RUNE,
            ItemID.WATER_RUNE,
            ItemID.AIR_RUNE,
            ItemID.EARTH_RUNE,
            ItemID.MIND_RUNE,
            ItemID.BODY_RUNE,
            ItemID.NATURE_RUNE,
            ItemID.CHAOS_RUNE,
            ItemID.LAW_RUNE,
            ItemID.COSMIC_RUNE,
            ItemID.DEATH_RUNE
        };
        final int random = new Random().nextInt(rune_ids.length);
        return rune_ids[random];
    }

    @Override
    public boolean isShowing() {
        return (
            config.showRandomRunes() &&
            plugin.isCompleted(Varbits.DIARY_WILDERNESS_EASY) &&
            !plugin.isCompleted(Varbits.DAILY_RUNES_COLLECTED)
        );
    }

    @Override
    public String getText() {
        return String.valueOf(getRandomRunesAmount());
    }

    @Override
    public String getTooltip() {
        return String.format(tooltip, getRandomRunesAmount());
    }

    private int getRandomRunesAmount() {
        final boolean easy   = plugin.isCompleted(Varbits.DIARY_WILDERNESS_EASY);
        final boolean medium = plugin.isCompleted(Varbits.DIARY_WILDERNESS_MEDIUM);
        final boolean hard   = plugin.isCompleted(Varbits.DIARY_WILDERNESS_HARD);
        final boolean elite  = plugin.isCompleted(Varbits.DIARY_WILDERNESS_ELITE);

        if (easy && medium && hard && elite) return 200;
        if (easy && medium && hard) return 120;
        if (easy && medium) return 80;
        if (easy) return 40;
        return 0;
    }
}
