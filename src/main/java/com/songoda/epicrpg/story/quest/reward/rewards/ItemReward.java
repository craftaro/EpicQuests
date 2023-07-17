package com.songoda.epicrpg.story.quest.reward.rewards;

import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.gui.GuiItems;
import com.songoda.epicrpg.story.quest.Quest;
import com.songoda.epicrpg.story.quest.reward.AbstractReward;
import com.songoda.epicrpg.story.quest.reward.RewardType;
import com.songoda.epicrpg.utils.ItemHolder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ItemReward extends AbstractReward implements ItemHolder {
    private final List<ItemStack> items = new ArrayList<>();

    public ItemReward(Quest quest) {
        super(quest);
    }

    @Override
    public RewardType getType() {
        return RewardType.ITEM;
    }

    @Override
    public void setup(Player player) {
        EpicRPG plugin = EpicRPG.getInstance();
        plugin.getGuiManager().showGUI(player, new GuiItems(plugin, player, this));
    }

    @Override
    public void give(Player player) {
        Map<Integer, ItemStack> overfilled = player.getInventory().addItem(this.items.toArray(new ItemStack[0]));
        for (ItemStack item : overfilled.values()) {
            player.getWorld().dropItemNaturally(player.getLocation(), item);
        }
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
