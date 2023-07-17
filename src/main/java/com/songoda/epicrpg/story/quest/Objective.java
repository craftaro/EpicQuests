package com.songoda.epicrpg.story.quest;

import com.songoda.epicrpg.dialog.AttachedSpeech;
import com.songoda.epicrpg.story.quest.requirement.Requirement;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Objective implements AttachedSpeech {
    private transient Quest quest;

    private final UUID uniqueId = UUID.randomUUID();

    private String title = "An Objective";
    private final List<Requirement> requirements = new LinkedList<>();

    private boolean visible = true;

    private UUID attachedSpeech;

    public Objective(Quest quest) {
        this.quest = quest;
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    public Quest getQuest() {
        return this.quest;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
    }

    public List<Requirement> getRequirements() {
        return Collections.unmodifiableList(this.requirements);
    }

    public void addRequirement(Requirement requirement) {
        this.requirements.add(requirement);
    }

    public void removeRequirement(Requirement requirement) {
        this.requirements.remove(requirement);
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public UUID getAttachedSpeech() {
        return this.attachedSpeech;
    }

    @Override
    public void setAttachedSpeech(UUID rejection) {
        this.attachedSpeech = rejection;
    }
}
