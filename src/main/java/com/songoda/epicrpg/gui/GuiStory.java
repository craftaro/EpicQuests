package com.songoda.epicrpg.gui;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.gui.Gui;
import com.songoda.core.gui.GuiUtils;
import com.songoda.core.input.ChatPrompt;
import com.songoda.core.utils.TextUtils;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.story.Story;
import com.songoda.epicrpg.story.StoryManager;
import com.songoda.epicrpg.story.quest.Quest;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

public class GuiStory extends Gui {

    private final EpicRPG plugin;
    private final Player player;
    private final Story story;

    public GuiStory(EpicRPG plugin, Player player, Story story) {
        this.plugin = plugin;
        this.player = player;
        StoryManager storyManager = plugin.getStoryManager();
        this.story = story;
        setRows(6);
        setDefaultItem(null);

        setTitle(story.getName());

        show();
    }

    public void show() {
        reset();

        setButton(0, 0, GuiUtils.createButtonItem(CompatibleMaterial.GREEN_DYE, "Rename Story"),
                (event) -> {
                    ChatPrompt.showPrompt(plugin, player,
                            "Enter a story name.",
                            response -> story.setName(response.getMessage()))
                            .setOnClose(() -> guiManager.showGUI(player, new GuiStory(plugin, player, story)));
                });

        setButton(0, 1, GuiUtils.createButtonItem(CompatibleMaterial.BLUE_DYE, "Create Quest"),
                (event) -> {
                    story.addQuest(new Quest(story));
                    show();
                });

        setButton(0, 2, GuiUtils.createButtonItem(CompatibleMaterial.RED_DYE, story.isActive() ? "Active" : "Inactive"),
                (event) -> {
                    story.setActive(!story.isActive());
                    show();
                });

        setButton(0, 8, GuiUtils.createButtonItem(CompatibleMaterial.OAK_DOOR, "Back"),
                (event) -> {
                    guiManager.showGUI(player, new GuiStories(plugin, player));
                });


        List<Quest> quests = story.getQuests();
        for (int i = 0; i < quests.size(); i++) {
            Quest quest = quests.get(i);
            setButton(i + 9, GuiUtils.createButtonItem(CompatibleMaterial.PAPER, quest.getName(),
                    "",
                    TextUtils.formatText("&fLeft-Click: &6to view"),
                    TextUtils.formatText("&fRight-Click: &6to delete")),
                    (event) -> {
                        if (event.clickType == ClickType.LEFT)
                            guiManager.showGUI(player, new GuiQuest(plugin, player, quest));
                        else if (event.clickType == ClickType.RIGHT)
                            ChatPrompt.showPrompt(plugin, player,
                                    "Type in 'DELETE' to confirm.",
                                    response -> {
                                        if (response.getMessage().trim().equalsIgnoreCase("delete"))
                                            story.removeQuest(quest);
                                    })
                                    .setOnClose(() -> guiManager.showGUI(player, new GuiStory(plugin, player, story)));
                    });
        }
    }
}