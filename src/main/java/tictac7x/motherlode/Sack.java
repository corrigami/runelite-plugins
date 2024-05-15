package tictac7x.motherlode;

import net.runelite.api.Client;
import net.runelite.api.Varbits;


public class Sack {
    private final Client client;

    private final int SACK_SIZE_SMALL = 108;
    private final int SACK_SIZE_UPGRADED = 189;

    public Sack(final Client client) {
        this.client = client;
    }

    public int getPaydirt() {
        return client.getVarbitValue(Varbits.SACK_NUMBER);
    }

    private boolean isSackUpgraded() {
        return client.getVarbitValue(Varbits.SACK_UPGRADED) == 1;
    }

    public int getSize() {
        return isSackUpgraded() ? SACK_SIZE_UPGRADED : SACK_SIZE_SMALL;
    }
}
