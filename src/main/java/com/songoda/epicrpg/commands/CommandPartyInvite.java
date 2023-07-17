package com.songoda.epicrpg.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.story.contender.ContendentManager;
import com.songoda.epicrpg.story.contender.PartyInvite;
import com.songoda.epicrpg.story.contender.StoryContender;
import com.songoda.epicrpg.story.contender.StoryParty;
import com.songoda.epicrpg.story.contender.StoryPlayer;
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
            PartyInvite partyInvite = this.contendentManager.getInvite(player.getUniqueId());
            if (partyInvite == null) {
                this.plugin.getLocale().getMessage("command.party.invite.noinvites").sendPrefixedMessage(sender);
                return ReturnType.FAILURE;
            }
            StoryContender contender = this.contendentManager.getContender(partyInvite.getSender());
            if (!(contender instanceof StoryParty)) {
                this.plugin.getLocale().getMessage("command.party.invite.noinvites").sendPrefixedMessage(sender);
                return ReturnType.FAILURE;
            }
            ((StoryParty) contender).addPlayer(this.contendentManager.getPlayer(partyInvite.getRecipient()));
            this.contendentManager.removeInvite(player.getUniqueId());
            this.plugin.getLocale().getMessage("command.party.invite.accepted").sendPrefixedMessage(sender);
            return ReturnType.SUCCESS;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        if (!offlinePlayer.isOnline())
            return ReturnType.FAILURE;

        StoryContender contender = this.contendentManager.getContender((Player) sender);

        if (contender instanceof StoryPlayer) {
            this.plugin.getLocale().getMessage("command.party.notinparty").sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }
        StoryPlayer storyPlayer = this.contendentManager.getPlayer((Player) sender);
        StoryParty storyParty = storyPlayer.getParty();

        if (!storyParty.isLeader(storyPlayer)) {
            this.plugin.getLocale().getMessage("command.party.notleader").sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }
        StoryPlayer storyPlayerThem = this.contendentManager.getPlayer(offlinePlayer);
        StoryParty storyPartyThem = storyPlayerThem.getParty();

        if (storyPartyThem != null) {
            this.plugin.getLocale().getMessage("command.party.invite.alreadyinparty")
                    .sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }

        if (this.contendentManager.getInvite(storyPlayerThem.getUniqueId()) != null) {
            this.plugin.getLocale().getMessage("command.party.invite.alreadyinvited")
                    .sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }

        this.contendentManager.addInvite(storyPlayer.getUniqueId(), storyPlayerThem.getUniqueId());
        Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin, () -> {
            if (this.contendentManager.isInvited(storyPlayerThem)
                    && this.contendentManager.removeInvite(storyPlayerThem.getUniqueId())) {
                if (offlinePlayer.isOnline())
                    this.plugin.getLocale().getMessage("command.party.invite.timeout.received")
                            .processPlaceholder("player", player.getName())
                            .sendPrefixedMessage(offlinePlayer.getPlayer());
                if (player.isOnline())
                    this.plugin.getLocale().getMessage("command.party.invite.timeout.sent")
                            .processPlaceholder("player", offlinePlayer.getName())
                            .sendPrefixedMessage(sender);
            }
        }, 20L * 15L);
        this.plugin.getLocale().getMessage("command.party.invite.success")
                .processPlaceholder("player", offlinePlayer.getName())
                .sendPrefixedMessage(sender);

        this.plugin.getLocale().getMessage("command.party.invite.received")
                .processPlaceholder("player", player.getName())
                .sendPrefixedMessage(offlinePlayer.getPlayer());
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
