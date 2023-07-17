package com.songoda.epicrpg.commands;

import com.craftaro.core.commands.AbstractCommand;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.story.contender.StoryContender;
import com.songoda.epicrpg.story.contender.StoryParty;
import com.songoda.epicrpg.story.contender.StoryPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandPartyCreate extends AbstractCommand {
    private final EpicRPG plugin;

    public CommandPartyCreate(EpicRPG plugin) {
        super(CommandType.PLAYER_ONLY, "party create");
        this.plugin = plugin;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {

        Player player = (Player) sender;

        StoryContender contender = this.plugin.getContendentManager().getContender(player);

        if (contender instanceof StoryParty) {
            this.plugin.getLocale().getMessage("command.party.create.already").sendPrefixedMessage(player);
            return ReturnType.FAILURE;
        }

        this.plugin.getContendentManager().createParty((StoryPlayer) contender);
        this.plugin.getLocale().getMessage("command.party.create.success").sendPrefixedMessage(sender);
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "epicrpg.party.create";
    }

    @Override
    public String getSyntax() {
        return "party create";
    }

    @Override
    public String getDescription() {
        return "Create a party.";
    }
}
