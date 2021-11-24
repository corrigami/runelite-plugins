package tictac7x.motherlode;

import java.util.Set;
import java.util.Arrays;
import javax.annotation.Nullable;
import com.google.common.collect.ImmutableSet;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;


public class Motherlode {
    private final Client client;
    private final MotherlodeInventory inventory;
    private final MotherlodeSack sack;
    private final MotherlodeVeins veins;
    private final MotherlodeRockfalls rockfalls;

    private final int DRAW_DISTANCE = 4000;
    private final Set<Integer> REGIONS = ImmutableSet.of(14679, 14680, 14681, 14935, 14936, 14937, 15191, 15192, 15193);

    private boolean in_region = false;
    private int pay_dirt_needed = 0;

    @Nullable
    private Sector player_sector = null;
    @Nullable
    private Integer player_x = null;
    @Nullable
    private Integer player_y = null;
    
    public Motherlode(final Client client) {
        this.client = client;
        this.inventory = new MotherlodeInventory(this);
        this.sack = new MotherlodeSack(this, inventory, client);
        this.veins = new MotherlodeVeins(this);
        this.rockfalls = new MotherlodeRockfalls(this);
    }

    public MotherlodeInventory getInventory() {
        return inventory;
    }

    public MotherlodeSack getSack() {
        return sack;
    }

    public MotherlodeVeins getVeins() {
        return veins;
    }

    public int getDrawDistance() {
        return DRAW_DISTANCE;
    }

    public MotherlodeRockfalls getRockfalls() {
        return rockfalls;
    }

    public Sector getSector(final int x, final int y) {
        final int[] location = new int[]{x, y};

        if (isInSector(UPSTAIRS_1, location)) {
            return Sector.UPSTAIRS_1;
        } else if (isInSector(UPSTAIRS_2, location)) {
            return Sector.UPSTAIRS_2;
        } else if (isInSector(UPSTAIRS_3, location)) {
            return Sector.UPSTAIRS_3;
        } else if (isInSector(UPSTAIRS_4, location)) {
            return Sector.UPSTAIRS_4;
        } else if (isInSector(UPSTAIRS_5, location)) {
            return Sector.UPSTAIRS_5;
        } else if (isInSector(UPSTAIRS_6, location)) {
            return Sector.UPSTAIRS_6;
        }

        return Sector.DOWNSTAIRS;
    }

    private boolean isInSector(final int[][] sector, final int[] location) {
        for (int[] point : sector) {
            if (Arrays.equals(point, location)) {
                return true;
            }
        }

        return false;
    }

    public void onGameStateChanged(final GameStateChanged event) {
        if (event.getGameState() == GameState.LOADING) {
            in_region = updateInRegion();
        }
    }

    public void onGameTick() {
        if (!in_region || client.getLocalPlayer() == null) return;

        final int player_x = client.getLocalPlayer().getWorldLocation().getX();
        final int player_y = client.getLocalPlayer().getWorldLocation().getY();

        if (this.player_x == null || this.player_y == null || this.player_x != player_x || this.player_y != player_y) {
            this.player_x = player_x;
            this.player_y = player_y;
            player_sector = getSector(player_x, player_y);
        }
    }

    public Sector getPlayerSector() {
        return player_sector;
    }

    private boolean updateInRegion() {
        if (client == null || client.getMapRegions() == null) return false;

        for (final int region : client.getMapRegions()) {
            if (REGIONS.contains(region)) {
                return true;
            }
        }

        return false;
    }

    public boolean inRegion() {
        return in_region;
    }

    public void updatePayDirtNeeded() {
        final int sack_size = sack.getSize();
        final int sack_pay_dirt = sack.countPayDirt();
        final int inventory_pay_dirt = inventory.countPayDirt();
        final int inventory_items = inventory.countItems();
        final int inventory_size = inventory.getSize();

        // Sack is full, but pay-dirt can be deposited one more time.
        // Needed pay dirt is all available space in inventory.
        if (sack_pay_dirt == sack_size) {
            pay_dirt_needed = inventory_size - inventory_items;
            return;

        // Sack is more than full, need to be emptied.
        } else if (sack_pay_dirt > sack_size) {
            pay_dirt_needed = inventory_pay_dirt * -1;
            return;
        }

        // Sack is not full.
        final int to_mine = sack_size - sack_pay_dirt;

        // More than 1 inventory needs to be deposited.
        if (to_mine > inventory_size) {
            pay_dirt_needed = inventory_size - inventory_items;

        // Last deposit to get sack full, but to be able to deposit one more time.
        } else {
            pay_dirt_needed = Math.min(to_mine - inventory_pay_dirt, inventory_size - inventory_items);
        }
    }

    public int getPayDirtNeeded() {
        return pay_dirt_needed;
    }

