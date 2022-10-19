package tictac7x.daily;

import net.runelite.api.events.VarbitChanged;
import tictac7x.InfoBox;

import javax.annotation.Nullable;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import com.google.inject.Provides;

import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.game.ItemManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.daily.infoboxes.Battlestaves;
import tictac7x.daily.infoboxes.BucketsOfSand;
import tictac7x.daily.infoboxes.BucketsOfSlime;
import tictac7x.daily.infoboxes.KingdomOfMiscellania;
import tictac7x.daily.infoboxes.OgreArrows;
import tictac7x.daily.infoboxes.PureEssence;
import tictac7x.daily.infoboxes.BowStrings;

@Slf4j
@PluginDescriptor(
    name = "Daily Tasks",
    description = "Daily infoboxes to annoy you to do your tasks",
    tags = { "daily","battlestaves","essence","ess","kingdom","battlestaff","sand","flax","bowstring","ogre","rantz","bone","bonemeal","slime","buckets","herb","boxes,nmz,dynamite,mith,grapple" }

)
public class TicTac7xDailyPlugin extends Plugin {
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
    private InfoBox infobox_ogre_arrows = null;

    @Nullable
    private InfoBox infobox_bow_strings = null;

    @Nullable
    private KingdomOfMiscellania infobox_miscellania_favor = null;

    @Provides
    DailyConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(DailyConfig.class);
    }

    @Override
    protected void startUp() {
        infobox_battlestaves = new Battlestaves(client, config, items, this);
        infoboxes.addInfoBox(infobox_battlestaves);

        infobox_buckets_of_sand = new BucketsOfSand(client, config, items, this);
        infoboxes.addInfoBox(infobox_buckets_of_sand);

        infobox_pure_essence = new PureEssence(client, config, items, this);
        infoboxes.addInfoBox(infobox_pure_essence);

        infobox_buckets_of_slime = new BucketsOfSlime(client, config, items, this);
        infoboxes.addInfoBox(infobox_buckets_of_slime);

        infobox_ogre_arrows = new OgreArrows(client, config, items, this);
        infoboxes.addInfoBox(infobox_ogre_arrows);

        infobox_bow_strings = new BowStrings(client, config, items, this);
        infoboxes.addInfoBox(infobox_bow_strings);

        infobox_miscellania_favor = new KingdomOfMiscellania(client, config, configs, items, this);
        infoboxes.addInfoBox(infobox_miscellania_favor);
    }

    @Override
    protected void shutDown() {
        infoboxes.removeInfoBox(infobox_battlestaves);
        infoboxes.removeInfoBox(infobox_buckets_of_sand);
        infoboxes.removeInfoBox(infobox_pure_essence);
        infoboxes.removeInfoBox(infobox_buckets_of_slime);
        infoboxes.removeInfoBox(infobox_ogre_arrows);
        infoboxes.removeInfoBox(infobox_bow_strings);
        infoboxes.removeInfoBox(infobox_miscellania_favor);
    }

    @Subscribe
    public void onVarbitChanged(final VarbitChanged event) {
        if (infobox_miscellania_favor != null) infobox_miscellania_favor.onVarbitChanged();
    }

    @Subscribe
    public void onConfigChanged(final ConfigChanged event) {
        if (infobox_miscellania_favor != null) infobox_miscellania_favor.onConfigChanged(event);
    }

    @Subscribe
    public void onGameTick(final GameTick event) {
        if (infobox_miscellania_favor != null) infobox_miscellania_favor.onGameTick();
    }
}
