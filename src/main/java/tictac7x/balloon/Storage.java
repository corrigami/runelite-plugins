package tictac7x.balloon;

import lombok.NonNull;
import java.util.Date;
import java.util.EnumSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.runelite.api.Client;
import java.text.SimpleDateFormat;
import net.runelite.api.widgets.Widget;
import net.runelite.api.ChatMessageType;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.client.config.ConfigManager;

public class Storage {
    enum Logs {
        LOGS,
        LOGS_OAK,
        LOGS_WILLOW,
        LOGS_YEW,
        LOGS_MAGIC
    }

    private static final int WIDGET_STORAGE = 229;
    private static final int WIDGET_STORAGE_MESSAGE = 1;
    private static final int WIDGET_STORAGE_STORE = 193;
    private static final int WIDGET_STORAGE_STORE_MESSAGE = 2;

    private final SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private final Pattern regex_store = Pattern.compile("You put the (.*) in the crate\\. You now have (\\d+).*");
    private final Pattern regex_fly = Pattern.compile("You board the balloon and fly to (the )?(.*)\\.");
    private final Pattern regex_check = Pattern.compile(".*?(\\d+).*?(\\d+).*?(\\d+).*?(\\d+).*?(\\d+).*?");

    private final BalloonConfig config;
    private final Client client;
    private final ConfigManager configs;

    private static final int STORAGE_LOGS_TYPES = 5;
    private final int[] storage = new int[STORAGE_LOGS_TYPES];

    public Storage(final BalloonConfig config, final Client client, final ConfigManager configs) {
        this.config = config;
        this.client = client;
        this.configs = configs;
        updateStorageFromCheck(config.getStorage(), false);
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
            case LOGS: logs_date = config.getStorageDateLogs(); break;
            case LOGS_OAK: logs_date = config.getStorageDateLogsOak(); break;
            case LOGS_WILLOW: logs_date = config.getStorageDateLogsWillow(); break;
            case LOGS_YEW: logs_date = config.getStorageDateLogsYew(); break;
            case LOGS_MAGIC: logs_date = config.getStorageDateLogsMagic(); break;
            default: return false;
        }

        try { return (
            config.show() == BalloonConfig.Show.INDEFINITELY
            || config.show() == BalloonConfig.Show.ALL && now.getTime() - date_format.parse(config.getStorageDateGeneral()).getTime() < duration
            || config.show() == BalloonConfig.Show.RECENTLY_USED && now.getTime() - date_format.parse(logs_date).getTime() < duration
        ); } catch (final Exception ignored) {}

        return false;
    }

    private void updateStorageStore(final String message) {
        final Matcher store = regex_store.matcher(message);

        if (store.matches()) {
            final Logs logs_type;
            final String logs = store.group(1);
            final int amount = Integer.parseInt(store.group(2));

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
    }

    private void updateStorageLogs(final Logs logs, final int amount) {
        storage[getLogsIndex(logs)] = amount;
        saveStorageDateToConfig(EnumSet.of(logs));
        saveStorageToConfig();
    }

    private void updateStorageFromCheck(final String message, final boolean update_dates) {
        final Matcher check = regex_check.matcher(message);

        // Extract logs amounts from the string.
        if (check.matches()) {
            storage[getLogsIndex(Logs.LOGS)]        = Integer.parseInt(check.group(1));
            storage[getLogsIndex(Logs.LOGS_OAK)]    = Integer.parseInt(check.group(2));
            storage[getLogsIndex(Logs.LOGS_WILLOW)] = Integer.parseInt(check.group(3));
            storage[getLogsIndex(Logs.LOGS_YEW)]    = Integer.parseInt(check.group(4));
            storage[getLogsIndex(Logs.LOGS_MAGIC)]  = Integer.parseInt(check.group(5));
        }

        // Save all logs counts into the config.
        saveStorageToConfig();

        // Update all logs dates.
        if (update_dates) {
            saveStorageDateToConfig(EnumSet.of(Logs.LOGS, Logs.LOGS_OAK, Logs.LOGS_WILLOW, Logs.LOGS_YEW, Logs.LOGS_MAGIC));
        }
    }

    private void saveStorageDateToConfig(final EnumSet<Logs> logs) {
        final Date now = new Date();

        for (final Logs log : logs) {
            final String key;

            switch (log) {
                case LOGS:
                    key = BalloonConfig.key_storage_date_logs; break;
                case LOGS_OAK:
                    key = BalloonConfig.key_storage_date_logs_oak; break;
                case LOGS_WILLOW:
                    key = BalloonConfig.key_storage_date_logs_willow; break;
                case LOGS_YEW:
                    key = BalloonConfig.key_storage_date_logs_yew; break;
                case LOGS_MAGIC:
                    key = BalloonConfig.key_storage_date_logs_magic; break;
                default: continue;
            }

            configs.setConfiguration(BalloonConfig.group, key, date_format.format(now));
            configs.setConfiguration(BalloonConfig.group, config.key_storage_date_general, date_format.format(now));
        }
    }

    private void saveStorageToConfig() {
        StringBuilder storage = new StringBuilder();
        for (int i = 0; i < STORAGE_LOGS_TYPES; i++) {
            storage.append(this.storage[i]).append(i == STORAGE_LOGS_TYPES - 1 ? "" : ",");
        }

        configs.setConfiguration(config.group, config.key_storage, storage.toString());
    }

    @NonNull
    private Integer getLogsIndex(final Logs logs) {
        switch (logs) {
            case LOGS: return 0;
            case LOGS_OAK: return 1;
            case LOGS_WILLOW: return 2;
            case LOGS_YEW: return 3;
            case LOGS_MAGIC: return 4;
        }

        return null;
    }

    public void onChatMessage(final ChatMessage message) {
        final Matcher matches;
        if (message.getType() == ChatMessageType.GAMEMESSAGE && (matches = regex_fly.matcher(message.getMessage())).matches()) {
            final String location = matches.group(2);
            final Logs logs;

            switch (location) {
                case "Taverley":
                case "Entrana":          logs = Logs.LOGS; break;
                case "Crafting Guild":   logs = Logs.LOGS_OAK; break;
                case "Varrock":          logs = Logs.LOGS_WILLOW; break;
                case "Castle Wars":      logs = Logs.LOGS_YEW; break;
                case "Gnome Stronghold": logs = Logs.LOGS_MAGIC; break;
                default: return;
            }

            updateStorageLogs(logs, getLogsCount(logs) - 1);
        }
    }

    public void onWidgetLoaded(final WidgetLoaded event) {
        if (event.getGroupId() == WIDGET_STORAGE) {
            final Widget storage = client.getWidget(WIDGET_STORAGE, WIDGET_STORAGE_MESSAGE);
            if (storage != null) {
                updateStorageFromCheck(storage.getText(), true);
            }
        } else if (event.getGroupId() == WIDGET_STORAGE_STORE) {
            final Widget store = client.getWidget(WIDGET_STORAGE_STORE, WIDGET_STORAGE_STORE_MESSAGE);
            if (store != null) {
                updateStorageStore(store.getText());
            }
        }
    }
}
