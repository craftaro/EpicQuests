package com.songoda.epicrpg.story.quest.requirement;

import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.dialog.AttachedSpeech;
import com.songoda.epicrpg.dialog.Speech;
import com.songoda.epicrpg.story.quest.Objective;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class AbstractRequirement implements Requirement, AttachedSpeech {

    private transient Objective objective;
    private UUID reject;

    public AbstractRequirement(Objective objective) {
        this.objective = objective;
    }

    @Override
    public Objective getObjective() {
        return objective;
    }

    public void setObjective(Objective objective) {
        this.objective = objective;
    }

    @Override
    public void reject(Player player) {
        Speech speech = EpicRPG.getInstance().getDialogManager().getSpeech(reject);
        if (speech == null) {
            player.sendMessage("You have not met the requirements to complete this objective...");
        } else {
            speech.sendMessages(player, speech.getDialog().getCitizen());
        }
    }

    @Override
    public UUID getAttachedSpeech() {
        return reject;
    }

    @Override
    public void setAttachedSpeech(UUID reject) {
        this.reject = reject;
    }
}
