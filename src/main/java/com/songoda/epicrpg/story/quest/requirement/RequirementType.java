package com.songoda.epicrpg.story.quest.requirement;

import com.songoda.epicrpg.story.quest.Objective;

import java.lang.reflect.Constructor;

public enum RequirementType {
    ITEM("ItemRequirement"),
    EQUIP("EquipRequirement");

    private final String className;


    RequirementType(String className) {
        this.className = className;
    }

    public Requirement init(Objective objective) {
        try {
            Class<?> clazz = Class.forName("com.songoda.epicrpg.story.quest.requirement.requirements." + this.className);
            Constructor<?> ctor = clazz.getConstructor(Objective.class);
            return (Requirement) ctor.newInstance(objective);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
