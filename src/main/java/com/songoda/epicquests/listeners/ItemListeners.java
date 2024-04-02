package com.songoda.epicquests.listeners;

import com.songoda.epicquests.EpicQuests;
import com.songoda.epicquests.story.quest.action.ActiveAction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class ItemListeners implements Listener {
    private final EpicQuests plugin;

    public ItemListeners(EpicQuests plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemDrop(PlayerDropItemEvent event) {
        for (ActiveAction action : this.plugin.getActionManager().getActiveActions())
            action.getAction().onDrop(event, action);
    }

    // We're putting this on normal, so it works with UltimateStacker
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onItemPickup(PlayerPickupItemEvent event) {
        for (ActiveAction action : this.plugin.getActionManager().getActiveActions())
            action.getAction().onPickup(event, action);
    }
}
