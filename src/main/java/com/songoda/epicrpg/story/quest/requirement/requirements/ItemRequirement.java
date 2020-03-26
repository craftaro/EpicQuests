package com.songoda.epicrpg.story.quest.requirement.requirements;

import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.gui.GuiItems;
import com.songoda.epicrpg.story.quest.Objective;
import com.songoda.epicrpg.story.quest.Quest;
import com.songoda.epicrpg.story.quest.requirement.AbstractRequirement;
import com.songoda.epicrpg.story.quest.reward.RewardType;
import com.songoda.epicrpg.utils.ItemHolder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemRequirement extends AbstractRequirement implements ItemHolder {

    private final List<ItemStack> items = new ArrayList<>();

    public ItemRequirement(Objective objective) {
        super(objective);
    }

    @Override
    public RewardType getType() {
        return RewardType.ITEM;
    }

    @Override
    public boolean isMet(Player player) {
        for (ItemStack item : items)
            if (!player.getInventory().contains(item))
                return false;
        return true;
    }

    @Override
    public void execute(Player player) {
        for (ItemStack itemStack : items)
            player.getInventory().remove(itemStack);
    }

    @Override
    public void setup(Player player) {
        EpicRPG plugin = EpicRPG.getInstance();
        plugin.getGuiManager().showGUI(player, new GuiItems(plugin, player, this));
    }

    public List<ItemStack> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void clearItems() {
        items.clear();
    }

    public void addItem(ItemStack item) {
        items.add(item);
    }

    public void removeItem(ItemStack item) {
        items.remove(item);
    }
}
