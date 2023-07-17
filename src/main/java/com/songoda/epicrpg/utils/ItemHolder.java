package com.songoda.epicrpg.utils;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface ItemHolder {
    void addItem(ItemStack item);

    void removeItem(ItemStack item);

    List<ItemStack> getItems();

    void clearItems();
}
