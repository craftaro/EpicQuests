package com.songoda.epicrpg.dialog;

import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.story.quest.Objective;
import com.songoda.epicrpg.story.quest.Quest;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Speech {

    private final UUID uniqueId = UUID.randomUUID();

    private transient Dialog dialog;

    private final List<UUID> questPrerequisites = new LinkedList<>();
    private boolean defaultDialog = false;

    private final List<String> messages = new LinkedList<>();

    public Speech(Dialog dialog) {
        this.dialog = dialog;
    }

    public void sendMessages(Player player, NPC npc) {
        int timeout = 0;
        for (String message : getMessages()) {
            Bukkit.getScheduler().runTaskLater(EpicRPG.getInstance(), () -> {
                String m = message.replace("@p", player.getName());
                if (player.isOnline())
                    EpicRPG.getInstance().getLocale().newMessage("[NPC] " + npc.getName() + ": " + m).sendMessage(player);
            }, timeout);
            timeout += 20;
        }
    }

    public Dialog getDialog() {
        return dialog;
    }

    public List<String> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    public void removeMessage(String message) {
        messages.remove(message);
    }

    public void moveMessageToEnd(String message) {
        messages.remove(message);
        messages.add(message);
    }

    public boolean isDefaultDialog() {
        return defaultDialog;
    }

    public void setDefaultDialog(boolean defaultDialog) {
        this.defaultDialog = defaultDialog;
    }

    public void addQuestPrerequisite(Quest quest) {
        addQuestPrerequisite(quest.getUniqueId());
    }

    public void addQuestPrerequisite(UUID quest) {
        questPrerequisites.add(quest);
    }

    public void removeQuestPrerequisite(Quest quest) {
        removeQuestPrerequisite(quest.getUniqueId());
    }

    public void removeQuestPrerequisite(UUID quest) {
        questPrerequisites.remove(quest);
    }

    public void clearQuestPrerequisites() {
        questPrerequisites.clear();
    }

    public List<UUID> getQuestPrerequisites() {
        return Collections.unmodifiableList(questPrerequisites);
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }
}
