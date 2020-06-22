package com.songoda.epicrpg.story.contender;

import com.songoda.epicrpg.story.Story;
import com.songoda.epicrpg.story.StoryManager;
import com.songoda.epicrpg.story.quest.Quest;
import org.bukkit.OfflinePlayer;

import java.util.*;

public class ContendentManager {

    private final StoryManager storyManager;

    private final Map<UUID, StoryPlayer> registeredPlayers = new HashMap<>();
    private final Map<UUID, PartyInvite> activeInvitations = new HashMap<>();

    public ContendentManager(StoryManager storyManager) {
        this.storyManager = storyManager;
    }

    public void addPlayer(StoryPlayer player) {
        if (player == null) return;
        registeredPlayers.put(player.getUniqueId(), player);
    }

    public StoryPlayer getPlayer(OfflinePlayer player) {
        return registeredPlayers.computeIfAbsent(player.getUniqueId(), k -> new StoryPlayer(player.getUniqueId()));
    }

    public StoryPlayer getPlayer(UUID uniqueId) {
        return registeredPlayers.computeIfAbsent(uniqueId, k -> new StoryPlayer(uniqueId));
    }

    public StoryContender getContender(OfflinePlayer player) {
        return getContender(player.getUniqueId());
    }

    public StoryContender getContender(UUID uniqueId) {
        StoryPlayer storyPlayer = registeredPlayers.computeIfAbsent(uniqueId, k -> new StoryPlayer(uniqueId));
        StoryParty storyParty = storyPlayer.getParty();
        return storyParty == null ? storyPlayer : storyParty;
    }

    public List<StoryPlayer> getPlayers() {
        return new ArrayList<>(registeredPlayers.values());
    }

    public StoryParty createParty(StoryPlayer player) {
        StoryParty storyParty = new StoryParty(player);
        player.setParty(storyParty);
        return storyParty;
    }


    public void discoverQuests(StoryContender contender) {
        for (Story story : storyManager.getStories()) {
            for (Quest quest : story.getEnabledQuests()) {
                if (contender.getCompletedQuests().stream().anyMatch(q -> quest.getUniqueId().equals(q))
                        || contender.getActiveQuests().stream().anyMatch(q -> q.getActiveQuest().equals(quest.getUniqueId())))
                    continue;
                if (contender.getCompletedQuests().containsAll(quest.getQuestPrerequisites())) {
                    contender.addActiveQuest(quest);
                }
            }
        }
    }

    public PartyInvite addInvite(UUID sender, UUID recipient) {
        return activeInvitations.put(recipient, new PartyInvite(sender, recipient));
    }

    public boolean removeInvite(UUID recipient) {
        return activeInvitations.remove(recipient) != null;
    }

    public PartyInvite getInvite(UUID recipient) {
        return activeInvitations.get(recipient);
    }

    public boolean isInvited(StoryPlayer storyPlayer) {
        return activeInvitations.containsKey(storyPlayer.getUniqueId());
    }
}
