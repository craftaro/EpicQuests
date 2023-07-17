package com.craftaro.epicrpg.story.quest.reward;

import com.craftaro.epicrpg.story.quest.Quest;
import org.bukkit.entity.Player;

public interface Reward {
    RewardType getType();

    void setup(Player player);

    void give(Player player);

    Quest getQuest();
}
