package com.craftaro.epicrpg.dialog;

import com.craftaro.epicrpg.story.contender.StoryContender;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Dialog {
    private int citizenId;
    private final List<Speech> messages = new LinkedList<>();

    public Dialog(int citizenId) {
        this.citizenId = citizenId;
    }

    public void sendMessages(Player player, StoryContender contender) {
        Speech speech = null;
        List<Speech> speeches = new ArrayList<>(this.messages);
        Collections.reverse(speeches);
        for (Speech s : speeches) {
            if (s.isDefaultDialog()
                    || !s.getQuestPrerequisites().isEmpty()
                    && contender.getCompletedQuests().containsAll(s.getQuestPrerequisites())) {
                speech = s;
                break;
            }
        }
        if (speech == null) {
            return;
        }
        speech.sendMessages(player, getCitizen());
    }

    public int getCitizenId() {
        return this.citizenId;
    }

    public NPC getCitizen() {
        return CitizensAPI.getNPCRegistry().getById(this.citizenId);
    }

    public void setCitizenId(int citizenId) {
        this.citizenId = citizenId;
    }

    public List<Speech> getMessages() {
        return Collections.unmodifiableList(this.messages);
    }

    public void addMessage(Speech messages) {
        this.messages.add(messages);
    }

    public void removeSpeech(Speech messages) {
        this.messages.remove(messages);
    }
}
