package tictac7x.deposit_worn_items;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@PluginDescriptor(
	name = "Deposit Worn Items",
	description = "Disable/Enable the deposit worn items button",
	tags = { "deposit", "worn", "items"}
)
public class TicTac7xDepositWornItemsPlugin extends Plugin {
	@Inject
	private Client client;

	@Inject
	private TicTac7xDepositWornItemsConfig config;

	@Inject
	private ConfigManager configManager;

	@Inject
	private OverlayManager overlayManager;

	@Provides
	TicTac7xDepositWornItemsConfig provideConfig(final ConfigManager configManager) {
		return configManager.getConfig(TicTac7xDepositWornItemsConfig.class);
	}

	@Inject
	private LockIconOverlay lockIconOverlay;

	@Override
	protected void startUp() {
		overlayManager.add(lockIconOverlay);
	}

	@Override
	protected void shutDown() {
		overlayManager.remove(lockIconOverlay);
	}

	@Subscribe
	public void onMenuEntryAdded(final MenuEntryAdded event) {
		final MenuEntry[] currentMenuEntries = client.getMenuEntries();
		if (currentMenuEntries.length < 2 || !currentMenuEntries[1].getOption().equals("Deposit worn items")) return;

        final List<MenuEntry> newMenuEntries = new ArrayList<>();

		// Deposit enabled.
		if (config.isDepositWornItemsEnabled()) {
			for (final MenuEntry menuEntry : currentMenuEntries) {
				if (menuEntry.getOption().equals("Deposit worn items") && config.isDepositWornItemsToggleable()) {
					newMenuEntries.add(client.createMenuEntry(0).setOption("Disable deposit button").onClick(e -> {
						configManager.setConfiguration(
							TicTac7xDepositWornItemsConfig.group,
							TicTac7xDepositWornItemsConfig.deposit_worn_items_enabled,
							false
						);
					}));
				}
				newMenuEntries.add(menuEntry);
			}

		// Deposit disabled.
		} else {
			for (final MenuEntry menuEntry : currentMenuEntries) {
				if (menuEntry.getOption().equals("Deposit worn items")) {
					menuEntry.setDeprioritized(true);
				}
				newMenuEntries.add(menuEntry);
			}

			if (config.isDepositWornItemsToggleable()) {
				newMenuEntries.add(client.createMenuEntry(0).setOption("Enable deposit button").onClick(e -> {
					configManager.setConfiguration(
							TicTac7xDepositWornItemsConfig.group,
							TicTac7xDepositWornItemsConfig.deposit_worn_items_enabled,
							true
					);
				}));
			}

			newMenuEntries.add(client.createMenuEntry(0).setOption("Disabled"));
		}

		client.setMenuEntries(newMenuEntries.toArray(new MenuEntry[0]));
	}
}