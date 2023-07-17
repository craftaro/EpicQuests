package com.craftaro.epicrpg.gui;

import com.craftaro.core.gui.Gui;
import com.craftaro.core.gui.GuiUtils;
import com.craftaro.core.third_party.com.cryptomorin.xseries.XMaterial;
import com.craftaro.epicrpg.EpicRPG;
import com.craftaro.epicrpg.dialog.AttachedSpeech;
import com.craftaro.epicrpg.dialog.Dialog;
import com.craftaro.epicrpg.dialog.DialogManager;
import org.bukkit.entity.Player;

import java.util.List;

public class GuiDialogs extends Gui {
    private final EpicRPG plugin;
    private final DialogManager dialogManager;
    private final Player player;
    private final AttachedSpeech attachedSpeech;

    public GuiDialogs(EpicRPG plugin, Player player, AttachedSpeech attachedSpeech) {
        this.plugin = plugin;
        this.dialogManager = plugin.getDialogManager();
        this.player = player;
        this.attachedSpeech = attachedSpeech;
        setRows(6);
        setDefaultItem(null);

        setTitle("Stories");

        show();
    }

    public void show() {
        reset();

        if (this.attachedSpeech == null) {
            setButton(0, 0, GuiUtils.createButtonItem(XMaterial.GREEN_DYE, "Create Dialog"),
                    (event) -> {
                        this.plugin.getContendentManager().getPlayer(this.player).setInDialogCreation(true);
                        this.player.sendMessage("Click a citizen to create a new dialog.");
                        this.player.closeInventory();
                    });

            setButton(0, 8, GuiUtils.createButtonItem(XMaterial.OAK_DOOR, "Back"),
                    (event) -> this.guiManager.showGUI(this.player, new GuiMain(this.plugin, this.player)));
        }

        List<Dialog> dialogs = this.dialogManager.getDialogs();
        for (int i = 0; i < dialogs.size(); i++) {
            Dialog dialog = dialogs.get(i);

            if (dialog.getCitizen() == null) {
                this.dialogManager.removeDialog(dialog);
                continue;
            }

            setButton(i + 9, GuiUtils.createButtonItem(XMaterial.PAPER, dialog.getCitizen().getName()),
                    (event) -> this.guiManager.showGUI(this.player, new GuiDialog(this.plugin, this.player, dialog, this.attachedSpeech)));
        }
    }
}
