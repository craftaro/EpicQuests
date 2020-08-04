package com.songoda.epicrpg.story.quest.action.actions;

import com.songoda.core.utils.TextUtils;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.data.ActionDataStore;
import com.songoda.epicrpg.gui.GuiObjective;
import com.songoda.epicrpg.story.quest.Objective;
import com.songoda.epicrpg.story.quest.action.AbstractAction;
import com.songoda.epicrpg.story.quest.action.ActiveAction;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class RightClickEntity extends AbstractAction {

    public RightClickEntity(EpicRPG plugin) {
        super(plugin);
    }

    @Override
    public String getType() {
        return "RIGHT_CLICK_ENTITY";
    }

    @Override
    public List<String> getDescription(ActionDataStore actionDataStore) {
        RightClickEntityDataStore dataStore = (RightClickEntityDataStore) actionDataStore;
        if (dataStore.getEntity() != null && Bukkit.getEntity(dataStore.entity) != null)
            return Collections.singletonList(TextUtils.formatText("&fEntity UUID: &6" + dataStore.getEntity()));
        else
            return Collections.singletonList(TextUtils.formatText("&cThe attached entity could not be found!"));
    }

    @Override
    public void onInteractWithEntity(PlayerInteractAtEntityEvent event, ActiveAction activeAction) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        RightClickEntityDataStore dataStore = (RightClickEntityDataStore) activeAction.getActionDataStore();

        if (dataStore.isBeingSetup(event.getPlayer())) {
            dataStore.setEntity(entity.getUniqueId());
            dataStore.finishSetup(plugin, player, activeAction);
        }

        if (entity.getUniqueId().equals(dataStore.getEntity()))
            performAction(activeAction, 1, player);
    }

    @Override
    public ActiveAction setup(Player player, Objective objective) {
        player.sendMessage("Right click an entity to assign this action.");
        RightClickEntityDataStore dataStore = new RightClickEntityDataStore(objective);
        dataStore.startSetup(player);
        return new ActiveAction(this, dataStore);
    }

    public class RightClickEntityDataStore extends ActionDataStore {

        private UUID entity;

        public RightClickEntityDataStore(Objective objective) {
            super(objective);
        }

        public UUID getEntity() {
            return entity;
        }

        public void setEntity(UUID entity) {
            this.entity = entity;
        }
    }
}
