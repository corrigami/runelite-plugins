package tictac7x.gotr;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.ObjectID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Guardians {
    private final Client client;
    private final ConfigManager configs;
    private final Pattern amounts = Pattern.compile("(?<current>.+)/(?<total>.+)");

    private NPC great_guardian;
    private GameObject catalytic_guardian;
    private GameObject elemental_guardian;

    private int current_guardians;
    private int total_guardians;

    public Guardians(final Client client, final ConfigManager configs) {
        this.client = client;
        this.configs = configs;
    }

    public GameObject getCatalyticGuardian() {
        return catalytic_guardian;
    }

    public GameObject getElementalGuardian() {
        return elemental_guardian;
    }

    public NPC getGreatGuardian() {
        return great_guardian;
    }

    public int getGuardiansCurrent() {
        return current_guardians;
    }

    public int getGuardiansTotal() {
        return total_guardians;
    }

    public void onGameObjectSpawned(final GameObject object) {
        switch (object.getId()) {
            case ObjectID.ESSENCE_PILE_CATALYTIC:
                catalytic_guardian = object;
                return;
            case ObjectID.ESSENCE_PILE_ELEMENTAL:
                elemental_guardian = object;
                return;
        }
    }

    public void onGameObjectDespawned(final GameObject object) {
        switch (object.getId()) {
            case ObjectID.ESSENCE_PILE_CATALYTIC:
                catalytic_guardian = null;
                return;
            case ObjectID.ESSENCE_PILE_ELEMENTAL:
                elemental_guardian = null;
                return;
        }
    }

    public void onNpcSpawned(final NPC npc) {
        if (npc.getId() == NpcID.THE_GREAT_GUARDIAN) {
            great_guardian = npc;
        }
    }

    public void onNpcDespawned(final NPC npc) {
        if (npc.getId() == NpcID.THE_GREAT_GUARDIAN) {
            great_guardian = null;
        }
    }

    public void onGameStateChanged(final GameState state) {
        if (state == GameState.LOADING) {
            catalytic_guardian = null;
            elemental_guardian = null;
        }
    }

    public void onGameTick() {
        final Widget widget = client.getWidget(746, 30);
        if (widget == null) return;

        final Matcher matcher = amounts.matcher(widget.getText());
        if (!matcher.find()) return;

        current_guardians = Integer.parseInt(matcher.group("current"));
        total_guardians = Integer.parseInt(matcher.group("total"));
    }
}
