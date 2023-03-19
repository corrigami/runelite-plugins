package tictac7x.storage.panel;

import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.PluginPanel;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Dimension;

public class PanelItem {
    private final JLabel item;

    public PanelItem(final int item_id, final int quantity, final ClientThread client_thread, final ItemManager items) {
        item = new JLabel();
        item.setPreferredSize(new Dimension(PluginPanel.PANEL_WIDTH - 20, 32));

        client_thread.invoke(() -> {
            item.setIcon(new ImageIcon(items.getImage(item_id, quantity, true)));
            item.setText(items.getItemComposition(item_id).getName());

            SwingUtilities.invokeLater(() -> {
                item.revalidate();
                item.repaint();
            });
        });
    }

    public Component get() {
        return item;
    }
}
