package com.songoda.epicrpg.gui;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.gui.Gui;
import com.songoda.core.gui.GuiUtils;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.dialog.AttachedSpeech;
import com.songoda.epicrpg.dialog.Dialog;
import com.songoda.epicrpg.dialog.DialogManager;
import com.songoda.epicrpg.dialog.Speech;
import com.songoda.epicrpg.story.quest.Objective;
import com.songoda.epicrpg.story.quest.requirement.Requirement;
import com.songoda.epicrpg.utils.TextUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GuiDialog extends Gui {

    private final EpicRPG plugin;
    private final Dialog dialog;
    private final Player player;
    private final AttachedSpeech attachedSpeech;

    public GuiDialog(EpicRPG plugin, Player player, Dialog dialog, AttachedSpeech attachedSpeech) {
        this.plugin = plugin;
        this.dialog = dialog;

        this.player = player;
        this.attachedSpeech = attachedSpeech;
        setRows(6);
        setDefaultItem(null);

        setTitle("Dialog");

        show();
    }


    public void show() {
        reset();

        if (attachedSpeech == null)
            setButton(0, 0, GuiUtils.createButtonItem(CompatibleMaterial.GREEN_DYE, "Add Speech"),
                    (event) -> {
                        dialog.addMessage(new Speech(dialog));
                        show();
                    });

        setButton(0, 8, GuiUtils.createButtonItem(CompatibleMaterial.OAK_DOOR, "Back"),
                (event) -> guiManager.showGUI(player, new GuiDialogs(plugin, player, attachedSpeech)));


        List<Speech> messages = dialog.getMessages();
        for (int i = 0; i < messages.size(); i++) {
            Speech speech = messages.get(i);
            List<String> lore = new ArrayList<>(Collections.singletonList(""));

            lore.addAll(speech.getMessages().isEmpty()
                    ? Collections.singletonList(TextUtils.formatText("&cNothing here..."))
                    : TextUtils.condense(speech.getMessages().get(0)));

            lore.addAll(Arrays.asList("", TextUtils.formatText(attachedSpeech == null ? "&fLeft-Click: &6to view" : "&fLeft-Click: &6to select"), TextUtils.formatText("&fRight-Click: &6to delete")));
            setButton(i + 9, GuiUtils.createButtonItem(CompatibleMaterial.PAPER, "Speech " + (i + 1), lore),
                    (event) -> {
                        if (event.clickType == ClickType.RIGHT) {
                            dialog.removeSpeech(speech);
                        } else {
                            if (attachedSpeech == null) {
                                guiManager.showGUI(player, new GuiSpeech(plugin, player, speech));
                                show();
                            } else {
                                attachedSpeech.setAttachedSpeech(speech.getUniqueId());
                                if (attachedSpeech instanceof Objective)
                                    guiManager.showGUI(player, new GuiObjective(plugin, player, (Objective) attachedSpeech));
                                else
                                    guiManager.showGUI(player, new GuiRequirements(plugin, player, ((Requirement) attachedSpeech).getObjective()));
                            }
                        }
                    });
        }
    }
}