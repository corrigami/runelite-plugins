package tictac7x.daily;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import com.google.inject.Provides;

import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.game.ItemManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;

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

    private DailyInfobox[] infoboxes_daily;

    @Provides
    DailyConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(DailyConfig.class);
    }

    @Override
    protected void startUp() {
        infoboxes_daily = new DailyInfobox[]{
            new Battlestaves(client, config, items, this),
            new BucketsOfSand(client, config, items, this),
            new PureEssence(client, config, items, this),
            new BucketsOfSlime(client, config, items, this),
            new OgreArrows(client, config, items, this),
            new BowStrings(client, config, items, this),
            new KingdomOfMiscellania(client, config, configs, items, this)
        };

        for (final DailyInfobox infobox : infoboxes_daily) {
            infoboxes.addInfoBox(infobox);
        }
    }

    @Override
    protected void shutDown() {
        for (final DailyInfobox infobox : infoboxes_daily) {
            infoboxes.removeInfoBox(infobox);
        }
    }

    @Subscribe
    public void onConfigChanged(final ConfigChanged event) {
        for (final DailyInfobox infobox : infoboxes_daily) {
            infobox.onConfigChanged(event);
        }
    }

    @Subscribe
    public void onGameTick(final GameTick event) {
        for (final DailyInfobox infobox : infoboxes_daily) {
            infobox.onGameTick();
        }
    }

    @Subscribe
    public void onVarbitChanged(final VarbitChanged event) {
        for (final DailyInfobox infobox : infoboxes_daily) {
            infobox.onVarbitChanged(event);
        }
    }
}
