package com.songoda.epicrpg.tasks;

import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.story.quest.action.ActiveAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RegionTask extends BukkitRunnable {
    private static RegionTask instance;
    private static EpicRPG plugin;

    public RegionTask(EpicRPG plug) {
        plugin = plug;
    }

    public static RegionTask startTask(EpicRPG plug) {
        plugin = plug;
        if (instance == null) {
            instance = new RegionTask(plugin);
            instance.runTaskTimerAsynchronously(plugin, 0, 20);
        }

        return instance;
    }

    @Override
    public void run() {
        for (ActiveAction action : plugin.getActionManager().getActiveActions()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                action.getAction().moveTick(player, action);
            }
        }
    }
}
