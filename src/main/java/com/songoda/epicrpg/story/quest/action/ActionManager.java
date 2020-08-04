package com.songoda.epicrpg.story.quest.action;

import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.story.quest.Objective;
import com.songoda.epicrpg.story.quest.action.actions.*;
import org.bukkit.Bukkit;

import java.util.*;
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
                new MineBlock(plugin));

        if (Bukkit.getPluginManager().isPluginEnabled("MythicMobs"))
            registerAction(new KillMythicMob(plugin));
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

    public void registerAction(AbstractAction action) {
        registeredActions.put(action.getType(), action);
    }

    public void registerActions(AbstractAction... actions) {
        for (AbstractAction action : actions)
            registerAction(action);
    }

    public ActiveAction addActiveAction(ActiveAction activeAction) {
        activeActions.add(activeAction);
        return activeAction;
    }

    public void removeActiveAction(ActiveAction action) {
        activeActions.remove(action);
    }

}
