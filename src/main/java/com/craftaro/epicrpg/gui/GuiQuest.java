package com.craftaro.epicrpg.gui;

import com.craftaro.core.gui.Gui;
import com.craftaro.core.gui.GuiUtils;
import com.craftaro.core.input.ChatPrompt;
import com.craftaro.third_party.com.cryptomorin.xseries.XMaterial;
import com.craftaro.core.utils.TextUtils;
import com.craftaro.epicrpg.EpicRPG;
import com.craftaro.epicrpg.Region.Region;
import com.craftaro.epicrpg.story.quest.Objective;
import com.craftaro.epicrpg.story.quest.Quest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GuiQuest extends Gui {
    private final EpicRPG plugin;
    private final Player player;
    private final Quest quest;

    public GuiQuest(EpicRPG plugin, Player player, Quest quest) {
        this.plugin = plugin;
        this.player = player;
        this.quest = quest;
        setRows(6);
        setDefaultItem(null);

        setTitle(quest.getName());

        show();
    }

    public void show() {
        reset();

        setButton(0, 0, GuiUtils.createButtonItem(XMaterial.GREEN_DYE, "Rename Quest"),
                (event) -> ChatPrompt.showPrompt(this.plugin, this.player,
                                "Enter a Quest name.",
                                response -> this.quest.setName(response.getMessage()))
                        .setOnClose(() -> this.guiManager.showGUI(this.player, new GuiQuest(this.plugin, this.player, this.quest))));

        setButton(0, 1, GuiUtils.createButtonItem(XMaterial.BLUE_DYE, "Create Objective"),
                (event) -> {
                    this.quest.addObjective(new Objective(this.quest));
                    show();
                });

        setButton(0, 2, GuiUtils.createButtonItem(XMaterial.RED_DYE, this.quest.isActive() ? "Active" : "Inactive"),
                (event) -> {
                    this.quest.setActive(!this.quest.isActive());
                    show();
                });

        setButton(0, 3, GuiUtils.createButtonItem(XMaterial.PURPLE_DYE, "Modify Prerequisites"),
                (event) -> {
                    this.guiManager.showGUI(this.player, new GuiQuestPrereqs(this.plugin, this.player, this.quest));
                    show();
                });

        setButton(0, 4, GuiUtils.createButtonItem(XMaterial.YELLOW_DYE, "Modify Rewards"),
                (event) -> {
                    this.guiManager.showGUI(this.player, new GuiRewards(this.plugin, this.player, this.quest));
                    show();
                });

        Region region = this.quest.getRegion();
        setButton(0, 5, GuiUtils.createButtonItem(XMaterial.ORANGE_DYE, "Modify Focus Region",
                        region != null ? Arrays.asList("Right-Click: to show region.", "Left-Click: to clear.")
                                : Collections.singletonList("Click to set region.")),
                (event) -> {
                    if (region == null) {
                        this.plugin.getSelectionManager().addActiveSelection(this.player, this.quest);
                        close();
                        this.player.sendMessage("Select your first region position.");
                    } else {
                        if (event.clickType == ClickType.RIGHT) {
                            this.plugin.getSelectionManager().addActiveView(this.player, region);
                            close();
                            this.player.sendMessage("Showing region for 15 seconds.");
                            Bukkit.getScheduler().runTaskLater(this.plugin, () ->
                                            this.plugin.getSelectionManager().removeActiveView(this.player),
                                    20 * 15);
                        } else if (event.clickType == ClickType.LEFT) {
                            this.quest.setRegion(null);
                            show();
                        }
                    }
                });

        setButton(0, 8, GuiUtils.createButtonItem(XMaterial.OAK_DOOR, "Back"),
                (event) -> {
                    this.guiManager.showGUI(this.player, new GuiStory(this.plugin, this.player, this.quest.getStory()));
                });

        List<Objective> objectives = this.quest.getObjectives();
        for (int i = 0; i < objectives.size(); i++) {
            Objective objective = objectives.get(i);
            setButton(i + 9, GuiUtils.createButtonItem(XMaterial.PAPER, objective.getTitle(),
                            "",
                            TextUtils.formatText("&fLeft-Click: &6to view"),
                            TextUtils.formatText("&fMiddle-Click: &6to move to end"),
                            TextUtils.formatText("&fRight-Click: &6to delete")),
                    (event) -> {
                        if (event.clickType == ClickType.LEFT) {
                            this.guiManager.showGUI(this.player, new GuiObjective(this.plugin, this.player, objective));
                        } else if (event.clickType == ClickType.MIDDLE) {
                            this.quest.moveObjectiveToEnd(objective);
                            show();
                        } else if (event.clickType == ClickType.RIGHT) {
                            this.quest.removeObjective(objective);
                            show();
                        }
                    });
        }
    }
}
