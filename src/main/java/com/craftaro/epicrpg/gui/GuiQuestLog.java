package com.craftaro.epicrpg.gui;

import com.craftaro.core.gui.CustomizableGui;
import com.craftaro.core.gui.GuiUtils;
import com.craftaro.core.third_party.com.cryptomorin.xseries.XMaterial;
import com.craftaro.core.utils.TextUtils;
import com.craftaro.epicrpg.story.Story;
import com.craftaro.epicrpg.story.StoryManager;
import com.craftaro.epicrpg.story.contender.StoryContender;
import com.craftaro.epicrpg.story.contender.StoryParty;
import com.craftaro.epicrpg.story.contender.StoryPlayer;
import com.craftaro.epicrpg.story.quest.ActiveQuest;
import com.craftaro.epicrpg.story.quest.Objective;
import com.craftaro.epicrpg.story.quest.Quest;
import com.craftaro.epicrpg.EpicRPG;
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

        this.storyCount = Math.toIntExact(this.storyManager.getStories().stream().filter(Story::isActive).count());

        setTitle("Quest Log");

        show();
    }

    public void show() {
        reset();

        List<Story> stories = this.storyManager.getStories();
        for (int i = 0; i < stories.size(); i++) {
            Story story = stories.get(i);
            if (this.storyCount == 1) {
                showQuests(story, false);
                break;
            }
            if (story.isActive()) {
                setButton(i + 9, GuiUtils.createButtonItem(XMaterial.PAPER, story.getName()),
                        (event) -> showQuests(story, false));
            }
        }
    }

    public void showQuests(Story story, boolean completed) {
        reset();

        setButton("objectives", 4, GuiUtils.createButtonItem(XMaterial.BELL,
                        this.storyPlayer.isSilent() ? "Click to show objectives." : "Click to hide objectives."),
                (event) -> {
                    this.storyPlayer.setSilent(!this.storyPlayer.isSilent());
                    showQuests(story, completed);
                });

        if (this.storyCount != 1) {
            setButton("back", 5, 3, GuiUtils.createButtonItem(XMaterial.ARROW, "Back"),
                    (event) -> show());
        }
        setButton("exit", 5, this.storyCount == 1 ? 3 : 4, GuiUtils.createButtonItem(XMaterial.OAK_DOOR, "Exit"),
                (event) -> this.player.closeInventory());


        long amountCompleted = story.getEnabledQuests().stream()
                .filter(q -> this.storyPlayer.getCompletedQuests().contains(q.getUniqueId())).count();

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

        setButton("quests", 5, 5, GuiUtils.createButtonItem(XMaterial.BOOK,
                        TextUtils.formatText(completed ? "&aOngoing Quests" : "&aCompleted Quests"), lore),
                (event) -> {
                    setTitle(completed ? "Quest Log" : "Quest Log (Completed)");
                    showQuests(story, !completed);
                });

        int i = 9;
        for (Quest quest : story.getEnabledQuests()) {
            boolean questCompleted = this.storyPlayer.getCompletedQuests().contains(quest.getUniqueId());
            if (completed && !questCompleted || !completed && questCompleted
                    || !this.storyPlayer.getActiveQuests().stream()
                    .map(ActiveQuest::getActiveQuest).collect(Collectors.toList())
                    .contains(quest.getUniqueId()) && !this.storyPlayer.getCompletedQuests().contains(quest.getUniqueId())) {
                continue;
            }

            List<Objective> objectives = new ArrayList<>();
            if (questCompleted) {
                objectives.addAll(quest.getObjectives());
            } else {
                for (Objective objective : quest.getObjectives()) {
                    objectives.add(objective);
                    if (!this.storyPlayer.isObjectiveCompleted(objective)) {
                        break;
                    }
                }
            }

            List<String> objectivesLore = new ArrayList<>(Collections.singletonList(""));
            for (Objective objective : objectives) {
                objectivesLore.add(TextUtils.formatText(
                        (this.storyPlayer.isObjectiveCompleted(objective) ? "&a&l✔ &f" : "&c&l✖ &d") + objective.getTitle()));
            }

            StoryContender contender = this.plugin.getContendentManager().getContender(this.player.getUniqueId());
            if (contender instanceof StoryParty) {
                StoryParty party = (StoryParty) contender;
                if (party.getActiveQuests().size() == 1) {
                    if (party.getActiveQuests().get(0).equals(quest)) {
                        objectivesLore.addAll(Arrays.asList("", TextUtils.formatText("&aYour party is doing this quest.")));
                    } else {
                        objectivesLore.addAll(Arrays.asList("", TextUtils.formatText("&aClick to swap to this quest.")));
                    }
                } else {
                    objectivesLore.addAll(Arrays.asList("", TextUtils.formatText("&7Choose this quest.")));
                }
            } else {
                if (this.storyPlayer.isFocused(quest)) {
                    objectivesLore.addAll(Arrays.asList("", TextUtils.formatText("&7Focused")));
                }
            }

            setButton(i, GuiUtils.createButtonItem(XMaterial.PAPER,
                    TextUtils.formatText("&d" + quest.getName()),
                    objectivesLore), (event) -> {
                if (!questCompleted) {
                    if (contender instanceof StoryParty) {
                        StoryParty party = (StoryParty) contender;
                        if (party.getActiveQuests().size() == 1) {
                            if (!party.getActiveQuests().get(0).equals(quest)) {
                                party.swapQuest(quest);
                            }
                        } else {
                            party.addActiveQuest(quest);
                        }
                    } else {
                        if (!this.storyPlayer.isFocused(quest)) {
                            this.storyPlayer.toggleAllFocusedOff();
                            this.storyPlayer.toggleFocus(quest);
                            showQuests(story, false);
                        }
                    }
                }
            });
            i++;
        }
    }
}
