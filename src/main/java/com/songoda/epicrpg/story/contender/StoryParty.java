package com.songoda.epicrpg.story.contender;

import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.story.quest.ActiveQuest;
import com.songoda.epicrpg.story.quest.Quest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StoryParty extends StoryContender {

    private Map<UUID, MemberType> players = new HashMap<>();

    public StoryParty(UUID uniqueId, StoryPlayer player) {
        super(uniqueId);
        players.put(player.getUniqueId(), MemberType.LEADER);
    }

    public StoryParty(StoryPlayer player) {
        super(UUID.randomUUID());
        players.put(player.getUniqueId(), MemberType.LEADER);
    }

    public void addPlayer(StoryPlayer player) {
        players.put(player.getUniqueId(), MemberType.MEMBER);
        player.setParty(this);
    }

    public void removePlayer(StoryPlayer player) {
        players.remove(player.getUniqueId());
    }

    public boolean isLeader(StoryPlayer storyPlayer) {
        return players.get(storyPlayer.getUniqueId()) == MemberType.LEADER;
    }

    public boolean isMember(StoryPlayer storyPlayer) {
        return players.containsKey(storyPlayer.getUniqueId());
    }

    public void disband() {
        for (UUID uniqueId : players.keySet()) {
            StoryPlayer player = EpicRPG.getInstance().getContendentManager().getPlayer(uniqueId);
            player.setParty(null);
        }
        players.clear();
    }

    @Override
    public void completeQuest(Quest quest) {
        ActiveQuest active = getActiveQuest(quest);
        for (UUID uniqueId : players.keySet()) {
            StoryPlayer player = EpicRPG.getInstance().getContendentManager().getPlayer(uniqueId);
            player.addCompletedQuests(getCompletedQuests());
        }
        activeQuests.remove(active);
    }

    public void swapQuest(Quest quest) {
        activeQuests.clear();
        addActiveQuest(quest);
    }

    public void swapQuest(ActiveQuest quest) {
        activeQuests.clear();
        addActiveQuest(quest);
    }
}
