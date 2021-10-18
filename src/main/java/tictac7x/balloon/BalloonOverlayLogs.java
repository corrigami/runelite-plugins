package tictac7x.balloon;

import lombok.SneakyThrows;
import tictac7x.Overlay;

import java.util.Date;
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
import net.runelite.client.ui.overlay.components.ComponentOrientation;
import net.runelite.client.ui.overlay.components.ImageComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;

public class BalloonOverlayLogs extends Overlay {
    private static final int WIDGET_STORAGE = 229;
    private static final int WIDGET_STORAGE_MESSAGE = 1;
    private static final int STORAGE_LOGS_TYPES = 5;
    public static final int STORAGE_INDEX_LOGS = 0;
    public static final int STORAGE_INDEX_LOGS_OAK = 1;
    public static final int STORAGE_INDEX_LOGS_WILLOW = 2;
    public static final int STORAGE_INDEX_LOGS_YEW = 3;
    public static final int STORAGE_INDEX_LOGS_MAGIC = 4;

    private final BalloonConfig config;
    private final Client client;
    private final ConfigManager configs;
    private final ItemManager items;

    private final PanelComponent panel = new PanelComponent();
    private final int[] storage = new int[STORAGE_LOGS_TYPES];
    private final SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public BalloonOverlayLogs(final BalloonConfig config, final Client client, final ConfigManager configs, final ItemManager items) {
        this.config = config;
        this.client = client;
        this.configs = configs;
        this.items = items;

        updateStorage(config.getStorage(), false);
    }

    public void onChatMessage(final ChatMessage message) {
        if (message.getType() == ChatMessageType.GAMEMESSAGE) {
            final String msg = message.getMessage();
            final int index;

            switch (msg) {
                case "You board the balloon and fly to Taverley.":
                case "You board the balloon and fly to Entrana.":
                    index = STORAGE_INDEX_LOGS;
                    break;
                case "You board the balloon and fly to the Crafting Guild.":
                    index = STORAGE_INDEX_LOGS_OAK;
                    break;
                case "You board the balloon and fly to Varrock.":
                    index = STORAGE_INDEX_LOGS_WILLOW;
                    break;
                case "You board the balloon and fly to Castle Wars.":
                    index = STORAGE_INDEX_LOGS_YEW;
                    break;
                case "You board the balloon and fly to the Gnome Stronghold.":
                    index = STORAGE_INDEX_LOGS_MAGIC;
                    break;
                default:
                    return;
            }

            // Deduct 1 log from storage.
            storage[index] -= 1;

            // Save storage state to config.
            updateStorage();

            // Update used log date.
            updateStorageDate(new int[]{index});
        }
    }

    public void onWidgetLoaded(final WidgetLoaded event) {
        if (event.getGroupId() == WIDGET_STORAGE) {

            final Widget storage = client.getWidget(WIDGET_STORAGE, WIDGET_STORAGE_MESSAGE);
            if (storage != null) {
                updateStorage(storage.getText(), true);
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

    @SneakyThrows
    @Override
    public Dimension render(Graphics2D graphics) {
        final BalloonConfig.style style = config.getStyle();

        if (style == BalloonConfig.style.HORIZONTAL || style == BalloonConfig.style.VERTICAL) {
            final Date now = date_format.parse(date_format.format(new Date()));
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

    private void updateStorage(String message, final boolean update_dates) {
        message = message.replaceAll("[^0-9]+", ",");
        message = message.startsWith(",") ? message.substring(1) : message;
        String[] logs = message.split(",");

        // Update all logs counts.
        for (int i = 0; i < STORAGE_LOGS_TYPES; i++) {
            storage[i] = Integer.parseInt(logs[i]);
        }

        // Save all logs counts into the config.
        updateStorage();

        // Update all logs dates.
        if (update_dates) {
            updateStorageDate(new int[]{
                STORAGE_INDEX_LOGS,
                STORAGE_INDEX_LOGS_OAK,
                STORAGE_INDEX_LOGS_WILLOW,
                STORAGE_INDEX_LOGS_YEW,
                STORAGE_INDEX_LOGS_MAGIC
            });
        }
    }

    private void updateStorageDate(final int[] index) {
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

    private void updateStorage() {
        StringBuilder storage = new StringBuilder();
        for (int i = 0; i < STORAGE_LOGS_TYPES; i++) {
            storage.append(this.storage[i]).append(i == STORAGE_LOGS_TYPES - 1 ? "" : ",");
        }

        configs.setConfiguration(config.group, config.key_storage, storage.toString());
    }
}
