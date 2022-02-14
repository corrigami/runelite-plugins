package tictac7x.balloon;

import java.util.Map;
import java.util.Set;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.ParseException;
import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import tictac7x.balloon.BalloonConfig.Logs;
import com.google.common.collect.ImmutableSet;

import net.runelite.api.ItemID;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.GameObject;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.widgets.Widget;
import net.runelite.api.ChatMessageType;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.api.events.GameObjectSpawned;

public class BalloonStorage {
    private static final int WIDGET_STORAGE = 229;
    private static final int WIDGET_STORAGE_MESSAGE = 1;
    private static final int WIDGET_STORAGE_STORE = 193;
    private static final int WIDGET_STORAGE_STORE_MESSAGE = 2;
    private static final int WIDGET_MAP = 469;
    private static final int WIDGET_MAP_CLOSE = 26;

    private final SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private final Pattern regex_store = Pattern.compile("You put the (.*) in the crate\\. You now have(<br>| )?(\\d+)(<br>| )?stored\\.");
    private final Pattern regex_fly = Pattern.compile("You board the balloon and fly to (the )?(.*)\\.");
    private final Pattern regex_needed = Pattern.compile("You need 1 (.*) to make this trip\\.");
    private final Pattern regex_check = Pattern.compile("This crate currently contains (\\d+) logs, (\\d+) oak logs, (\\d+) willow logs, (\\d+)<br>yew logs and (\\d+) magic logs\\.");
    private final Set<Integer> balloons = ImmutableSet.of(19133, 19135, 19137, 19139, 19141, 19143);

    private final BalloonConfig config;
    private final Client client;
    private final ConfigManager configs;

    private final Logs[] logs = new Logs[] { Logs.LOGS_REGULAR, Logs.LOGS_OAK, Logs.LOGS_WILLOW, Logs.LOGS_YEW, Logs.LOGS_MAGIC };
    private final Map<Logs, Date> storage_dates;
    private final Map<Logs, Integer> inventory;

    @Nullable
    private GameObject visible_balloon = null;

    public BalloonStorage(final BalloonConfig config, final Client client, final ConfigManager configs) {
        this.config = config;
        this.client = client;
        this.configs = configs;

        this.storage_dates = loadStorageDatesFromConfig();
        this.inventory = new HashMap<>();
    }

    private Map<Logs, Date> loadStorageDatesFromConfig() {
        final Map<Logs, Date> storage_dates = new HashMap<>();

        for (final Logs logs : this.logs) {
            try {
                storage_dates.put(logs, date_format.parse(configs.getConfiguration(BalloonConfig.group, getLogsDateKey(logs))));
            } catch (final Exception invalid_date) {
                storage_dates.put(logs, null);
            }
        }

        return storage_dates;
    }

    public int getLogs(final Logs logs) {
        return Integer.parseInt(configs.getConfiguration(BalloonConfig.group, getLogsKey(logs)));
    }

    @Nullable
    public Date getLogsDate(final Logs logs) {
        return storage_dates.get(logs);
    }

    public boolean shouldLogsBeDisplayed(final Logs logs) {
        // All logs displayed all the time.
        if (config.show() == BalloonConfig.Show.INDEFINITELY) return true;

        // All logs displayed near the balloon.
        if (config.showNearBalloon() && visible_balloon != null) return true;

        final Date date_now = new Date();
        final int duration = config.getDisplayDuration() * 60 * 1000;

        // All logs are displayed for certain time.
        if (config.show() == BalloonConfig.Show.ALL) {
            for (final Date date_logs : storage_dates.values()) {
                if (date_logs != null && date_now.getTime() - date_logs.getTime() < duration) {
                    return true;
                }
            }
        }

        // Only recently used logs are displayed for certain time.
        final Date date_logs = getLogsDate(logs);
        return config.show() == BalloonConfig.Show.RECENTLY_USED && date_logs != null && date_now.getTime() - date_logs.getTime() < duration;
    }

