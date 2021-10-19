package tictac7x.balloon;

import tictac7x.Overlay;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.awt.Dimension;
import java.awt.Graphics2D;
import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.widgets.Widget;
import net.runelite.api.ChatMessageType;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.game.ItemManager;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ComponentOrientation;
import net.runelite.client.ui.overlay.components.ImageComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;

public class BalloonOverlayLogs extends Overlay {
    private static final int WIDGET_STORAGE = 229;
    private static final int WIDGET_STORAGE_MESSAGE = 1;
    private static final int WIDGET_STORAGE_STORE = 193;
    private static final int WIDGET_STORAGE_STORE_MESSAGE = 2;
    private static final int STORAGE_LOGS_TYPES = 5;
    public static final int STORAGE_INDEX_LOGS = 0;
    public static final int STORAGE_INDEX_LOGS_OAK = 1;
    public static final int STORAGE_INDEX_LOGS_WILLOW = 2;
    public static final int STORAGE_INDEX_LOGS_YEW = 3;
    public static final int STORAGE_INDEX_LOGS_MAGIC = 4;

    private final Pattern regex_store = Pattern.compile("You put the (.*) in the crate\\. You now have (\\d+).*");
    private final Pattern regex_fly = Pattern.compile("You board the balloon and fly to (the )?(.*)\\.");
    private final Pattern regex_check = Pattern.compile(".*?(\\d+).*?(\\d+).*?(\\d+).*?(\\d+).*?(\\d+).*?");
    private final SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private final BalloonConfig config;
    private final Client client;
    private final ConfigManager configs;
    private final ItemManager items;

    private final PanelComponent panel = new PanelComponent();
    private final int[] storage = new int[STORAGE_LOGS_TYPES];

    public BalloonOverlayLogs(final BalloonConfig config, final Client client, final ConfigManager configs, final ItemManager items) {
        this.config = config;
        this.client = client;
        this.configs = configs;
        this.items = items;

        updateStorageFromCheck(config.getStorage(), false);

        setPreferredPosition(OverlayPosition.BOTTOM_LEFT);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
    }

