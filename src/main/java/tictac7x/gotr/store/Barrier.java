package tictac7x.gotr.store;

import net.runelite.api.GameObject;
import tictac7x.gotr.Notifications;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

public class Barrier {
    private final int BARRIER_ACTIVE = 43700;
    private final int BARRIER_INACTIVE = 43849;
    private final int BARRIER_REENTER_GAMETICKS = 51;

    private final Notifications notifications;

    private int gameTickInactiveBarrierDespawned = 0;
    private Optional<Instant> barrierReenterTimeLeft = Optional.empty();
    private boolean notifiedBeforePassableBarrier = false;
    private boolean notifiedPassableBarrier = false;

    public Barrier(final Notifications notifications) {
        this.notifications = notifications;
    }

    public Optional<Instant> getBarrierReenterTimeLeft() {
        return barrierReenterTimeLeft;
    }

    public void onGameObjectSpawned(final GameObject gameObject) {
        if (gameObject.getId() == BARRIER_ACTIVE && gameTickInactiveBarrierDespawned == 1) {
            barrierReenterTimeLeft = Optional.of(Instant.now().plusMillis(BARRIER_REENTER_GAMETICKS * 600));
            notifiedBeforePassableBarrier = false;
            notifiedPassableBarrier = false;
        }
    }

    public void onGameObjectDespawned(final GameObject gameObject) {
        if (gameObject.getId() == BARRIER_INACTIVE) {
            gameTickInactiveBarrierDespawned = 1;
        }
    }

    public void onGameTick() {
        if (gameTickInactiveBarrierDespawned < 10) {
            gameTickInactiveBarrierDespawned++;
        }

        if (barrierReenterTimeLeft.isPresent()) {
            final long seconds = Duration.between(Instant.now(), barrierReenterTimeLeft.get()).getSeconds();

            if (seconds < 5 && !notifiedBeforePassableBarrier) {
                notifiedBeforePassableBarrier = true;
                notifications.notifyBeforePassableBarrier();
            }

            if (seconds < 0 && !notifiedPassableBarrier) {
                notifiedPassableBarrier = true;
                notifications.notifyAboutPassableBarrier();
            }
        }
    }
}
