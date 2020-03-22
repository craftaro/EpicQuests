package com.songoda.epicrpg.story.quest;

import com.songoda.epicrpg.dialog.AttachedSpeech;
import com.songoda.epicrpg.story.quest.requirement.Requirement;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Objective implements AttachedSpeech {

    private transient Quest quest;

    private UUID uniqueId = UUID.randomUUID();

    private String title = "An Objective";
    private final List<Requirement> requirements = new LinkedList<>();

    private UUID attachedSpeech;

    public Objective(Quest quest) {
        this.quest = quest;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public Quest getQuest() {
        return quest;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
    }

    public List<Requirement> getRequirements() {
        return Collections.unmodifiableList(requirements);
    }

    public void addRequirement(Requirement requirement) {
        requirements.add(requirement);
    }

    public void removeRequirement(Requirement requirement) {
        requirements.remove(requirement);
    }

    @Override
    public UUID getAttachedSpeech() {
        return attachedSpeech;
    }

    @Override
    public void setAttachedSpeech(UUID linkedConversation) {
        this.attachedSpeech = linkedConversation;
    }

}
