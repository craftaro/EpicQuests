package com.craftaro.epicrpg.story.quest.requirement;

import com.craftaro.epicrpg.story.quest.Objective;
import org.bukkit.entity.Player;

public interface Requirement {
    RequirementType getType();

    boolean isMet(Player player);

    void execute(Player player);

    void setup(Player player);

    void reject(Player player);

    Objective getObjective();
}
