package tictac7x.charges.items.barrows;

import com.google.gson.Gson;
import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.triggers.TriggerItem;
import tictac7x.charges.store.Store;

public class GuthansWarspear extends ChargedItem {
    public GuthansWarspear(
        final Client client,
        final ClientThread client_thread,
        final ConfigManager configs,
        final ItemManager items,
        final InfoBoxManager infoboxes,
        final ChatMessageManager chat_messages,
        final Notifier notifier,
        final ChargesImprovedConfig config,
        final Store store,
        final Gson gson
    ) {
        super(ChargesImprovedConfig.barrows_set, ItemID.GUTHANS_WARSPEAR, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, gson);
        this.items = new TriggerItem[]{
            new TriggerItem(ItemID.GUTHANS_WARSPEAR).fixedCharges(100),
            new TriggerItem(ItemID.GUTHANS_WARSPEAR_100).fixedCharges(100),
            new TriggerItem(ItemID.GUTHANS_WARSPEAR_75).fixedCharges(75),
            new TriggerItem(ItemID.GUTHANS_WARSPEAR_50).fixedCharges(50),
            new TriggerItem(ItemID.GUTHANS_WARSPEAR_25).fixedCharges(25),
            new TriggerItem(ItemID.GUTHANS_WARSPEAR_0).fixedCharges(0)
        };
    }
}