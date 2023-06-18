package tictac7x.gotr;

import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemID;

public class Inventory {
    private boolean has_uncharged_cell;
    private boolean has_charged_cell;
    private boolean has_elemental_stones;
    private boolean has_catalytical_stones;
    private boolean has_air_talisman;
    private boolean has_mind_talisman;
    private boolean has_water_talisman;
    private boolean has_earth_talisman;
    private boolean has_fire_talisman;
    private boolean has_cosmic_talisman;
    private boolean has_nature_talisman;
    private boolean has_chaos_talisman;
    private boolean has_body_talisman;
    private boolean has_death_talisman;
    private boolean has_law_talisman;
    private boolean has_blood_talisman;

    public boolean hasUnchargedCell() {
        return has_uncharged_cell;
    }

    public boolean hasChargedCell() {
        return has_charged_cell;
    }

    public boolean hasElementalStones() {
        return has_elemental_stones;
    }

    public boolean hasCatalyticalStones() {
        return has_catalytical_stones;
    }

    public boolean hasAirTalisman() {
        return has_air_talisman;
    }

    public boolean hasBloodTalisman() {
        return has_blood_talisman;
    }

    public boolean hasBodyTalisman() {
        return has_body_talisman;
    }

    public boolean hasChaosTalisman() {
        return has_chaos_talisman;
    }

    public boolean hasCosmicTalisman() {
        return has_cosmic_talisman;
    }

    public boolean hasDeathTalisman() {
        return has_death_talisman;
    }

    public boolean hasEarthTalismanalisman() {
        return has_earth_talisman;
    }

    public boolean hasFireTalisman() {
        return has_fire_talisman;
    }

    public boolean hasLawTalisman() {
        return has_law_talisman;
    }

    public boolean hasMindTalisman() {
        return has_mind_talisman;
    }

    public boolean hasNatureTalisman() {
        return has_nature_talisman;
    }

    public boolean hasWaterTalisman() {
        return has_water_talisman;
    }

    public void onItemContainerChanged(final ItemContainer inventory) {
        if (inventory.getId() != InventoryID.INVENTORY.getId()) return;

        has_charged_cell = inventory.contains(ItemID.WEAK_CELL) || inventory.contains(ItemID.MEDIUM_CELL) || inventory.contains(ItemID.STRONG_CELL) || inventory.contains(ItemID.OVERCHARGED_CELL);

        has_uncharged_cell = inventory.contains(ItemID.UNCHARGED_CELL);

        has_elemental_stones = inventory.contains(ItemID.ELEMENTAL_GUARDIAN_STONE) || inventory.contains(ItemID.POLYELEMENTAL_GUARDIAN_STONE);

        has_catalytical_stones = inventory.contains(ItemID.CATALYTIC_GUARDIAN_STONE);

        has_air_talisman = inventory.contains(ItemID.PORTAL_TALISMAN_AIR);

        has_mind_talisman = inventory.contains(ItemID.PORTAL_TALISMAN_MIND);

        has_water_talisman = inventory.contains(ItemID.PORTAL_TALISMAN_WATER);

        has_earth_talisman = inventory.contains(ItemID.PORTAL_TALISMAN_EARTH);

        has_cosmic_talisman = inventory.contains(ItemID.PORTAL_TALISMAN_COSMIC);

        has_nature_talisman = inventory.contains(ItemID.PORTAL_TALISMAN_NATURE);

        has_chaos_talisman = inventory.contains(ItemID.PORTAL_TALISMAN_CHAOS);

        has_death_talisman = inventory.contains(ItemID.PORTAL_TALISMAN_DEATH);

        has_body_talisman = inventory.contains(ItemID.PORTAL_TALISMAN_BODY);

        has_fire_talisman = inventory.contains(ItemID.PORTAL_TALISMAN_FIRE);

        has_blood_talisman = inventory.contains(ItemID.PORTAL_TALISMAN_BLOOD);

        has_law_talisman = inventory.contains(ItemID.PORTAL_TALISMAN_LAW);
    }
}
