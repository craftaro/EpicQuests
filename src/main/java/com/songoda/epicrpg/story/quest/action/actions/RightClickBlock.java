package com.songoda.epicrpg.story.quest.action.actions;

import com.songoda.core.utils.LocationUtils;
import com.songoda.core.utils.TextUtils;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.data.ActionDataStore;
import com.songoda.epicrpg.gui.GuiObjective;
import com.songoda.epicrpg.story.quest.Objective;
import com.songoda.epicrpg.story.quest.action.AbstractAction;
import com.songoda.epicrpg.story.quest.action.ActiveAction;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Collections;
import java.util.List;

public class RightClickBlock extends AbstractAction {

    public RightClickBlock(EpicRPG plugin) {
        super(plugin);
    }

    @Override
    public String getType() {
        return "RIGHT_CLICK_BLOCK";
    }

    @Override
    public List<String> getDescription(ActionDataStore actionDataStore) {
        RightClickBlockDataStore dataStore = (RightClickBlockDataStore) actionDataStore;
        Location location = dataStore.getLocation();
        return Collections.singletonList(TextUtils.formatText("&fBlock: &6"
                + (dataStore.getLocation() == null ? "Undefined" : location.getX() + ":" + location.getY() + ":" + location.getZ())));
    }

    @Override
    public void onInteract(PlayerInteractEvent event, ActiveAction activeAction) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if (block == null)
            return;

        RightClickBlockDataStore dataStore = (RightClickBlockDataStore) activeAction.getActionDataStore();

        if (dataStore.isBeingSetup(event.getPlayer())) {
            dataStore.setLocation(block.getLocation());
            dataStore.finishSetup(plugin, player, activeAction);
            return;
        }

        if (LocationUtils.isLocationMatching(block.getLocation(), dataStore.location))
            performAction(activeAction, 1, player);
    }

    @Override
    public ActiveAction setup(Player player, Objective objective) {
        player.sendMessage("Right click a block to assign this action.");
        RightClickBlockDataStore dataStore = new RightClickBlockDataStore(objective);
        dataStore.startSetup(player);
        return new ActiveAction(this, dataStore);
    }

    public class RightClickBlockDataStore extends ActionDataStore {

        private Location location;

        public RightClickBlockDataStore(Objective objective) {
            super(objective);
        }

        public Location getLocation() {
            if (location == null) return null;
            return location.clone();
        }

        public void setLocation(Location location) {
            this.location = location;
        }
    }
}
