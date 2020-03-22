package com.songoda.epicrpg.gui;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.gui.Gui;
import com.songoda.core.gui.GuiUtils;
import com.songoda.core.input.ChatPrompt;
import com.songoda.core.utils.TextUtils;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.dialog.Speech;
import com.songoda.epicrpg.story.quest.Objective;
import com.songoda.epicrpg.story.quest.action.Action;
import com.songoda.epicrpg.story.quest.action.ActionManager;
import com.songoda.epicrpg.story.quest.action.ActiveAction;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GuiObjective extends Gui {

    private final EpicRPG plugin;
    private final Player player;
    private final ActionManager actionManager;
    private final Objective objective;

    public GuiObjective(EpicRPG plugin, Player player, Objective objective) {
        this.plugin = plugin;
        this.player = player;
        actionManager = plugin.getActionManager();
        this.objective = objective;
        setRows(6);
        setDefaultItem(null);

        setTitle(objective.getTitle());

        show();
    }

    public void show() {
        if (inventory != null)
            inventory.clear();
        setActionForRange(0, 53, null);

        setButton(0, 0, GuiUtils.createButtonItem(CompatibleMaterial.GREEN_DYE, "Retitle Objective"),
                (event) -> {
                    ChatPrompt.showPrompt(plugin, player,
                            "Enter a title.",
                            response -> objective.setTitle(response.getMessage()))
                            .setOnClose(() -> guiManager.showGUI(player, new GuiObjective(plugin, player, objective)));
                });

        setButton(0, 1, GuiUtils.createButtonItem(CompatibleMaterial.BLUE_DYE, "Add Action"),
                (event) -> {
                    guiManager.showGUI(player, new GuiActionTypes(plugin, player, objective));
                });

        setButton(0, 2, GuiUtils.createButtonItem(CompatibleMaterial.PINK_DYE, "Modify Requirements"),
                (event) -> {
                    guiManager.showGUI(player, new GuiRequirements(plugin, player, objective));
                    show();
                });

        Speech speech = plugin.getDialogManager().getSpeech(objective.getAttachedSpeech());
        setButton(0, 3, GuiUtils.createButtonItem(CompatibleMaterial.RED_DYE, "Attach Speech",
                TextUtils.formatText("&fAttached too: &6" + (speech == null ? "NONE" : speech.getDialog().getCitizen().getName()))),
                (event) -> {
                    guiManager.showGUI(player, new GuiDialogs(plugin, player, objective));
                    show();
                });

        setButton(0, 8, GuiUtils.createButtonItem(CompatibleMaterial.OAK_DOOR, "Back"),
                (event) -> {
                    guiManager.showGUI(player, new GuiQuest(plugin, player, objective.getQuest()));
                });


        List<ActiveAction> actions = actionManager.getActiveActions().stream()
                .filter(a -> a.getObjective() == objective).collect(Collectors.toList());
        for (int i = 0; i < actions.size(); i++) {
            ActiveAction activeAction = actions.get(i);
            Action action = activeAction.getAction();

            List<String> lore = new ArrayList<>(action.getDescription(activeAction.getActionDataStore()));
            lore.add(TextUtils.formatText("&fAmount: &6" + activeAction.getAmount()));
            lore.addAll(Arrays.asList("",
                    TextUtils.formatText("&fLeft-Click: &6to setup"),
                    TextUtils.formatText("&fRight-Click: &6to delete")));
            setButton(i + 9, GuiUtils.createButtonItem(CompatibleMaterial.PAPER,
                    action.getType(), lore),
                    (event) -> {
                        if (event.clickType == ClickType.LEFT) {
                            player.closeInventory();
                            actionManager.addActiveAction(action.setup(player, objective));
                            actionManager.removeActiveAction(activeAction);
                            show();
                        } else if (event.clickType == ClickType.RIGHT) {
                            actionManager.removeActiveAction(activeAction);
                            show();
                        }
                    });
        }
    }
}