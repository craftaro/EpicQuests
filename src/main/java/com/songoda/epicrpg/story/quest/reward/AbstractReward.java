package com.songoda.epicrpg.story.quest.reward;

import com.songoda.epicrpg.story.quest.Quest;

public abstract class AbstractReward implements Reward {

    private transient Quest quest;

    public AbstractReward(Quest quest) {
        this.quest = quest;
    }

    @Override
    public Quest getQuest() {
        return quest;
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
    }
}