    public void onChatMessage(final ChatMessage message) {
        final Matcher matches;
        if (message.getType() == ChatMessageType.GAMEMESSAGE && (matches = regex_fly.matcher(message.getMessage())).matches()) {
            final String location = matches.group(2);
            final int index;

            switch (location) {
                case "Taverley":
                case "Entrana":          index = STORAGE_INDEX_LOGS;        break;
                case "Crafting Guild":   index = STORAGE_INDEX_LOGS_OAK;    break;
                case "Varrock":          index = STORAGE_INDEX_LOGS_WILLOW; break;
                case "Castle Wars":      index = STORAGE_INDEX_LOGS_YEW;    break;
                case "Gnome Stronghold": index = STORAGE_INDEX_LOGS_MAGIC;  break;
                default: return;
            }

            updateStorageLogs(index, storage[index] - 1);
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

    public int getLogsCount(final int index) {
        try {
            return storage[index];
        } catch (final Exception exception) {
            return 0;
        }
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        final BalloonConfig.style style = config.getStyle();

        if (style == BalloonConfig.style.HORIZONTAL || style == BalloonConfig.style.VERTICAL) {
            final Date now;
            try { now = date_format.parse(date_format.format(new Date())); } catch (final Exception ignored) { return null; }
            final int duration = config.getDuration() * 60 * 1000;

            panel.getChildren().clear();
            panel.setOrientation(config.getStyle() == BalloonConfig.style.HORIZONTAL ? ComponentOrientation.HORIZONTAL : ComponentOrientation.VERTICAL);

            try {
                // Logs - Entrana / Taverly.
                if (!config.showRecentOnly() || !config.getStorageDateLogs().equals("") && (now.getTime() - date_format.parse(config.getStorageDateLogs()).getTime() < duration)) {
                    panel.getChildren().add(new ImageComponent(items.getImage(ItemID.LOGS, getLogsCount(STORAGE_INDEX_LOGS), true)));
                }

                // Oak logs - Crafting guild.
                if (!config.showRecentOnly() || !config.getStorageDateLogsOak().equals("") && (now.getTime() - date_format.parse(config.getStorageDateLogsOak()).getTime()) < duration) {
                    panel.getChildren().add(new ImageComponent(items.getImage(ItemID.OAK_LOGS, getLogsCount(STORAGE_INDEX_LOGS_OAK), true)));
                }

                // Willow logs - Varrock.
                if (!config.showRecentOnly() || !config.getStorageDateLogsWillow().equals("") && (now.getTime() - date_format.parse(config.getStorageDateLogsWillow()).getTime()) < duration) {
                    panel.getChildren().add(new ImageComponent(items.getImage(ItemID.WILLOW_LOGS, getLogsCount(STORAGE_INDEX_LOGS_WILLOW), true)));
                }

                // Yew logs - Castle wars.
                if (!config.showRecentOnly() || !config.getStorageDateLogsYew().equals("") && (now.getTime() - date_format.parse(config.getStorageDateLogsYew()).getTime()) < duration) {
                    panel.getChildren().add(new ImageComponent(items.getImage(ItemID.YEW_LOGS, getLogsCount(STORAGE_INDEX_LOGS_YEW), true)));
                }

                // Magic logs - Grand tree.
                if (!config.showRecentOnly() || !config.getStorageDateLogsMagic().equals("") && (now.getTime() - date_format.parse(config.getStorageDateLogsMagic()).getTime()) < duration) {
                    panel.getChildren().add(new ImageComponent(items.getImage(ItemID.MAGIC_LOGS, getLogsCount(STORAGE_INDEX_LOGS_MAGIC), true)));
                }

                return panel.render(graphics);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private void updateStorageStore(final String message) {
        final Matcher store = regex_store.matcher(message);

        if (store.matches()) {
            final int index;
            final String logs = store.group(1);
            final int amount = Integer.parseInt(store.group(2));

            switch (logs) {
                case "Logs":        index = STORAGE_INDEX_LOGS;        break;
                case "Oak logs":    index = STORAGE_INDEX_LOGS_OAK;    break;
                case "Willow logs": index = STORAGE_INDEX_LOGS_WILLOW; break;
                case "Yew logs":    index = STORAGE_INDEX_LOGS_YEW;    break;
                case "Magic logs":  index = STORAGE_INDEX_LOGS_MAGIC;  break;
                default: return;
            }

            updateStorageLogs(index, amount);
        }
    }

    private void updateStorageLogs(final int index, final int amount) {
        storage[index] = amount;
        saveStorageDateToConfig(new int[]{index});
        saveStorageToConfig();
    }

    private void updateStorageFromCheck(final String message, final boolean update_dates) {
        final Matcher check = regex_check.matcher(message);

        // Extract logs amounts from the string.
        if (check.matches()) {
            storage[STORAGE_INDEX_LOGS]        = Integer.parseInt(check.group(1));
            storage[STORAGE_INDEX_LOGS_OAK]    = Integer.parseInt(check.group(2));
            storage[STORAGE_INDEX_LOGS_WILLOW] = Integer.parseInt(check.group(3));
            storage[STORAGE_INDEX_LOGS_YEW]    = Integer.parseInt(check.group(4));
            storage[STORAGE_INDEX_LOGS_MAGIC]  = Integer.parseInt(check.group(5));
        }

        // Save all logs counts into the config.
        saveStorageToConfig();

        // Update all logs dates.
        if (update_dates) {
            saveStorageDateToConfig(new int[]{
                STORAGE_INDEX_LOGS,
                STORAGE_INDEX_LOGS_OAK,
                STORAGE_INDEX_LOGS_WILLOW,
                STORAGE_INDEX_LOGS_YEW,
                STORAGE_INDEX_LOGS_MAGIC
            });
        }
    }

    private void saveStorageDateToConfig(final int[] index) {
        final Date now = new Date();

        for (final int i : index) {
            String key = null;

            switch (i) {
                case STORAGE_INDEX_LOGS:
                    key = BalloonConfig.key_storage_date_logs;
                    break;
                case STORAGE_INDEX_LOGS_OAK:
                    key = BalloonConfig.key_storage_date_logs_oak;
                    break;
                case STORAGE_INDEX_LOGS_WILLOW:
                    key = BalloonConfig.key_storage_date_logs_willow;
                    break;
                case STORAGE_INDEX_LOGS_YEW:
                    key = BalloonConfig.key_storage_date_logs_yew;
                    break;
                case STORAGE_INDEX_LOGS_MAGIC:
                    key = BalloonConfig.key_storage_date_logs_magic;
                    break;
            }

            if (key != null) {
                configs.setConfiguration(BalloonConfig.group, key, date_format.format(now));
            }
        }
    }

    private void saveStorageToConfig() {
        StringBuilder storage = new StringBuilder();
        for (int i = 0; i < STORAGE_LOGS_TYPES; i++) {
            storage.append(this.storage[i]).append(i == STORAGE_LOGS_TYPES - 1 ? "" : ",");
        }

        configs.setConfiguration(config.group, config.key_storage, storage.toString());
    }
}
