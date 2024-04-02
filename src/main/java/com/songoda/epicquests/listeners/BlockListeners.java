package com.songoda.epicquests.listeners;

import com.songoda.epicquests.EpicQuests;
import com.songoda.epicquests.story.quest.action.ActiveAction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockListeners implements Listener {
    private final EpicQuests plugin;

    public BlockListeners(EpicQuests plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemDrop(BlockBreakEvent event) {
        for (ActiveAction action : this.plugin.getActionManager().getActiveActions()) {
            action.getAction().onBlockBreak(event, action);
        }
    }
}
