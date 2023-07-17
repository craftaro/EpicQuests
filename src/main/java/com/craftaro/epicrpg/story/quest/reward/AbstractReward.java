package com.craftaro.epicrpg.story.quest.reward;

import com.craftaro.epicrpg.story.quest.Quest;

public abstract class AbstractReward implements Reward {
    private transient Quest quest;

    public AbstractReward(Quest quest) {
        this.quest = quest;
    }

    @Override
    public Quest getQuest() {
        return this.quest;
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
    }
}
