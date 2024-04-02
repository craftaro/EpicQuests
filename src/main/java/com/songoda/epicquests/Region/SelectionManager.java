package com.songoda.epicquests.Region;

import com.songoda.epicquests.story.quest.Quest;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SelectionManager {
    private final Map<UUID, ActiveSelection> activeSelections = new HashMap<>();
    private final Map<UUID, ActiveView> activeView = new HashMap<>();

    public ActiveSelection getActiveSelection(Player player) {
        return this.activeSelections.get(player.getUniqueId());
    }

    public void removeActiveSelection(Player player) {
        this.activeSelections.remove(player.getUniqueId());
    }

    public void addActiveSelection(Player player, Quest quest) {
        this.activeSelections.put(player.getUniqueId(), new ActiveSelection(quest));
    }

    public ActiveView getActiveView(Player player) {
        return this.activeView.get(player.getUniqueId());
    }

    public void removeActiveView(Player player) {
        this.activeView.remove(player.getUniqueId());
    }

    public ActiveView addActiveView(Player player, Region region) {
        ActiveView view = new ActiveView(region);
        this.activeView.put(player.getUniqueId(), view);
        return view;
    }

}
