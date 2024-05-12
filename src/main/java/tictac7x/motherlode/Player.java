package tictac7x.motherlode;

import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;

import javax.inject.Inject;

import java.util.Arrays;
import java.util.List;

import static tictac7x.motherlode.Sector.DOWNSTAIRS;

public class Player {
    private final Client client;

    public Player(final Client client) {
        this.client = client;
    }

    private final int[] MOTHERLODE_REGIONS = new int[]{ 14679, 14680, 14681, 14935, 14936, 14937, 15191, 15192, 15193 };

    private boolean inMotherlode = false;
    private List<Sector> sectors = Arrays.asList(DOWNSTAIRS);

    public boolean isInMotherlode() {
        return inMotherlode;
    }

    public List<Sector> getSectors() {
        return sectors;
    }

    public void onGameTick() {
        this.sectors = Motherlode.getSectors(client.getLocalPlayer().getWorldLocation().getX(), client.getLocalPlayer().getWorldLocation().getY(), true);
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
