package com.craftaro.epicrpg.dialog;

import com.craftaro.epicrpg.story.quest.Quest;
import com.craftaro.epicrpg.EpicRPG;
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
    private final transient EpicRPG plugin;

    private final List<UUID> questPrerequisites = new LinkedList<>();
    private boolean defaultDialog = false;

    private final List<String> messages = new LinkedList<>();

    public Speech(Dialog dialog, EpicRPG plugin) {
        this.dialog = dialog;
        this.plugin = plugin;
    }

    public void sendMessages(Player player, NPC npc) {
        int timeout = 0;
        for (String message : getMessages()) {
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                String msg = message
                        .replace("@p", player.getName())
                        .replace("%player%", player.getName());
                if (player.isOnline()) {
                    this.plugin.getLocale().newMessage("[NPC] " + npc.getName() + ": " + msg).sendMessage(player);
                }
            }, timeout);
            timeout += 20;
        }
    }

    public Dialog getDialog() {
        return this.dialog;
    }

    public List<String> getMessages() {
        return Collections.unmodifiableList(this.messages);
    }

    public void addMessage(String message) {
        this.messages.add(message);
    }

    public void removeMessage(String message) {
        this.messages.remove(message);
    }

    public void moveMessageToEnd(String message) {
        this.messages.remove(message);
        this.messages.add(message);
    }

    public boolean isDefaultDialog() {
        return this.defaultDialog;
    }

    public void setDefaultDialog(boolean defaultDialog) {
        this.defaultDialog = defaultDialog;
    }

    public void addQuestPrerequisite(Quest quest) {
        addQuestPrerequisite(quest.getUniqueId());
    }

    public void addQuestPrerequisite(UUID quest) {
        this.questPrerequisites.add(quest);
    }

    public void removeQuestPrerequisite(Quest quest) {
        removeQuestPrerequisite(quest.getUniqueId());
    }

    public void removeQuestPrerequisite(UUID quest) {
        this.questPrerequisites.remove(quest);
    }

    public void clearQuestPrerequisites() {
        this.questPrerequisites.clear();
    }

    public List<UUID> getQuestPrerequisites() {
        return Collections.unmodifiableList(this.questPrerequisites);
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }
}
