package tictac7x.gotr;

import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;

import java.awt.*;

public class Teleporter {
    final public int teleporter_id;
    final public int rune_id;
    final public int talisman_id;
    final private EssenceType essence_type;

    final public static int ACTIVE_ANIMATION_ID = 9363;

    public static Teleporter AIR = new Teleporter(ObjectID.GUARDIAN_OF_AIR, ItemID.AIR_RUNE, ItemID.PORTAL_TALISMAN_AIR, EssenceType.ELEMENTAL);
    public static Teleporter WATER = new Teleporter(ObjectID.GUARDIAN_OF_WATER, ItemID.WATER_RUNE, ItemID.PORTAL_TALISMAN_WATER, EssenceType.ELEMENTAL);
    public static Teleporter EARTH = new Teleporter(ObjectID.GUARDIAN_OF_EARTH, ItemID.EARTH_RUNE, ItemID.PORTAL_TALISMAN_EARTH, EssenceType.ELEMENTAL);
    public static Teleporter FIRE = new Teleporter(ObjectID.GUARDIAN_OF_FIRE, ItemID.FIRE_RUNE, ItemID.PORTAL_TALISMAN_FIRE, EssenceType.ELEMENTAL);

    public static Teleporter MIND = new Teleporter(ObjectID.GUARDIAN_OF_MIND, ItemID.MIND_RUNE, ItemID.PORTAL_TALISMAN_MIND, EssenceType.CATALYST);
    public static Teleporter BODY = new Teleporter(ObjectID.GUARDIAN_OF_BODY, ItemID.BODY_RUNE, ItemID.PORTAL_TALISMAN_BODY, EssenceType.CATALYST);
    public static Teleporter COSMIC = new Teleporter(ObjectID.GUARDIAN_OF_COSMIC, ItemID.COSMIC_RUNE, ItemID.PORTAL_TALISMAN_COSMIC, EssenceType.CATALYST);
    public static Teleporter CHAOS = new Teleporter(ObjectID.GUARDIAN_OF_CHAOS, ItemID.CHAOS_RUNE, ItemID.PORTAL_TALISMAN_CHAOS, EssenceType.CATALYST);
    public static Teleporter NATURE = new Teleporter(ObjectID.GUARDIAN_OF_NATURE, ItemID.NATURE_RUNE, ItemID.PORTAL_TALISMAN_NATURE, EssenceType.CATALYST);
    public static Teleporter LAW = new Teleporter(ObjectID.GUARDIAN_OF_LAW, ItemID.LAW_RUNE, ItemID.PORTAL_TALISMAN_LAW, EssenceType.CATALYST);
    public static Teleporter DEATH = new Teleporter(ObjectID.GUARDIAN_OF_DEATH, ItemID.DEATH_RUNE, ItemID.PORTAL_TALISMAN_DEATH, EssenceType.CATALYST);
    public static Teleporter BLOOD = new Teleporter(ObjectID.GUARDIAN_OF_BLOOD, ItemID.BLOOD_RUNE, ItemID.PORTAL_TALISMAN_BLOOD, EssenceType.CATALYST);

    public static Teleporter[] ALL = new Teleporter[]{
        AIR, WATER, EARTH, FIRE, MIND, BODY, COSMIC, CHAOS, NATURE, LAW, DEATH, BLOOD
    };

    public Teleporter(final int teleporter_id, final int rune_id, final int talisman_id, final EssenceType essence_type) {
        this.teleporter_id = teleporter_id;
        this.rune_id = rune_id;
        this.talisman_id = talisman_id;
        this.essence_type = essence_type;
    }

    public boolean isElemental() {
        return essence_type == EssenceType.ELEMENTAL;
    }

    public boolean isCatalyst() {
        return essence_type == EssenceType.CATALYST;
    }
}