package com.songoda.epicquests.story.quest.reward;

import com.songoda.epicquests.story.quest.Quest;

import java.lang.reflect.Constructor;

public enum RewardType {
    ITEM("ItemReward"),
    COMMAND("CommandReward"),
    XP("XpReward");

    private final String className;

    RewardType(String className) {
        this.className = className;
    }

    public AbstractReward init(Quest quest) {
        try {
            Class<?> clazz = Class.forName("com.songoda.epicquests.story.quest.reward.rewards." + this.className);
            Constructor<?> ctor = clazz.getConstructor(Quest.class);
            return (AbstractReward) ctor.newInstance(quest);
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
