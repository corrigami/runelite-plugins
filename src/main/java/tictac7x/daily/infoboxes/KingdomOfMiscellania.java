package tictac7x.daily.infoboxes;

import com.google.common.collect.ImmutableSet;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.ItemID;
import net.runelite.api.Quest;
import net.runelite.api.QuestState;
import net.runelite.api.Varbits;
import net.runelite.api.events.VarbitChanged;
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
    private final ClientThread client_thread;
    private final ConfigManager configs;

    private final String percentage_format = "%.0f%%";
    private final String tooltip = "You need to work harder to increase your kingdom of Miscellania favor: " + percentage_format;

    private final Quest quest_throne_of_miscellania = Quest.THRONE_OF_MISCELLANIA;
    private final Quest quest_royal_trouble = Quest.ROYAL_TROUBLE;

    private final static int FAVOR_MAX = 127;
    private double favor_percentage = 0;

    private final Set<Integer> regions = ImmutableSet.of(9787, 9788, 9789, 10043, 10044, 10045, 10299, 10300, 10301, 10555, 10556, 10557);

    @Nullable
    private Instant favor_date = null;

    public KingdomOfMiscellania(final Client client, final ClientThread client_thread, final DailyConfig config, final ConfigManager configs, final ItemManager items, final TicTac7xDailyPlugin plugin) {
        super(client, config, DailyConfig.kingdom_of_miscellania_id, items.getImage(ItemID.CASKET), plugin);
        this.client_thread = client_thread;
        this.configs = configs;

        try {
            favor_date = Instant.parse(config.getKingdomOfMiscellaniaFavorDate());
            updateMiscellaniaFavorPercentage();
        }
        catch (final Exception ignored) {}
    }

    @Override
    public Supplier<Boolean> getRenderSupplier() {
        return () -> (
            config.showMiscellania() &&
            quest_throne_of_miscellania.getState(client) == QuestState.FINISHED &&
            favor_percentage < 100
        );
    }

    @Override
    public Supplier<String> getTextSupplier() {
        return () -> String.format(percentage_format, favor_percentage);
    }

    @Override
    public Supplier<String> getTooltipSupplier() {
        return () -> String.format(tooltip, favor_percentage);
    }

    public void onVarbitChanged() {
        client_thread.invokeLater(() -> {
            // Miscellania Kingdom favor varbit updated.
            if (
                getMiscellaniaFavorVarbit() != config.getKingdomOfMiscellaniaFavor() &&
                client.getGameState() != GameState.LOGGING_IN &&
                inRegion()
            ) {
                configs.setConfiguration(DailyConfig.group, DailyConfig.kingdom_of_miscellania_favor, getMiscellaniaFavorVarbit());
                configs.setConfiguration(DailyConfig.group, DailyConfig.kingdom_of_miscellania_favor_date, Instant.now().toString());
            }
        });
    }

    public void onConfigChanged(final ConfigChanged event) {
        if (event.getGroup().equals(DailyConfig.group)) {
            // Miscellania Kingdom favor date changed.
            if (event.getKey().equals(DailyConfig.kingdom_of_miscellania_favor_date)) {
                try {favor_date = Instant.parse(event.getNewValue()); }
                catch (final Exception ignored) {}
            }

            if (
                event.getKey().equals(DailyConfig.kingdom_of_miscellania_favor) ||
                event.getKey().equals(DailyConfig.kingdom_of_miscellania_favor_date)
            ) {
                updateMiscellaniaFavorPercentage();
            }
        }
    }

    private void updateMiscellaniaFavorPercentage() {
        if (favor_date == null) return;

        client_thread.invokeLater(() -> {
            final double favor_modifier = quest_royal_trouble.getState(client) == QuestState.FINISHED ? 1.0 : 2.5;

            final Instant instant_now = Instant.now();
            final Instant instant_favor = favor_date;

            final LocalDate date_now = LocalDateTime.ofInstant(instant_now, ZoneOffset.UTC).toLocalDate();
            final LocalDate date_favor = LocalDateTime.ofInstant(instant_favor, ZoneOffset.UTC).toLocalDate();
            final long days = Math.abs(DAYS.between(date_now, date_favor));

            // Round down, percentage can't go below 0%.
            favor_percentage = Math.max(
                // Calculated favor.
                Math.floor(
                    ((double) config.getKingdomOfMiscellaniaFavor() * 100 / FAVOR_MAX) - days * favor_modifier
                ),

                0 // Min value.
            );
        });
    }

    private int getMiscellaniaFavorVarbit() {
        return client.getVarbitValue(Varbits.KINGDOM_APPROVAL);
    }

    private boolean inRegion() {
        for (final int region : client.getMapRegions()) {
            if (regions.contains(region)) {
                return true;
            }
        }

        return false;
    }
}
