package com.songoda.epicrpg.data;

import com.songoda.epicrpg.story.quest.Objective;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class ActionDataStore {

    private final Objective objective;
    private UUID setter;

    public ActionDataStore(Objective objective) {
        this.objective = objective;
    }

    public Objective getObjective() {
        return objective;
    }

    public void startSetup(Entity setter) {
        startSetup(setter.getUniqueId());
    }

    public void startSetup(UUID setter) {
        this.setter = setter;
    }

    public boolean isBeingSetup(Entity entity) {
        return isBeingSetup(entity.getUniqueId());
    }

    public boolean isBeingSetup(UUID setter) {
        if (this.setter == null) return false;
        return this.setter.equals(setter);
    }

    public void finishSetup() {
        setter = null;
    }
}
