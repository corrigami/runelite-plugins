package tictac7x.charges.triggers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TriggerWidget {
    @Nonnull
    public final String message;

    @Nullable
    public final Integer charges;

    public final int widget_group;
    public final int widget_child;

    public TriggerWidget(@Nonnull final String message, final int widget_group, final int widget_child, final int charges) {
        this.message = message;
        this.charges = charges;
        this.widget_group = widget_group;
        this.widget_child = widget_child;
    }

    public TriggerWidget(@Nonnull final String message, final int widget_group, final int widget_child) {
        this.message = message;
        this.charges = null;
        this.widget_group = widget_group;
        this.widget_child = widget_child;
    }

    public TriggerWidget(@Nonnull final String message) {
        this.message = message;
        this.charges = null;
        this.widget_group = 193;
        this.widget_child = 2;
    }

    public TriggerWidget(@Nonnull final String message, final int charges) {
        this.message = message;
        this.charges = charges;
        this.widget_group = 193;
        this.widget_child = 2;
    }
}