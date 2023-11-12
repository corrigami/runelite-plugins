package tictac7x.charges.items;

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
import tictac7x.charges.item.triggers.OnChatMessage;
import tictac7x.charges.item.triggers.OnHitsplatApplied;
import tictac7x.charges.item.triggers.TriggerBase;
import tictac7x.charges.item.triggers.TriggerItem;
import tictac7x.charges.store.HitsplatTarget;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;

public class J_RingOfRecoil extends ChargedItem {
    public J_RingOfRecoil(
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
        super(ChargesImprovedConfig.ring_of_recoil, ItemKey.RING_OF_RECOIL, ItemID.RING_OF_RECOIL, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, gson);

        this.items = new TriggerItem[]{
            new TriggerItem(ItemID.RING_OF_RECOIL),
        };

        this.triggers = new TriggerBase[]{
            // Check.
            new OnChatMessage("You can inflict (?<charges>.+) more points? of damage before a ring will shatter.").setDynamically(),

            // Trying to break when full.
            new OnChatMessage("The ring is fully charged. There would be no point in breaking it.").onItemClick().fixedCharges(40),

            // Shattered.
            new OnChatMessage("Your Ring of Recoil has shattered.").notification().fixedCharges(40),

            // Take damage.
            new OnHitsplatApplied(HitsplatTarget.SELF).moreThanZeroDamage().isEquipped().decreaseCharges(1),
        };
    }
}