package com.craftaro.epicrpg.story.quest.action.actions;

import com.craftaro.core.utils.TextUtils;
import com.craftaro.epicrpg.EpicRPG;
import com.craftaro.epicrpg.data.ActionDataStore;
import com.craftaro.epicrpg.story.quest.Objective;
import com.craftaro.epicrpg.story.quest.action.AbstractAction;
import com.craftaro.epicrpg.story.quest.action.ActiveAction;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class DropItem extends AbstractAction {
    public DropItem(EpicRPG plugin) {
        super(plugin);
    }

    @Override
    public String getType() {
        return "DROP_ITEM";
    }

    @Override
    public List<String> getDescription(ActionDataStore actionDataStore) {
        DropItemDataStore dataStore = (DropItemDataStore) actionDataStore;
        return dataStore.getItemStack() == null ? Collections.singletonList("None") : Collections.singletonList(TextUtils.formatText("&fItem: &6" + dataStore.getItemStack().getType().name()));
    }

    @Override
    public void onDrop(PlayerDropItemEvent event, ActiveAction activeAction) {
        DropItemDataStore dataStore = (DropItemDataStore) activeAction.getActionDataStore();

        ItemStack item = event.getItemDrop().getItemStack();

        if (item.isSimilar(dataStore.getItemStack())) {
            performAction(activeAction, item.getAmount(), event.getPlayer());
        }

        Player player = event.getPlayer();

        if (!dataStore.isBeingSetup(player)) {
            return;
        }
        dataStore.setItemStack(event.getItemDrop().getItemStack());
        dataStore.finishSetup(this.plugin, player, activeAction);
    }

    @Override
    public ActiveAction setup(Player player, Objective objective) {
        player.sendMessage("Drop the item you would like assigned to this action.");
        DropItemDataStore dataStore = new DropItemDataStore(objective);
        dataStore.startSetup(player.getUniqueId());

        // Do setup here.
        return new ActiveAction(this, dataStore);
    }

    public static class DropItemDataStore extends ActionDataStore {
        private ItemStack itemStack;

        public DropItemDataStore(Objective objective) {
            super(objective);
        }

        public ItemStack getItemStack() {
            return this.itemStack;
        }

        public void setItemStack(ItemStack itemStack) {
            this.itemStack = itemStack;
        }
    }
}
