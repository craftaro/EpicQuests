package com.craftaro.epicrpg.gui;

import com.craftaro.core.gui.Gui;
import com.craftaro.core.gui.GuiUtils;
import com.craftaro.core.third_party.com.cryptomorin.xseries.XMaterial;
import com.craftaro.epicrpg.EpicRPG;
import com.craftaro.epicrpg.story.quest.Objective;
import com.craftaro.epicrpg.story.quest.requirement.AbstractRequirement;
import com.craftaro.epicrpg.story.quest.requirement.Requirement;
import com.craftaro.epicrpg.utils.TextUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

public class GuiRequirements extends Gui {
    private final EpicRPG plugin;
    private final Player player;
    private final Objective objective;

    public GuiRequirements(EpicRPG plugin, Player player, Objective objective) {
        this.plugin = plugin;
        this.player = player;
        this.objective = objective;
        setRows(6);
        setDefaultItem(null);

        setTitle(objective.getTitle() + " - requirements");

        show();
    }

    public void show() {
        reset();

        setButton(0, 0, GuiUtils.createButtonItem(XMaterial.GREEN_DYE, "Add Requirement"),
                (event) -> this.guiManager.showGUI(this.player, new GuiRequirementTypes(this.plugin, this.player, this.objective)));

        setButton(0, 8, GuiUtils.createButtonItem(XMaterial.OAK_DOOR, "Back"),
                (event) -> this.guiManager.showGUI(this.player, new GuiObjective(this.plugin, this.player, this.objective)));


        List<Requirement> requirements = this.objective.getRequirements();
        for (int i = 0; i < requirements.size(); i++) {
            AbstractRequirement requirement = (AbstractRequirement) requirements.get(i);

            setButton(i + 9, GuiUtils.createButtonItem(XMaterial.PAPER, requirement.getType().name(),
                            "",
                            TextUtils.formatText("&fRight-Click: &6to remove"),
                            TextUtils.formatText("&fMiddle-Click: &6to attach dialog"),
                            TextUtils.formatText("&fLeft-Click: &6to setup")),
                    (event) -> {
                        if (event.clickType == ClickType.RIGHT) {
                            this.objective.removeRequirement(requirement);
                            show();
                        } else if (event.clickType == ClickType.MIDDLE) {
                            this.guiManager.showGUI(this.player, new GuiDialogs(this.plugin, this.player, requirement));
                        } else if (event.clickType == ClickType.LEFT) {
                            requirement.setup(this.player);
                        }
                    });
        }
    }
}
