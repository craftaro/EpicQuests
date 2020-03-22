package com.songoda.epicrpg.gui;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.gui.Gui;
import com.songoda.core.gui.GuiUtils;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.story.quest.Objective;
import com.songoda.epicrpg.story.quest.action.Action;
import com.songoda.epicrpg.story.quest.action.ActionManager;
import org.bukkit.entity.Player;

import java.util.List;

public class GuiActionTypes extends Gui {

    public GuiActionTypes(EpicRPG plugin, Player player, Objective objective) {
        setRows(6);
        setDefaultItem(null);
        ActionManager actionManager = plugin.getActionManager();

        setTitle("Pick an action type");

        setActionForRange(0, 53, null);

        setButton(0, 8, GuiUtils.createButtonItem(CompatibleMaterial.BARRIER, "Cancel"),
                (event) -> {
                    guiManager.showGUI(player, new GuiObjective(plugin, player, objective));
                });


        List<Action> actions = actionManager.getActions();
        for (int i = 0; i < actions.size(); i++) {
            Action action = actions.get(i);
            setButton(i + 9, GuiUtils.createButtonItem(CompatibleMaterial.PAPER, action.getType()),
                    (event) -> {
                        player.closeInventory();
                        actionManager.addActiveAction(action.setup(player, objective));
                    });
        }
    }
}