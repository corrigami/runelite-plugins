package tictac7x.gotr.overlays;

import net.runelite.api.GameState;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;
import tictac7x.gotr.store.Inventory;
import tictac7x.gotr.TicTac7xGotrImprovedConfig;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Optional;

public class GreatGuardianOverlay extends Overlay {
    private final ModelOutlineRenderer modelOutlineRenderer;
    private final TicTac7xGotrImprovedConfig config;
    private final Inventory inventory;

    private Optional<NPC> greatGuardian = Optional.empty();

    public GreatGuardianOverlay(final ModelOutlineRenderer modelOutlineRenderer, final TicTac7xGotrImprovedConfig config, final Inventory inventory) {
        this.modelOutlineRenderer = modelOutlineRenderer;
        this.config = config;
        this.inventory = inventory;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.UNDER_WIDGETS);
    }

    public void onNpcSpawned(final NPC npc) {
        if (npc.getId() == NpcID.THE_GREAT_GUARDIAN) {
            greatGuardian = Optional.of(npc);
        }
    }

    public void onNpcDespawned(final NPC npc) {
        if (npc.getId() == NpcID.THE_GREAT_GUARDIAN) {
            greatGuardian = Optional.empty();
        }
    }

    public void onGameStateChanged(final GameState game_state) {
        if (game_state == GameState.LOADING) {
            greatGuardian = Optional.empty();
        }
    }

    @Override
    public Dimension render(final Graphics2D graphics) {
        // Great guardian highlighting disabled.
        if (!config.highlightGreatGuardian()) return null;

        // Great guardian NPC object not available.
        if (!greatGuardian.isPresent()) return null;

        // No essence in inventory.
        if (!inventory.hasEssence()) return null;

        try {
            modelOutlineRenderer.drawOutline(greatGuardian.get(), 2, inventory.hasElementalEssence() ? config.getElementalColor() : config.getCatalyticColor(), 2);
        } catch (final Exception ignored) {}

        return null;
    }
}
