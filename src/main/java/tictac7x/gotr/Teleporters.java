package tictac7x.gotr;

import net.runelite.api.DynamicObject;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.ObjectID;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Teleporters {
    private final List<Integer> teleporter_elemental_ids = Arrays.asList(ObjectID.GUARDIAN_OF_AIR, ObjectID.GUARDIAN_OF_WATER, ObjectID.GUARDIAN_OF_EARTH, ObjectID.GUARDIAN_OF_FIRE);
    private final List<Integer> teleporter_catalytic_ids = Arrays.asList(ObjectID.GUARDIAN_OF_MIND, ObjectID.GUARDIAN_OF_BODY, ObjectID.GUARDIAN_OF_COSMIC, ObjectID.GUARDIAN_OF_CHAOS, ObjectID.GUARDIAN_OF_NATURE, ObjectID.GUARDIAN_OF_LAW, ObjectID.GUARDIAN_OF_DEATH, ObjectID.GUARDIAN_OF_BLOOD);

    private Set<GameObject> teleporters = new HashSet<>();
    private Optional<Instant> teleporters_time_left = Optional.empty();

    private final List<Integer> teleporters_active_ids = new ArrayList<>();

    public Set<GameObject> getTeleporters() {
        return teleporters;
    }

    public Optional<Instant> getTeleportersTimeLeft() {
        return teleporters_time_left;
    }

    public void onGameObjectSpawned(final GameObject object) {
        if (teleporter_elemental_ids.contains(object.getId()) || teleporter_catalytic_ids.contains(object.getId())) teleporters.add(object);
    }

    public void onGameObjectDespawned(final GameObject object) {
        if (teleporter_elemental_ids.contains(object.getId()) || teleporter_catalytic_ids.contains(object.getId())) teleporters.remove(object);
    }

    public void onGameStateChanged(final GameState state) {
        if (state == GameState.LOADING) {
            teleporters.clear();
        }
    }

    public void onGameTick() {
        boolean updated = false;

        for (final GameObject teleporter : teleporters) {
            if (((DynamicObject) teleporter.getRenderable()).getAnimation().getId() == 9363) {
                if (!teleporters_active_ids.contains(teleporter.getId())) {
                    if (!updated) teleporters_active_ids.clear();
                    updated = true;
                    teleporters_active_ids.add(teleporter.getId());
                }
            }
        }

        if (updated) {
            teleporters_time_left = Optional.of(Instant.now().plusMillis(33 * 600));
        }
    }

    public boolean isElementalTeleporter(final int id) {
        return teleporter_elemental_ids.contains(id);
    }

    public boolean isCatalyticTeleporter(final int id) {
        return teleporter_catalytic_ids.contains(id);
    }
}
