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

        setTitle((quest == null ? "Dialog" : quest.getName()) + " - prereqs");

        show();
    }

    public void show() {
        reset();

        setButton(0, 0, GuiUtils.createButtonItem(CompatibleMaterial.GREEN_DYE, "Add Quest"),
                (event) -> {
                    if (quest == null)
                        guiManager.showGUI(player, new GuiPickQuest(plugin, player, speech));
                    else
                        guiManager.showGUI(player, new GuiPickQuest(plugin, player, quest));
                });

        setButton(0, 8, GuiUtils.createButtonItem(CompatibleMaterial.OAK_DOOR, "Back"),
                (event) -> {
                    if (quest == null)
                        guiManager.showGUI(player, new GuiSpeech(plugin, player, speech));
                    else
                        guiManager.showGUI(player, new GuiQuest(plugin, player, quest));
                });


        List<UUID> prereqs = quest == null ? speech.getQuestPrerequisites() : quest.getQuestPrerequisites();
        for (int i = 0; i < prereqs.size(); i++) {
            UUID uuid = prereqs.get(i);
            Optional<Quest> optional = storyManager.getQuests().stream().filter(q -> q.getUniqueId().equals(uuid)).findFirst();
            if (!optional.isPresent()) continue;
            Quest quest = optional.get();

            setButton(i + 9, GuiUtils.createButtonItem(CompatibleMaterial.PAPER, quest.getName()),
                    (event) -> {
                        if (this.quest == null)
                            speech.removeQuestPrerequisite(uuid);
                        else
                            this.quest.removeQuestPrerequisite(uuid);
                        show();
                    });
        }
    }
}