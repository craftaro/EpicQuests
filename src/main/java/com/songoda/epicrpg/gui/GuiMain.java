package com.songoda.epicrpg.gui;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.gui.Gui;
import com.songoda.core.gui.GuiUtils;
import com.songoda.epicrpg.EpicRPG;
import org.bukkit.entity.Player;

public class GuiMain extends Gui {

    private final EpicRPG plugin;

    public GuiMain(EpicRPG plugin, Player player) {
        this.plugin = plugin;
        setRows(3);
        setDefaultItem(null);

        setTitle("EpicRPG");

        setButton(1, 3, GuiUtils.createButtonItem(CompatibleMaterial.PAPER, "Dialog"),
                (event) -> guiManager.showGUI(player, new GuiDialogs(plugin, player, null)));

        setButton(1, 5, GuiUtils.createButtonItem(CompatibleMaterial.PAPER, "Stories"), (event) ->
                guiManager.showGUI(player, new GuiStories(plugin, player)));

    }

}
