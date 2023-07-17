package com.craftaro.epicrpg.story.quest;

import com.craftaro.epicrpg.story.quest.requirement.Requirement;
import com.craftaro.epicrpg.story.quest.reward.Reward;
import com.craftaro.epicrpg.Region.Region;
import com.craftaro.epicrpg.story.Story;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Quest {
    private transient Story story;

    private final UUID uniqueId = UUID.randomUUID();
    private String name = "Unnamed Quest";
    private final List<UUID> questPrerequisites = new LinkedList<>();
    private final List<Reward> rewards = new LinkedList<>();
    private final List<Requirement> requirements = new LinkedList<>(); // ToDo: Confused as to why this is not used.

    private Region region;

    private boolean active = false;
    private boolean ordered = false;

    private final Map<UUID, Objective> objectives = new LinkedHashMap<>();

    public Quest(Story story) {
        this.story = story;
    }

    public Objective addObjective(Objective objective) {
        this.objectives.put(objective.getUniqueId(), objective);
        return objective;
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    public Objective getObjective(UUID uuid) {
        return this.objectives.get(uuid);
    }

    public void removeObjective(Objective objective) {
        this.objectives.remove(objective.getUniqueId());
    }

    public List<Objective> getObjectives() {
        return new ArrayList<>(this.objectives.values());
    }

    public void moveObjectiveToEnd(Objective objective) {
        this.objectives.remove(objective.getUniqueId());
        this.objectives.put(objective.getUniqueId(), objective);
    }

    public Story getStory() {
        return this.story;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return this.active;
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
        this.questPrerequisites.add(quest);
    }

    public void removeQuestPrerequisite(Quest quest) {
        removeQuestPrerequisite(quest.getUniqueId());
    }

    public void removeQuestPrerequisite(UUID quest) {
        this.questPrerequisites.remove(quest);
    }

    public List<UUID> getQuestPrerequisites() {
        return Collections.unmodifiableList(this.questPrerequisites);
    }

    public void addReward(Reward reward) {
        this.rewards.add(reward);
    }

    public void removeReward(Reward reward) {
        this.rewards.remove(reward);
    }

    public List<Reward> getRewards() {
        return Collections.unmodifiableList(this.rewards);
    }

    public void giveRewards(Player player) {
        for (Reward reward : this.rewards) {
            reward.give(player);
        }
    }

    public boolean isOrdered() {
        return this.ordered;
    }

    public void setOrdered(boolean ordered) {
        this.ordered = ordered;
    }

    public Region getRegion() {
        return this.region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public void clearRegion() {
        this.region = null;
    }

    @Override
    public int hashCode() {
        return this.uniqueId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Quest quest = (Quest) obj;
        return this.uniqueId.equals(quest.uniqueId);
    }
}
