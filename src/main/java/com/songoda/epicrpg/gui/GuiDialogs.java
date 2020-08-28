package com.songoda.epicrpg.gui;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.gui.Gui;
import com.songoda.core.gui.GuiUtils;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.dialog.AttachedSpeech;
import com.songoda.epicrpg.dialog.Dialog;
import com.songoda.epicrpg.dialog.DialogManager;
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
        if (inventory != null)
            inventory.clear();
        setActionForRange(0, 53, null);

        if (attachedSpeech == null) {
            setButton(0, 0, GuiUtils.createButtonItem(CompatibleMaterial.GREEN_DYE, "Create Dialog"),
                    (event) -> {
                        plugin.getContendentManager().getPlayer(player).setInDialogCreation(true);
                        player.sendMessage("Click a citizen to create a new dialog.");
                        player.closeInventory();
                    });

            setButton(0, 8, GuiUtils.createButtonItem(CompatibleMaterial.OAK_DOOR, "Back"),
                    (event) -> {
                        guiManager.showGUI(player, new GuiMain(plugin, player));
                    });
        }

        List<Dialog> dialogs = dialogManager.getDialogs();
        for (int i = 0; i < dialogs.size(); i++) {
            Dialog dialog = dialogs.get(i);

            if (dialog.getCitizen() == null) {
                dialogManager.removeDialog(dialog);
                continue;
            }

            setButton(i + 9, GuiUtils.createButtonItem(CompatibleMaterial.PAPER, dialog.getCitizen().getName()),
                    (event) -> guiManager.showGUI(player, new GuiDialog(plugin, player, dialog, attachedSpeech)));

        }
    }
}