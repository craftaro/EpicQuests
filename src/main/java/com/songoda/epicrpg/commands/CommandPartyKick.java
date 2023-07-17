package com.songoda.epicrpg.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.story.contender.StoryContender;
import com.songoda.epicrpg.story.contender.StoryParty;
import com.songoda.epicrpg.story.contender.StoryPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandPartyKick extends AbstractCommand {
    private final EpicRPG plugin;

    public CommandPartyKick(EpicRPG plugin) {
        super(CommandType.PLAYER_ONLY, "party kick");
        this.plugin = plugin;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        if (args.length != 2) {
            return ReturnType.SYNTAX_ERROR;
        }
        Player player = (Player) sender;

        StoryContender contender = this.plugin.getContendentManager().getContender(player);

        if (contender instanceof StoryPlayer) {
            this.plugin.getLocale().getMessage("command.party.notinparty").sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }

        StoryParty storyParty = (StoryParty) contender;
        StoryPlayer storyPlayer = this.plugin.getContendentManager().getPlayer(player);

        if (!storyParty.isLeader(storyPlayer)) {
            this.plugin.getLocale().getMessage("command.party.notleader").sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);

        if (!offlinePlayer.hasPlayedBefore()) {
            this.plugin.getLocale().newMessage("&cThis player does not exist.").sendPrefixedMessage(player);
            return ReturnType.FAILURE;
        }

        StoryPlayer them = this.plugin.getContendentManager().getPlayer(player);

        if (!storyParty.isMember(them)) {
            this.plugin.getLocale().newMessage("&cThis player is not in your party...").sendPrefixedMessage(player);
            return ReturnType.FAILURE;
        }

        this.plugin.getQuestTask().remove(storyParty, player);
        storyPlayer.setParty(null);
        storyParty.removePlayer(storyPlayer);

        this.plugin.getLocale().newMessage("&aYou kicked " + offlinePlayer.getName() + " successfully!").sendPrefixedMessage(player);
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "epicrpg.party.kick";
    }

    @Override
    public String getSyntax() {
        return "party kick <player>";
    }

    @Override
    public String getDescription() {
        return "Kick a player from your party.";
    }
}
