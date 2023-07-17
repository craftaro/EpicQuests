package com.craftaro.epicrpg.Region;

import com.craftaro.epicrpg.story.quest.Quest;
import com.craftaro.epicrpg.EpicRPG;
import com.craftaro.epicrpg.gui.GuiQuest;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ActiveSelection {
    private final Quest quest;

    private Location pos1;

    public ActiveSelection(Quest quest) {
        this.quest = quest;
    }

    public void commit(Player player, Location location, EpicRPG plugin) {
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
