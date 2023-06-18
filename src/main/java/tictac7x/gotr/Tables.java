package tictac7x.gotr;

import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.ObjectID;

public class Tables {
    private GameObject uncharged_cells_table;

    public GameObject getUnchargedCellsTable() {
        return uncharged_cells_table;
    }

    public void onGameObjectSpawned(final GameObject object) {
        if (object.getId() == ObjectID.UNCHARGED_CELLS_43732) uncharged_cells_table = object;
    }

    public void onGameObjectDespawned(final GameObject object) {
        if (object.getId() == ObjectID.UNCHARGED_CELLS_43732) uncharged_cells_table = null;
    }

    public void onGameStateChanged(final GameState state) {
        if (state == GameState.LOADING) uncharged_cells_table = null;
    }
}
