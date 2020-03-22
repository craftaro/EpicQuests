package com.songoda.epicrpg.story.quest.reward.rewards;

import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.gui.GuiCommands;
import com.songoda.epicrpg.story.quest.Quest;
import com.songoda.epicrpg.story.quest.reward.AbstractReward;
import com.songoda.epicrpg.story.quest.reward.RewardType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandReward extends AbstractReward {

    private List<String> commands = new ArrayList<>();

    public CommandReward(Quest quest) {
        super(quest);
    }

    @Override
    public RewardType getType() {
        return RewardType.COMMAND;
    }

    @Override
    public void setup(Player player) {
        EpicRPG plugin = EpicRPG.getInstance();
        plugin.getGuiManager().showGUI(player, new GuiCommands(plugin, player, this));
    }

    @Override
    public void give(Player player) {
        Bukkit.getScheduler().runTask(EpicRPG.getInstance(), () -> {
            for (String command : commands.toArray(new String[0]))
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
        });
    }

    public List<String> getCommands() {
        return Collections.unmodifiableList(commands);
    }

    public void addCommand(String command) {
        commands.add(command);
    }

    public void removeCommand(String command) {
        commands.remove(command);
    }
}
