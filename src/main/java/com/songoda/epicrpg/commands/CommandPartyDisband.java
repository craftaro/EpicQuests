package com.songoda.epicrpg.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.story.contender.StoryContender;
import com.songoda.epicrpg.story.contender.StoryParty;
import com.songoda.epicrpg.story.contender.StoryPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandPartyDisband extends AbstractCommand {

    private final EpicRPG plugin;

    public CommandPartyDisband(EpicRPG plugin) {
        super(CommandType.PLAYER_ONLY, "party disband");
        this.plugin = plugin;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {

        Player player = (Player) sender;

        StoryContender contender = plugin.getContendentManager().getContender(player);

        if (contender instanceof StoryPlayer) {
            plugin.getLocale().getMessage("command.party.notinparty").sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }
        StoryPlayer storyPlayer = plugin.getContendentManager().getPlayer(player);
        StoryParty storyParty = storyPlayer.getParty();

        if (!storyParty.isLeader(storyPlayer)) {
            plugin.getLocale().getMessage("command.party.notleader").sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }

        plugin.getQuestTask().removeAll(storyParty);
        storyParty.disband();
        plugin.getLocale().getMessage("command.party.disband").sendPrefixedMessage(sender);
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "epicrpg.party.disband";
    }

    @Override
    public String getSyntax() {
        return "party disband";
    }

    @Override
    public String getDescription() {
        return "Create a disband.";
    }
}
