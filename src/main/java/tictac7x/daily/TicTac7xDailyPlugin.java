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

        if (infobox_battlestaves == null) {
            infobox_battlestaves = createInfoBoxBattlestaves();
            infobox_buckets_of_sand = createInfoBoxBucketsOfSand();
            infobox_pure_essence = createInfoBoxPureEssence();
            infobox_buckets_of_slime = createInfoBoxBucketsOfSlime();
            infobox_miscellania = createInfoBoxMiscellania();
        }

        infoboxes.addInfoBox(infobox_battlestaves);
        infoboxes.addInfoBox(infobox_buckets_of_sand);
        infoboxes.addInfoBox(infobox_pure_essence);
        infoboxes.addInfoBox(infobox_buckets_of_slime);
        infoboxes.addInfoBox(infobox_miscellania);
    }

    @Override
    protected void shutDown() {
        if (infobox_battlestaves != null) {
            infoboxes.removeInfoBox(infobox_battlestaves);
            infoboxes.removeInfoBox(infobox_buckets_of_sand);
            infoboxes.removeInfoBox(infobox_pure_essence);
            infoboxes.removeInfoBox(infobox_buckets_of_slime);
            infoboxes.removeInfoBox(infobox_miscellania);
        }
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

    private InfoBox createInfoBoxBattlestaves() {
        return new InfoBox(
            DailyConfig.battlestaves_id,
            items.getImage(ItemID.BATTLESTAFF),
            this::showBattlestaves,
            () -> String.valueOf(getBattlestavesAmount()),
            () -> String.format(TOOLTIP_BATTLESTAVES, getBattlestavesAmount()),
            this::getDailyColor,
            this
        );
    }

    private boolean showBattlestaves() {
        return (
            config.showBattlestaves() &&
            client.getVarbitValue(Varbits.DAILY_STAVES_COLLECTED) == 0
        );
    }

    private int getBattlestavesAmount() {
        if (client.getVarbitValue(Varbits.DIARY_VARROCK_EASY) == 1) {
            if (client.getVarbitValue(Varbits.DIARY_VARROCK_MEDIUM) == 1) {
                if (client.getVarbitValue(Varbits.DIARY_VARROCK_HARD) == 1) {
                    if (client.getVarbitValue(Varbits.DIARY_VARROCK_ELITE) == 1) {
                        return 120;
                    } else return 60;
                } else return 30;
            } else return 15;
        } else return 5;
    }

    private InfoBox createInfoBoxBucketsOfSand() {
        return new InfoBox(
            DailyConfig.buckets_of_sand_id,
            items.getImage(ItemID.BUCKET_OF_SAND),
            this::showBucketsOfSand,
            () -> String.valueOf(BUCKETS_OF_SAND_AMOUNT),
            () -> String.format(TOOLTIP_BUCKETS_OF_SAND, BUCKETS_OF_SAND_AMOUNT),
            this::getDailyColor,
            this
        );
    }

    private boolean showBucketsOfSand() {
        return (
            config.showBucketsOfSand() &&
            client.getAccountType() != AccountType.ULTIMATE_IRONMAN &&
            client.getVarbitValue(Varbits.QUEST_THE_HAND_IN_THE_SAND) >= BUCKETS_OF_SAND_QUEST_COMPLETE &&
            client.getVarbitValue(Varbits.DAILY_SAND_COLLECTED) == 0
        );
    }

    private InfoBox createInfoBoxPureEssence() {
        return new InfoBox(
            DailyConfig.pure_essence_id,
            items.getImage(ItemID.PURE_ESSENCE),
            this::showPureEssence,
            () -> String.valueOf(getPureEssenceAmount()),
            () -> String.format(TOOLTIP_PURE_ESSENCE, getPureEssenceAmount()),
            this::getDailyColor,
            this
        );
    }

    private boolean showPureEssence() {
        return (
            config.showPureEssence() &&
            client.getVarbitValue(Varbits.DIARY_ARDOUGNE_EASY) == 1 &&
            client.getVarbitValue(Varbits.DIARY_ARDOUGNE_MEDIUM) == 1 &&
            client.getVarbitValue(Varbits.DAILY_ESSENCE_COLLECTED) == 0
        );
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

    private InfoBox createInfoBoxBucketsOfSlime() {
        return new InfoBox(
            DailyConfig.buckets_of_slime_id,
            items.getImage(ItemID.BUCKET_OF_SLIME),
            this::showBucketsOfSlime,
            () -> String.valueOf(getBucketsOfSlimeAmount()),
            () -> String.format(TOOLTIP_BUCKETS_OF_SLIME, getBucketsOfSlimeAmount()),
            this::getDailyColor,
            this
        );
    }

    private boolean showBucketsOfSlime() {
        return (
            config.showBucketsOfSlime() &&
            client.getVarbitValue(Varbits.DIARY_MORYTANIA_EASY) == 1 &&
            client.getVarbitValue(Varbits.DIARY_MORYTANIA_MEDIUM) == 1 &&
            getBucketsOfSlimeAmount() > 0
        );
    }

    private int getBucketsOfSlimeAmount() {
        int buckets_of_slime;

        if (client.getVarbitValue(Varbits.DIARY_MORYTANIA_MEDIUM) == 1) {
            if (client.getVarbitValue(Varbits.DIARY_MORYTANIA_HARD) == 1) {
                if (client.getVarbitValue(Varbits.DIARY_MORYTANIA_ELITE) == 1) {
                    buckets_of_slime = 39;
                } else buckets_of_slime = 26;
            } else buckets_of_slime = 13;
        } else buckets_of_slime = 0;

        return buckets_of_slime - client.getVarbitValue(Varbits.DAILY_BONEMEAL_STATE);
    }

    private Color getDailyColor() {
        return Overlay.color_red;
    }

    @Subscribe
    public void onVarbitChanged(final VarbitChanged event) {
        // Miscellania Kingdom favor varbit updated.
        if (getMiscellaniaFavorVarbit() != config.getMiscellaniaFavor()) {
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
