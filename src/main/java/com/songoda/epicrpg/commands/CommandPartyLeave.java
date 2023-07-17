package com.songoda.epicrpg.commands;

import com.craftaro.core.commands.AbstractCommand;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.story.contender.StoryContender;
import com.songoda.epicrpg.story.contender.StoryParty;
import com.songoda.epicrpg.story.contender.StoryPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandPartyLeave extends AbstractCommand {
    private final EpicRPG plugin;

    public CommandPartyLeave(EpicRPG plugin) {
        super(CommandType.PLAYER_ONLY, "party leave");
        this.plugin = plugin;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        Player player = (Player) sender;

        StoryContender contender = this.plugin.getContendentManager().getContender(player);

        if (contender instanceof StoryPlayer) {
            this.plugin.getLocale().getMessage("command.party.notinparty").sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }

        StoryParty storyParty = (StoryParty) contender;
        StoryPlayer storyPlayer = this.plugin.getContendentManager().getPlayer(player);

        if (storyParty.isLeader(storyPlayer)) {
            this.plugin.getLocale().getMessage("command.party.leave.leader").sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }

        this.plugin.getQuestTask().remove(storyParty, player);
        storyPlayer.setParty(null);
        storyParty.removePlayer(storyPlayer);

        this.plugin.getLocale().getMessage("command.party.leave.success").sendPrefixedMessage(sender);
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "epicrpg.party.leave";
    }

    @Override
    public String getSyntax() {
        return "party leave";
    }

    @Override
    public String getDescription() {
        return "leave a party";
    }
}
