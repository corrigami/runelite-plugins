package tictac7x.daily;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import com.google.inject.Provides;

import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.game.ItemManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;

import tictac7x.daily.infoboxes.*;

@Slf4j
@PluginDescriptor(
    name = "Daily Tasks",
    description = "Daily infoboxes to annoy you to do your tasks",
    tags = { "daily","battlestaves","essence","ess","kingdom","battlestaff","sand","flax","bowstring","ogre","rantz","bone","bonemeal","slime","buckets","herb","boxes,nmz,dynamite,mith,grapple","dynamite","rune","herb","box" }

)
public class TicTac7xDailyPlugin extends Plugin {
    private final String plugin_version = "v0.2.4";
    private final String plugin_message = "" +
        "<colHIGHLIGHT>Daily Tasks " + plugin_version + ":<br>" +
        "<colHIGHLIGHT>* Dynamite from Thirus added.<br>" +
        "<colHIGHLIGHT>* Random runes from Lundail added.<br>" +
        "<colHIGHLIGHT>* Herb boxes from Nightmare Zone added.";

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

    @Inject
    private ChatMessageManager chat_messages;

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
            new Dynamite(client, config, items, this),
            new RandomRunes(client, config, items, this),
            new HerbBoxes(client, config, items, this),
            new KingdomOfMiscellania(client, config, configs, items, this),
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

    @Subscribe
    public void onGameStateChanged(final GameStateChanged event) {
        if (event.getGameState() != GameState.LOGGED_IN) return;

        // Send message about plugin updates for once.
        if (!config.getVersion().equals(plugin_version)) {
            configs.setConfiguration(DailyConfig.group, DailyConfig.version, plugin_version);
            chat_messages.queue(QueuedMessage.builder()
                .type(ChatMessageType.CONSOLE)
                .runeLiteFormattedMessage(plugin_message)
                .build()
            );
        }
    }

    public boolean isCompleted(final int diary) {
        return client.getVarbitValue(diary) == 1;
    }
}
