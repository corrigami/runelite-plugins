package tictac7x.daily;

import tictac7x.InfoBox;
import tictac7x.Overlay;

import javax.annotation.Nullable;
import javax.inject.Inject;

import java.awt.Color;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static java.time.temporal.ChronoUnit.DAYS;

import lombok.extern.slf4j.Slf4j;
import com.google.inject.Provides;

import net.runelite.api.Quest;
import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.Varbits;
import net.runelite.api.GameState;
import net.runelite.api.QuestState;
import net.runelite.api.vars.AccountType;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.game.ItemManager;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.daily.infoboxes.Battlestaves;
import tictac7x.daily.infoboxes.BucketsOfSand;
import tictac7x.daily.infoboxes.BucketsOfSlime;
import tictac7x.daily.infoboxes.PureEssence;

@Slf4j
@PluginDescriptor(
    name = "Daily Tasks",
    description = "Daily infoboxes to annoy you to do your tasks",
    tags = { "daily","battlestaves","essence","ess","kingdom","battlestaff","sand","flax","bowstring","ogre","rantz","bone","bonemeal","slime","buckets","herb","boxes,nmz,dynamite,mith,grapple" }

)
public class TicTac7xDailyPlugin extends Plugin {
    private static final String TOOLTIP_BATTLESTAVES = "Buy %d battlestaves from Zaff at Varrock";
    private static final String TOOLTIP_BUCKETS_OF_SAND = "Collect %d buckets of sand from Bert at Yanille";
    private static final String TOOLTIP_PURE_ESSENCE = "Collect %d pure essence from Wizard Cromperty at East-Ardougne";
    private static final String TOOLTIP_BUCKETS_OF_SLIME = "Exchange bones for %d buckets of slime and bonemeal from Robin at Porty Phasmatys";
    private static final String TOOLTIP_MISCELLANIA_PERCENTAGE = "%.0f%%";
    private static final String TOOLTIP_MISCELLANIA = "You need to work harder to increase your kingdom of Miscellania favor: " + TOOLTIP_MISCELLANIA_PERCENTAGE;

    private static final int BUCKETS_OF_SAND_QUEST_COMPLETE = 160;
    private static final int BUCKETS_OF_SAND_AMOUNT = 84;
    private static final double MISCELLANIA_FAVOR_MAX = 127;

    private final Quest throne_of_miscellania = Quest.THRONE_OF_MISCELLANIA;
    private final Quest royal_trouble = Quest.ROYAL_TROUBLE;

    @Nullable
    private Instant miscellania_date = null;

    @Inject
    private Client client;

    @Inject
    private ConfigManager configs;

    @Inject
    private DailyConfig config;

    @Inject
    private InfoBoxManager infoboxes;

    @Inject
    private ItemManager items;

    @Nullable
    private InfoBox infobox_battlestaves = null;

    @Nullable
    private InfoBox infobox_buckets_of_sand = null;

    @Nullable
    private InfoBox infobox_pure_essence = null;

    @Nullable
    private InfoBox infobox_buckets_of_slime = null;

    @Nullable
    private InfoBox infobox_miscellania = null;

    @Provides
    DailyConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(DailyConfig.class);
    }

    @Override
    protected void startUp() {
        try { miscellania_date = Instant.parse(config.getMiscellaniaFavorDate()); }
        catch (final Exception ignored) {}

        infobox_battlestaves = new Battlestaves(client, config, items, this);
        infoboxes.addInfoBox(infobox_battlestaves);

        infobox_buckets_of_sand = new BucketsOfSand(client, config, items, this);
        infoboxes.addInfoBox(infobox_buckets_of_sand);

        infobox_pure_essence = new PureEssence(client, config, items, this);
        infoboxes.addInfoBox(infobox_pure_essence);

        infobox_buckets_of_slime = new BucketsOfSlime(client, config, items, this);
        infoboxes.addInfoBox(infobox_buckets_of_slime);

        infobox_miscellania = createInfoBoxMiscellania();
        infoboxes.addInfoBox(infobox_miscellania);
    }

    @Override
    protected void shutDown() {
        infoboxes.removeInfoBox(infobox_battlestaves);
        infoboxes.removeInfoBox(infobox_buckets_of_sand);
        infoboxes.removeInfoBox(infobox_pure_essence);
        infoboxes.removeInfoBox(infobox_buckets_of_slime);
        infoboxes.removeInfoBox(infobox_miscellania);
    }

    private InfoBox createInfoBoxMiscellania() {
        return new InfoBox(
            DailyConfig.miscellania_id,
            items.getImage(ItemID.CASKET),
            this::showMiscellania,
            () -> String.format(TOOLTIP_MISCELLANIA_PERCENTAGE, getMiscellaniaFavorPercentage()),
            () -> String.format(TOOLTIP_MISCELLANIA, getMiscellaniaFavorPercentage()),
            this::getDailyColor,
            this
        );
    }

    private boolean showMiscellania() {
        return (
            config.showMiscellania() &&
            throne_of_miscellania.getState(client) == QuestState.FINISHED &&
            getMiscellaniaFavorPercentage() < 100
        );
    }

    private double getMiscellaniaFavorPercentage() {
        if (miscellania_date == null) return 0;

        final double favor_modifier = royal_trouble.getState(client) == QuestState.FINISHED ? 1.0 : 2.5;

        final Instant instant_now = Instant.now();
        final Instant instant_favor = miscellania_date;

        final LocalDate date_now = LocalDateTime.ofInstant(instant_now, ZoneOffset.UTC).toLocalDate();
        final LocalDate date_favor = LocalDateTime.ofInstant(instant_favor, ZoneOffset.UTC).toLocalDate();
        final long days = Math.abs(DAYS.between(date_now, date_favor));

        // Round down, percentage cant go down 0%.
        return Math.max(Math.floor((getMiscellaniaFavorVarbit() * 100 / MISCELLANIA_FAVOR_MAX) - days * favor_modifier), 0);
    }

    private int getMiscellaniaFavorVarbit() {
        return client.getVarbitValue(Varbits.KINGDOM_APPROVAL);
    }


    private Color getDailyColor() {
        return Overlay.color_red;
    }

    @Subscribe
    public void onVarbitChanged(final VarbitChanged event) {
        // Miscellania Kingdom favor varbit updated.
        if (getMiscellaniaFavorVarbit() != config.getMiscellaniaFavor() && client.getGameState() != GameState.LOGGING_IN) {
            configs.setConfiguration(DailyConfig.group, DailyConfig.miscellania_favor, getMiscellaniaFavorVarbit());
            configs.setConfiguration(DailyConfig.group, DailyConfig.miscellania_favor_date, Instant.now().toString());
        }
    }

    @Subscribe
    public void onConfigChanged(final ConfigChanged event) {
        // Miscellania Kingdom favor date changed.
        if (event.getGroup().equals(DailyConfig.group) && event.getKey().equals(DailyConfig.miscellania_favor_date)) {
            try { miscellania_date = Instant.parse(event.getNewValue()); }
            catch (final Exception ignored) {}
        }
    }
}
