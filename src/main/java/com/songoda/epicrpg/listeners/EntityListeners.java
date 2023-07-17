package com.songoda.epicrpg.listeners;

import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.story.quest.action.ActiveAction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityListeners implements Listener {
    private final EpicRPG plugin;

    public EntityListeners(EpicRPG plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        for (ActiveAction action : this.plugin.getActionManager().getActiveActions()) {
            action.getAction().onEntityKill(event, action);
        }
    }
}
