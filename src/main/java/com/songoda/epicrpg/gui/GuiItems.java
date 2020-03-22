package com.songoda.epicrpg.gui;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.gui.Gui;
import com.songoda.core.gui.GuiUtils;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.story.quest.requirement.requirements.ItemRequirement;
import com.songoda.epicrpg.story.quest.reward.rewards.ItemReward;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GuiItems extends Gui {

    private EpicRPG plugin;
    private Player player;
    private ItemReward reward;
    private ItemRequirement requirement;

    public GuiItems(EpicRPG plugin, Player player, ItemReward reward) {
        this.reward = reward;
        init(plugin, player);
    }

    public GuiItems(EpicRPG plugin, Player player, ItemRequirement requirement) {
        this.requirement = requirement;
        init(plugin, player);
    }

    public void init(EpicRPG plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        setRows(6);
        setDefaultItem(null);
        setAcceptsItems(true);

        setTitle("Item");

        show();
    }

    public void show() {
        if (inventory != null)
            inventory.clear();
        setActionForRange(0, 53, null);

        setActionForRange(9, 53, (event) -> {
            ItemStack item = player.getItemOnCursor();
            if (item.getType() != Material.AIR) {
                if (reward == null)
                    requirement.addItem(item);
                else
                    reward.addItem(item);
                show();
            }
        });

        setButton(0, 8, GuiUtils.createButtonItem(CompatibleMaterial.OAK_DOOR, "Back"),
                (event) -> {
                    if (reward == null)
                        guiManager.showGUI(player, new GuiRequirements(plugin, player, requirement.getObjective()));
                    else
                        guiManager.showGUI(player, new GuiRewards(plugin, player, reward.getQuest()));
                });


        List<ItemStack> items = reward == null ? requirement.getItems() : reward.getItems();
        for (int i = 0; i < items.size(); i++) {
            ItemStack itemStack = items.get(i);

            setButton(i + 9, itemStack,
                    (event) -> {
                        if (reward == null)
                            requirement.removeItem(itemStack);
                        else
                            reward.removeItem(itemStack);
                        show();
                    });
        }
    }
}