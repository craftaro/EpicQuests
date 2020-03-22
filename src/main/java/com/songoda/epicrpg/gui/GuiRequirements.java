package com.songoda.epicrpg.gui;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.gui.Gui;
import com.songoda.core.gui.GuiUtils;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.story.StoryManager;
import com.songoda.epicrpg.story.quest.Objective;
import com.songoda.epicrpg.story.quest.requirement.AbstractRequirement;
import com.songoda.epicrpg.story.quest.requirement.Requirement;
import com.songoda.epicrpg.utils.TextUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

public class GuiRequirements extends Gui {

    private final EpicRPG plugin;
    private final Player player;
    private final StoryManager storyManager;
    private final Objective objective;

    public GuiRequirements(EpicRPG plugin, Player player, Objective objective) {
        this.plugin = plugin;
        this.player = player;
        this.storyManager = plugin.getStoryManager();
        this.objective = objective;
        setRows(6);
        setDefaultItem(null);

        setTitle(objective.getTitle() + " - requirements");

        show();
    }

    public void show() {
        if (inventory != null)
            inventory.clear();
        setActionForRange(0, 53, null);

        setButton(0, 0, GuiUtils.createButtonItem(CompatibleMaterial.GREEN_DYE, "Add Requirement"),
                (event) -> {
                    guiManager.showGUI(player, new GuiRequirementTypes(plugin, player, objective));
                });

        setButton(0, 8, GuiUtils.createButtonItem(CompatibleMaterial.OAK_DOOR, "Back"),
                (event) -> {
                    guiManager.showGUI(player, new GuiObjective(plugin, player, objective));
                });


        List<Requirement> requirements = objective.getRequirements();
        for (int i = 0; i < requirements.size(); i++) {
            AbstractRequirement requirement = (AbstractRequirement) requirements.get(i);

            setButton(i + 9, GuiUtils.createButtonItem(CompatibleMaterial.PAPER, requirement.getType().name(),
                    "",
                    TextUtils.formatText("&fRight-Click: &6to remove"),
                    TextUtils.formatText("&fMiddle-Click: &6to attach dialog"),
                    TextUtils.formatText("&fLeft-Click: &6to setup")),
                    (event) -> {
                        if (event.clickType == ClickType.RIGHT) {
                            objective.removeRequirement(requirement);
                            show();
                        } else if (event.clickType == ClickType.MIDDLE) {
                            guiManager.showGUI(player, new GuiDialogs(plugin, player, requirement));
                        } else if (event.clickType == ClickType.LEFT) {
                            requirement.setup(player);
                        }
                    });
        }
    }
}