package com.songoda.epicrpg.Region;

import com.songoda.epicrpg.story.quest.Quest;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SelectionManager {

    private final Map<UUID, ActiveSelection> activeSelections = new HashMap<>();
    private final Map<UUID, ActiveView> activeView = new HashMap<>();

    public ActiveSelection getActiveSelection(Player player) {
        return activeSelections.get(player.getUniqueId());
    }

    public void removeActiveSelection(Player player) {
        activeSelections.remove(player.getUniqueId());
    }

    public void addActiveSelection(Player player, Quest quest) {
        activeSelections.put(player.getUniqueId(), new ActiveSelection(quest));
    }

    public ActiveView getActiveView(Player player) {
        return activeView.get(player.getUniqueId());
    }

    public void removeActiveView(Player player) {
        activeView.remove(player.getUniqueId());
    }

    public ActiveView addActiveView(Player player, Region region) {
        ActiveView view = new ActiveView(region);
        activeView.put(player.getUniqueId(), view);
        return view;
    }

}
