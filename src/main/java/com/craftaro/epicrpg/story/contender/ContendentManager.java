package com.craftaro.epicrpg.story.contender;

import com.craftaro.epicrpg.story.quest.Quest;
import com.craftaro.epicrpg.story.Story;
import com.craftaro.epicrpg.story.StoryManager;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ContendentManager {
    private final StoryManager storyManager;

    private final Map<UUID, StoryPlayer> registeredPlayers = new HashMap<>();
    private final Map<UUID, PartyInvite> activeInvitations = new HashMap<>();

    public ContendentManager(StoryManager storyManager) {
        this.storyManager = storyManager;
    }

    public void addPlayer(StoryPlayer player) {
        if (player == null) {
            return;
        }
        this.registeredPlayers.put(player.getUniqueId(), player);
    }

    public StoryPlayer getPlayer(OfflinePlayer player) {
        return this.registeredPlayers.computeIfAbsent(player.getUniqueId(), uuid -> new StoryPlayer(player.getUniqueId()));
    }

    public StoryPlayer getPlayer(UUID uniqueId) {
        return this.registeredPlayers.computeIfAbsent(uniqueId, uuid -> new StoryPlayer(uniqueId));
    }

    public StoryContender getContender(OfflinePlayer player) {
        return getContender(player.getUniqueId());
    }

    public StoryContender getContender(UUID uniqueId) {
        StoryPlayer storyPlayer = this.registeredPlayers.computeIfAbsent(uniqueId, uuid -> new StoryPlayer(uniqueId));
        StoryParty storyParty = storyPlayer.getParty();
        return storyParty == null ? storyPlayer : storyParty;
    }

    public List<StoryPlayer> getPlayers() {
        return new ArrayList<>(this.registeredPlayers.values());
    }

    public StoryParty createParty(StoryPlayer player) {
        StoryParty storyParty = new StoryParty(player);
        player.setParty(storyParty);
        return storyParty;
    }


    public void discoverQuests(StoryContender contender) {
        for (Story story : this.storyManager.getStories()) {
            for (Quest quest : story.getEnabledQuests()) {
                if (contender.getCompletedQuests().stream().anyMatch(q -> quest.getUniqueId().equals(q))
                        || contender.getActiveQuests().stream().anyMatch(q -> q.getActiveQuest().equals(quest.getUniqueId()))) {
                    continue;
                }
                if (contender.getCompletedQuests().containsAll(quest.getQuestPrerequisites())) {
                    contender.addActiveQuest(quest);
                }
            }
        }
    }

    public PartyInvite addInvite(UUID sender, UUID recipient) {
        return this.activeInvitations.put(recipient, new PartyInvite(sender, recipient));
    }

    public boolean removeInvite(UUID recipient) {
        return this.activeInvitations.remove(recipient) != null;
    }

    public PartyInvite getInvite(UUID recipient) {
        return this.activeInvitations.get(recipient);
    }

    public boolean isInvited(StoryPlayer storyPlayer) {
        return this.activeInvitations.containsKey(storyPlayer.getUniqueId());
    }
}
