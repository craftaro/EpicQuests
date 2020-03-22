package com.songoda.epicrpg.story.quest;

import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.story.quest.action.ActiveAction;

import java.util.*;

public class RemainingObjective {

    private final UUID uniqueId;

    private final Map<UUID, Integer> remainingActions = new HashMap<>();

    public RemainingObjective(Objective objective) {
        this.uniqueId = objective.getUniqueId();
        for (ActiveAction activeAction : EpicRPG.getInstance().getActionManager().getActiveActionsByObjective(objective))
            remainingActions.put(activeAction.getUniqueId(), activeAction.getAmount());
    }

    public Set<UUID> getRemainingActions() {
        return Collections.unmodifiableSet(remainingActions.keySet());
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public void removeAction(ActiveAction action) {
        remainingActions.remove(action.getUniqueId());
    }

    public void completeAction(ActiveAction action, int amount) {
        if (!remainingActions.containsKey(action.getUniqueId())) return;
        remainingActions.put(action.getUniqueId(), remainingActions.get(action.getUniqueId()) - amount);
        amount = getAmount(action);
        if (amount <= 0) remainingActions.remove(action.getUniqueId());
    }

    public int getAmount(ActiveAction activeAction) {
        if (!remainingActions.containsKey(activeAction.getUniqueId()))
            return 0;
        return remainingActions.get(activeAction.getUniqueId());
    }
}
