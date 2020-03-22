package com.songoda.epicrpg.gui;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.gui.Gui;
import com.songoda.core.gui.GuiUtils;
import com.songoda.core.input.ChatPrompt;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.story.quest.requirement.requirements.ItemRequirement;
import com.songoda.epicrpg.story.quest.reward.rewards.CommandReward;
import com.songoda.epicrpg.utils.TextUtils;
import org.bukkit.entity.Player;

import java.util.List;

public class GuiCommands extends Gui {

    private EpicRPG plugin;
    private Player player;
    private CommandReward reward;
    private ItemRequirement requirement;

    public GuiCommands(EpicRPG plugin, Player player, CommandReward reward) {
        this.reward = reward;
        init(plugin, player);
    }

    public GuiCommands(EpicRPG plugin, Player player, ItemRequirement requirement) {
        this.requirement = requirement;
        init(plugin, player);
    }

    public void init(EpicRPG plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        setRows(6);
        setDefaultItem(null);
        setAcceptsItems(true);

        setTitle("Commands");

        show();
    }

    public void show() {
        if (inventory != null)
            inventory.clear();
        setActionForRange(0, 53, null);

        setButton(0, 0, GuiUtils.createButtonItem(CompatibleMaterial.GREEN_DYE, "Add command"),
                (event) -> {
                    ChatPrompt.showPrompt(plugin, player,
                            "Enter a Command. (You can use: @p)",
                            response -> reward.addCommand(response.getMessage()))
                            .setOnClose(() -> guiManager.showGUI(player, new GuiCommands(plugin, player, reward)));
                });

        setButton(0, 8, GuiUtils.createButtonItem(CompatibleMaterial.OAK_DOOR, "Back"),
                (event) -> {
                    guiManager.showGUI(player, new GuiRewards(plugin, player, reward.getQuest()));
                });


        List<String> commands = reward.getCommands();
        for (int i = 0; i < commands.size(); i++) {
            String command = commands.get(i);

            setButton(i + 9, GuiUtils.createButtonItem(CompatibleMaterial.PAPER, TextUtils.condense(command)),
                    (event) -> {
                        reward.removeCommand(command);
                        show();
                    });
        }
    }
}