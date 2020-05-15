package com.songoda.epicrpg.Region;

import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.gui.GuiQuest;
import com.songoda.epicrpg.story.quest.Quest;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ActiveSelection {

    private final Quest quest;

    private Location pos1;

    public ActiveSelection(Quest quest) {
        this.quest = quest;
    }

    public void commit(Player player, Location location, EpicRPG plugin) {
        if (pos1 == null) {
            pos1 = location;
            player.sendMessage("Position 1 set.");
            return;
        }

        quest.setRegion(new Region(pos1, location));
        player.sendMessage("Region created!");
        plugin.getGuiManager().showGUI(player, new GuiQuest(plugin, player, quest));
    }

}
