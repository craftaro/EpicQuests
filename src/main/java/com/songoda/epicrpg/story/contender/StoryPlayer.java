package com.songoda.epicrpg.story.contender;

import java.util.UUID;

public class StoryPlayer extends StoryContender {

    private StoryParty party;
    private boolean inDialogCreation = false;

    public StoryPlayer(UUID uniqueId) {
        super(uniqueId);
    }

    public boolean isInDialogCreation() {
        return inDialogCreation;
    }

    public void setInDialogCreation(boolean inDialogCreation) {
        this.inDialogCreation = inDialogCreation;
    }

    public StoryParty getParty() {
        return party;
    }

    public void setParty(StoryParty party) {
        this.party = party;
    }
}
