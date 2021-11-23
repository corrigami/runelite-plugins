package tictac7x.motherlode;

import com.google.common.collect.ImmutableSet;
import net.runelite.api.TileObject;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MotherlodeVeins {
    private final Motherlode motherlode;

    private final Set<Integer> ORE_VEINS = ImmutableSet.of(26661, 26662, 26663, 26664);
    private final Set<Integer> ORE_VEINS_DEPLETED = ImmutableSet.of(26665, 26666, 26667, 26668);
    private final int ORE_VEIN_RESPAWN = 180;

    private final Set<OreVein> ore_veins = new HashSet<>();

    public MotherlodeVeins(final Motherlode motherlode) {
        this.motherlode = motherlode;
    }

    public void onTileObjectSpawned(final TileObject object) {
        if (!motherlode.inRegion()) return;

        if (isOreVein(object) || isOreVeinDepleted(object)) {
            final boolean depleted = isOreVeinDepleted(object);
            final int x = object.getWorldLocation().getX();
            final int y = object.getWorldLocation().getY();

            final Optional<OreVein> ore_vein = ore_veins.stream().filter(ore_vein_depleted ->
                ore_vein_depleted.x == x &&
                ore_vein_depleted.y == y
            ).findAny();

            if (ore_vein.isPresent()) {
                ore_vein.get().depleted = depleted;
                ore_vein.get().ticks = 0;
            } else {
                ore_veins.add(new OreVein(
                        x, y,
                        motherlode.getSector(x, y),
                        depleted
                ));
            }
        }
    }

    public void onTileObjectDespawned(final TileObject object) {
        if (!motherlode.inRegion()) return;

        if (isOreVeinDepleted(object)) {
            ore_veins.stream().filter(ore_vein_depleted ->
                    object.getWorldLocation().getX() == ore_vein_depleted.x &&
                            object.getWorldLocation().getY() == ore_vein_depleted.y
            ).findAny().ifPresent(ore_veins::remove);
        }
    }

    public void onGameTick() {
        if (!motherlode.inRegion()) return;

        for (final OreVein ore_vein_depleted : ore_veins) {
            ore_vein_depleted.onGameTick();
        }
    }

    public boolean isOreVein(final TileObject object) {
        return ORE_VEINS.contains(object.getId());
    }

    public boolean isOreVeinDepleted(final TileObject object) {
        return ORE_VEINS_DEPLETED.contains(object.getId());
    }

    public float getDepletedOreVeinProgress(final TileObject object) {
        final Optional<OreVein> ore_vein = ore_veins.stream().filter(ore_vein_depleted ->
                object.getWorldLocation().getX() == ore_vein_depleted.x &&
                        object.getWorldLocation().getY() == ore_vein_depleted.y
        ).findAny();

        return (ore_vein.isPresent()) ? (float) -ore_vein.get().ticks / ORE_VEIN_RESPAWN : 0;
    }

    @Nullable
    public OreVein getOreVein(final TileObject object) {
        return ore_veins.stream().filter(ore_vein ->
            object.getWorldLocation().getX() == ore_vein.x &&
            object.getWorldLocation().getY() == ore_vein.y
        ).findAny().orElse(null);
    }
}
