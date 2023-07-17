package com.craftaro.epicrpg.story;

import com.craftaro.epicrpg.story.quest.Quest;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Story {
    private final UUID uniqueId = UUID.randomUUID();
    private String name = "Unnamed Story";
    private final List<Quest> quests = new LinkedList<>();

    private boolean active = false;

    public Quest addQuest(Quest quest) {
        this.quests.add(quest);
        return quest;
    }

    public void removeQuest(Quest quest) {
        this.quests.remove(quest);
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    public List<Quest> getQuests() {
        return Collections.unmodifiableList(this.quests);
    }

    public List<Quest> getEnabledQuests() {
        return this.quests.stream().filter(Quest::isActive).collect(Collectors.toList());
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
}
