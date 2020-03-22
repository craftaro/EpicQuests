package com.songoda.epicrpg.listers;

import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.dialog.Dialog;
import com.songoda.epicrpg.gui.GuiDialogs;
import com.songoda.epicrpg.story.player.StoryPlayer;
import com.songoda.epicrpg.story.quest.action.ActiveAction;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
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
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;
        for (ActiveAction action : plugin.getActionManager().getActiveActions())
            action.getAction().onInteract(event, action);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractAtEntityEvent event) {
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;
        for (ActiveAction action : plugin.getActionManager().getActiveActions())
            if (!action.getAction().onInteractWithEntity(event, action))
                return;

        StoryPlayer storyPlayer = plugin.getPlayerManager().getPlayer(event.getPlayer());
        if (event.getRightClicked().hasMetadata("NPC")) {
            NPC npc = CitizensAPI.getNPCRegistry().getNPC(event.getRightClicked());
            if (npc == null) return;
            if (storyPlayer.isInDialogCreation()) {
                plugin.getDialogManager().addDialog(npc.getId());
                plugin.getGuiManager().showGUI(event.getPlayer(), new GuiDialogs(plugin, event.getPlayer(), null));
                storyPlayer.setInDialogCreation(false);
                return;
            }
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                Dialog dialog = plugin.getDialogManager().getDialog(npc.getId());
                if (dialog != null)
                    dialog.sendMessages(event.getPlayer(), storyPlayer);
            }, 10L);
        }
    }
}
