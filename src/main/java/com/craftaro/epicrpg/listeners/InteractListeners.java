package com.craftaro.epicrpg.listeners;

import com.craftaro.core.compatibility.ServerVersion;
import com.craftaro.epicrpg.story.quest.action.ActiveAction;
import com.craftaro.epicrpg.EpicRPG;
import com.craftaro.epicrpg.Region.ActiveSelection;
import com.craftaro.epicrpg.dialog.Dialog;
import com.craftaro.epicrpg.gui.GuiDialogs;
import com.craftaro.epicrpg.story.contender.StoryContender;
import com.craftaro.epicrpg.story.contender.StoryPlayer;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class InteractListeners implements Listener {
    private final EpicRPG plugin;

    public InteractListeners(EpicRPG plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (ServerVersion.isServerVersionAtLeast(ServerVersion.V1_9)
                && event.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }

        Player player = event.getPlayer();
        ActiveSelection activeSelection = this.plugin.getSelectionManager().getActiveSelection(player);

        if (activeSelection != null && event.hasBlock()) {
            activeSelection.commit(player, event.getClickedBlock().getLocation(), this.plugin);
            event.setCancelled(true);
            return;
        }

        for (ActiveAction action : this.plugin.getActionManager().getActiveActions()) {
            action.getAction().onInteract(event, action);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractAtEntityEvent event) {
        if (ServerVersion.isServerVersionAtLeast(ServerVersion.V1_9)
                && event.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }

        for (ActiveAction action : this.plugin.getActionManager().getActiveActions()) {
            action.getAction().onInteractWithEntity(event, action);
        }

        StoryPlayer storyPlayer = this.plugin.getContendentManager().getPlayer(event.getPlayer());
        Entity rightClicked = event.getRightClicked();
        if (CitizensAPI.getNPCRegistry().isNPC(rightClicked)) {
            NPC npc = CitizensAPI.getNPCRegistry().getNPC(rightClicked);
            if (npc == null) {
                return;
            }
            if (storyPlayer.isInDialogCreation()) {
                this.plugin.getDialogManager().addDialog(npc.getId());
                this.plugin.getGuiManager().showGUI(event.getPlayer(), new GuiDialogs(this.plugin, event.getPlayer(), null));
                storyPlayer.setInDialogCreation(false);
                return;
            }
            StoryContender storyContender = this.plugin.getContendentManager().getContender(event.getPlayer());
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                Dialog dialog = this.plugin.getDialogManager().getDialog(npc.getId());
                if (dialog != null) {
                    dialog.sendMessages(event.getPlayer(), storyContender);
                }
            }, 10L);
        }
    }
}
