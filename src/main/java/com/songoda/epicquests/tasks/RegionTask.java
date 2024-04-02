package com.songoda.epicquests.tasks;

import com.songoda.epicquests.EpicQuests;
import com.songoda.epicquests.story.quest.action.ActiveAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RegionTask extends BukkitRunnable {
    private static RegionTask instance;
    private static EpicQuests plugin;

    public RegionTask(EpicQuests plug) {
        plugin = plug;
    }

    public static RegionTask startTask(EpicQuests plug) {
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
