package com.songoda.epicrpg.gui;

import com.craftaro.core.gui.Gui;
import com.craftaro.core.gui.GuiUtils;
import com.craftaro.core.third_party.com.cryptomorin.xseries.XMaterial;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.dialog.Speech;
import com.songoda.epicrpg.story.StoryManager;
import com.songoda.epicrpg.story.quest.Quest;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GuiQuestPrereqs extends Gui {
    private EpicRPG plugin;
    private Player player;
    private StoryManager storyManager;
    private Quest quest;
    private Speech speech;

    public GuiQuestPrereqs(EpicRPG plugin, Player player, Quest quest) {
        this.quest = quest;
        System.out.println(quest);
        init(plugin, player);
    }

    public GuiQuestPrereqs(EpicRPG plugin, Player player, Speech speech) {
        this.speech = speech;
        init(plugin, player);
    }

    public void init(EpicRPG plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.storyManager = plugin.getStoryManager();
        setRows(6);
        setDefaultItem(null);

        setTitle((this.quest == null ? "Dialog" : this.quest.getName()) + " - prereqs");

        show();
    }

    public void show() {
        reset();

        setButton(0, 0, GuiUtils.createButtonItem(XMaterial.GREEN_DYE, "Add Quest"),
                (event) -> {
                    if (this.quest == null) {
                        this.guiManager.showGUI(this.player, new GuiPickQuest(this.plugin, this.player, this.speech));
                    } else {
                        this.guiManager.showGUI(this.player, new GuiPickQuest(this.plugin, this.player, this.quest));
                    }
                });

        setButton(0, 8, GuiUtils.createButtonItem(XMaterial.OAK_DOOR, "Back"),
                (event) -> {
                    if (this.quest == null) {
                        this.guiManager.showGUI(this.player, new GuiSpeech(this.plugin, this.player, this.speech));
                    } else {
                        this.guiManager.showGUI(this.player, new GuiQuest(this.plugin, this.player, this.quest));
                    }
                });


        List<UUID> prereqs = this.quest == null ? this.speech.getQuestPrerequisites() : this.quest.getQuestPrerequisites();
        for (int i = 0; i < prereqs.size(); i++) {
            UUID uuid = prereqs.get(i);
            Optional<Quest> optional = this.storyManager.getQuests().stream().filter(q -> q.getUniqueId().equals(uuid)).findFirst();
            if (!optional.isPresent()) {
                continue;
            }
            Quest quest = optional.get();

            setButton(i + 9, GuiUtils.createButtonItem(XMaterial.PAPER, quest.getName()),
                    (event) -> {
                        if (this.quest == null) {
                            this.speech.removeQuestPrerequisite(uuid);
                        } else {
                            this.quest.removeQuestPrerequisite(uuid);
                        }
                        show();
                    });
        }
    }
}
