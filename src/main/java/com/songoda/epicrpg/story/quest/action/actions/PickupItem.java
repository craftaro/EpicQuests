package com.songoda.epicrpg.story.quest.action.actions;

import com.songoda.core.input.ChatPrompt;
import com.songoda.core.utils.ItemUtils;
import com.songoda.core.utils.TextUtils;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.data.ActionDataStore;
import com.songoda.epicrpg.gui.GuiObjective;
import com.songoda.epicrpg.story.quest.Objective;
import com.songoda.epicrpg.story.quest.action.AbstractAction;
import com.songoda.epicrpg.story.quest.action.ActiveAction;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class PickupItem extends AbstractAction {

    public PickupItem(EpicRPG plugin) {
        super(plugin);
    }

    @Override
    public String getType() {
        return "PICKUP_ITEM";
    }

    @Override
    public List<String> getDescription(ActionDataStore actionDataStore) {
        PickupItemDataStore dataStore = (PickupItemDataStore) actionDataStore;
        return dataStore.getItemStack() == null ? Collections.singletonList("None") : Collections.singletonList(TextUtils.formatText("&fItem: &6" + dataStore.getItemStack().getType().name()));
    }

    @Override
    public void onPickup(PlayerPickupItemEvent event, ActiveAction activeAction) {
        PickupItemDataStore dataStore = (PickupItemDataStore) activeAction.getActionDataStore();

        ItemStack item = event.getItem().getItemStack();

        if (!item.isSimilar(dataStore.getItemStack())) return;

        performAction(activeAction, item.getAmount(), event.getPlayer());

    }

    @Override
    public void onDrop(PlayerDropItemEvent event, ActiveAction activeAction) {
        Player player = event.getPlayer();
        PickupItemDataStore dataStore = (PickupItemDataStore) activeAction.getActionDataStore();

        if (!dataStore.isBeingSetup(player)) return;
        dataStore.setItemStack(event.getItemDrop().getItemStack());

        ChatPrompt.showPrompt(plugin, player,
                "Enter a required amount.",
                response -> {
                    activeAction.setAmount(Integer.parseInt(response.getMessage()));
                    plugin.getGuiManager().showGUI(player, new GuiObjective(plugin, player, dataStore.getObjective()));
                });
        dataStore.finishSetup();
    }

    @Override
    public ActiveAction setup(Player player, Objective objective) {
        player.sendMessage("Drop the item you would like assigned to this action.");
        PickupItemDataStore dataStore = new PickupItemDataStore(objective);
        dataStore.startSetup(player.getUniqueId());

        // Do setup here.
        return new ActiveAction(this, dataStore);
    }

    public class PickupItemDataStore extends ActionDataStore {

        private ItemStack itemStack;

        public PickupItemDataStore(Objective objective) {
            super(objective);
        }

        public ItemStack getItemStack() {
            return itemStack;
        }

        public void setItemStack(ItemStack itemStack) {
            this.itemStack = itemStack;
        }
    }
}
