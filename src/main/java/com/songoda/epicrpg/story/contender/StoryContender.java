package com.songoda.epicrpg.story.contender;

import com.songoda.epicrpg.story.quest.ActiveQuest;
import com.songoda.epicrpg.story.quest.Objective;
import com.songoda.epicrpg.story.quest.Quest;
import com.songoda.epicrpg.story.quest.RemainingObjective;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class StoryContender {
    private final UUID uniqueId;

    protected final List<ActiveQuest> activeQuests = new ArrayList<>();
    protected final Set<UUID> completedQuests = new HashSet<>();

    public StoryContender(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public ActiveQuest addActiveQuest(Quest quest) {
        ActiveQuest activeQuest = new ActiveQuest(quest);
        this.activeQuests.add(activeQuest);
        return activeQuest;
    }

    public ActiveQuest addActiveQuest(ActiveQuest quest) {
        this.activeQuests.add(quest);
        return quest;
    }

    public UUID addCompletedQuest(UUID quest) {
        this.completedQuests.add(quest);
        return quest;
    }

    public List<ActiveQuest> getActiveQuests() {
        return Collections.unmodifiableList(this.activeQuests);
    }

    public Set<UUID> getCompletedQuests() {
        return Collections.unmodifiableSet(this.completedQuests);
    }

    public boolean hasCompletedQuest(UUID uniqueId) {
        return this.completedQuests.contains(uniqueId);
    }

    public ActiveQuest getActiveQuest(Quest quest) {
        for (ActiveQuest q : this.activeQuests) {
            if (q.getActiveQuest().equals(quest.getUniqueId())) {
                return q;
            }
        }
        return null;
    }

    public ActiveQuest getNextQuest() {
        for (ActiveQuest quest : this.activeQuests) {
            return quest;
        }
        return null;
    }

    public void completeQuest(Quest quest) {
        ActiveQuest active = getActiveQuest(quest);
        this.completedQuests.add(active.getActiveQuest());
        this.activeQuests.remove(active);
    }

    protected boolean addCompletedQuests(Set<UUID> completedQuests) {
        return this.completedQuests.addAll(completedQuests);
    }

    public boolean isObjectiveCompleted(Objective objective) {
        for (ActiveQuest activeQuest : getActiveQuests()) {
            for (RemainingObjective remainingObjective : activeQuest.getRemainingObjectives().values()) {
                if (remainingObjective.getUniqueId().equals(objective.getUniqueId())) {
                    return false;
                }
            }
        }

        return this.completedQuests.contains(objective.getQuest().getUniqueId()) || isActiveQuest(objective.getQuest());
    }

    private boolean isActiveQuest(Quest quest) {
        for (ActiveQuest activeQuest : this.activeQuests) {
            if (activeQuest.getActiveQuest().equals(quest.getUniqueId())) {
                return true;
            }
        }

        return false;
    }

    public boolean isFocused(Quest quest) {
        ActiveQuest activeQuest = getActiveQuest(quest);
        return activeQuest != null && activeQuest.isFocused();
    }

    public void toggleAllFocusedOff() {
        for (ActiveQuest quest : this.activeQuests) {
            quest.setFocused(false);
        }
    }

    public void toggleFocus(Quest quest) {
        ActiveQuest activeQuest = getActiveQuest(quest);
        activeQuest.setFocused(!activeQuest.isFocused());
    }

    public void reset() {
        this.activeQuests.clear();
        this.completedQuests.clear();
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public String toString() {
        return "StoryContender{" +
                "uniqueId=" + this.uniqueId +
                ", activeQuests=" + this.activeQuests +
                ", completedQuests=" + this.completedQuests +
                '}';
    }
}
