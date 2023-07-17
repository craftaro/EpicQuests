package com.craftaro.epicrpg.commands;

import com.craftaro.core.commands.AbstractCommand;
import com.craftaro.epicrpg.EpicRPG;
import com.craftaro.epicrpg.gui.GuiQuestLog;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandQuestLog extends AbstractCommand {
    private final EpicRPG plugin;

    public CommandQuestLog(EpicRPG plugin) {
        super(CommandType.PLAYER_ONLY, "questlog");
        this.plugin = plugin;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        this.plugin.getGuiManager().showGUI((Player) sender, new GuiQuestLog(this.plugin, (Player) sender));
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "epicrpg.questlog";
    }

    @Override
    public String getSyntax() {
        return "questlog";
    }

    @Override
    public String getDescription() {
        return "Open the quest log.";
    }
}
