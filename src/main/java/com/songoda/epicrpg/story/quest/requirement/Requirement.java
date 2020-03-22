package com.songoda.epicrpg.story.quest.requirement;

import com.songoda.epicrpg.story.quest.Objective;
import com.songoda.epicrpg.story.quest.reward.RewardType;
import org.bukkit.entity.Player;

public interface Requirement {

    RewardType getType();

    boolean isMet(Player player);

    void execute(Player player);

    void setup(Player player);

    void reject(Player player);

    Objective getObjective();

}
