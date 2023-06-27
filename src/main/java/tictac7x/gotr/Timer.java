package tictac7x.gotr;

import net.runelite.api.ChatMessageType;
import net.runelite.api.events.ChatMessage;

import java.time.Instant;
import java.util.Optional;

public class Timer {
    private final String game_starting_30 = "The rift will become active in 30 seconds.";
    private final String game_starting_10 = "The rift will become active in 10 seconds.";
    private final String game_starting_5 = "The rift will become active in 5 seconds.";
    private final String game_ended = "The Portal Guardians will keep their rifts open for another 30 seconds.";

    private Optional<Instant> time_to_start = Optional.empty();

    public Optional<Instant> getTime() {
        return time_to_start;
    }

    public void onChatMesage(final ChatMessage message) {
        if (message.getType() != ChatMessageType.GAMEMESSAGE) return;

        if (message.getMessage().equals(game_ended)) {
            time_to_start = Optional.of(Instant.now().plusSeconds(61));
        } else if (message.getMessage().equals(game_starting_30)) {
            time_to_start = Optional.of(Instant.now().plusMillis(51 * 600));
        } else if (message.getMessage().equals(game_starting_10)) {
            time_to_start = Optional.of(Instant.now().plusMillis(17 * 600));
        } else if (message.getMessage().equals(game_starting_5)) {
            time_to_start = Optional.of(Instant.now().plusMillis(9 * 600));
        }
    }
}
