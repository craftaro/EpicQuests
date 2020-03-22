package com.songoda.epicrpg.story.player;

import com.songoda.epicrpg.story.Story;
import com.songoda.epicrpg.story.StoryManager;
import com.songoda.epicrpg.story.quest.Quest;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerManager {

    private final StoryManager storyManager;

    private final Map<UUID, StoryPlayer> registeredPlayers = new HashMap<>();

    public PlayerManager(StoryManager storyManager) {
        this.storyManager = storyManager;
    }

    public void addPlayer(StoryPlayer player) {
        registeredPlayers.put(player.getUniqueId(), player);
    }

    public StoryPlayer getPlayer(OfflinePlayer player) {
        return registeredPlayers.computeIfAbsent(player.getUniqueId(), k -> new StoryPlayer(player.getUniqueId()));
    }

    public List<StoryPlayer> getPlayers() {
        return new ArrayList<>(registeredPlayers.values());
    }

    public void discoverQuests(Player player) {
        StoryPlayer storyPlayer = getPlayer(player);
        for (Story story : storyManager.getStories()) {
            for (Quest quest : story.getEnabledQuests()) {
                if (storyPlayer.getCompletedQuests().stream().anyMatch(q -> quest.getUniqueId().equals(q))
                        || storyPlayer.getActiveQuests().stream().anyMatch(q -> q.getActiveQuest().equals(quest.getUniqueId())))
                    continue;
                if (storyPlayer.getCompletedQuests().containsAll(quest.getQuestPrerequisites())) {
                    storyPlayer.addActiveQuest(quest);
                }
            }
        }
    }
}
