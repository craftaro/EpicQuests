package com.craftaro.epicrpg.story.quest.requirement.requirements;

import com.craftaro.epicrpg.EpicRPG;
import com.craftaro.epicrpg.gui.GuiItems;
import com.craftaro.epicrpg.story.quest.Objective;
import com.craftaro.epicrpg.story.quest.requirement.AbstractRequirement;
import com.craftaro.epicrpg.story.quest.requirement.RequirementType;
import com.craftaro.epicrpg.utils.ItemHolder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EquipRequirement extends AbstractRequirement implements ItemHolder {
    private final EpicRPG plugin;

    private final List<ItemStack> items = new ArrayList<>();

    public EquipRequirement(Objective objective) {
        super(objective);
        this.plugin = JavaPlugin.getPlugin(EpicRPG.class);
    }

    @Override
    public RequirementType getType() {
        return RequirementType.EQUIP;
    }

    @Override
    public boolean isMet(Player player) {
        if (player.getEquipment() == null) {
            return false;
        }

        top:
        for (ItemStack item : this.items) {
            ItemStack[] contents = player.getEquipment().getArmorContents();
            for (ItemStack it : contents) {
                if (it.isSimilar(item)) {
                    continue top;
                }
            }
            return false;
        }
        return true;
    }

    @Override
    public void execute(Player player) {
        for (ItemStack itemStack : this.items) {
            player.getInventory().removeItem(itemStack);
        }
    }

    @Override
    public void setup(Player player) {
        this.plugin.getGuiManager().showGUI(player, new GuiItems(this.plugin, player, this));
    }

    public List<ItemStack> getItems() {
        return Collections.unmodifiableList(this.items);
    }

    public void clearItems() {
        this.items.clear();
    }

    public void addItem(ItemStack item) {
        this.items.add(item);
    }

    public void removeItem(ItemStack item) {
        this.items.remove(item);
    }
}
