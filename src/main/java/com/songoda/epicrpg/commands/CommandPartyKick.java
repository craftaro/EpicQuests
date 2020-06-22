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
        if (args.length != 2) return ReturnType.SYNTAX_ERROR;
        Player player = (Player) sender;

        StoryContender contender = plugin.getContendentManager().getContender(player);

        if (contender instanceof StoryPlayer) {
            plugin.getLocale().newMessage("&cYou are not in a party...").sendPrefixedMessage(player);
            return ReturnType.FAILURE;
        }

        StoryParty storyParty = (StoryParty) contender;
        StoryPlayer storyPlayer = plugin.getContendentManager().getPlayer(player);

        if (!storyParty.isLeader(storyPlayer)) {
            plugin.getLocale().newMessage("&cYou must be the leader of this party to kick people from it...").sendPrefixedMessage(player);
            return ReturnType.FAILURE;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);

        if (!offlinePlayer.hasPlayedBefore()) {
            plugin.getLocale().newMessage("&cThis player does not exist.").sendPrefixedMessage(player);
            return ReturnType.FAILURE;
        }

        StoryPlayer them = plugin.getContendentManager().getPlayer(player);

        if (!storyParty.isMember(them)) {
            plugin.getLocale().newMessage("&cThis player is not in your party...").sendPrefixedMessage(player);
            return ReturnType.FAILURE;
        }

        plugin.getQuestTask().remove(storyParty, player);
        storyPlayer.setParty(null);
        storyParty.removePlayer(storyPlayer);

        plugin.getLocale().newMessage("&aYou kicked " + offlinePlayer.getName() + " successfully!").sendPrefixedMessage(player);
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
