package com.songoda.epicrpg.story.quest.action;

import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.story.quest.Objective;
import com.songoda.epicrpg.story.quest.action.actions.DropItem;
import com.songoda.epicrpg.story.quest.action.actions.EnterRegion;
import com.songoda.epicrpg.story.quest.action.actions.EquipItem;
import com.songoda.epicrpg.story.quest.action.actions.KillEntityType;
import com.songoda.epicrpg.story.quest.action.actions.KillMythicMob;
import com.songoda.epicrpg.story.quest.action.actions.MineBlock;
import com.songoda.epicrpg.story.quest.action.actions.PickupItem;
import com.songoda.epicrpg.story.quest.action.actions.RightClickBlock;
import com.songoda.epicrpg.story.quest.action.actions.RightClickCitizen;
import com.songoda.epicrpg.story.quest.action.actions.RightClickEntity;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ActionManager {
    private final Map<String, Action> registeredActions = new HashMap<>();
    private final Set<ActiveAction> activeActions = new HashSet<>();

    public ActionManager(EpicRPG plugin) {
        registerActions(new RightClickCitizen(plugin),
                new EnterRegion(plugin),
                new PickupItem(plugin),
                new KillEntityType(plugin),
                new RightClickBlock(plugin),
                new RightClickEntity(plugin),
                new MineBlock(plugin),
                new DropItem(plugin),
                new EquipItem(plugin));

        if (Bukkit.getPluginManager().isPluginEnabled("MythicMobs")) {
            registerAction(new KillMythicMob(plugin));
        }
    }

    public Action getAction(String key) {
        return this.registeredActions.get(key);
    }

    public List<Action> getActions() {
        return new ArrayList<>(this.registeredActions.values());
    }

    public Set<ActiveAction> getActiveActions() {
        return Collections.unmodifiableSet(new HashSet<>(this.activeActions));
    }

    public List<ActiveAction> getActiveActionsByObjective(Objective objective) {
        return this.activeActions.stream().filter(a -> a.getObjective() == objective).collect(Collectors.toList());
    }

    public void registerAction(AbstractAction action) {
        this.registeredActions.put(action.getType(), action);
    }

    public void registerActions(AbstractAction... actions) {
        for (AbstractAction action : actions) {
            registerAction(action);
        }
    }

    public ActiveAction addActiveAction(ActiveAction activeAction) {
        this.activeActions.add(activeAction);
        return activeAction;
    }

    public void removeActiveAction(ActiveAction action) {
        this.activeActions.remove(action);
    }
}
