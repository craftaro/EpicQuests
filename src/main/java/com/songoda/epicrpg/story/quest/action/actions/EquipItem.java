package com.songoda.epicrpg.story.quest.action.actions;

import com.songoda.core.utils.TextUtils;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.data.ActionDataStore;
import com.songoda.epicrpg.story.quest.Objective;
import com.songoda.epicrpg.story.quest.action.AbstractAction;
import com.songoda.epicrpg.story.quest.action.ActiveAction;
import io.lumine.xikage.mythicmobs.utils.events.extra.ArmorEquipEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class EquipItem extends AbstractAction {

    public EquipItem(EpicRPG plugin) {
        super(plugin);
    }

    @Override
    public String getType() {
        return "EQUIP_ITEM";
    }

    @Override
    public List<String> getDescription(ActionDataStore actionDataStore) {
        EquipItemDataStore dataStore = (EquipItemDataStore) actionDataStore;
        return dataStore.getItemStack() == null ? Collections.singletonList("None") : Collections.singletonList(TextUtils.formatText("&fItem: &6" + dataStore.getItemStack().getType().name()));
    }

    @Override
    public void onEquip(ArmorEquipEvent event, ActiveAction activeAction) {
        EquipItemDataStore dataStore = (EquipItemDataStore) activeAction.getActionDataStore();

        ItemStack item = event.getNewArmorPiece();

        if (!item.isSimilar(dataStore.getItemStack())) return;

        performAction(activeAction, item.getAmount(), event.getPlayer());

    }

    @Override
    public void onDrop(PlayerDropItemEvent event, ActiveAction activeAction) {
        Player player = event.getPlayer();
        EquipItemDataStore dataStore = (EquipItemDataStore) activeAction.getActionDataStore();

        if (!dataStore.isBeingSetup(player)) return;
        dataStore.setItemStack(event.getItemDrop().getItemStack());
        dataStore.finishSetup(plugin, player, activeAction);
    }

    @Override
    public ActiveAction setup(Player player, Objective objective) {
        player.sendMessage("Drop the item you would like assigned to this action.");
        EquipItemDataStore dataStore = new EquipItemDataStore(objective);
        dataStore.startSetup(player.getUniqueId());

        // Do setup here.
        return new ActiveAction(this, dataStore);
    }

    public class EquipItemDataStore extends ActionDataStore {

        private ItemStack itemStack;

        public EquipItemDataStore(Objective objective) {
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
