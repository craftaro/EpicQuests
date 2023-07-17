package com.craftaro.epicrpg.story.quest.action;

import com.craftaro.epicrpg.EpicRPG;
import com.craftaro.epicrpg.dialog.Speech;
import com.craftaro.epicrpg.story.contender.StoryContender;
import com.craftaro.epicrpg.story.quest.ActiveQuest;
import com.craftaro.epicrpg.story.quest.RemainingObjective;
import com.craftaro.epicrpg.story.quest.requirement.Requirement;
import io.lumine.mythic.bukkit.utils.events.extra.ArmorEquipEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.Iterator;
import java.util.List;

public abstract class AbstractAction implements Action {
    protected final transient EpicRPG plugin;

    public AbstractAction(EpicRPG plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onInteract(PlayerInteractEvent event, ActiveAction activeAction) {
    }

    @Override
    public void onInteractWithEntity(PlayerInteractAtEntityEvent event, ActiveAction activeAction) {
    }

    @Override
    public void onPickup(PlayerPickupItemEvent event, ActiveAction activeAction) {
    }

    @Override
    public void onDrop(PlayerDropItemEvent event, ActiveAction activeAction) {
    }

    @Override
    public void onEquip(ArmorEquipEvent event, ActiveAction activeAction) {
    }

    @Override
    public void onEntityKill(EntityDeathEvent event, ActiveAction activeAction) {
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event, ActiveAction action) {
    }

    @Override
    public void moveTick(Player player, ActiveAction action) {
    }

    protected boolean performAction(ActiveAction activeAction, int amount, Player player) {
        StoryContender contender = this.plugin.getContendentManager().getContender(player);
        for (ActiveQuest activeQuest : contender.getActiveQuests()) {
            if (activeAction.getObjective() == null) {
                this.plugin.getActionManager().removeActiveAction(activeAction);
                continue;
            }

            Iterator<RemainingObjective> remainingObjectives = activeQuest.getRemainingObjectives().values().iterator();

            if (remainingObjectives.hasNext() && !remainingObjectives.next().getUniqueId().equals(activeAction.getObjective().getUniqueId())) {
                continue;
            }
            List<Requirement> requirements = activeAction.getObjective().getRequirements();
            for (Requirement requirement : requirements) {
                if (!requirement.isMet(player)) {
                    requirement.reject(player);
                    return false;
                }
            }
            for (Requirement requirement : requirements) {
                requirement.execute(player);
            }
            Speech speech = this.plugin.getDialogManager().getSpeech(activeAction.getObjective().getAttachedSpeech());
            if (activeQuest.completeAction(activeAction, amount, activeAction.getObjective())) {
                if (speech != null) {
                    speech.sendMessages(player, speech.getDialog().getCitizen());
                }
            }

            contender.toggleAllFocusedOff();
            activeQuest.setFocused(true);
            return false;
        }
        return true;
    }
}
