package tictac7x.storage.panel;

import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.IconTextField;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Dimension;
import java.util.function.Consumer;

public class Search {
    private final IconTextField search;
    private final Consumer<String> onChange;

    public Search(Consumer<String> onChange) {
        this.onChange = onChange;

        search = new IconTextField();
        search.setIcon(IconTextField.Icon.SEARCH);
        search.setPreferredSize(new Dimension(PluginPanel.PANEL_WIDTH, 30));
        search.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        search.setHoverBackgroundColor(ColorScheme.DARK_GRAY_HOVER_COLOR);
        search.getDocument().addDocumentListener(new SearchListener(search, Search.this.onChange));
    }

    public IconTextField get() {
        return search;
    }
}

class SearchListener implements DocumentListener {
    private final IconTextField field;
    private final Consumer<String> onChange;

    public SearchListener(final IconTextField field, final Consumer<String> onChange) {
        this.field = field;
        this.onChange = onChange;
    }

    @Override
    public void insertUpdate(final DocumentEvent event) {
        onChange.accept(field.getText());
    }

    @Override
    public void removeUpdate(final DocumentEvent event) {
        onChange.accept(field.getText());
    }

    @Override
    public void changedUpdate(final DocumentEvent event) {
        onChange.accept(field.getText());
    }
}