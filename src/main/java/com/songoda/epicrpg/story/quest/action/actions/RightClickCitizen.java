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
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.Collections;
import java.util.List;

public class RightClickCitizen extends AbstractAction {

    public RightClickCitizen(EpicRPG plugin) {
        super(plugin);
    }

    @Override
    public String getType() {
        return "RIGHT_CLICK_CITIZEN";
    }

    @Override
    public List<String> getDescription(ActionDataStore actionDataStore) {
        RightClickCitizenDataStore dataStore = (RightClickCitizenDataStore) actionDataStore;
        NPC npc = CitizensAPI.getNPCRegistry().getById(dataStore.citizenId);
        if (npc != null)
            return Collections.singletonList(TextUtils.formatText("&fCitizen Name: &6" + CitizensAPI.getNPCRegistry().getById(dataStore.citizenId).getName()));
        else
            return Collections.singletonList(TextUtils.formatText("&cThe attached citizen could not be found!"));
    }

    @Override
    public void onInteractWithEntity(PlayerInteractAtEntityEvent event, ActiveAction activeAction) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        RightClickCitizenDataStore dataStore = (RightClickCitizenDataStore) activeAction.getActionDataStore();

        if (!CitizensAPI.getNPCRegistry().isNPC(entity)) return;

        if (dataStore.isBeingSetup(event.getPlayer())) {
            dataStore.setCitizenId(CitizensAPI.getNPCRegistry().getNPC(entity).getId());
            dataStore.finishSetup(plugin, player, activeAction);
            return;
        }

        if (CitizensAPI.getNPCRegistry().getNPC(entity).getId() == dataStore.getCitizenId())
            performAction(activeAction, 1, player);
    }

    @Override
    public ActiveAction setup(Player player, Objective objective) {
        player.sendMessage("Right click a citizen to assign this action.");
        RightClickCitizenDataStore dataStore = new RightClickCitizenDataStore(objective);
        dataStore.startSetup(player);
        return new ActiveAction(this, dataStore);
    }

    public class RightClickCitizenDataStore extends ActionDataStore {

        private int citizenId;

        public RightClickCitizenDataStore(Objective objective) {
            super(objective);
        }

        public int getCitizenId() {
            return citizenId;
        }

        public void setCitizenId(int citizenId) {
            this.citizenId = citizenId;
        }
    }
}
