package tictac7x.motherlode;

import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;

import javax.inject.Inject;

public class Player {
    @Inject
    private Client client;

    private final int[] MOTHERLODE_REGIONS = new int[]{ 14679, 14680, 14681, 14935, 14936, 14937, 15191, 15192, 15193 };

    private boolean inMotherlode = false;

    public boolean isInMotherlode() {
        return inMotherlode;
    }

    public void onGameStateChanged(final GameStateChanged event) {
        if (event.getGameState() == GameState.LOADING) {
            inMotherlode = checkIsInMotherlode();
        }
    }

    private boolean checkIsInMotherlode() {
        for (final int playerRegion : client.getMapRegions()) {
            for (final int motherlodeRegion : MOTHERLODE_REGIONS) {
                if (motherlodeRegion == playerRegion) {
                    return true;
                }
            }
        }

        return false;
    }
}
