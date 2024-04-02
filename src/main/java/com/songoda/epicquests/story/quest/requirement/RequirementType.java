package com.songoda.epicquests.story.quest.requirement;

import com.songoda.epicquests.story.quest.Objective;

import java.lang.reflect.Constructor;

public enum RequirementType {
    ITEM("ItemRequirement"),
    EQUIP("EquipRequirement");

    private final String className;


    RequirementType(String className) {
        this.className = className;
    }

    public AbstractRequirement init(Objective objective) {
        try {
            Class<?> clazz = Class.forName("com.songoda.epicquests.story.quest.requirement.requirements." + this.className);
            Constructor<?> ctor = clazz.getConstructor(Objective.class);
            return (AbstractRequirement) ctor.newInstance(objective);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
