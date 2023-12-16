package tictac7x.gotr.store;

import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Guardians {
    private final int GUARDIANS_WIDGET_GROUP = 746;
    private final int GUARDIANS_WIDGET_CHILD = 30;
    private final Pattern regexGuardiansAmount = Pattern.compile("(?<current>.+)/(?<total>.+)");

    private final Client client;

    private boolean guardiansWidgetVisible = false;
    private Optional<Integer> totalAmount = Optional.empty();
    private Optional<Integer> currentAmount = Optional.empty();

    public Guardians(final Client client) {
        this.client = client;
    }

    public boolean canBuildGuardians() {
        return (guardiansWidgetVisible && currentAmount.isPresent() && totalAmount.isPresent() && currentAmount.get() < totalAmount.get());
    }

    public void onGameTick() {
        final Optional<Widget> widget = Optional.ofNullable(client.getWidget(GUARDIANS_WIDGET_GROUP, GUARDIANS_WIDGET_CHILD));
        if (!widget.isPresent() || widget.get().isHidden()) {
            guardiansWidgetVisible = false;
            return;
        }

        guardiansWidgetVisible = true;
        final Matcher matcher = regexGuardiansAmount.matcher(widget.get().getText());
        if (matcher.find()) {
            currentAmount = Optional.of(Integer.parseInt(matcher.group("current")));
            totalAmount = Optional.of(Integer.parseInt(matcher.group("total")));
        }
    }
}
