package com.songoda.epicrpg.story.quest.action;

import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.data.ActionDataStore;
import com.songoda.epicrpg.dialog.Speech;
import com.songoda.epicrpg.story.player.StoryPlayer;
import com.songoda.epicrpg.story.quest.ActiveQuest;
import com.songoda.epicrpg.story.quest.Objective;
import com.songoda.epicrpg.story.quest.requirement.Requirement;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.List;

public abstract class AbstractAction implements Action {

    @Override
    public void onInteract(PlayerInteractEvent event, ActiveAction activeAction) {

    }

    @Override
    public boolean onInteractWithEntity(PlayerInteractAtEntityEvent event, ActiveAction activeAction) {
        return false;
    }

    @Override
    public void onPickup(PlayerPickupItemEvent event, ActiveAction activeAction) {

    }

    @Override
    public void onDrop(PlayerDropItemEvent event, ActiveAction activeAction) {

    }

    @Override
    public void onEntityKill(EntityDeathEvent event, ActiveAction activeAction) {

    }

    @Override
    public void moveTick(Player player, ActiveAction action) {

    }

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
