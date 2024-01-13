package com.craftaro.epicrpg.gui;

import com.craftaro.core.gui.Gui;
import com.craftaro.core.gui.GuiUtils;
import com.craftaro.core.input.ChatPrompt;
import com.craftaro.third_party.com.cryptomorin.xseries.XMaterial;
import com.craftaro.epicrpg.EpicRPG;
import com.craftaro.epicrpg.story.quest.reward.rewards.CommandReward;
import com.craftaro.epicrpg.utils.TextUtils;
import org.bukkit.entity.Player;

import java.util.List;

public class GuiCommands extends Gui {
    private EpicRPG plugin;
    private Player player;
    private final CommandReward reward;

    public GuiCommands(EpicRPG plugin, Player player, CommandReward reward) {
        this.reward = reward;
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
        reset();

        setButton(0, 0, GuiUtils.createButtonItem(XMaterial.GREEN_DYE, "Add command"),
                (event) -> ChatPrompt.showPrompt(this.plugin, this.player,
                                "Enter a Command. (You can use: @p)",
                                response -> this.reward.addCommand(response.getMessage()))
                        .setOnClose(() -> this.guiManager.showGUI(this.player, new GuiCommands(this.plugin, this.player, this.reward))));

        setButton(0, 8, GuiUtils.createButtonItem(XMaterial.OAK_DOOR, "Back"),
                (event) -> this.guiManager.showGUI(this.player, new GuiRewards(this.plugin, this.player, this.reward.getQuest())));

        List<String> commands = this.reward.getCommands();
        for (int i = 0; i < commands.size(); i++) {
            String command = commands.get(i);

            setButton(i + 9, GuiUtils.createButtonItem(XMaterial.PAPER, TextUtils.condense(command)),
                    (event) -> {
                        this.reward.removeCommand(command);
                        show();
                    });
        }
    }
}
