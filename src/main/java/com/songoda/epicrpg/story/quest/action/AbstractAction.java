package com.songoda.epicrpg.story.quest.action;

import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.dialog.Speech;
import com.songoda.epicrpg.story.player.StoryPlayer;
import com.songoda.epicrpg.story.quest.ActiveQuest;
import com.songoda.epicrpg.story.quest.requirement.Requirement;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class AbstractAction implements Action {

    public boolean performAction(ActiveAction activeAction, int amount, Player player) {
        EpicRPG plugin = EpicRPG.getInstance();
        StoryPlayer storyPlayer = plugin.getPlayerManager().getPlayer(player);
        for (ActiveQuest activeQuest : storyPlayer.getActiveQuests()) {
            if (activeAction.getObjective() == null) {
                plugin.getActionManager().removeActiveAction(activeAction);
                continue;
            }
            if (!activeQuest.getRemainingObjectives().values().iterator().next().getUniqueId()
                    .equals(activeAction.getObjective()
                            .getUniqueId()))
                continue;
            List<Requirement> requirements = activeAction.getObjective().getRequirements();
            for (Requirement requirement : requirements)
                if (!requirement.isMet(player)) {
                    requirement.reject(player);
                    return false;
                }
            for (Requirement requirement : requirements)
                requirement.execute(player);
            Speech speech = plugin.getDialogManager().getSpeech(activeAction.getObjective().getAttachedSpeech());
            if (activeQuest.completeAction(activeAction, amount, activeAction.getObjective()))
                if (speech != null)
                    speech.sendMessages(player, speech.getDialog().getCitizen());

            storyPlayer.toggleAllFocusedOff();
            activeQuest.setFocused(true);
            return false;
        }
        return true;
    }
}
