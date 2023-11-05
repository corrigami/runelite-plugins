package tictac7x.daily;

import javax.annotation.Nullable;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import com.google.inject.Provides;

import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.game.ItemManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;

import tictac7x.daily.common.DailyInfobox;
import tictac7x.daily.dailies.Battlestaves;
import tictac7x.daily.dailies.BowStrings;
import tictac7x.daily.dailies.BucketsOfSand;
import tictac7x.daily.dailies.BucketsOfSlime;
import tictac7x.daily.dailies.Dynamite;
import tictac7x.daily.dailies.HerbBoxes;
import tictac7x.daily.dailies.ImplingJars;
import tictac7x.daily.dailies.KingdomOfMiscellania;
import tictac7x.daily.dailies.OgreArrows;
import tictac7x.daily.dailies.PureEssence;
import tictac7x.daily.dailies.RandomRunes;

@Slf4j
@PluginDescriptor(
    name = "Daily Tasks",
    description = "Daily infoboxes to annoy you to do your tasks",
    tags = {
        "daily",
        "battlestaves",
        "battlestaff",
        "bowstring",
        "buckets",
        "sand",
        "slime",
        "bone",
        "bonemeal",
        "dynamite",
        "herb",
        "boxes",
        "nmz",
        "impling",
        "jar",
        "kingdom",
        "ogre",
        "rantz",
        "essence",
        "runes",
    }
)
public class TicTac7xDailyTasksPlugin extends Plugin {
    private final String plugin_version = "v0.3";
    private final String plugin_message = "" +
        "<colHIGHLIGHT>Daily Tasks " + plugin_version + ":<br>" +
        "<colHIGHLIGHT>* New daily added to remind you buy 10 impling jars from Elnock Inquisitor.";

    @Inject
    private Client client;

    @Inject
    private ConfigManager configManager;

    @Inject
    private TicTac7xDailyTasksConfig config;

    @Inject
    private InfoBoxManager infoBoxManager;

    @Inject
    private ItemManager itemManager;

    @Inject
    private ChatMessageManager chatMessageManager;

    @Nullable
    private DailyInfobox[] dailyInfoboxes;

    @Provides
    TicTac7xDailyTasksConfig provideConfig(final ConfigManager configManager) {
        return configManager.getConfig(TicTac7xDailyTasksConfig.class);
    }

    @Override
    protected void startUp() {
        dailyInfoboxes = new DailyInfobox[]{
            new Battlestaves(client, config, itemManager, this),
            new BucketsOfSand(client, config, itemManager, this),
            new PureEssence(client, config, itemManager, this),
            new BucketsOfSlime(client, config, itemManager, this),
            new OgreArrows(client, config, itemManager, this),
            new BowStrings(client, config, itemManager, this),
            new Dynamite(client, config, itemManager, this),
            new RandomRunes(client, config, itemManager, this),
            new HerbBoxes(client, config, itemManager, this),
            new KingdomOfMiscellania(client, config, configManager, itemManager, this),
            new ImplingJars(client, config, itemManager, this),
        };

        for (final DailyInfobox infobox : dailyInfoboxes) {
            infoBoxManager.addInfoBox(infobox);
        }
    }

    @Override
    protected void shutDown() {
        for (final DailyInfobox infobox : dailyInfoboxes) {
            infoBoxManager.removeInfoBox(infobox);
        }
    }

    @Subscribe
    public void onVarbitChanged(final VarbitChanged event) {
        for (final DailyInfobox infobox : dailyInfoboxes) {
            infobox.onVarbitChanged(event);
        }
    }

    @Subscribe
    public void onGameStateChanged(final GameStateChanged event) {
        if (event.getGameState() != GameState.LOGGED_IN) return;

        // Send message about plugin updates for once.
        if (!config.getVersion().equals(plugin_version)) {
            configManager.setConfiguration(TicTac7xDailyTasksConfig.group, TicTac7xDailyTasksConfig.version, plugin_version);
            chatMessageManager.queue(QueuedMessage.builder()
                .type(ChatMessageType.CONSOLE)
                .runeLiteFormattedMessage(plugin_message)
                .build()
            );
        }
    }
}
