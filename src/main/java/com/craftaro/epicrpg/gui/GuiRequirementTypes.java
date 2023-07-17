package com.craftaro.epicrpg.gui;

import com.craftaro.core.gui.Gui;
import com.craftaro.core.gui.GuiUtils;
import com.craftaro.core.third_party.com.cryptomorin.xseries.XMaterial;
import com.craftaro.epicrpg.EpicRPG;
import com.craftaro.epicrpg.story.quest.Objective;
import com.craftaro.epicrpg.story.quest.requirement.Requirement;
import com.craftaro.epicrpg.story.quest.requirement.RequirementType;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class GuiRequirementTypes extends Gui {
    public GuiRequirementTypes(EpicRPG plugin, Player player, Objective objective) {
        setRows(6);
        setDefaultItem(null);

        setTitle("Pick a requirement type");

        setActionForRange(0, 53, null);

        setButton(0, 8, GuiUtils.createButtonItem(XMaterial.BARRIER, "Cancel"),
                (event) -> {
                    this.guiManager.showGUI(player, new GuiRequirements(plugin, player, objective));
                });


        List<RequirementType> requirements = Arrays.asList(RequirementType.values());
        for (int i = 0; i < requirements.size(); i++) {
            RequirementType requirementType = requirements.get(i);
            setButton(i + 9, GuiUtils.createButtonItem(XMaterial.PAPER, requirementType.name()),
                    (event) -> {
                        player.closeInventory();
                        Requirement requirement = requirementType.init(objective);
                        objective.addRequirement(requirement);
                        requirement.setup(player);
                    });
        }
    }
}
