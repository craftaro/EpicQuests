package com.songoda.epicrpg.gui;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.gui.Gui;
import com.songoda.core.gui.GuiUtils;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.settings.Settings;
import com.songoda.epicrpg.utils.TextUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GuiMain extends Gui {

    private final EpicRPG plugin;

    public GuiMain(EpicRPG plugin, Player player) {
        this.plugin = plugin;
        setRows(3);
        setDefaultItem(null);

        setTitle("EpicRPG");

        // decorate the edges
        ItemStack glass2 = GuiUtils.getBorderItem(Settings.GLASS_TYPE_2.getMaterial(CompatibleMaterial.BLUE_STAINED_GLASS_PANE));
        ItemStack glass3 = GuiUtils.getBorderItem(Settings.GLASS_TYPE_3.getMaterial(CompatibleMaterial.LIGHT_BLUE_STAINED_GLASS_PANE));

        // edges will be type 3
        GuiUtils.mirrorFill(this, 0, 2, true, true, glass3);
        GuiUtils.mirrorFill(this, 1, 1, false, true, glass3);

        // decorate corners with type 2
        GuiUtils.mirrorFill(this, 0, 0, true, true, glass2);
        GuiUtils.mirrorFill(this, 1, 0, true, true, glass2);
        GuiUtils.mirrorFill(this, 0, 1, true, true, glass2);

        setButton(1, 3, GuiUtils.createButtonItem(CompatibleMaterial.BOOK, TextUtils.formatText("&9&lDialog")),
                (event) -> guiManager.showGUI(player, new GuiDialogs(plugin, player, null)));

        setButton(1, 5, GuiUtils.createButtonItem(CompatibleMaterial.WRITTEN_BOOK, TextUtils.formatText("&a&lStories")), (event) ->
                guiManager.showGUI(player, new GuiStories(plugin, player)));

    }

}
