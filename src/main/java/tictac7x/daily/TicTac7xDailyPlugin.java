package tictac7x.daily;

import javax.annotation.Nullable;
import javax.inject.Inject;
import net.runelite.api.Client;
import lombok.extern.slf4j.Slf4j;
import com.google.inject.Provides;
import net.runelite.api.ItemID;
import net.runelite.api.Varbits;
import net.runelite.api.vars.AccountType;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.InfoBox;
import tictac7x.Overlay;

import java.awt.*;

@Slf4j
@PluginDescriptor(
	name = "Daily Tasks",
	description = "Reminds you to do your daily tasks",
	tags = { "daily","battlestaves","essence","ess","kingdom","battlestaff","sand","flax","bowstring","ogre","rantz","bone","bonemeal","slime","buckets","herb","boxes,nmz,dynamite,mith,grapple" }

)
public class TicTac7xDailyPlugin extends Plugin {
	private static final String TOOLTIP_BATTLESTAVES = "%d battlestaves can be bought from Zaff at Varrock";
	private static final String TOOLTIP_BUCKETS_OF_SAND = "%d buckets of sand can be collected from Bert at Yanille";

	private static final int BUCKETS_OF_SAND_QUEST_COMPLETE = 160;
	private static final int BUCKETS_OF_SAND_AMOUNT = 84;

	@Inject
	private Client client;

	@Inject
	private DailyConfig config;

	@Inject
	private InfoBoxManager infoboxes;

	@Inject
	private ItemManager items;

	@Nullable
	private InfoBox infobox_battlestaves = null;

	@Nullable
	private InfoBox infobox_sand = null;

	@Provides
	DailyConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(DailyConfig.class);
	}

	@Override
	protected void startUp() {
		if (infobox_battlestaves == null) {
			infobox_battlestaves = createInfoBoxBattlestaves();
			infobox_sand = createInfoBoxSand();
		}

		infoboxes.addInfoBox(infobox_battlestaves);
		infoboxes.addInfoBox(infobox_sand);
	}

	@Override
	protected void shutDown() {
		if (infobox_battlestaves != null) {
			infoboxes.removeInfoBox(infobox_battlestaves);
			infoboxes.removeInfoBox(infobox_sand);
		}
	}

	private InfoBox createInfoBoxBattlestaves() {
		return new InfoBox(
			DailyConfig.battlestaves_id,
			items.getImage(ItemID.BATTLESTAFF),
			this::showBattlestaves,
			() -> String.valueOf(getBattlestavesAmount()),
			() -> String.format(TOOLTIP_BATTLESTAVES, getBattlestavesAmount()),
			this::getDailyColor,
			this
		);
	}

	private boolean showBattlestaves() {
		return (
			config.showBattlestaves() &&
			client.getVar(Varbits.DAILY_STAVES_COLLECTED) == 0
		);
	}

	private int getBattlestavesAmount() {
		if (client.getVar(Varbits.DIARY_VARROCK_EASY) == 1) {
			if (client.getVar(Varbits.DIARY_VARROCK_MEDIUM) == 1) {
				if (client.getVar(Varbits.DIARY_VARROCK_HARD) == 1) {
					if (client.getVar(Varbits.DIARY_VARROCK_ELITE) == 1) {
						return 120;
					} else return 60;
				} else return 30;
			} else return 15;
		} else return 5;
	}

	private InfoBox createInfoBoxSand() {
		return new InfoBox(
			DailyConfig.buckets_of_sand_id,
			items.getImage(ItemID.BUCKET_OF_SAND),
			this::showBucketsOfSand,
			() -> String.valueOf(BUCKETS_OF_SAND_AMOUNT),
			() -> String.format(TOOLTIP_BUCKETS_OF_SAND, BUCKETS_OF_SAND_AMOUNT),
			this::getDailyColor,
			this
		);
	}

	private boolean showBucketsOfSand() {
		return (
			config.showBucketsOfSand() &&
			client.getAccountType() != AccountType.ULTIMATE_IRONMAN &&
			client.getVar(Varbits.QUEST_THE_HAND_IN_THE_SAND) >= BUCKETS_OF_SAND_QUEST_COMPLETE &&
			client.getVar(Varbits.DAILY_SAND_COLLECTED) == 0
		);
	}

	private Color getDailyColor() {
		return Overlay.color_red;
	}
}
