package com.songoda.epicrpg.dialog;

import java.util.*;

public class DialogManager {

    private final Map<Integer, Dialog> registeredDialog = new LinkedHashMap<>();

    public Dialog getDialog(int citizenId) {
        return registeredDialog.get(citizenId);
    }

    public List<Dialog> getDialogs() {
        return new LinkedList<>(registeredDialog.values());
    }

    public Dialog addDialog(int citizenId) {
        Dialog dialog = new Dialog(citizenId);
        registeredDialog.put(citizenId, dialog);
        return dialog;
    }

    public void addDialog(Dialog dialog) {
        for (Speech speech : dialog.getMessages())
            speech.setDialog(dialog);
        registeredDialog.put(dialog.getCitizenId(), dialog);
    }

    public void removeDialog(Dialog dialog) {
        registeredDialog.remove(dialog);
    }

    public Speech getSpeech(UUID uniqueId) {
        for (Dialog dialog : registeredDialog.values())
            for (Speech speech : dialog.getMessages())
                if (speech.getUniqueId().equals(uniqueId))
                    return speech;
        return null;
    }
}
