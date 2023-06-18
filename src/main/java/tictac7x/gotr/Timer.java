package tictac7x.gotr;

import net.runelite.api.ChatMessageType;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.config.ConfigManager;

import java.time.Instant;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Timer {
    private final Pattern rift_will_become_active = Pattern.compile("The rift will become active in (?<seconds>.+) seconds.");
    private final String game_ended = "The Portal Guardians will keep their rifts open for another 30 seconds.";

    private final ConfigManager configs;
    private final TicTac7xGotrImprovedConfig config;

    private Optional<Instant> time_to_start = Optional.empty();

    public Timer(final ConfigManager configs, final TicTac7xGotrImprovedConfig config) {
        this.configs = configs;
        this.config = config;
    }

    public Optional<Instant> getTime() {
        return time_to_start;
    }

    public void onChatMesage(final ChatMessage message) {
        if (message.getType() != ChatMessageType.GAMEMESSAGE) return;

        if (message.getMessage().equals(game_ended)) {
            time_to_start = Optional.of(Instant.now().plusSeconds(61));
        } else {
            final Matcher matcher = rift_will_become_active.matcher(message.getMessage());
            if (matcher.find()) {
                time_to_start = Optional.of(Instant.now().plusSeconds(Integer.parseInt(matcher.group("seconds")) + 1));
            }
        }
    }
}