    private void updateStorageFromStore(final String store) {
        final Matcher matches = regex_store.matcher(store);
        if (!matches.matches()) return;

        final Logs logs;
        final String logs_text = matches.group(1);
        final int amount = Integer.parseInt(matches.group(3));

        switch (logs_text) {
            case "Logs":        logs = Logs.LOGS_REGULAR; break;
            case "Oak logs":    logs = Logs.LOGS_OAK;     break;
            case "Willow logs": logs = Logs.LOGS_WILLOW;  break;
            case "Yew logs":    logs = Logs.LOGS_YEW;     break;
            case "Magic logs":  logs = Logs.LOGS_MAGIC;   break;
            default: return;
        }

        updateStorageLogs(logs, amount);
    }

    private void updateStorageLogs(final Logs logs, final int amount) {
        saveStorageLogsDateToConfig(logs, new Date());
        saveStorageLogsToConfig(logs, amount);
    }

    private void updateStorageFromCheck(final String check) {
        final Matcher matches = regex_check.matcher(check);
        if (!matches.matches()) return;

        updateStorageLogs(Logs.LOGS_REGULAR, Integer.parseInt(matches.group(1)));
        updateStorageLogs(Logs.LOGS_OAK, Integer.parseInt(matches.group(2)));
        updateStorageLogs(Logs.LOGS_WILLOW, Integer.parseInt(matches.group(3)));
        updateStorageLogs(Logs.LOGS_YEW, Integer.parseInt(matches.group(4)));
        updateStorageLogs(Logs.LOGS_MAGIC, Integer.parseInt(matches.group(5)));
    }

    private void updateStorageFromNeeded(final String needed) {
        final Matcher matches = regex_needed.matcher(needed);
        if (!matches.matches()) return;

        final String logs_text = matches.group(1);
        final Logs logs;

        switch (logs_text) {
            case "normal logs": logs = Logs.LOGS_REGULAR; break;
            case "oak logs":    logs = Logs.LOGS_OAK;     break;
            case "willow logs": logs = Logs.LOGS_WILLOW;  break;
            case "yew logs":    logs = Logs.LOGS_YEW;     break;
            case "magic logs":  logs = Logs.LOGS_MAGIC;   break;
            default: return;
        }

        updateStorageLogs(logs, 0);
    }

