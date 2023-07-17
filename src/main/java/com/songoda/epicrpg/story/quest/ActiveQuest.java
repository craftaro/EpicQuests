package com.songoda.epicrpg.story.quest;

import com.songoda.epicrpg.story.quest.action.ActiveAction;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ActiveQuest {
    private final UUID activeQuest;
    private final Map<UUID, RemainingObjective> remainingObjectives = new LinkedHashMap<>();

    private boolean focused;

    public ActiveQuest(Quest activeQuest) {
        this.activeQuest = activeQuest.getUniqueId();
        for (Objective objective : activeQuest.getObjectives()) {
            RemainingObjective remainingObjective = new RemainingObjective(objective);
            this.remainingObjectives.put(objective.getUniqueId(), remainingObjective);
        }
    }

    public UUID getActiveQuest() {
        return this.activeQuest;
    }

    public Map<UUID, RemainingObjective> getRemainingObjectives() {
        return Collections.unmodifiableMap(this.remainingObjectives);
    }

    public boolean completeAction(ActiveAction action, int amount, Objective objective) {
        RemainingObjective remainingObjective = this.remainingObjectives.get(objective.getUniqueId());

        if (remainingObjective == null) return false;

        remainingObjective.completeAction(action, amount);

        if (remainingObjective.getRemainingActions().isEmpty()) {
            this.remainingObjectives.remove(objective.getUniqueId());
            return true;
        }
        return false;
    }

    public boolean isFocused() {
        return this.focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.activeQuest);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Quest) {
            return ((Quest) obj).getUniqueId().equals(this.activeQuest);
        } else if (obj instanceof ActiveQuest) {
            return ((ActiveQuest) obj).activeQuest.equals(this.activeQuest);
        }
        return false;
    }
}
