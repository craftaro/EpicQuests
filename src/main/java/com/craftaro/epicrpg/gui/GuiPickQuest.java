package com.craftaro.epicrpg.gui;

import com.craftaro.core.gui.Gui;
import com.craftaro.core.gui.GuiUtils;
import com.craftaro.third_party.com.cryptomorin.xseries.XMaterial;
import com.craftaro.epicrpg.EpicRPG;
import com.craftaro.epicrpg.dialog.Speech;
import com.craftaro.epicrpg.story.StoryManager;
import com.craftaro.epicrpg.story.quest.Quest;
import org.bukkit.entity.Player;

import java.util.List;

public class GuiPickQuest extends Gui {
    private EpicRPG plugin;
    private Player player;
    private Quest quest;
    private Speech speech;
    private StoryManager storyManager;

    public GuiPickQuest(EpicRPG plugin, Player player, Quest quest) {
        this.quest = quest;
        init(plugin, player);
    }

    public GuiPickQuest(EpicRPG plugin, Player player, Speech speech) {
        this.speech = speech;
        init(plugin, player);
    }

    public void init(EpicRPG plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.storyManager = plugin.getStoryManager();
        setRows(6);
        setDefaultItem(null);

        setTitle("Pick a quest.");

        show();
    }

    public void show() {
        reset();

        setButton(0, 8, GuiUtils.createButtonItem(XMaterial.OAK_DOOR, "Back"),
                (event) -> {
                    if (this.quest != null) {
                        this.guiManager.showGUI(this.player, new GuiQuestPrereqs(this.plugin, this.player, this.quest));
                    } else {
                        this.guiManager.showGUI(this.player, new GuiQuestPrereqs(this.plugin, this.player, this.speech));
                    }
                });

        List<Quest> quests = this.storyManager.getQuests();
        quests.remove(this.quest);
        for (int i = 0; i < quests.size(); i++) {
            Quest quest = quests.get(i);
            setButton(i + 9, GuiUtils.createButtonItem(XMaterial.PAPER, quest.getName()),
                    (event) -> {
                        if (this.quest == null) {
                            this.speech.addQuestPrerequisite(quest);
                            this.guiManager.showGUI(this.player, new GuiQuestPrereqs(this.plugin, this.player, this.speech));
                        } else {
                            this.quest.addQuestPrerequisite(quest);
                            this.guiManager.showGUI(this.player, new GuiQuestPrereqs(this.plugin, this.player, this.quest));
                        }
                    });
        }
    }
}
