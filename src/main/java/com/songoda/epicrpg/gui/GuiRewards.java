package com.songoda.epicrpg.gui;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.gui.Gui;
import com.songoda.core.gui.GuiUtils;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.story.StoryManager;
import com.songoda.epicrpg.story.quest.Quest;
import com.songoda.epicrpg.story.quest.reward.Reward;
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

        setButton(0, 0, GuiUtils.createButtonItem(CompatibleMaterial.GREEN_DYE, "Add Reward"),
                (event) -> {
                    guiManager.showGUI(player, new GuiRewardTypes(plugin, player, quest));
                });

        setButton(0, 8, GuiUtils.createButtonItem(CompatibleMaterial.OAK_DOOR, "Back"),
                (event) -> {
                    guiManager.showGUI(player, new GuiQuest(plugin, player, quest));
                });


        List<Reward> rewards = quest.getRewards();
        for (int i = 0; i < rewards.size(); i++) {
            Reward reward = rewards.get(i);

            setButton(i + 9, GuiUtils.createButtonItem(CompatibleMaterial.PAPER, reward.getType().name()),
                    (event) -> {
                        if (event.clickType == ClickType.RIGHT) {
                            quest.removeReward(reward);
                            show();
                        } else if (event.clickType == ClickType.LEFT) {
                            reward.setup(player);
                        }
                    });
        }
    }
}