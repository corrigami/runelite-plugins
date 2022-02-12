package tictac7x.balloon;

import com.google.common.collect.ImmutableSet;
import lombok.NonNull;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.runelite.api.*;

import java.text.SimpleDateFormat;

import net.runelite.api.events.*;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;

import javax.annotation.Nullable;

public class BalloonStorage {
    enum Logs { LOGS, LOGS_OAK, LOGS_WILLOW, LOGS_YEW, LOGS_MAGIC }

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

    private static final int STORAGE_LOGS_TYPES = 5;
    private final int[] storage = new int[STORAGE_LOGS_TYPES];
    private final int[] inventory = new int[STORAGE_LOGS_TYPES];

    @Nullable
    private GameObject visible_balloon = null;

    public BalloonStorage(final BalloonConfig config, final Client client, final ConfigManager configs) {
        this.config = config;
        this.client = client;
        this.configs = configs;
        loadStorageFromConfig();
    }

    public int getLogsCount(final Logs logs) {
        return storage[getLogsIndex(logs)];
    }

    public boolean showLogs(final Logs logs) {
        final Date now;
        try { now = date_format.parse(date_format.format(new Date())); } catch (final Exception ignored) { return false; }
        final int duration = config.getDuration() * 60 * 1000;

        final String logs_date;
        switch (logs) {
            case LOGS:        logs_date = config.getStorageDateLogs();       break;
            case LOGS_OAK:    logs_date = config.getStorageDateLogsOak();    break;
            case LOGS_WILLOW: logs_date = config.getStorageDateLogsWillow(); break;
            case LOGS_YEW:    logs_date = config.getStorageDateLogsYew();    break;
            case LOGS_MAGIC:  logs_date = config.getStorageDateLogsMagic();  break;
            default: return false;
        }

        try { return (
            config.show() == BalloonConfig.Show.INDEFINITELY
            || config.show() == BalloonConfig.Show.ALL && now.getTime() - date_format.parse(config.getStorageDateGeneral()).getTime() < duration
            || config.show() == BalloonConfig.Show.RECENTLY_USED && now.getTime() - date_format.parse(logs_date).getTime() < duration
            || config.showNearBalloon() && visible_balloon != null
        ); } catch (final Exception ignored) {}

        return false;
    }

    private void loadStorageFromConfig() {
        final String[] storage = config.getStorage().split(",");

        for (int i = 0; i < storage.length; i++) {
            this.storage[i] = Integer.parseInt(storage[i]);
        }
    }

    private void updateStorageFromStore(final Matcher matches) {
        final Logs logs_type;
        final String logs = matches.group(1);
        final int amount = Integer.parseInt(matches.group(3));

        switch (logs) {
            case "Logs":        logs_type = Logs.LOGS;        break;
            case "Oak logs":    logs_type = Logs.LOGS_OAK;    break;
            case "Willow logs": logs_type = Logs.LOGS_WILLOW; break;
            case "Yew logs":    logs_type = Logs.LOGS_YEW;    break;
            case "Magic logs":  logs_type = Logs.LOGS_MAGIC;  break;
            default: return;
        }

        updateStorageLogs(logs_type, amount);
    }

    private void updateStorageLogs(final Logs logs, final int amount) {
        storage[getLogsIndex(logs)] = amount;
        saveStorageDateToConfig(EnumSet.of(logs));
        saveStorageToConfig();
    }

    private void updateStorageFromCheck(final Matcher matches) {
        // Extract logs amounts from the storage check matches.
        storage[getLogsIndex(Logs.LOGS)]        = Integer.parseInt(matches.group(1));
        storage[getLogsIndex(Logs.LOGS_OAK)]    = Integer.parseInt(matches.group(2));
        storage[getLogsIndex(Logs.LOGS_WILLOW)] = Integer.parseInt(matches.group(3));
        storage[getLogsIndex(Logs.LOGS_YEW)]    = Integer.parseInt(matches.group(4));
        storage[getLogsIndex(Logs.LOGS_MAGIC)]  = Integer.parseInt(matches.group(5));

        // Save all logs counts into the config.
        saveStorageToConfig();

        // Update all logs dates.
        saveStorageDateToConfig(EnumSet.of(Logs.LOGS, Logs.LOGS_OAK, Logs.LOGS_WILLOW, Logs.LOGS_YEW, Logs.LOGS_MAGIC));
    }

    private void updateStorageFromNeeded(final Matcher matches) {
        final String logs_type = matches.group(1);
        final Logs logs;

        switch (logs_type) {
            case "normal logs": logs = Logs.LOGS;        break;
            case "oak logs":    logs = Logs.LOGS_OAK;    break;
            case "willow logs": logs = Logs.LOGS_WILLOW; break;
            case "yew logs":    logs = Logs.LOGS_YEW;    break;
            case "magic logs":  logs = Logs.LOGS_MAGIC;  break;
            default: return;
        }

        updateStorageLogs(logs, 0);
    }

