package com.songoda.epicrpg.story.quest;

import com.songoda.epicrpg.story.Story;
import com.songoda.epicrpg.story.quest.requirement.Requirement;
import com.songoda.epicrpg.story.quest.reward.Reward;
import org.bukkit.entity.Player;

import java.util.*;

public class Quest {

    private transient Story story;

    private UUID uniqueId = UUID.randomUUID();
    private String name = "Unnamed Quest";
    private final List<UUID> questPrerequisites = new LinkedList<>();
    private final List<Reward> rewards = new LinkedList<>();
    private final List<Requirement> requirements = new LinkedList<>();

    private boolean active = false;
    private boolean ordered = false;

    private final Map<UUID, Objective> objectives = new LinkedHashMap<>();

    public Quest(Story story) {
        this.story = story;
    }

    public Objective addObjective(Objective objective) {
        objectives.put(objective.getUniqueId(), objective);
        return objective;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public Objective getObjective(UUID uuid) {
        return objectives.get(uuid);
    }

    public void removeObjective(Objective objective) {
        objectives.remove(objective.getUniqueId());
    }

    public List<Objective> getObjectives() {
        return new ArrayList<>(objectives.values());
    }

    public void moveObjectiveToEnd(Objective objective) {
        objectives.remove(objective.getUniqueId());
        objectives.put(objective.getUniqueId(), objective);
    }

    public Story getStory() {
        return story;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setStory(Story story) {
        this.story = story;
    }

    public void addQuestPrerequisite(Quest quest) {
        addQuestPrerequisite(quest.getUniqueId());
    }

    public void addQuestPrerequisite(UUID quest) {
        questPrerequisites.add(quest);
    }

    public void removeQuestPrerequisite(Quest quest) {
        removeQuestPrerequisite(quest.getUniqueId());
    }

    public void removeQuestPrerequisite(UUID quest) {
        questPrerequisites.remove(quest);
    }

    public List<UUID> getQuestPrerequisites() {
        return Collections.unmodifiableList(questPrerequisites);
    }

    public void addReward(Reward reward) {
        rewards.add(reward);
    }

    public void removeReward(Reward reward) {
        rewards.remove(reward);
    }

    public List<Reward> getRewards() {
        return Collections.unmodifiableList(rewards);
    }

    public void giveRewards(Player player) {
        for (Reward reward : rewards)
            reward.give(player);
    }

    public boolean isOrdered() {
        return ordered;
    }

    public void setOrdered(boolean ordered) {
        this.ordered = ordered;
    }

    @Override
    public int hashCode() {
        return uniqueId.hashCode();
    }
}
