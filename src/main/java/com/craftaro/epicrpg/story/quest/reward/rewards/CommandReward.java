package com.craftaro.epicrpg.story.quest.reward.rewards;

import com.craftaro.epicrpg.EpicRPG;
import com.craftaro.epicrpg.gui.GuiCommands;
import com.craftaro.epicrpg.story.quest.Quest;
import com.craftaro.epicrpg.story.quest.reward.AbstractReward;
import com.craftaro.epicrpg.story.quest.reward.RewardType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandReward extends AbstractReward {
    private final EpicRPG plugin;

    private final List<String> commands = new ArrayList<>();

    public CommandReward(Quest quest) {
        super(quest);
        this.plugin = JavaPlugin.getPlugin(EpicRPG.class);
    }

    @Override
    public RewardType getType() {
        return RewardType.COMMAND;
    }

    @Override
    public void setup(Player player) {
        this.plugin.getGuiManager().showGUI(player, new GuiCommands(this.plugin, player, this));
    }

    @Override
    public void give(Player player) {
        Bukkit.getScheduler().runTask(this.plugin, () -> {
            for (String command : this.commands) {
                Bukkit.getServer().dispatchCommand(
                        Bukkit.getConsoleSender(),
                        command
                                .replace("@p", player.getName())
                                .replace("%player%", player.getName())
                );
            }
        });
    }

    public List<String> getCommands() {
        return Collections.unmodifiableList(this.commands);
    }

    public void addCommand(String command) {
        this.commands.add(command);
    }

    public void removeCommand(String command) {
        this.commands.remove(command);
    }
}
