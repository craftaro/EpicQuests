package com.craftaro.epicrpg.story.quest.action;

import com.craftaro.epicrpg.data.ActionDataStore;
import com.craftaro.epicrpg.story.quest.Objective;

import java.util.UUID;

public class ActiveAction {
    private final UUID uniqueId = UUID.randomUUID();
    private final Action action;
    private int amount = 0;
    private final ActionDataStore actionDataStore;

    public ActiveAction(Action action, int amount, ActionDataStore actionDataStore) {
        this.action = action;
        this.amount = amount;
        this.actionDataStore = actionDataStore;
    }

    public ActiveAction(Action action, ActionDataStore actionDataStore) {
        this.action = action;
        this.actionDataStore = actionDataStore;
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    public Action getAction() {
        return this.action;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return this.amount;
    }

    public ActionDataStore getActionDataStore() {
        return this.actionDataStore;
    }

    public Objective getObjective() {
        return this.actionDataStore.getObjective();
    }
}
