package com.craftaro.epicrpg.gui;

import com.craftaro.core.gui.Gui;
import com.craftaro.core.gui.GuiUtils;
import com.craftaro.core.third_party.com.cryptomorin.xseries.XMaterial;
import com.craftaro.epicrpg.EpicRPG;
import com.craftaro.epicrpg.story.quest.requirement.Requirement;
import com.craftaro.epicrpg.story.quest.reward.Reward;
import com.craftaro.epicrpg.utils.ItemHolder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GuiItems extends Gui {
    private EpicRPG plugin;
    private Player player;
    private final ItemHolder holder;

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
        this.holder.clearItems();
        for (int i = 9; i < this.inventory.getSize(); i++) {
            ItemStack itemStack = getItem(i);
            if (itemStack != null && itemStack.getType() != Material.AIR) {
                this.holder.addItem(itemStack);
            }
        }
    }

    public void show() {
        reset();

        setButton(0, 8, GuiUtils.createButtonItem(XMaterial.OAK_DOOR, "Back"),
                (event) -> {
                    save();
                    if (this.holder instanceof Requirement) {
                        this.guiManager.showGUI(this.player, new GuiRequirements(this.plugin, this.player, ((Requirement) this.holder).getObjective()));
                    } else {
                        this.guiManager.showGUI(this.player, new GuiRewards(this.plugin, this.player, ((Reward) this.holder).getQuest()));
                    }
                });

        List<ItemStack> items = this.holder.getItems();
        for (int i = 0; i < items.size(); i++) {
            ItemStack itemStack = items.get(i);
            setItem(i + 9, itemStack);
        }
    }
}
