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
import tictac7x.charges.item.ChargedItemWithStatus;
import tictac7x.charges.item.triggers.OnChatMessage;
import tictac7x.charges.item.triggers.OnHitsplatApplied;
import tictac7x.charges.item.triggers.TriggerBase;
import tictac7x.charges.item.triggers.TriggerItem;
import tictac7x.charges.store.HitsplatTarget;
import tictac7x.charges.store.Store;

public class J_RingOfSuffering extends ChargedItemWithStatus {
    public J_RingOfSuffering(
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
        super(ChargesImprovedConfig.ring_of_suffering, ItemID.RING_OF_SUFFERING, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, gson);

        this.items = new TriggerItem[]{
            new TriggerItem(ItemID.RING_OF_SUFFERING).fixedCharges(0),
            new TriggerItem(ItemID.RING_OF_SUFFERING_I).fixedCharges(0),
            new TriggerItem(ItemID.RING_OF_SUFFERING_I_25246).fixedCharges(0),
            new TriggerItem(ItemID.RING_OF_SUFFERING_I_26761).fixedCharges(0),
            new TriggerItem(ItemID.RING_OF_SUFFERING_R),
            new TriggerItem(ItemID.RING_OF_SUFFERING_RI),
            new TriggerItem(ItemID.RING_OF_SUFFERING_RI_25248),
            new TriggerItem(ItemID.RING_OF_SUFFERING_RI_26762),
        };

        this.triggers = new TriggerBase[]{
            // Check
            new OnChatMessage("Your ring currently has (?<charges>.+) recoil charges? remaining. The recoil effect is currently enabled.").setDynamically().onItemClick().activate(),
            new OnChatMessage("Your ring currently has (?<charges>.+) recoil charges? remaining. The recoil effect is currently disabled.").setDynamically().onItemClick().deactivate(),

            // Charge
            new OnChatMessage("You load your ring with .+ rings? of recoil. It now has (?<charges>.+) recoil charges.").setDynamically(),

            // Get hit.
            new OnHitsplatApplied(HitsplatTarget.SELF).moreThanZeroDamage().isEquipped().isActivated().decreaseCharges(1),
        };
    }
}