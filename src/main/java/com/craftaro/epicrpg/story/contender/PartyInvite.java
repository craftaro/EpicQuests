package com.craftaro.epicrpg.story.contender;

import java.util.UUID;

public class PartyInvite {
    private final UUID sender;
    private final UUID recipient;

    public PartyInvite(UUID sender, UUID recipient) {
        this.sender = sender;
        this.recipient = recipient;
    }

    public UUID getSender() {
        return this.sender;
    }

    public UUID getRecipient() {
        return this.recipient;
    }
}
