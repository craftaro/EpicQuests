package com.songoda.epicrpg.story.quest.reward.rewards;

import com.songoda.core.input.ChatPrompt;
import com.songoda.core.utils.NumberUtils;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.gui.GuiRewards;
import com.songoda.epicrpg.story.quest.Quest;
import com.songoda.epicrpg.story.quest.reward.AbstractReward;
import com.songoda.epicrpg.story.quest.reward.RewardType;
import org.bukkit.entity.Player;

public class XpReward extends AbstractReward {
    private int xp;

    public XpReward(Quest quest) {
        super(quest);
    }

    @Override
    public RewardType getType() {
        return RewardType.ITEM;
    }

    @Override
    public void setup(Player player) {
        EpicRPG plugin = EpicRPG.getInstance();
        ChatPrompt.showPrompt(plugin, player,
                        "Enter an xp amount.",
                        response -> {
                            String msg = response.getMessage();
                            if (NumberUtils.isInt(msg)) {
                                this.xp = Integer.parseInt(msg);
                            }
                        })
                .setOnClose(() -> plugin.getGuiManager().showGUI(player, new GuiRewards(plugin, player, getQuest())));
    }

    @Override
    public void give(Player player) {
        player.giveExp(this.xp);
    }
}
