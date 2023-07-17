package com.songoda.epicrpg.dialog;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DialogManager {
    private final Map<Integer, Dialog> registeredDialog = new LinkedHashMap<>();

    public Dialog getDialog(int citizenId) {
        return this.registeredDialog.get(citizenId);
    }

    public List<Dialog> getDialogs() {
        return new LinkedList<>(this.registeredDialog.values());
    }

    public Dialog addDialog(int citizenId) {
        Dialog dialog = new Dialog(citizenId);
        this.registeredDialog.put(citizenId, dialog);
        return dialog;
    }

    public void addDialog(Dialog dialog) {
        for (Speech speech : dialog.getMessages()) {
            speech.setDialog(dialog);
        }
        this.registeredDialog.put(dialog.getCitizenId(), dialog);
    }

    public void removeDialog(Dialog dialog) {
        this.registeredDialog.remove(dialog);
    }

    public Speech getSpeech(UUID uniqueId) {
        for (Dialog dialog : this.registeredDialog.values()) {
            for (Speech speech : dialog.getMessages()) {
                if (speech.getUniqueId().equals(uniqueId)) {
                    return speech;
                }
            }
        }
        return null;
    }
}
