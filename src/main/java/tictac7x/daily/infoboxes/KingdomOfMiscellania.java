package tictac7x.daily.infoboxes;

import com.google.common.collect.ImmutableSet;
import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.Quest;
import net.runelite.api.QuestState;
import net.runelite.api.Varbits;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import tictac7x.daily.DailyConfig;
import tictac7x.daily.DailyInfobox;
import tictac7x.daily.TicTac7xDailyPlugin;

import javax.annotation.Nullable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;
import java.util.function.Supplier;

import static java.time.temporal.ChronoUnit.DAYS;

public class KingdomOfMiscellania extends DailyInfobox {
    private final ConfigManager configs;

    private final String percentage_format = "%.0f%%";
    private final String tooltip = "You need to work harder to increase your kingdom of Miscellania favor: " + percentage_format;

    private final Quest quest_throne_of_miscellania = Quest.THRONE_OF_MISCELLANIA;
    private final Quest quest_royal_trouble = Quest.ROYAL_TROUBLE;

    private final static int FAVOR_MAX = 127;

    private final Set<Integer> regions = ImmutableSet.of(9787, 9788, 9789, 10043, 10044, 10045, 10299, 10300, 10301, 10555, 10556, 10557);

    @Nullable
    private LocalDate date_favor = null;

    @Nullable
    private Double favor_percentage = null;

    public KingdomOfMiscellania(final Client client, final DailyConfig config, final ConfigManager configs, final ItemManager items, final TicTac7xDailyPlugin plugin) {
        super(client, config, DailyConfig.kingdom_of_miscellania_id, items.getImage(ItemID.CASKET), plugin);
        this.configs = configs;

        try { date_favor = LocalDateTime.ofInstant(Instant.parse(config.getKingdomOfMiscellaniaFavorDate()), ZoneOffset.UTC).toLocalDate();
        } catch (final Exception ignored) {}
    }

    @Override
    public Supplier<Boolean> getRenderSupplier() {
        return () -> (
            config.showKingdomOfMiscellaniaFavor() &&
            quest_throne_of_miscellania.getState(client) == QuestState.FINISHED &&
            favor_percentage !=  null && favor_percentage < 100
        );
    }

    @Override
    public Supplier<String> getTextSupplier() {
        return () -> String.format(percentage_format, (favor_percentage != null ? favor_percentage : 0));
    }

    @Override
    public Supplier<String> getTooltipSupplier() {
        return () -> String.format(tooltip, favor_percentage);
    }

    public void onConfigChanged(final ConfigChanged event) {
        // Miscellania Kingdom favor date changed.
        if (event.getGroup().equals(DailyConfig.group) && event.getKey().equals(DailyConfig.kingdom_of_miscellania_favor_date)) {
            try { date_favor = LocalDateTime.ofInstant(Instant.parse(event.getNewValue()), ZoneOffset.UTC).toLocalDate(); }
            catch (final Exception ignored) {}
        }
    }

    public void onGameTick() {
        // Find favor percentage on first game tick.
        if (favor_percentage == null) {
            updateMiscellaniaFavorPercentage();
        }

        final int kingdom_favor_varbit = client.getVarbitValue(Varbits.KINGDOM_APPROVAL);

        // Kingdom favor changed.
        if (kingdom_favor_varbit != config.getKingdomOfMiscellaniaFavor()) {
            configs.setConfiguration(DailyConfig.group, DailyConfig.kingdom_of_miscellania_favor, kingdom_favor_varbit);
            configs.setConfiguration(DailyConfig.group, DailyConfig.kingdom_of_miscellania_favor_date, Instant.now().toString());
        }
    }

    private void updateMiscellaniaFavorPercentage() {
        if (date_favor == null) return;

        final double favor_modifier = quest_royal_trouble.getState(client) == QuestState.FINISHED ? 0.99 : 0.975;

        final LocalDate date_now = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC).toLocalDate();
        final long days = Math.abs(DAYS.between(date_now, date_favor));

        // Round down, percentage can't go below 0%.
        favor_percentage = Math.floor(
            ((double) config.getKingdomOfMiscellaniaFavor() * Math.pow(favor_modifier, days) * 100 / FAVOR_MAX)
        )
        ;
    }
}
