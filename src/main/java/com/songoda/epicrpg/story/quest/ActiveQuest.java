package com.songoda.epicrpg.story.quest;

import com.songoda.epicrpg.story.quest.action.ActiveAction;

import java.util.*;

public class ActiveQuest {

    private final UUID activeQuest;
    private Map<UUID, RemainingObjective> remainingObjectives = new LinkedHashMap<>();

    private boolean focused;

    public ActiveQuest(Quest activeQuest) {
        this.activeQuest = activeQuest.getUniqueId();
        for (Objective objective : activeQuest.getObjectives()) {
            RemainingObjective remainingObjective = new RemainingObjective(objective);
            remainingObjectives.put(objective.getUniqueId(), remainingObjective);
        }
    }

    public UUID getActiveQuest() {
        return activeQuest;
    }

    public Map<UUID, RemainingObjective> getRemainingObjectives() {
        return Collections.unmodifiableMap(remainingObjectives);
    }

    public boolean completeAction(ActiveAction action, int amount, Objective objective) {
        RemainingObjective remainingObjective = remainingObjectives.get(objective.getUniqueId());

        if (remainingObjective == null) return false;

        remainingObjective.completeAction(action, amount);

        if (remainingObjective.getRemainingActions().isEmpty()) {
            remainingObjectives.remove(objective.getUniqueId());
            return true;
        }
        return false;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    @Override
    public int hashCode() {
        return Objects.hash(activeQuest);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Quest) {
            return ((Quest) object).getUniqueId().equals(activeQuest);
        } else if (object instanceof ActiveQuest) {
            return ((ActiveQuest) object).activeQuest.equals(activeQuest);
        }
        return false;
    }
}
