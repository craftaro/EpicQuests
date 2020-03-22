package com.songoda.epicrpg.story;

import com.songoda.epicrpg.story.quest.Quest;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Story {

    private UUID uniqueId = UUID.randomUUID();
    private String name = "Unnamed Story";
    private final List<Quest> quests = new LinkedList<>();

    private boolean active = false;

    public Quest addQuest(Quest quest) {
        quests.add(quest);
        return quest;
    }

    public void removeQuest(Quest quest) {
        quests.remove(quest);
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public List<Quest> getQuests() {
        return Collections.unmodifiableList(quests);
    }

    public List<Quest> getEnabledQuests() {
        return quests.stream().filter(Quest::isActive).collect(Collectors.toList());
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
}
