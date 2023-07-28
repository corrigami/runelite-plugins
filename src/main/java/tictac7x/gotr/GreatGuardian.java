package tictac7x.gotr;

import net.runelite.api.GameState;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.events.GameStateChanged;

import java.util.Optional;

public class GreatGuardian {
    private Optional<NPC> great_guardian = Optional.empty();

    public Optional<NPC> getGreatGuardian() {
        return great_guardian;
    }

    public void onNpcSpawned(final NPC npc) {
        if (npc.getId() == NpcID.THE_GREAT_GUARDIAN) {
            great_guardian = Optional.of(npc);
        }
    }

    public void onNpcDespawned(final NPC npc) {
        if (npc.getId() == NpcID.THE_GREAT_GUARDIAN) {
            great_guardian = Optional.empty();
        }
    }

    public void onGameStateChanged(final GameState game_state) {
        if (game_state == GameState.LOADING) {
            great_guardian = Optional.empty();
        }
    }
}
