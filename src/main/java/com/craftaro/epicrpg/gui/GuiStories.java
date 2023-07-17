package com.craftaro.epicrpg.gui;

import com.craftaro.core.gui.Gui;
import com.craftaro.core.gui.GuiUtils;
import com.craftaro.core.input.ChatPrompt;
import com.craftaro.core.third_party.com.cryptomorin.xseries.XMaterial;
import com.craftaro.epicrpg.story.Story;
import com.craftaro.epicrpg.story.StoryManager;
import com.craftaro.epicrpg.utils.TextUtils;
import com.craftaro.epicrpg.EpicRPG;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

public class GuiStories extends Gui {
    private final EpicRPG plugin;
    private final Player player;
    private final StoryManager storyManager;

    public GuiStories(EpicRPG plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.storyManager = plugin.getStoryManager();
        setRows(6);
        setDefaultItem(null);

        setTitle("Stories");

        show();
    }

    public void show() {
        reset();

        setButton(0, 0, GuiUtils.createButtonItem(XMaterial.GREEN_DYE, "Create Story"),
                (event) -> {
                    this.storyManager.addStory(new Story());
                    show();
                });

        setButton(0, 8, GuiUtils.createButtonItem(XMaterial.OAK_DOOR, "Back"),
                (event) -> this.guiManager.showGUI(this.player, new GuiMain(this.plugin, this.player)));

        List<Story> stories = this.storyManager.getStories();
        for (int i = 0; i < stories.size(); i++) {
            Story story = stories.get(i);
            setButton(i + 9, GuiUtils.createButtonItem(XMaterial.PAPER, story.getName(),
                            "",
                            TextUtils.formatText("&fLeft-Click: &6to view"),
                            TextUtils.formatText("&fRight-Click: &6to delete")),
                    (event) -> {
                        if (event.clickType == ClickType.LEFT) {
                            this.guiManager.showGUI(this.player, new GuiStory(this.plugin, this.player, story));
                        } else if (event.clickType == ClickType.RIGHT) {
                            ChatPrompt.showPrompt(this.plugin, this.player,
                                            "Type in 'DELETE' to confirm.",
                                            response -> {
                                                if (response.getMessage().trim().equalsIgnoreCase("delete")) {
                                                    this.storyManager.removeStory(story);
                                                }
                                            })
                                    .setOnClose(() -> this.guiManager.showGUI(this.player, new GuiStories(this.plugin, this.player)));
                        }
                    });
        }
    }
}
