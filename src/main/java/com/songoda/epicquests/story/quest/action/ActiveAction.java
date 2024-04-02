package com.songoda.epicquests.story.quest.action;

import com.songoda.epicquests.data.ActionDataStore;
import com.songoda.epicquests.story.quest.Objective;

public class ActiveAction {
    private final AbstractAction action;
    private final ActionDataStore actionDataStore;

    public ActiveAction(AbstractAction action, int amount, ActionDataStore actionDataStore) {
        this.action = action;
        this.actionDataStore = actionDataStore;
        actionDataStore.setAmount(amount);
    }

    public ActiveAction(AbstractAction action, ActionDataStore actionDataStore) {
        this.action = action;
        this.actionDataStore = actionDataStore;
    }

    public AbstractAction getAction() {
        return this.action;
    }

    public ActionDataStore getActionDataStore() {
        return this.actionDataStore;
    }

    public Objective getObjective() {
        return this.actionDataStore.getObjective();
    }

    public int getAmount() {
        return this.actionDataStore.getAmount();
    }

    public int getId() {
        return this.actionDataStore.getId();
    }

    public void setAmount(int amount) {
        this.actionDataStore.setAmount(amount);
    }
}
