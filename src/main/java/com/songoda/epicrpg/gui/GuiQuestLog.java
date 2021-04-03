package com.songoda.epicrpg.gui;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.gui.CustomizableGui;
import com.songoda.core.gui.Gui;
import com.songoda.core.gui.GuiUtils;
import com.songoda.core.utils.TextUtils;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.story.Story;
import com.songoda.epicrpg.story.StoryManager;
import com.songoda.epicrpg.story.contender.StoryContender;
import com.songoda.epicrpg.story.contender.StoryParty;
import com.songoda.epicrpg.story.contender.StoryPlayer;
import com.songoda.epicrpg.story.quest.ActiveQuest;
import com.songoda.epicrpg.story.quest.Objective;
import com.songoda.epicrpg.story.quest.Quest;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GuiQuestLog extends CustomizableGui {

    private final EpicRPG plugin;
    private final Player player;
    private final StoryPlayer storyPlayer;
    private final StoryManager storyManager;

    private final int storyCount;

    public GuiQuestLog(EpicRPG plugin, Player player) {
        super(plugin, "questlog");
        this.plugin = plugin;
        this.player = player;
        this.storyPlayer = plugin.getContendentManager().getPlayer(player);
        this.storyManager = plugin.getStoryManager();
        setRows(6);
        setDefaultItem(null);

        storyCount = Math.toIntExact(storyManager.getStories().stream().filter(Story::isActive).count());

        setTitle("Quest Log");

        show();
    }

    public void show() {
        reset();

        List<Story> stories = storyManager.getStories();
        for (int i = 0; i < stories.size(); i++) {
            Story story = stories.get(i);
            if (storyCount == 1) {
                showQuests(story, false);
                break;
            }
            if (story.isActive())
                setButton(i + 9, GuiUtils.createButtonItem(CompatibleMaterial.PAPER, story.getName()),
                        (event) -> showQuests(story, false));
        }
    }

    public void showQuests(Story story, boolean completed) {
        reset();

        setButton("objectives", 4, GuiUtils.createButtonItem(CompatibleMaterial.BELL,
                storyPlayer.isSilent() ? "Click to show objectives." : "Click to hide objectives."),
                (event) -> {
                    storyPlayer.setSilent(!storyPlayer.isSilent());
                    showQuests(story, completed);
                });

        if (storyCount != 1)
            setButton("back", 5, 3, GuiUtils.createButtonItem(CompatibleMaterial.ARROW, "Back"),
                    (event) -> show());
        setButton("exit", 5, storyCount == 1 ? 3 : 4, GuiUtils.createButtonItem(CompatibleMaterial.OAK_DOOR, "Exit"),
                (event) -> player.closeInventory());


        long amountCompleted = story.getEnabledQuests().stream()
                .filter(q -> storyPlayer.getCompletedQuests().contains(q.getUniqueId())).count();

        List<String> lore = new ArrayList<>();
        if (completed) {
            lore.addAll(Arrays.asList(TextUtils.formatText("&7View quests you are"),
                    TextUtils.formatText("&7currently working towards."),
                    "",
                    TextUtils.formatText("&eClick to view!")));
        } else {
            lore.addAll(Arrays.asList(TextUtils.formatText("&7Take a peak at the past"),
                    TextUtils.formatText("&7and browse quests you've"),
                    TextUtils.formatText("&7already completed."),
                    "",
                    TextUtils.formatText("&7Completed: &a" + amountCompleted),
                    "",
                    TextUtils.formatText("&eClick to view!")));

        }

        setButton("quests", 5, 5, GuiUtils.createButtonItem(CompatibleMaterial.BOOK,
                TextUtils.formatText(completed ? "&aOngoing Quests" : "&aCompleted Quests"), lore),
                (event) -> {
                    setTitle(completed ? "Quest Log" : "Quest Log (Completed)");
                    showQuests(story, !completed);
                });

        int i = 9;
        for (Quest quest : story.getEnabledQuests()) {
            boolean questCompleted = storyPlayer.getCompletedQuests().contains(quest.getUniqueId());
            if (completed && !questCompleted || !completed && questCompleted
                    || !storyPlayer.getActiveQuests().stream()
                    .map(ActiveQuest::getActiveQuest).collect(Collectors.toList())
                    .contains(quest.getUniqueId()) && !storyPlayer.getCompletedQuests().contains(quest.getUniqueId()))
                continue;

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

            StoryContender contender = plugin.getContendentManager().getContender(player.getUniqueId());
            if (contender instanceof StoryParty) {
                StoryParty party = (StoryParty) contender;
                if (party.getActiveQuests().size() == 1) {
                    if (party.getActiveQuests().get(0).equals(quest))
                        objectivesLore.addAll(Arrays.asList("", TextUtils.formatText("&aYour party is doing this quest.")));
                    else
                        objectivesLore.addAll(Arrays.asList("", TextUtils.formatText("&aClick to swap to this quest.")));
                } else
                    objectivesLore.addAll(Arrays.asList("", TextUtils.formatText("&7Choose this quest.")));
            } else {
                if (storyPlayer.isFocused(quest))
                    objectivesLore.addAll(Arrays.asList("", TextUtils.formatText("&7Focused")));
            }

            setButton(i, GuiUtils.createButtonItem(CompatibleMaterial.PAPER,
                    TextUtils.formatText("&d" + quest.getName()),
                    objectivesLore), (event) -> {
                if (!questCompleted) {
                    if (contender instanceof StoryParty) {
                        StoryParty party = (StoryParty) contender;
                        if (party.getActiveQuests().size() == 1) {
                            if (!party.getActiveQuests().get(0).equals(quest))
                                party.swapQuest(quest);
                        } else
                            party.addActiveQuest(quest);
                    } else {
                        if (!storyPlayer.isFocused(quest)) {
                            storyPlayer.toggleAllFocusedOff();
                            storyPlayer.toggleFocus(quest);
                            showQuests(story, false);
                        }
                    }
                }
            });
            i++;
        }
    }
}