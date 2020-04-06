package com.songoda.epicrpg.story.quest.action.actions;

import com.songoda.core.input.ChatPrompt;
import com.songoda.core.utils.TextUtils;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.data.ActionDataStore;
import com.songoda.epicrpg.gui.GuiObjective;
import com.songoda.epicrpg.story.quest.Objective;
import com.songoda.epicrpg.story.quest.action.AbstractAction;
import com.songoda.epicrpg.story.quest.action.ActiveAction;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.Collections;
import java.util.List;

public class KillEntityType extends AbstractAction {

    @Override
    public String getType() {
        return "KILL_ENTITY_TYPE";
    }

    @Override
    public List<String> getDescription(ActionDataStore actionDataStore) {
        KillEntityTypeDataStore dataStore = (KillEntityTypeDataStore) actionDataStore;
        return Collections.singletonList(TextUtils.formatText("&fEntity Type: &6"
                + (dataStore.getEntityType() == null ? "Undefined" : dataStore.getEntityType().name())));
    }

    @Override
    public void onInteract(PlayerInteractEvent event, ActiveAction activeAction) {
    }

    @Override
    public boolean onInteractWithEntity(PlayerInteractAtEntityEvent event, ActiveAction activeAction) {
        EpicRPG plugin = EpicRPG.getInstance();
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        KillEntityTypeDataStore dataStore = (KillEntityTypeDataStore) activeAction.getActionDataStore();

        if (dataStore.isBeingSetup(event.getPlayer())) {
            dataStore.setEntityType(entity.getType());
            ChatPrompt.showPrompt(plugin, player,
                    "Enter a required amount.",
                    response -> {
                        activeAction.setAmount(Integer.parseInt(response.getMessage()));
                        plugin.getGuiManager().showGUI(player, new GuiObjective(plugin, player, dataStore.getObjective()));
                    });
            dataStore.finishSetup();
        }
        return true;
    }

    @Override
    public void onPickup(PlayerPickupItemEvent event, ActiveAction activeAction) {
    }

    @Override
    public void onDrop(PlayerDropItemEvent event, ActiveAction activeAction) {
    }

    @Override
    public void onEntityKill(EntityDeathEvent event, ActiveAction activeAction) {
        KillEntityTypeDataStore dataStore = (KillEntityTypeDataStore) activeAction.getActionDataStore();
        Player player = event.getEntity().getKiller();
        if (player == null) return;

        if (dataStore.getEntityType() == event.getEntityType())
            performAction(activeAction, 1, event.getEntity().getKiller());
    }

    @Override
    public ActiveAction setup(Player player, Objective objective) {
        player.sendMessage("Right click the entity type you want to assign to this action.");
        KillEntityTypeDataStore dataStore = new KillEntityTypeDataStore(objective);
        dataStore.startSetup(player);
        return new ActiveAction(this, dataStore);
    }

    public class KillEntityTypeDataStore extends ActionDataStore {

        private EntityType entityType;

        public KillEntityTypeDataStore(Objective objective) {
            super(objective);
        }

        public EntityType getEntityType() {
            return entityType;
        }

        public void setEntityType(EntityType entityType) {
            this.entityType = entityType;
        }
    }
}
