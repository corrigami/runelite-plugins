package tictac7x.gotr;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.ObjectID;
import net.runelite.api.widgets.Widget;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Guardians {
    private final Client client;

    private Optional<GameObject> elemental_guardian = Optional.empty();
    private Optional<GameObject> catalytic_guardian = Optional.empty();
    private Optional<Integer> total_amount = Optional.empty();
    private Optional<Integer> current_amount = Optional.empty();

    private final int GUARDIANS_WIDGET_GROUP = 746;
    private final int GUARDIANS_WIDGET_CHILD = 30;
    private final Pattern regex_amounts = Pattern.compile("(?<current>.+)/(?<total>.+)");

    public Guardians(final Client client) {
        this.client = client;
    }

    public void onGameObjectSpawned(final GameObject object) {
        switch (object.getId()) {
            case ObjectID.ESSENCE_PILE_ELEMENTAL:
                elemental_guardian = Optional.of(object);
                return;
            case ObjectID.ESSENCE_PILE_CATALYTIC:
                catalytic_guardian = Optional.of(object);
                return;
        }
    }

    public void onGameObjectDespawned(final GameObject object) {
        switch (object.getId()) {
            case ObjectID.ESSENCE_PILE_ELEMENTAL:
                elemental_guardian = Optional.empty();
                return;
            case ObjectID.ESSENCE_PILE_CATALYTIC:
                catalytic_guardian = Optional.empty();
                return;
        }
    }

    public void onGameStateChanged(final GameState state) {
        if (state == GameState.LOADING) {
            elemental_guardian = Optional.empty();
            catalytic_guardian = Optional.empty();
        }
    }

    public void onGameTick() {
        final Optional<Widget> widget = Optional.ofNullable(client.getWidget(GUARDIANS_WIDGET_GROUP, GUARDIANS_WIDGET_CHILD));
        if (!widget.isPresent()) return;

        final Matcher matcher = regex_amounts.matcher(widget.get().getText());
        if (matcher.find()) {
            current_amount = Optional.of(Integer.parseInt(matcher.group("current")));
            total_amount = Optional.of(Integer.parseInt(matcher.group("total")));
        }
    }
}
