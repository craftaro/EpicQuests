package com.songoda.epicrpg.data;

import com.songoda.core.input.ChatPrompt;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.gui.GuiObjective;
import com.songoda.epicrpg.story.quest.Objective;
import com.songoda.epicrpg.story.quest.action.ActiveAction;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class ActionDataStore {

    private final Objective objective;
    private UUID setter;

    public ActionDataStore(Objective objective) {
        this.objective = objective;
    }

    public Objective getObjective() {
        return objective;
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
        if (this.setter == null) return false;
        return this.setter.equals(setter);
    }

    public void finishSetup(EpicRPG plugin, Player player, ActiveAction activeAction) {
        setter = null;

        ChatPrompt.showPrompt(plugin, player,
                "Enter a required amount.",
                response -> {
                    activeAction.setAmount(Integer.parseInt(response.getMessage()));
                    plugin.getGuiManager().showGUI(player, new GuiObjective(plugin, player, getObjective()));
                });
    }
}
