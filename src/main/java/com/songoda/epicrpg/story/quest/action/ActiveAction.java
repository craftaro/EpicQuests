package com.songoda.epicrpg.story.quest.action;

import com.songoda.epicrpg.data.ActionDataStore;
import com.songoda.epicrpg.story.quest.Objective;

import java.util.UUID;

public class ActiveAction {

    private final UUID uniqueId = UUID.randomUUID();
    private final Action action;
    private int amount = 0;
    private ActionDataStore actionDataStore;

    public ActiveAction(Action action, int amount, ActionDataStore actionDataStore) {
        this.action = action;
        this.amount = amount;
        this.actionDataStore = actionDataStore;
    }

    public ActiveAction(Action action,ActionDataStore actionDataStore) {
        this.action = action;
        this.actionDataStore = actionDataStore;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public Action getAction() {
        return action;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public ActionDataStore getActionDataStore() {
        return actionDataStore;
    }

    public Objective getObjective() {
        return actionDataStore.getObjective();
    }
}
