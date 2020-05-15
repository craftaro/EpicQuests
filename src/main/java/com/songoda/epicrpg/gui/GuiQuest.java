package com.songoda.epicrpg.gui;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.gui.Gui;
import com.songoda.core.gui.GuiUtils;
import com.songoda.core.input.ChatPrompt;
import com.songoda.core.utils.TextUtils;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.Region.ActiveView;
import com.songoda.epicrpg.Region.Region;
import com.songoda.epicrpg.story.quest.Objective;
import com.songoda.epicrpg.story.quest.Quest;
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
        if (inventory != null)
            inventory.clear();
        setActionForRange(0, 53, null);

        setButton(0, 0, GuiUtils.createButtonItem(CompatibleMaterial.GREEN_DYE, "Rename Quest"),
                (event) -> {
                    ChatPrompt.showPrompt(plugin, player,
                            "Enter a Quest name.",
                            response -> quest.setName(response.getMessage()))
                            .setOnClose(() -> guiManager.showGUI(player, new GuiQuest(plugin, player, quest)));
                });

        setButton(0, 1, GuiUtils.createButtonItem(CompatibleMaterial.BLUE_DYE, "Create Objective"),
                (event) -> {
                    quest.addObjective(new Objective(quest));
                    show();
                });

        setButton(0, 2, GuiUtils.createButtonItem(CompatibleMaterial.RED_DYE, quest.isActive() ? "Active" : "Inactive"),
                (event) -> {
                    quest.setActive(!quest.isActive());
                    show();
                });

        setButton(0, 3, GuiUtils.createButtonItem(CompatibleMaterial.PURPLE_DYE, "Modify Prerequisites"),
                (event) -> {
                    guiManager.showGUI(player, new GuiQuestPrereqs(plugin, player, quest));
                    show();
                });

        setButton(0, 4, GuiUtils.createButtonItem(CompatibleMaterial.YELLOW_DYE, "Modify Rewards"),
                (event) -> {
                    guiManager.showGUI(player, new GuiRewards(plugin, player, quest));
                    show();
                });

        Region region = quest.getRegion();
        setButton(0, 4, GuiUtils.createButtonItem(CompatibleMaterial.ORANGE_DYE, "Modify Focus Region",
                region != null ? Arrays.asList("Right-Click: to show region.", "Left-Click: to clear.")
                        : Collections.singletonList("Click to set region.")),
                (event) -> {
                    if (region == null) {
                        plugin.getSelectionManager().addActiveSelection(player, quest);
                        close();
                        player.sendMessage("Select your first region position.");
                    } else {
                        if (event.clickType == ClickType.RIGHT) {
                            plugin.getSelectionManager().addActiveView(player, region);
                            close();
                            player.sendMessage("Showing region for 15 seconds.");
                            Bukkit.getScheduler().runTaskLater(plugin, () ->
                                    plugin.getSelectionManager().removeActiveView(player),
                                    20 * 15);
                        } else if (event.clickType == ClickType.LEFT) {
                            quest.setRegion(null);
                            show();
                        }
                    }
                });

        setButton(0, 8, GuiUtils.createButtonItem(CompatibleMaterial.OAK_DOOR, "Back"),
                (event) -> {
                    guiManager.showGUI(player, new GuiStory(plugin, player, quest.getStory()));
                });


        List<Objective> objectives = quest.getObjectives();
        for (int i = 0; i < objectives.size(); i++) {
            Objective objective = objectives.get(i);
            setButton(i + 9, GuiUtils.createButtonItem(CompatibleMaterial.PAPER, objective.getTitle(),
                    "",
                    TextUtils.formatText("&fLeft-Click: &6to view"),
                    TextUtils.formatText("&fMiddle-Click: &6to move to end"),
                    TextUtils.formatText("&fRight-Click: &6to delete")),
                    (event) -> {
                        if (event.clickType == ClickType.LEFT)
                            guiManager.showGUI(player, new GuiObjective(plugin, player, objective));
                        else if (event.clickType == ClickType.MIDDLE) {
                            quest.moveObjectiveToEnd(objective);
                            show();
                        } else if (event.clickType == ClickType.RIGHT) {
                            quest.removeObjective(objective);
                            show();
                        }
                    });
        }
    }
}