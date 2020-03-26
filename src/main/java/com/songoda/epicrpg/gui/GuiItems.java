package com.songoda.epicrpg.gui;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.gui.Gui;
import com.songoda.core.gui.GuiUtils;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.story.quest.requirement.Requirement;
import com.songoda.epicrpg.story.quest.reward.Reward;
import com.songoda.epicrpg.utils.ItemHolder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GuiItems extends Gui {

    private EpicRPG plugin;
    private Player player;
    private ItemHolder holder;

    public GuiItems(EpicRPG plugin, Player player, ItemHolder holder) {
        this.holder = holder;
        init(plugin, player);
    }

    public void init(EpicRPG plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        setRows(6);
        setDefaultItem(null);
        setAcceptsItems(true);

        setTitle("Item");

        setOnClose((event) -> save());

        setUnlockedRange(1, 0, 6, 9);

        show();
    }

    public void save() {
        holder.clearItems();
        for (int i = 9; i < inventory.getSize(); i++) {
            ItemStack itemStack = getItem(i);
            if (itemStack != null && itemStack.getType() != Material.AIR)
                holder.addItem(itemStack);
        }
    }

    public void show() {
        if (inventory != null)
            inventory.clear();

        setButton(0, 8, GuiUtils.createButtonItem(CompatibleMaterial.OAK_DOOR, "Back"),
                (event) -> {
                    save();
                    if (holder instanceof Requirement)
                        guiManager.showGUI(player, new GuiRequirements(plugin, player, ((Requirement) holder).getObjective()));
                    else
                        guiManager.showGUI(player, new GuiRewards(plugin, player, ((Reward) holder).getQuest()));
                });


        List<ItemStack> items = holder.getItems();
        for (int i = 0; i < items.size(); i++) {
            ItemStack itemStack = items.get(i);
            setItem(i + 9, itemStack);
        }
    }
}