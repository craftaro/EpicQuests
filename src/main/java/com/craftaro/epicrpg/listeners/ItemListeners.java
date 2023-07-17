package com.craftaro.epicrpg.listeners;

import com.craftaro.epicrpg.story.quest.action.ActiveAction;
import com.craftaro.epicrpg.EpicRPG;
import io.lumine.mythic.bukkit.utils.events.extra.ArmorEquipEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class ItemListeners implements Listener {
    private final EpicRPG plugin;

    public ItemListeners(EpicRPG plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemDrop(PlayerDropItemEvent event) {

        for (ActiveAction action : this.plugin.getActionManager().getActiveActions()) {
            action.getAction().onDrop(event, action);
        }
    }

    // We're putting this on normal, so it works with UltimateStacker
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onItemPickup(PlayerPickupItemEvent event) {
        for (ActiveAction action : this.plugin.getActionManager().getActiveActions()) {
            action.getAction().onPickup(event, action);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemEquip(ArmorEquipEvent event) {
        for (ActiveAction action : this.plugin.getActionManager().getActiveActions()) {
            action.getAction().onEquip(event, action);
        }
    }
}
