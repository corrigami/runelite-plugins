package tictac7x.daily.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.Quest;
import net.runelite.api.QuestState;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import tictac7x.daily.DailyConfig;
import tictac7x.daily.DailyInfobox;
import tictac7x.daily.TicTac7xDailyPlugin;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.function.Supplier;

public class KingdomOfMiscellania extends DailyInfobox {
    private final ConfigManager configs;

    private final String percentage_format = "%d%%";
    private final String tooltip = "You need to work harder to increase your kingdom of Miscellania favor: " + percentage_format;

    private final Quest quest_throne_of_miscellania = Quest.THRONE_OF_MISCELLANIA;
    private final Quest quest_royal_trouble = Quest.ROYAL_TROUBLE;
    private final ZoneId timezone = ZoneId.of("Europe/London");
    
    private final static int VARBIT_KINGDOM_APPROVAL = 72;
    private final static int FAVOR_MAX = 127;
    private final static double FAVOR_LOST_MODIFIER_WITHOUT_ROYAL_TROUBLE = 0.975;
    private final static double FAVOR_LOST_MODIFIER_WITH_ROYAL_TROUBLE = 0.99;

    public KingdomOfMiscellania(final Client client, final DailyConfig config, final ConfigManager configs, final ItemManager items, final TicTac7xDailyPlugin plugin) {
        super(DailyConfig.kingdom_of_miscellania, items.getImage(ItemID.CASKET), client, config, plugin);
        this.configs = configs;
    }

    @Override
    public Supplier<Boolean> getRenderSupplier() {
        return () -> (
            quest_throne_of_miscellania.getState(client) == QuestState.FINISHED &&
            getFavorPercentage() <= config.showKingdomOfMiscellaniaFavor()
        );
    }

    @Override
    public Supplier<String> getTextSupplier() {
        return () -> String.format(percentage_format, getFavorPercentage());
    }

    @Override
    public Supplier<String> getTooltipSupplier() {
        return () -> String.format(tooltip, getFavorPercentage());
    }

    @Override
    public void onVarbitChanged(final VarbitChanged event) {
        if (event.getVarbitId() == VARBIT_KINGDOM_APPROVAL) {
            configs.setConfiguration(DailyConfig.group, DailyConfig.kingdom_of_miscellania_favor_date, LocalDateTime.now(timezone).format(DateTimeFormatter.ISO_LOCAL_DATE));
            configs.setConfiguration(DailyConfig.group, DailyConfig.kingdom_of_miscellania_favor, event.getValue());
        }
    }

    private int getFavorPercentage() {
        try {
            final LocalDate now = LocalDate.now(timezone);
            final LocalDate visited = LocalDate.parse(config.getKingdomOfMiscellaniaFavorDate());
            final long days = Math.abs(ChronoUnit.DAYS.between(now, visited));

            int percentage = config.getKingdomOfMiscellaniaFavor() * 100 / FAVOR_MAX;
            for (int i = 0; i < days; i++) {
                percentage = (int) Math.ceil(percentage * (quest_royal_trouble.getState(client) == QuestState.FINISHED ? FAVOR_LOST_MODIFIER_WITH_ROYAL_TROUBLE : FAVOR_LOST_MODIFIER_WITHOUT_ROYAL_TROUBLE));
            }

            return percentage;
        } catch (final Exception exception) {
            return 0;
        }
    }
}
