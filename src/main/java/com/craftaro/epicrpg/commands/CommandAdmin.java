package com.craftaro.epicrpg.commands;

import com.craftaro.core.commands.AbstractCommand;
import com.craftaro.epicrpg.EpicRPG;
import com.craftaro.epicrpg.gui.GuiMain;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandAdmin extends AbstractCommand {
    private final EpicRPG plugin;

    public CommandAdmin(EpicRPG plugin) {
        super(CommandType.PLAYER_ONLY, "admin");
        this.plugin = plugin;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        this.plugin.getGuiManager().showGUI((Player) sender, new GuiMain(this.plugin, (Player) sender));
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "epicrpg.admin";
    }

    @Override
    public String getSyntax() {
        return "admin";
    }

    @Override
    public String getDescription() {
        return "Open the admin panel.";
    }
}
