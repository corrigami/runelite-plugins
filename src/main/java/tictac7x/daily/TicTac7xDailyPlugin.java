package tictac7x.daily;

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

import tictac7x.InfoBox;
import tictac7x.daily.infoboxes.BowStrings;
import tictac7x.daily.infoboxes.OgreArrows;
import tictac7x.daily.infoboxes.PureEssence;
import tictac7x.daily.infoboxes.Battlestaves;
import tictac7x.daily.infoboxes.BucketsOfSand;
import tictac7x.daily.infoboxes.BucketsOfSlime;
import tictac7x.daily.infoboxes.KingdomOfMiscellania;

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

    private InfoBox
        infobox_battlestaves,
        infobox_buckets_of_sand,
        infobox_pure_essence,
        infobox_buckets_of_slime,
        infobox_ogre_arrows,
        infobox_bow_strings;

    private KingdomOfMiscellania infobox_miscellania_favor;

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
    public void onConfigChanged(final ConfigChanged event) {
        infobox_miscellania_favor.onConfigChanged(event);
    }

    @Subscribe
    public void onGameTick(final GameTick event) {
        infobox_miscellania_favor.onGameTick();
    }
}
