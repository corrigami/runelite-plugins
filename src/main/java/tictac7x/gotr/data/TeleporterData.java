package tictac7x.gotr.data;

import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;

public class TeleporterData {
    final public int teleporter_id;
    final public int rune_id;
    final public int talisman_id;
    final public EssenceType essence_type;

    public static TeleporterData AIR = new TeleporterData(ObjectID.GUARDIAN_OF_AIR, ItemID.AIR_RUNE, ItemID.PORTAL_TALISMAN_AIR, EssenceType.ELEMENTAL);
    public static TeleporterData WATER = new TeleporterData(ObjectID.GUARDIAN_OF_WATER, ItemID.WATER_RUNE, ItemID.PORTAL_TALISMAN_WATER, EssenceType.ELEMENTAL);

    public TeleporterData(final int teleporter_id, final int rune_id, final int talisman_id, final EssenceType essence_type) {
        this.teleporter_id = teleporter_id;
        this.rune_id = rune_id;
        this.talisman_id = talisman_id;
        this.essence_type = essence_type;
    }
}
