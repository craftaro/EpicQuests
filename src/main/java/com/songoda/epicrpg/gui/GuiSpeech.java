package com.songoda.epicrpg.gui;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.gui.Gui;
import com.songoda.core.gui.GuiUtils;
import com.songoda.core.input.ChatPrompt;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.dialog.DialogManager;
import com.songoda.epicrpg.dialog.Speech;
import com.songoda.epicrpg.utils.TextUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Arrays;
import java.util.List;

public class GuiSpeech extends Gui {

    private final EpicRPG plugin;
    private final DialogManager dialogManager;
    private final Player player;
    private final Speech speech;

    public GuiSpeech(EpicRPG plugin, Player player, Speech speech) {
        this.plugin = plugin;
        this.dialogManager = plugin.getDialogManager();
        this.player = player;
        this.speech = speech;
        setRows(6);
        setDefaultItem(null);

        setTitle("Speech");

        show();
    }

    public void show() {
        if (inventory != null)
            inventory.clear();
        setActionForRange(0, 53, null);

        setButton(0, 0, GuiUtils.createButtonItem(CompatibleMaterial.GREEN_DYE, "Add Message"),
                (event) -> {
                    ChatPrompt.showPrompt(plugin, player,
                            "Enter a Message. You may use @p to include the players name.",
                            response -> speech.addMessage(response.getMessage()))
                            .setOnClose(() -> guiManager.showGUI(player, new GuiSpeech(plugin, player, speech)));
                });

        setButton(0, 1, GuiUtils.createButtonItem(CompatibleMaterial.RED_DYE,
                speech.isDefaultDialog() ? "Default" : "Not default"),
                (event) -> {
                    speech.setDefaultDialog(!speech.isDefaultDialog());
                    if (speech.isDefaultDialog())
                        speech.clearQuestPrerequisites();
                    show();
                });

        if (!speech.isDefaultDialog())
            setButton(0, 2, GuiUtils.createButtonItem(CompatibleMaterial.PURPLE_DYE, "Modify Prerequisites"),
                    (event) -> {
                        guiManager.showGUI(player, new GuiQuestPrereqs(plugin, player, speech));
                        show();
                    });

        setButton(0, 8, GuiUtils.createButtonItem(CompatibleMaterial.OAK_DOOR, "Back"),
                (event) -> {
                    guiManager.showGUI(player, new GuiDialog(plugin, player, speech.getDialog(), null));
                });

        List<String> messages = speech.getMessages();

        for (int i = 0; i < messages.size(); i++) {
            String message = messages.get(i);
            List<String> lore = TextUtils.condense(message);
            lore.addAll(Arrays.asList("", TextUtils.formatText("&fLeft-Click: &6to move to the end"), TextUtils.formatText("&fRight-Click: &6to delete")));
            setButton(i + 9, GuiUtils.createButtonItem(CompatibleMaterial.PAPER, lore),
                    (event) -> {
                        if (event.clickType == ClickType.RIGHT)
                            speech.removeMessage(message);
                        else if (event.clickType == ClickType.LEFT)
                            speech.moveMessageToEnd(message);
                        show();
                    });
        }
    }
}