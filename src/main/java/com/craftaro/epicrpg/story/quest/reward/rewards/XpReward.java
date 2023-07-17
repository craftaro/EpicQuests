package com.craftaro.epicrpg.story.quest.reward.rewards;

import com.craftaro.core.input.ChatPrompt;
import com.craftaro.core.utils.NumberUtils;
import com.craftaro.epicrpg.EpicRPG;
import com.craftaro.epicrpg.gui.GuiRewards;
import com.craftaro.epicrpg.story.quest.Quest;
import com.craftaro.epicrpg.story.quest.reward.AbstractReward;
import com.craftaro.epicrpg.story.quest.reward.RewardType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class XpReward extends AbstractReward {
    private final EpicRPG plugin;

    private int xp;

    public XpReward(Quest quest) {
        super(quest);

        this.plugin = JavaPlugin.getPlugin(EpicRPG.class);
    }

    @Override
    public RewardType getType() {
        return RewardType.ITEM;
    }

    @Override
    public void setup(Player player) {

        ChatPrompt.showPrompt(this.plugin, player,
                        "Enter an xp amount.",
                        response -> {
                            String msg = response.getMessage();
                            if (NumberUtils.isInt(msg)) {
                                this.xp = Integer.parseInt(msg);
                            }
                        })
                .setOnClose(() -> this.plugin.getGuiManager().showGUI(player, new GuiRewards(this.plugin, player, getQuest())));
    }

    @Override
    public void give(Player player) {
        player.giveExp(this.xp);
    }
}
