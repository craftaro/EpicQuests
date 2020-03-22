package com.songoda.epicrpg.gui;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.gui.Gui;
import com.songoda.core.gui.GuiUtils;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.dialog.Speech;
import com.songoda.epicrpg.story.StoryManager;
import com.songoda.epicrpg.story.quest.Quest;
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
        if (inventory != null)
            inventory.clear();
        setActionForRange(0, 53, null);


        setButton(0, 8, GuiUtils.createButtonItem(CompatibleMaterial.OAK_DOOR, "Back"),
                (event) -> {
                    if (quest == null)
                        guiManager.showGUI(player, new GuiQuestPrereqs(plugin, player, quest));
                    else
                        guiManager.showGUI(player, new GuiQuestPrereqs(plugin, player, speech));
                });


        List<Quest> quests = storyManager.getQuests();
        quests.remove(quest);
        for (int i = 0; i < quests.size(); i++) {
            Quest quest = quests.get(i);
            setButton(i + 9, GuiUtils.createButtonItem(CompatibleMaterial.PAPER, quest.getName()),
                    (event) -> {
                        if (this.quest == null) {
                            this.speech.addQuestPrerequisite(quest);
                            guiManager.showGUI(player, new GuiQuestPrereqs(plugin, player, this.speech));
                        } else {
                            this.quest.addQuestPrerequisite(quest);
                            guiManager.showGUI(player, new GuiQuestPrereqs(plugin, player, this.quest));
                        }
                    });
        }
    }
}