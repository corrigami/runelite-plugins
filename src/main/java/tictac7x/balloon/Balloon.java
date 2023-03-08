package tictac7x.balloon;

import com.google.common.collect.ImmutableSet;
import net.runelite.api.GameState;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;

import java.util.Set;

public class Balloon {
    private final Set<Integer> balloon_game_object_ids = ImmutableSet.of(19133, 19135, 19137, 19139, 19141, 19143);
    private boolean visible = false;

    public void onGameObjectSpawned(final GameObjectSpawned event) {
        if (balloon_game_object_ids.contains(event.getGameObject().getId())) {
            this.visible = true;
        }
    }

    public void onGameStateChanged(final GameStateChanged event) {
        if (event.getGameState() == GameState.LOADING) {
            this.visible = false;
        }
    }

    public boolean isVisible() {
        return this.visible;
    }
}
