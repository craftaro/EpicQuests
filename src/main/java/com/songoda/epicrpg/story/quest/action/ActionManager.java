package com.songoda.epicrpg.story.quest.action;

import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.story.quest.Objective;
import com.songoda.epicrpg.story.quest.action.actions.EnterRegion;
import com.songoda.epicrpg.story.quest.action.actions.KillEntityType;
import com.songoda.epicrpg.story.quest.action.actions.PickupItem;
import com.songoda.epicrpg.story.quest.action.actions.RightClickCitizenAction;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import java.util.*;
import java.util.stream.Collectors;

public class ActionManager {

    private final Map<String, Action> registeredActions = new HashMap<>();
    private final Set<ActiveAction> activeActions = new HashSet<>();

    public ActionManager(EpicRPG plugin) {
        PluginManager pluginManager = Bukkit.getPluginManager();

        if (pluginManager.isPluginEnabled("Citizens"))
            registeredActions.put("RIGHT_CLICK_CITIZEN", new RightClickCitizenAction(plugin));

        registeredActions.put("ENTER_REGION", new EnterRegion(plugin));
        registeredActions.put("PICKUP_ITEM", new PickupItem(plugin));
        registeredActions.put("KILL_ENTITY_TYPE", new KillEntityType(plugin));
    }

    public Action getAction(String key) {
        return registeredActions.get(key);
    }

    public List<Action> getActions() {
        return new ArrayList<>(registeredActions.values());
    }

    public Set<ActiveAction> getActiveActions() {
        return Collections.unmodifiableSet(new HashSet<>(activeActions));
    }

    public List<ActiveAction> getActiveActionsByObjective(Objective objective) {
        return activeActions.stream().filter(a -> a.getObjective() == objective).collect(Collectors.toList());
    }

    public ActiveAction addActiveAction(ActiveAction activeAction) {
        activeActions.add(activeAction);
        return activeAction;
    }

    public void removeActiveAction(ActiveAction action) {
        activeActions.remove(action);
    }

}