    private final int[][] UPSTAIRS_1 = {
        {3743,5687},    {3744,5687},
        {3735,5686},    {3736,5686},                                                    {3740,5686},    {3741,5686},    {3742,5686},    {3743,5686},    {3744,5686},    {3745,5686},    {3746,5686},    {3747,5686},
        {3734,5685},    {3735,5685},    {3736,5685},    {3737,5685},    {3738,5685},    {3739,5685},    {3740,5685},    {3741,5685},    {3742,5685},                                    {3745,5685},    {3746,5685},    {3747,5685},    {3748,5685},
        {3735,5684},                    {3737,5684},    {3738,5684},    {3739,5684},    {3740,5684},    {3741,5684},    {3742,5684},    {3743,5684},                    {3745,5684},    {3746,5684},                    {3748,5684},
        {3736,5683},    {3737,5683},    {3738,5683},    {3739,5683},                    {3741,5683},    {3742,5683},
        {3737,5682},    {3738,5682}
    };

    private final int[][] UPSTAIRS_2 = {
        {3750,5685},
        {3748,5684},    {3749,5684},    {3750,5684},                    {3752,5684},    {3753,5684},
        {3747,5683},    {3748,5683},    {3749,5683},    {3750,5683},    {3751,5683},    {3752,5683},    {3753,5683},    {3754,5683},
        {3748,5682},    {3749,5682},    {3750,5682},                    {3752,5682},    {3753,5682},    {3754,5682},
        {3751,5681},    {3752,5681},
        {3751,5680},    {3752,5680},    {3753,5680},
        {3752,5679},
        {3751,5678},    {3752,5678},    {3753,5678},    {3754,5678},
        {3753,5677},                    {3755,5677},
        {3752,5676},    {3753,5676},    {3754,5676},    {3755,5676},    {3756,5676},
        {3754,5675},    {3755,5675},
    };

    private final int[][] UPSTAIRS_3 = {
        {3758,5685},    {3759,5685},    {3760,5685},    {3761,5685},
        {3756,5684},    {3757,5684},                    {3759,5684},    {3760,5684},    {3761,5684},
        {3755,5683},    {3756,5683},    {3757,5683},    {3758,5683},    {3759,5683},    {3760,5683},    {3761,5683},    {3762,5683},
        {3755,5682},    {3756,5682},    {3757,5682},                    {3759,5682},                    {3761,5682},
        {3755,5681},    {3756,5681},    {3757,5681},    {3758,5681},                                    {3761,5681},
        {3757,5680},    {3758,5680},
        {3756,5679},    {3757,5679},
        {3756,5678},    {3757,5678},    {3758,5678},
        {3757,5677},
    };

    private final int[][] UPSTAIRS_4 = {
        {3763,5683},    {3764,5683},
        {3763,5682},    {3764,5682},
        {3763,5681},    {3764,5681},
        {3760,5680},    {3761,5680},    {3762,5680},                    {3764,5680},
        {3760,5679},    {3761,5679},    {3762,5679},    {3763,5679},    {3764,5679},    {3765,5679},
        {3758,5678},    {3759,5678},    {3760,5678},    {3761,5678},    {3762,5678},    {3763,5678},    {3764,5678},    {3765,5678},    {3766,5678},
        {3757,5677},    {3758,5677},    {3759,5677},    {3760,5677},    {3761,5677},    {3762,5677},                    {3764,5677},    {3765,5677},
        {3759,5676},                    {3761,5676},    {3762,5676},
        {3761,5675},    {3762,5675},
    };

    private final int[][] UPSTAIRS_5 = {
        {3755,5676},    {3756,5676},    {3757,5676},
        {3755,5675},    {3756,5675},    {3757,5675},    {3758,5675},
        {3756,5674},    {3757,5674},                    {3759,5674},    {3760,5674},    {3761,5674},
        {3757,5673},    {3758,5673},    {3759,5673},    {3760,5673},    {3761,5673},    {3762,5673},
        {3759,5672},    {3760,5672},    {3761,5672},    {3762,5672},
        {3760,5671},    {3761,5671},    {3762,5671},
        {3761,5670},    {3762,5670},
        {3761,5669},
        {3761,5668},
    };

    private final int[][] UPSTAIRS_6 = {
        {3763,5670},    {3764,5670},
        {3764,5669},
        {3762,5668},    {3763,5668},    {3764,5668},
        {3762,5667},    {3763,5667},    {3764,5667},
        {3763,5666},
        {3762,5665},    {3763,5665},    {3764,5665},
        {3761,5664},    {3762,5664},
        {3761,5663},    {3762,5663},    {3763,5663},
        {3761,5662},    {3762,5662},    {3763,5662},
        {3761,5661},    {3762,5661},
        {3761,5660},    {3762,5660},    {3763,5660},
        {3763,5659},    {3764,5659},
        {3762,5658},    {3763,5658},    {3764,5658},    {3765,5658},
        {3762,5657},    {3763,5657},    {3764,5657},    {3765,5657},    {3766,5657},
        {3763,5656},    {3764,5656},    {3765,5656},
    };
}