    private void updateStorageFromFlying(final Matcher matches) {
        final String location = matches.group(2);
        final Logs logs;

        switch (location) {
            case "Taverley":
            case "Entrana":          logs = Logs.LOGS;        break;
            case "Crafting Guild":   logs = Logs.LOGS_OAK;    break;
            case "Varrock":          logs = Logs.LOGS_WILLOW; break;
            case "Castle Wars":      logs = Logs.LOGS_YEW;    break;
            case "Gnome Stronghold": logs = Logs.LOGS_MAGIC;  break;
            default: return;
        }

        final int count = getLogsCount(logs);

        // Only update storage, if the amount of logs was previously known.
        if (count > 0) {
            // Check if logs from inventory were not used.
            final ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);
            if (
                inventory != null
                && this.inventory[getLogsIndex(Logs.LOGS)] == inventory.count(ItemID.LOGS)
                && this.inventory[getLogsIndex(Logs.LOGS_OAK)] == inventory.count(ItemID.OAK_LOGS)
                && this.inventory[getLogsIndex(Logs.LOGS_WILLOW)] == inventory.count(ItemID.WILLOW_LOGS)
                && this.inventory[getLogsIndex(Logs.LOGS_YEW)] == inventory.count(ItemID.YEW_LOGS)
                && this.inventory[getLogsIndex(Logs.LOGS_MAGIC)] == inventory.count(ItemID.MAGIC_LOGS)
            ) {
                updateStorageLogs(logs, getLogsCount(logs) - 1);
            }
        }
    }

    private void saveStorageDateToConfig(final EnumSet<Logs> logs) {
        final Date now = new Date();

        for (final Logs log : logs) {
            final String key;

            switch (log) {
                case LOGS:        key = BalloonConfig.key_storage_date_logs;        break;
                case LOGS_OAK:    key = BalloonConfig.key_storage_date_logs_oak;    break;
                case LOGS_WILLOW: key = BalloonConfig.key_storage_date_logs_willow; break;
                case LOGS_YEW:    key = BalloonConfig.key_storage_date_logs_yew;    break;
                case LOGS_MAGIC:  key = BalloonConfig.key_storage_date_logs_magic;  break;
                default: continue;
            }

            configs.setConfiguration(BalloonConfig.group, key, date_format.format(now));
            configs.setConfiguration(BalloonConfig.group, config.key_storage_date_general, date_format.format(now));
        }
    }

    private void saveStorageToConfig() {
        String storage = "";
        for (int i = 0; i < STORAGE_LOGS_TYPES; i++) {
            storage += this.storage[i] + (i == STORAGE_LOGS_TYPES - 1 ? "" : ",");
        }

        configs.setConfiguration(config.group, config.key_storage, storage);
    }

    @NonNull
    private Integer getLogsIndex(final Logs logs) {
        switch (logs) {
            case LOGS:        return 0;
            case LOGS_OAK:    return 1;
            case LOGS_WILLOW: return 2;
            case LOGS_YEW:    return 3;
            case LOGS_MAGIC:  return 4;
        }

        return null;
    }

    public void onChatMessage(final ChatMessage message) {
        final Matcher matches;

        if (message.getType() == ChatMessageType.GAMEMESSAGE && (matches = regex_fly.matcher(message.getMessage())).matches()) {
            updateStorageFromFlying(matches);
        }
    }

    public void onWidgetLoaded(final WidgetLoaded event) {
        // Storage checked or logs needed.
        if (event.getGroupId() == WIDGET_STORAGE) {
            final Widget widget = client.getWidget(WIDGET_STORAGE, WIDGET_STORAGE_MESSAGE);
            if (widget != null) {
                Matcher matches;

                // Storage checked.
                if ((matches = regex_check.matcher(widget.getText())).matches()) {
                    updateStorageFromCheck(matches);

                // Logs needed.
                } else if ((matches = regex_needed.matcher(widget.getText())).matches()) {
                    updateStorageFromNeeded(matches);
                }
            }

        // Logs stored.
        } else if (event.getGroupId() == WIDGET_STORAGE_STORE) {
            final Widget widget = client.getWidget(WIDGET_STORAGE_STORE, WIDGET_STORAGE_STORE_MESSAGE);
            if (widget != null) {
                Matcher matches;

                if ((matches = regex_store.matcher(widget.getText())).matches()) {
                    updateStorageFromStore(matches);
                }
            }

        // Flying map opened.
        } else if (event.getGroupId() == WIDGET_MAP) {
            final Widget widget = client.getWidget(WIDGET_MAP, WIDGET_MAP_CLOSE);

            // Close button visible, save inventory state.
            if (widget != null && widget.isHidden() == false) {
                final ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);

                if (inventory != null) {
                    this.inventory[getLogsIndex(Logs.LOGS)] = inventory.count(ItemID.LOGS);
                    this.inventory[getLogsIndex(Logs.LOGS_OAK)] = inventory.count(ItemID.OAK_LOGS);
                    this.inventory[getLogsIndex(Logs.LOGS_WILLOW)] = inventory.count(ItemID.WILLOW_LOGS);
                    this.inventory[getLogsIndex(Logs.LOGS_YEW)] = inventory.count(ItemID.YEW_LOGS);
                    this.inventory[getLogsIndex(Logs.LOGS_MAGIC)] = inventory.count(ItemID.MAGIC_LOGS);
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
}
