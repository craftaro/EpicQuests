package com.songoda.epicrpg.story.player;

import com.songoda.epicrpg.story.quest.ActiveQuest;
import com.songoda.epicrpg.story.quest.Objective;
import com.songoda.epicrpg.story.quest.Quest;
import com.songoda.epicrpg.story.quest.RemainingObjective;
import com.songoda.epicrpg.story.quest.action.ActiveAction;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class StoryPlayer {

    private final UUID uniqueId;

    private boolean inDialogCreation = false;

    private final Set<ActiveQuest> activeQuests = new HashSet<>();
    private final Set<UUID> completedQuests = new HashSet<>();

    public StoryPlayer(UUID uniqueId) {
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

    public Set<ActiveQuest> getActiveQuests() {
        return Collections.unmodifiableSet(activeQuests);
    }

    public Set<UUID> getCompletedQuests() {
        return Collections.unmodifiableSet(completedQuests);
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

    public UUID getUniqueId() {
        return uniqueId;
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

    public boolean isInDialogCreation() {
        return inDialogCreation;
    }

    public void setInDialogCreation(boolean inDialogCreation) {
        this.inDialogCreation = inDialogCreation;
    }

    public void reset() {
        activeQuests.clear();
        completedQuests.clear();
    }
}
