package com.songoda.epicrpg.story.quest.action.actions;

import com.songoda.core.utils.LocationUtils;
import com.songoda.core.utils.TextUtils;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.data.ActionDataStore;
import com.songoda.epicrpg.gui.GuiObjective;
import com.songoda.epicrpg.story.quest.Objective;
import com.songoda.epicrpg.story.quest.action.AbstractAction;
import com.songoda.epicrpg.story.quest.action.ActiveAction;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Collections;
import java.util.List;

public class EnterRegion extends AbstractAction {

    public EnterRegion(EpicRPG plugin) {
        super(plugin);
    }

    @Override
    public String getType() {
        return "ENTER_REGION";
    }

    @Override
    public List<String> getDescription(ActionDataStore actionDataStore) {
        EnterRegionDataStore dataStore = (EnterRegionDataStore) actionDataStore;
        Location pos1 = dataStore.getPos1();
        Location pos2 = dataStore.getPos2();
        return Collections.singletonList(TextUtils.formatText("&fRegion: &6"
                + (dataStore.getPos2() == null ? "Undefined" : pos1.getX() + ":" + pos1.getY() + ":" + pos1.getZ()
                + ", " + pos2.getX() + ":" + pos2.getY() + ":" + pos2.getZ())));
    }

    @Override
    public void onInteract(PlayerInteractEvent event, ActiveAction activeAction) {
        EnterRegionDataStore dataStore = (EnterRegionDataStore) activeAction.getActionDataStore();
        Player player = event.getPlayer();
        if (dataStore.isBeingSetup(player)) {
            Location location = event.getClickedBlock().getLocation();
            if (dataStore.pos1 == null) {
                dataStore.setPos1(location);
                player.sendMessage("Pos 1 set.");
            } else if (dataStore.pos2 == null) {
                dataStore.setPos2(location);
                player.sendMessage("Setup complete!.");
                dataStore.finishSetup();
                plugin.getGuiManager().showGUI(player, new GuiObjective(plugin, player, dataStore.getObjective()));
            }
        }
    }

    @Override
    public void moveTick(Player player, ActiveAction activeAction) {
        EnterRegionDataStore dataStore = (EnterRegionDataStore) activeAction.getActionDataStore();
        if (dataStore.getPos2() == null)
            return;

        if (LocationUtils.isInArea(player.getLocation(), dataStore.pos1, dataStore.pos2)) {
            performAction(activeAction, 1, player);
        }
    }

    @Override
    public ActiveAction setup(Player player, Objective objective) {
        player.sendMessage("Select both corners of your desired region to complete this setup.");
        EnterRegionDataStore dataStore = new EnterRegionDataStore(objective);
        dataStore.startSetup(player);
        return new ActiveAction(this, dataStore);
    }

    public class EnterRegionDataStore extends ActionDataStore {

        private EntityType entityType;

        private Location pos1;
        private Location pos2;

        public EnterRegionDataStore(Objective objective) {
            super(objective);
        }

        public EntityType getEntityType() {
            return entityType;
        }

        public void setEntityType(EntityType entityType) {
            this.entityType = entityType;
        }

        public Location getPos1() {
            return pos1;
        }

        public void setPos1(Location pos1) {
            this.pos1 = pos1;
        }

        public Location getPos2() {
            return pos2;
        }

        public void setPos2(Location pos2) {
            this.pos2 = pos2;
        }

    }
}
