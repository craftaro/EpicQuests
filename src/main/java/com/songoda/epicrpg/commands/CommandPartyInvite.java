package com.songoda.epicrpg.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.story.contender.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandPartyInvite extends AbstractCommand {

    private final EpicRPG plugin;
    private final ContendentManager contendentManager;

    public CommandPartyInvite(EpicRPG plugin, ContendentManager contendentManager) {
        super(CommandType.PLAYER_ONLY, "party invite");
        this.plugin = plugin;
        this.contendentManager = contendentManager;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {

        Player player = (Player) sender;

        if (args[0].equals("accept")) {
            PartyInvite partyInvite = contendentManager.getInvite(((Player) sender).getUniqueId());
            if (partyInvite == null) {
                plugin.getLocale().newMessage("&cYou have no pending invites...").sendPrefixedMessage(sender);
                return ReturnType.FAILURE;
            }
            StoryContender contender = contendentManager.getContender(partyInvite.getSender());
            if (!(contender instanceof StoryParty)) {
                plugin.getLocale().newMessage("&cYou have no pending invites...").sendPrefixedMessage(sender);
                return ReturnType.FAILURE;
            }
            ((StoryParty) contender).addPlayer(contendentManager.getPlayer(partyInvite.getRecipient()));
            plugin.getLocale().newMessage("&aYou joined a party!").sendPrefixedMessage(sender);
            return ReturnType.SUCCESS;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        if (!offlinePlayer.isOnline())
            return ReturnType.FAILURE;

        StoryContender contender = contendentManager.getContender((Player) sender);

        if (contender instanceof StoryPlayer) {
            plugin.getLocale().newMessage("&cYou are not in a party...").sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }
        StoryPlayer storyPlayer = contendentManager.getPlayer((Player) sender);
        StoryParty storyParty = storyPlayer.getParty();

        if (!storyParty.isLeader(storyPlayer)) {
            plugin.getLocale().newMessage("&cYou are not the leader of this party...").sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }
        StoryPlayer storyPlayerThem = contendentManager.getPlayer(offlinePlayer);
        StoryParty storyPartyThem = storyPlayerThem.getParty();

        if (storyPartyThem != null) {
            plugin.getLocale().newMessage("&cThey are already in a party...").sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }

        if (contendentManager.getInvite(storyPlayerThem.getUniqueId()) != null) {
            plugin.getLocale().newMessage("&cThey already have a pending invite...").sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }

        contendentManager.addInvite(storyPlayer.getUniqueId(), storyPlayerThem.getUniqueId());
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            if (contendentManager.removeInvite(storyPlayerThem.getUniqueId())) {
                if (offlinePlayer.isOnline())
                    plugin.getLocale().newMessage("&cYour invite has timed out...").sendPrefixedMessage(offlinePlayer.getPlayer());
                if (player.isOnline())
                    plugin.getLocale().newMessage("&cYour invite has timed out...").sendPrefixedMessage(sender);
            }
        }, 20L * 15L);
        plugin.getLocale().newMessage("&aInvite sent successfully!").sendPrefixedMessage(sender);
        plugin.getLocale().newMessage("&7You received an invite from &6" + player.getName() + "&7. Do &6/rpg party invite accept &7to join their party.").sendPrefixedMessage(offlinePlayer.getPlayer());
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "epicrpg.party.invite";
    }

    @Override
    public String getSyntax() {
        return "party invite <player/accept>";
    }

    @Override
    public String getDescription() {
        return "Invite a friend to your party or accept an existing invite.";
    }
}
