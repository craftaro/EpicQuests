package com.songoda.epicrpg.story.quest.reward;

import com.songoda.epicrpg.story.quest.Quest;

import java.lang.reflect.Constructor;

public enum RewardType {
    ITEM("ItemReward"),
    COMMAND("CommandReward"),
    XP("XpReward");

    private final String className;


    RewardType(String className) {
        this.className = className;
    }

    public Reward init(Quest quest) {
        try {
            Class<?> clazz = Class.forName("com.songoda.epicrpg.story.quest.reward.rewards." + this.className);
            Constructor<?> ctor = clazz.getConstructor(Quest.class);
            return (Reward) ctor.newInstance(quest);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