    private void updateStorageFromFlying(final String flying) {
        final Matcher matches = regex_fly.matcher(flying);
        if (!matches.matches()) return;

        final String location = matches.group(2);
        final Logs logs;

        switch (location) {
            case BalloonConfig.location_taverley:
            case BalloonConfig.location_entrana:        logs = Logs.LOGS_REGULAR; break;
            case BalloonConfig.location_crafting_guild: logs = Logs.LOGS_OAK;     break;
            case BalloonConfig.location_varrock:        logs = Logs.LOGS_WILLOW;  break;
            case BalloonConfig.location_castle_wars:    logs = Logs.LOGS_YEW;     break;
            case BalloonConfig.location_grand_tree:     logs = Logs.LOGS_MAGIC;   break;
            default: return;
        }

        final int amount = getLogs(logs);

        // Only update storage, if the amount of logs was previously known.
        if (amount > 0) {
            // Check if logs from inventory were not used.
            final ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);
            if (
                inventory != null
                && this.inventory.containsKey(Logs.LOGS_REGULAR) && this.inventory.get(Logs.LOGS_REGULAR) == inventory.count(ItemID.LOGS)
                && this.inventory.containsKey(Logs.LOGS_OAK) && this.inventory.get(Logs.LOGS_OAK) == inventory.count(ItemID.OAK_LOGS)
                && this.inventory.containsKey(Logs.LOGS_WILLOW) && this.inventory.get(Logs.LOGS_WILLOW) == inventory.count(ItemID.WILLOW_LOGS)
                && this.inventory.containsKey(Logs.LOGS_YEW) && this.inventory.get(Logs.LOGS_YEW) == inventory.count(ItemID.YEW_LOGS)
                && this.inventory.containsKey(Logs.LOGS_MAGIC) && this.inventory.get(Logs.LOGS_MAGIC) == inventory.count(ItemID.MAGIC_LOGS)
            ) {
                updateStorageLogs(logs, amount - 1);
            }
        }
    }

    private void saveStorageLogsToConfig(final Logs logs, final int amount) {
        configs.setConfiguration(config.group, getLogsKey(logs), amount);
    }

    private void saveStorageLogsDateToConfig(final Logs logs, final Date date) {
        configs.setConfiguration(BalloonConfig.group, getLogsDateKey(logs), date_format.format(date));
    }

    public void onChatMessage(final ChatMessage message) {
        if (message.getType() == ChatMessageType.GAMEMESSAGE) {
            updateStorageFromFlying(message.getMessage());
        }
    }

    public void onWidgetLoaded(final WidgetLoaded event) {
        // Storage checked or logs needed.
        if (event.getGroupId() == WIDGET_STORAGE) {
            final Widget widget = client.getWidget(WIDGET_STORAGE, WIDGET_STORAGE_MESSAGE);
            if (widget != null) {
                updateStorageFromCheck(widget.getText());
                updateStorageFromNeeded(widget.getText());
            }

        // Logs stored.
        } else if (event.getGroupId() == WIDGET_STORAGE_STORE) {
            final Widget widget = client.getWidget(WIDGET_STORAGE_STORE, WIDGET_STORAGE_STORE_MESSAGE);
            if (widget != null) {
                updateStorageFromStore(widget.getText());
            }

        // Flying map opened.
        } else if (event.getGroupId() == WIDGET_MAP) {
            final Widget widget = client.getWidget(WIDGET_MAP, WIDGET_MAP_CLOSE);

            // Close button visible, save inventory state.
            if (widget != null && widget.isHidden() == false) {
                final ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);

                if (inventory != null) {
                    this.inventory.put(Logs.LOGS_REGULAR, inventory.count(ItemID.LOGS));
                    this.inventory.put(Logs.LOGS_OAK, inventory.count(ItemID.OAK_LOGS));
                    this.inventory.put(Logs.LOGS_WILLOW, inventory.count(ItemID.WILLOW_LOGS));
                    this.inventory.put(Logs.LOGS_YEW, inventory.count(ItemID.YEW_LOGS));
                    this.inventory.put(Logs.LOGS_MAGIC, inventory.count(ItemID.MAGIC_LOGS));
                }
            }
        }
    }

    public void onGameObjectSpawned(final GameObjectSpawned event) {
        final GameObject object = event.getGameObject();
        if (balloons.contains(object.getId())) {
            visible_balloon = object;
        }
    }

    public void onGameStateChanged(final GameStateChanged event) {
        if (event.getGameState() == GameState.LOADING) {
            visible_balloon = null;
        }
    }

    public void onConfigChanged(final ConfigChanged event) throws ParseException {
        if (event.getGroup().equals(BalloonConfig.group)) {
            if (event.getKey().equals(getLogsDateKey(Logs.LOGS_REGULAR))) {
                storage_dates.put(Logs.LOGS_REGULAR, date_format.parse(event.getNewValue()));
            } else if (event.getKey().equals(getLogsDateKey(Logs.LOGS_OAK))) {
                storage_dates.put(Logs.LOGS_OAK, date_format.parse(event.getNewValue()));
            } else if (event.getKey().equals(getLogsDateKey(Logs.LOGS_WILLOW))) {
                storage_dates.put(Logs.LOGS_WILLOW, date_format.parse(event.getNewValue()));
            } else if (event.getKey().equals(getLogsDateKey(Logs.LOGS_YEW))) {
                storage_dates.put(Logs.LOGS_YEW, date_format.parse(event.getNewValue()));
            } else if (event.getKey().equals(getLogsDateKey(Logs.LOGS_MAGIC))) {
                storage_dates.put(Logs.LOGS_MAGIC, date_format.parse(event.getNewValue()));
            }
        }
    }

    private String getLogsKey(final Logs logs) {
        return String.valueOf(logs).toLowerCase();
    }

    private String getLogsDateKey(final Logs logs) {
        return String.valueOf(logs).toLowerCase() + "_date";
    }
}
