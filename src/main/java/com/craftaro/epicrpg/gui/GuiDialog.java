package com.craftaro.epicrpg.gui;

import com.craftaro.core.gui.Gui;
import com.craftaro.core.gui.GuiUtils;
import com.craftaro.third_party.com.cryptomorin.xseries.XMaterial;
import com.craftaro.epicrpg.EpicRPG;
import com.craftaro.epicrpg.dialog.AttachedSpeech;
import com.craftaro.epicrpg.dialog.Dialog;
import com.craftaro.epicrpg.dialog.Speech;
import com.craftaro.epicrpg.story.quest.Objective;
import com.craftaro.epicrpg.story.quest.requirement.Requirement;
import com.craftaro.epicrpg.utils.TextUtils;
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

        if (this.attachedSpeech == null) {
            setButton(0, 0, GuiUtils.createButtonItem(XMaterial.GREEN_DYE, "Add Speech"),
                    (event) -> {
                        this.dialog.addMessage(new Speech(this.dialog));
                        show();
                    });
        }

        setButton(0, 8, GuiUtils.createButtonItem(XMaterial.OAK_DOOR, "Back"),
                (event) -> this.guiManager.showGUI(this.player, new GuiDialogs(this.plugin, this.player, this.attachedSpeech)));

        List<Speech> messages = this.dialog.getMessages();
        for (int i = 0; i < messages.size(); i++) {
            Speech speech = messages.get(i);
            List<String> lore = new ArrayList<>(Collections.singletonList(""));

            lore.addAll(speech.getMessages().isEmpty()
                    ? Collections.singletonList(TextUtils.formatText("&cNothing here..."))
                    : TextUtils.condense(speech.getMessages().get(0)));

            lore.addAll(Arrays.asList("", TextUtils.formatText(this.attachedSpeech == null ? "&fLeft-Click: &6to view" : "&fLeft-Click: &6to select"), TextUtils.formatText("&fRight-Click: &6to delete")));
            setButton(i + 9, GuiUtils.createButtonItem(XMaterial.PAPER, "Speech " + (i + 1), lore),
                    (event) -> {
                        if (event.clickType == ClickType.RIGHT) {
                            this.dialog.removeSpeech(speech);
                        } else {
                            if (this.attachedSpeech == null) {
                                this.guiManager.showGUI(this.player, new GuiSpeech(this.plugin, this.player, speech));
                                show();
                            } else {
                                this.attachedSpeech.setAttachedSpeech(speech.getUniqueId());
                                if (this.attachedSpeech instanceof Objective) {
                                    this.guiManager.showGUI(this.player, new GuiObjective(this.plugin, this.player, (Objective) this.attachedSpeech));
                                } else {
                                    this.guiManager.showGUI(this.player, new GuiRequirements(this.plugin, this.player, ((Requirement) this.attachedSpeech).getObjective()));
                                }
                            }
                        }
                    });
        }
    }
}
