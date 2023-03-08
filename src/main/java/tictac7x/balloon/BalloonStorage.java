package tictac7x.balloon;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;

public class BalloonStorage {
    private static final int WIDGET_CHECK_PARENT_ID = 229;
    private static final int WIDGET_CHECK_CHILD_ID = 1;
    private static final int WIDGET_STORE_PARENT_ID = 193;
    private static final int WIDGET_STORE_CHILD_ID = 2;

    private final Pattern regex_left_plural = Pattern.compile("You have (?<count>.+) sets of (?<type>.*) left in storage.");
    private final Pattern regex_left_singular = Pattern.compile("You have one set of (?<type>.*) left in storage.");
    private final Pattern regex_last = Pattern.compile("You used the last of your (?<type>.*).");
    private final Pattern regex_store = Pattern.compile("You put the (.*) in the crate. You now have (?<count>.+) stored.");
    private final Pattern regex_needed = Pattern.compile("You need 1 (?<type>.*) logs to make this trip.");
    private final Pattern regex_check = Pattern.compile("This crate currently contains (?<regular>.+) logs, (?<oak>.+) oak logs, (?<willow>.+) willow logs, (?<yew>.+) yew logs and (?<magic>.+) magic logs.");

    private final Client client;
    private final ClientThread client_thread;
    private final ConfigManager configs;

    public BalloonStorage(final Client client, final ClientThread client_thread, final ConfigManager configs) {
        this.client = client;
        this.client_thread = client_thread;
        this.configs = configs;
    }

    public void onWidgetLoaded(final WidgetLoaded event) {
        if (event.getGroupId() == WIDGET_CHECK_PARENT_ID) {
            client_thread.invokeLater(() -> {
                final Widget widget = client.getWidget(WIDGET_CHECK_PARENT_ID, WIDGET_CHECK_CHILD_ID);
                if (widget == null) return;

                final String message = widget.getText().replaceAll("<br>", " ");
                updateLogsCountFromCheckMessage(message);
                updateLogsCountFromNeededMessage(message);
            });
        } else if (event.getGroupId() == WIDGET_STORE_PARENT_ID) {
            client_thread.invokeLater(() -> {
                final Widget widget = client.getWidget(WIDGET_STORE_PARENT_ID, WIDGET_STORE_CHILD_ID);
                if (widget == null) return;

                final String message = widget.getText().replaceAll("<br>", " ");
                updateLogsCountFromStoreMessage(message);
            });
        }
    }

    public void onChatMessage(final ChatMessage event) {
        if (event.getType() != ChatMessageType.SPAM) return;
        updateLogsCountFromLeftPluralMessage(event.getMessage());
        updateLogsCountFromLeftSingularMessage(event.getMessage());
        updateLogsCountFromLastMessage(event.getMessage());
    }

    private void updateLogsCountFromLeftPluralMessage(final String message) {
        final Matcher matcher = regex_left_plural.matcher(message);
        if (!matcher.find()) return;

        final String type = matcher.group("type");
        final int count = Integer.parseInt(matcher.group("count"));

        saveLogsCountBasedOnType(type, count);
    }

    private void updateLogsCountFromLeftSingularMessage(final String message) {
        final Matcher matcher = regex_left_singular.matcher(message);
        if (!matcher.find()) return;

        final String type = matcher.group("type");
        saveLogsCountBasedOnType(type, 1);
    }

    private void updateLogsCountFromLastMessage(final String message) {
        final Matcher matcher = regex_last.matcher(message);
        if (!matcher.find()) return;

        final String type = matcher.group("type");
        saveLogsCountBasedOnType(type, 0);
    }

    private void updateLogsCountFromCheckMessage(final String message) {
        final Matcher matcher = regex_check.matcher(message);
        if (!matcher.find()) return;

        final int logs_regular = Integer.parseInt(matcher.group("regular"));
        final int logs_oak     = Integer.parseInt(matcher.group("oak"    ));
        final int logs_willow  = Integer.parseInt(matcher.group("willow" ));
        final int logs_yew     = Integer.parseInt(matcher.group("yew"    ));
        final int logs_magic   = Integer.parseInt(matcher.group("magic"  ));

        saveLogsCountBasedOnType("normal", logs_regular);
        saveLogsCountBasedOnType("oak",    logs_oak    );
        saveLogsCountBasedOnType("willow", logs_willow );
        saveLogsCountBasedOnType("yew",    logs_yew    );
        saveLogsCountBasedOnType("magic",  logs_magic  );
    }

    private void updateLogsCountFromNeededMessage(final String message) {
        final Matcher matcher = regex_needed.matcher(message);
        if (!matcher.find()) return;

        final String type = matcher.group("type");
        saveLogsCountBasedOnType(type, 0);
    }

    private void updateLogsCountFromStoreMessage(final String message) {
        final Matcher matcher = regex_store.matcher(message);
        if (!matcher.find()) return;

        final String type = matcher.group("type");
        final int count = Integer.parseInt(matcher.group("count"));

        saveLogsCountBasedOnType(type, count);
    }

    private void saveLogsCountBasedOnType(final String type, final int count) {
        switch (type) {
            case "Logs":
            case "normal":
                configs.setConfiguration(BalloonConfig.group, BalloonConfig.logs_regular, count);
                break;
            case "Oak logs":
            case "oak":
                configs.setConfiguration(BalloonConfig.group, BalloonConfig.logs_oak,     count);
                break;
            case "Willow logs":
            case "willow":
                configs.setConfiguration(BalloonConfig.group, BalloonConfig.logs_willow,  count);
                break;
            case "Yew logs":
            case "yew":
                configs.setConfiguration(BalloonConfig.group, BalloonConfig.logs_yew,     count);
                break;
            case "Magic logs":
            case "magic":
                configs.setConfiguration(BalloonConfig.group, BalloonConfig.logs_magic,   count);
                break;
        }
    }
}
