package com.songoda.epicrpg.story;

import com.songoda.epicrpg.story.quest.Objective;
import com.songoda.epicrpg.story.quest.Quest;
import com.songoda.epicrpg.story.quest.requirement.AbstractRequirement;
import com.songoda.epicrpg.story.quest.requirement.Requirement;
import com.songoda.epicrpg.story.quest.reward.AbstractReward;
import com.songoda.epicrpg.story.quest.reward.Reward;

import java.util.*;

public class StoryManager {

    private List<Story> stories = new LinkedList<>();

    public Story addStory(Story story) {
        for (Quest quest : story.getQuests()) {
            quest.setStory(story);
            for (Reward reward : quest.getRewards())
                ((AbstractReward) reward).setQuest(quest);
            for (Objective objective : quest.getObjectives()) {
                for (Requirement requirement : objective.getRequirements())
                    ((AbstractRequirement) requirement).setObjective(objective);
                objective.setQuest(quest);
            }
        }
        stories.add(story);
        return story;
    }

    public void removeStory(Story story) {
        stories.remove(story);
    }

    public List<Story> getStories() {
        return Collections.unmodifiableList(stories);
    }

    public List<Quest> getQuests() {
        List<Quest> quests = new ArrayList<>();
        for (Story story : stories)
            if (story.isActive())
                quests.addAll(story.getQuests());
        return quests;
    }

    public Quest getQuest(UUID activeQuest) {
        for (Quest quest : getQuests()) {
            if (activeQuest.equals(quest.getUniqueId()))
                return quest;
        }
        return null;
    }

    public Quest getEnabledQuest(UUID activeQuest) {
        for (Quest quest : getQuests()) {
            if (activeQuest.equals(quest.getUniqueId()) && quest.isActive())
                return quest;
        }
        return null;
    }
}
