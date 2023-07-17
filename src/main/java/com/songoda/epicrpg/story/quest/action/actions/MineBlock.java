package com.songoda.epicrpg.story.quest.action.actions;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.utils.TextUtils;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.data.ActionDataStore;
import com.songoda.epicrpg.story.quest.Objective;
import com.songoda.epicrpg.story.quest.action.AbstractAction;
import com.songoda.epicrpg.story.quest.action.ActiveAction;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Collections;
import java.util.List;

public class MineBlock extends AbstractAction {
    public MineBlock(EpicRPG plugin) {
        super(plugin);
    }

    @Override
    public String getType() {
        return "MINE_BLOCK";
    }

    @Override
    public List<String> getDescription(ActionDataStore actionDataStore) {
        RightClickBlockDataStore dataStore = (RightClickBlockDataStore) actionDataStore;
        CompatibleMaterial material = dataStore.getMaterial();
        return Collections.singletonList(TextUtils.formatText("&fBlock: &6"
                + (dataStore.getMaterial() == null ? "Undefined" : material.name())));
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event, ActiveAction action) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        RightClickBlockDataStore dataStore = (RightClickBlockDataStore) action.getActionDataStore();

        if (dataStore.isBeingSetup(event.getPlayer())) {
            dataStore.setMaterial(CompatibleMaterial.getMaterial(block.getType()));
            dataStore.finishSetup(this.plugin, player, action);
            return;
        }

        if (CompatibleMaterial.getMaterial(block.getType()) == dataStore.getMaterial()) {
            performAction(action, 1, player);
        }
    }

    @Override
    public ActiveAction setup(Player player, Objective objective) {
        player.sendMessage("Break a block to assign this action.");
        RightClickBlockDataStore dataStore = new RightClickBlockDataStore(objective);
        dataStore.startSetup(player);
        return new ActiveAction(this, dataStore);
    }

    public static class RightClickBlockDataStore extends ActionDataStore {
        private CompatibleMaterial material;

        public RightClickBlockDataStore(Objective objective) {
            super(objective);
        }

        public CompatibleMaterial getMaterial() {
            return this.material;
        }

        public void setMaterial(CompatibleMaterial material) {
            this.material = material;
        }
    }
}
