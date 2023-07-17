package com.songoda.epicrpg.gui;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.gui.Gui;
import com.songoda.core.gui.GuiUtils;
import com.songoda.core.input.ChatPrompt;
import com.songoda.core.utils.TextUtils;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.story.Story;
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
        this.story = story;
        setRows(6);
        setDefaultItem(null);

        setTitle(story.getName());

        show();
    }

    public void show() {
        reset();

        setButton(0, 0, GuiUtils.createButtonItem(CompatibleMaterial.GREEN_DYE, "Rename Story"),
                (event) -> ChatPrompt.showPrompt(this.plugin, this.player,
                                "Enter a story name.",
                                response -> this.story.setName(response.getMessage()))
                        .setOnClose(() -> this.guiManager.showGUI(this.player, new GuiStory(this.plugin, this.player, this.story))));

        setButton(0, 1, GuiUtils.createButtonItem(CompatibleMaterial.BLUE_DYE, "Create Quest"),
                (event) -> {
                    this.story.addQuest(new Quest(this.story));
                    show();
                });

        setButton(0, 2, GuiUtils.createButtonItem(CompatibleMaterial.RED_DYE, this.story.isActive() ? "Active" : "Inactive"),
                (event) -> {
                    this.story.setActive(!this.story.isActive());
                    show();
                });

        setButton(0, 8, GuiUtils.createButtonItem(CompatibleMaterial.OAK_DOOR, "Back"),
                (event) -> this.guiManager.showGUI(this.player, new GuiStories(this.plugin, this.player)));


        List<Quest> quests = this.story.getQuests();
        for (int i = 0; i < quests.size(); i++) {
            Quest quest = quests.get(i);
            setButton(i + 9, GuiUtils.createButtonItem(CompatibleMaterial.PAPER, quest.getName(),
                            "",
                            TextUtils.formatText("&fLeft-Click: &6to view"),
                            TextUtils.formatText("&fRight-Click: &6to delete")),
                    (event) -> {
                        if (event.clickType == ClickType.LEFT) {
                            this.guiManager.showGUI(this.player, new GuiQuest(this.plugin, this.player, quest));
                        } else if (event.clickType == ClickType.RIGHT) {
                            ChatPrompt.showPrompt(this.plugin, this.player,
                                            "Type in 'DELETE' to confirm.",
                                            response -> {
                                                if (response.getMessage().trim().equalsIgnoreCase("delete")) {
                                                    this.story.removeQuest(quest);
                                                }
                                            })
                                    .setOnClose(() -> this.guiManager.showGUI(this.player, new GuiStory(this.plugin, this.player, this.story)));
                        }
                    });
        }
    }
}
