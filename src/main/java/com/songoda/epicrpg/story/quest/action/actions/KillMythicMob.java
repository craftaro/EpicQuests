package com.songoda.epicrpg.story.quest.action.actions;

import com.songoda.core.utils.TextUtils;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.data.ActionDataStore;
import com.songoda.epicrpg.gui.GuiObjective;
import com.songoda.epicrpg.story.quest.Objective;
import com.songoda.epicrpg.story.quest.action.AbstractAction;
import com.songoda.epicrpg.story.quest.action.ActiveAction;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.Collections;
import java.util.List;

public class KillMythicMob extends AbstractAction {

    public KillMythicMob(EpicRPG plugin) {
        super(plugin);
    }

    @Override
    public String getType() {
        return "KILL_MYTHIC_MOB";
    }

    @Override
    public List<String> getDescription(ActionDataStore actionDataStore) {
        KillMythicMobDataStore dataStore = (KillMythicMobDataStore) actionDataStore;

        MythicMob mob = MythicMobs.inst().getMobManager().getMobTypes().stream()
                .filter(type -> type.getInternalName().equals(dataStore.mobType)).findFirst().orElse(null);
        if (mob != null)
            return Collections.singletonList(TextUtils.formatText("&fMythic Mob Name: &6" + mob.getDisplayName()));
        else
            return Collections.singletonList(TextUtils.formatText("&cThe attached mob could not be found!"));
    }


    @Override
    public void onInteractWithEntity(PlayerInteractAtEntityEvent event, ActiveAction activeAction) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        KillMythicMobDataStore dataStore = (KillMythicMobDataStore) activeAction.getActionDataStore();

        MobManager mobManager = MythicMobs.inst().getMobManager();

        if (!mobManager.isActiveMob(event.getRightClicked().getUniqueId())
                || !dataStore.isBeingSetup(event.getPlayer()))
            return;

        dataStore.setMobType(mobManager.getActiveMob(entity.getUniqueId()).get().getMobType());
        dataStore.finishSetup(plugin, player, activeAction);
    }

    @Override
    public void onEntityKill(EntityDeathEvent event, ActiveAction activeAction) {
        KillMythicMobDataStore dataStore = (KillMythicMobDataStore) activeAction.getActionDataStore();
        Player player = event.getEntity().getKiller();
        if (player == null) return;

        MobManager mobManager = MythicMobs.inst().getMobManager();

        if (!mobManager.isActiveMob(event.getEntity().getUniqueId()))
            return;

        ActiveMob mob = mobManager.getActiveMob(event.getEntity().getUniqueId()).orElse(null);

        if (mob == null)
            return;

        if (mob.getMobType().equals(dataStore.mobType))
            performAction(activeAction, 1, event.getEntity().getKiller());
    }

    @Override
    public ActiveAction setup(Player player, Objective objective) {
        player.sendMessage("Right click a mythic mob to assign this action.");
        KillMythicMobDataStore dataStore = new KillMythicMobDataStore(objective);
        dataStore.startSetup(player);
        return new ActiveAction(this, dataStore);
    }

    public class KillMythicMobDataStore extends ActionDataStore {

        private String mobType;

        public KillMythicMobDataStore(Objective objective) {
            super(objective);
        }

        public String getMobType() {
            return mobType;
        }

        public void setMobType(String mobType) {
            this.mobType = mobType;
        }
    }
}
