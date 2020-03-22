package com.songoda.epicrpg.gui;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.gui.Gui;
import com.songoda.core.gui.GuiUtils;
import com.songoda.core.utils.TextUtils;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.story.Story;
import com.songoda.epicrpg.story.StoryManager;
import com.songoda.epicrpg.story.player.StoryPlayer;
import com.songoda.epicrpg.story.quest.ActiveQuest;
import com.songoda.epicrpg.story.quest.Objective;
import com.songoda.epicrpg.story.quest.Quest;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GuiQuestLog extends Gui {

    private final EpicRPG plugin;
    private final Player player;
    private final StoryPlayer storyPlayer;
    private final StoryManager storyManager;

    public GuiQuestLog(EpicRPG plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.storyPlayer = plugin.getPlayerManager().getPlayer(player);
        this.storyManager = plugin.getStoryManager();
        setRows(6);
        setDefaultItem(null);

        setTitle("Quest Log");

        show();
    }

    public void show() {
        if (inventory != null)
            inventory.clear();
        setActionForRange(0, 53, null);


        List<Story> stories = storyManager.getStories();
        for (int i = 0; i < stories.size(); i++) {
            Story story = stories.get(i);
            if (story.isActive())
                setButton(i + 9, GuiUtils.createButtonItem(CompatibleMaterial.PAPER, story.getName()),
                        (event) -> {
                            showQuests(story, false);
                        });
        }
    }

    public void showQuests(Story story, boolean completed) {
        inventory.clear();
        setActionForRange(0, 53, null);

        setButton(8, GuiUtils.createButtonItem(CompatibleMaterial.PAPER, completed ? "Show Uncompleted" : "Show Completed"),
                (event) -> {
                    showQuests(story, !completed);
                });

        int i = 9;
        for (Quest quest : story.getEnabledQuests()) {
            boolean questCompleted = storyPlayer.getCompletedQuests().contains(quest.getUniqueId());
            if (completed && !questCompleted || !completed && questCompleted
                    || !storyPlayer.getActiveQuests().stream()
                    .map(ActiveQuest::getActiveQuest).collect(Collectors.toList())
                    .contains(quest.getUniqueId()) && !storyPlayer.getCompletedQuests().contains(quest.getUniqueId())) continue;

            List<Objective> objectives = new ArrayList<>();
            if (questCompleted) {
                objectives.addAll(quest.getObjectives());
            } else {
                for (Objective objective : quest.getObjectives()) {
                    objectives.add(objective);
                    if (!storyPlayer.isObjectiveCompleted(objective))
                        break;
                }
            }

            List<String> objectivesLore = new ArrayList<>(Collections.singletonList(""));
            for (Objective objective : objectives)
                objectivesLore.add(TextUtils.formatText(
                        (storyPlayer.isObjectiveCompleted(objective) ? "&a&l✔ &f" : "&c&l✖ &d") + objective.getTitle()));

            if (storyPlayer.isFocused(quest))
                objectivesLore.addAll(Arrays.asList("", TextUtils.formatText("&7Focused")));

            setButton(i, GuiUtils.createButtonItem(CompatibleMaterial.PAPER,
                    TextUtils.formatText("&d" + quest.getName()),
                    objectivesLore), (event) -> {
                if (!questCompleted) {
                    storyPlayer.toggleAllFocusedOff();
                    storyPlayer.toggleFocus(quest);
                    showQuests(story, false);
                }
            });
            i++;
        }
    }
}