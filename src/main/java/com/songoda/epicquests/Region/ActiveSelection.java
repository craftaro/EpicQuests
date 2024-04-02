package com.songoda.epicquests.Region;

import com.songoda.epicquests.EpicQuests;
import com.songoda.epicquests.gui.GuiQuest;
import com.songoda.epicquests.story.quest.Quest;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ActiveSelection {
    private final Quest quest;

    private Location pos1;

    public ActiveSelection(Quest quest) {
        this.quest = quest;
    }

    public void commit(Player player, Location location, EpicQuests plugin) {
        if (this.pos1 == null) {
            this.pos1 = location;
            player.sendMessage("Position 1 set.");
            return;
        }

        this.quest.setRegion(new Region(this.pos1, location));
        player.sendMessage("Region created!");
        plugin.getSelectionManager().removeActiveSelection(player);
        plugin.getGuiManager().showGUI(player, new GuiQuest(plugin, player, this.quest));
    }

}
