package com.songoda.epicrpg.story.contender;

import com.songoda.epicrpg.story.quest.ActiveQuest;
import com.songoda.epicrpg.story.quest.Objective;
import com.songoda.epicrpg.story.quest.Quest;
import com.songoda.epicrpg.story.quest.RemainingObjective;

import java.util.*;

public class StoryContender {

    private final UUID uniqueId;

    protected final List<ActiveQuest> activeQuests = new ArrayList<>();
    protected final Set<UUID> completedQuests = new HashSet<>();

    public StoryContender(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public ActiveQuest addActiveQuest(Quest quest) {
        ActiveQuest activeQuest = new ActiveQuest(quest);
        activeQuests.add(activeQuest);
        return activeQuest;
    }

    public ActiveQuest addActiveQuest(ActiveQuest quest) {
        activeQuests.add(quest);
        return quest;
    }

    public UUID addCompletedQuest(UUID quest) {
        completedQuests.add(quest);
        return quest;
    }

    public List<ActiveQuest> getActiveQuests() {
        return Collections.unmodifiableList(activeQuests);
    }

    public Set<UUID> getCompletedQuests() {
        return Collections.unmodifiableSet(completedQuests);
    }

    public boolean hasCompletedQuest(UUID uniqueId) {
        return this.completedQuests.contains(uniqueId);
    }

    public ActiveQuest getActiveQuest(Quest quest) {
        for (ActiveQuest q : activeQuests) {
            if (q.getActiveQuest().equals(quest.getUniqueId()))
            return q;
        }
        return null;
    }

    public ActiveQuest getNextQuest() {
        for (ActiveQuest quest : activeQuests) {
            return quest;
        }
        return null;
    }

    public void completeQuest(Quest quest) {
        ActiveQuest active = getActiveQuest(quest);
        completedQuests.add(active.getActiveQuest());
        activeQuests.remove(active);
    }

    protected boolean addCompletedQuests(Set<UUID> completedQuests) {
        return this.completedQuests.addAll(completedQuests);
    }

    public boolean isObjectiveCompleted(Objective objective) {
        for (ActiveQuest activeQuest : getActiveQuests()) {
            for (RemainingObjective remainingObjective : activeQuest.getRemainingObjectives().values())
                if (remainingObjective.getUniqueId().equals(objective.getUniqueId()))
                    return false;
        }
        return completedQuests.contains(objective.getQuest().getUniqueId()) || isActiveQuest(objective.getQuest());
    }

    private boolean isActiveQuest(Quest quest) {
        for (ActiveQuest activeQuest : activeQuests)
            if (activeQuest.getActiveQuest().equals(quest.getUniqueId()))
                return true;
        return false;
    }

    public boolean isFocused(Quest quest) {
        ActiveQuest activeQuest = getActiveQuest(quest);
        return activeQuest != null && activeQuest.isFocused();
    }

    public void toggleAllFocusedOff() {
        for (ActiveQuest quest : activeQuests)
            quest.setFocused(false);
    }

    public void toggleFocus(Quest quest) {
        ActiveQuest activeQuest = getActiveQuest(quest);
        activeQuest.setFocused(!activeQuest.isFocused());
    }

    public void reset() {
        activeQuests.clear();
        completedQuests.clear();
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public String toString() {
        return "StoryContender{" +
                "uniqueId=" + uniqueId +
                ", activeQuests=" + activeQuests +
                ", completedQuests=" + completedQuests +
                '}';
    }
}
