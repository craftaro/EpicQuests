package com.craftaro.epicrpg.gui;

import com.craftaro.core.gui.Gui;
import com.craftaro.core.gui.GuiUtils;
import com.craftaro.third_party.com.cryptomorin.xseries.XMaterial;
import com.craftaro.epicrpg.EpicRPG;
import com.craftaro.epicrpg.story.quest.Quest;
import com.craftaro.epicrpg.story.quest.reward.Reward;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

public class GuiRewards extends Gui {
    private final EpicRPG plugin;
    private final Player player;
    private final Quest quest;

    public GuiRewards(EpicRPG plugin, Player player, Quest quest) {
        this.plugin = plugin;
        this.player = player;
        this.quest = quest;
        setRows(6);
        setDefaultItem(null);

        setTitle(quest.getName() + " - rewards");

        show();
    }

    public void show() {
        reset();

        setButton(0, 0, GuiUtils.createButtonItem(XMaterial.GREEN_DYE, "Add Reward"),
                (event) -> this.guiManager.showGUI(this.player, new GuiRewardTypes(this.plugin, this.player, this.quest)));

        setButton(0, 8, GuiUtils.createButtonItem(XMaterial.OAK_DOOR, "Back"),
                (event) -> this.guiManager.showGUI(this.player, new GuiQuest(this.plugin, this.player, this.quest)));


        List<Reward> rewards = this.quest.getRewards();
        for (int i = 0; i < rewards.size(); i++) {
            Reward reward = rewards.get(i);

            setButton(i + 9, GuiUtils.createButtonItem(XMaterial.PAPER, reward.getType().name()),
                    (event) -> {
                        if (event.clickType == ClickType.RIGHT) {
                            this.quest.removeReward(reward);
                            show();
                        } else if (event.clickType == ClickType.LEFT) {
                            reward.setup(this.player);
                        }
                    });
        }
    }
}
