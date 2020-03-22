package com.songoda.epicrpg.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.gui.GuiMain;
import com.songoda.epicrpg.gui.GuiQuestLog;
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
        plugin.getGuiManager().showGUI((Player) sender, new GuiQuestLog(plugin, (Player) sender));
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
        return "/epicrpg questlog";
    }

    @Override
    public String getDescription() {
        return "Open the quest log.";
    }
}
