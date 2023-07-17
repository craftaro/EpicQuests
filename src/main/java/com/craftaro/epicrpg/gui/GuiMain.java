package com.craftaro.epicrpg.gui;

import com.craftaro.core.gui.Gui;
import com.craftaro.core.gui.GuiUtils;
import com.craftaro.core.third_party.com.cryptomorin.xseries.XMaterial;
import com.craftaro.epicrpg.EpicRPG;
import com.craftaro.epicrpg.settings.Settings;
import com.craftaro.epicrpg.utils.TextUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GuiMain extends Gui {
    public GuiMain(EpicRPG plugin, Player player) {
        setRows(3);
        setDefaultItem(null);

        setTitle("EpicRPG");

        // decorate the edges
        ItemStack glass2 = GuiUtils.getBorderItem(Settings.GLASS_TYPE_2.getMaterial(XMaterial.BLUE_STAINED_GLASS_PANE));
        ItemStack glass3 = GuiUtils.getBorderItem(Settings.GLASS_TYPE_3.getMaterial(XMaterial.LIGHT_BLUE_STAINED_GLASS_PANE));

        // edges will be type 3
        mirrorFill(0, 2, true, true, glass3);
        mirrorFill(1, 1, false, true, glass3);

        // decorate corners with type 2
        mirrorFill(0, 0, true, true, glass2);
        mirrorFill(1, 0, true, true, glass2);
        mirrorFill(0, 1, true, true, glass2);

        setButton(1, 3, GuiUtils.createButtonItem(XMaterial.BOOK, TextUtils.formatText("&9&lDialog")),
                (event) -> this.guiManager.showGUI(player, new GuiDialogs(plugin, player, null)));

        setButton(1, 5, GuiUtils.createButtonItem(XMaterial.WRITTEN_BOOK, TextUtils.formatText("&a&lStories")), (event) ->
                this.guiManager.showGUI(player, new GuiStories(plugin, player)));
    }
}
