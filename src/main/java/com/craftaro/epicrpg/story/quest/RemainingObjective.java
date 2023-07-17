package com.craftaro.epicrpg.story.quest;

import com.craftaro.epicrpg.EpicRPG;
import com.craftaro.epicrpg.story.quest.action.ActiveAction;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class RemainingObjective {
    private final UUID uniqueId;

    private final Map<UUID, Integer> remainingActions = new HashMap<>();

    public RemainingObjective(Objective objective) {
        this.uniqueId = objective.getUniqueId();
        for (ActiveAction activeAction : EpicRPG.getPlugin(EpicRPG.class).getActionManager().getActiveActionsByObjective(objective)) {
            this.remainingActions.put(activeAction.getUniqueId(), activeAction.getAmount());
        }
    }

    public Set<UUID> getRemainingActions() {
        return Collections.unmodifiableSet(this.remainingActions.keySet());
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    public void removeAction(ActiveAction action) {
        this.remainingActions.remove(action.getUniqueId());
    }

    public void completeAction(ActiveAction action, int amount) {
        if (!this.remainingActions.containsKey(action.getUniqueId())) {
            return;
        }
        this.remainingActions.put(action.getUniqueId(), this.remainingActions.get(action.getUniqueId()) - amount);
        amount = getAmount(action);
        if (amount <= 0) {
            this.remainingActions.remove(action.getUniqueId());
        }
    }

    public int getAmount(ActiveAction activeAction) {
        if (!this.remainingActions.containsKey(activeAction.getUniqueId())) {
            return 0;
        }
        return this.remainingActions.get(activeAction.getUniqueId());
    }
}
