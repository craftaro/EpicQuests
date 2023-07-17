package com.songoda.epicrpg.data;

import com.songoda.core.input.ChatPrompt;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.gui.GuiObjective;
import com.songoda.epicrpg.story.quest.Objective;
import com.songoda.epicrpg.story.quest.action.ActiveAction;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ActionDataStore {
    private final Objective objective;
    private UUID setter;

    public ActionDataStore(Objective objective) {
        this.objective = objective;
    }

    public Objective getObjective() {
        return this.objective;
    }

    public void startSetup(Entity setter) {
        startSetup(setter.getUniqueId());
    }

    public void startSetup(UUID setter) {
        this.setter = setter;
    }

    public boolean isBeingSetup(Entity entity) {
        return isBeingSetup(entity.getUniqueId());
    }

    public boolean isBeingSetup(UUID setter) {
        return this.setter != null && this.setter.equals(setter);
    }

    public void finishSetup(EpicRPG plugin, Player player, ActiveAction activeAction) {
        this.setter = null;

        ChatPrompt.showPrompt(plugin, player,
                "Enter a required amount.",
                response -> {
                    activeAction.setAmount(Integer.parseInt(response.getMessage()));
                    plugin.getGuiManager().showGUI(player, new GuiObjective(plugin, player, getObjective()));
                });
    }
}
