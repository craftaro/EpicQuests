package com.songoda.epicrpg.storage.json;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.data.ActionDataStore;
import com.songoda.epicrpg.dialog.Dialog;
import com.songoda.epicrpg.storage.json.adapters.*;
import com.songoda.epicrpg.story.Story;
import com.songoda.epicrpg.story.contender.StoryPlayer;
import com.songoda.epicrpg.story.quest.Objective;
import com.songoda.epicrpg.story.quest.action.Action;
import com.songoda.epicrpg.story.quest.action.ActiveAction;
import com.songoda.epicrpg.story.quest.requirement.Requirement;
import com.songoda.epicrpg.story.quest.reward.Reward;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class JsonStorage {

    private final EpicRPG plugin;

    private final String actionsDir;
    private final String storiesDir;
    private final String playersDir;
    private final String dialogDir;

    private final Gson gsonActions;
    private final Gson gsonStories;
    private final Gson gsonPlayers;
    private final Gson gsonDialog;

    private final List<String> actionsLast = new ArrayList<>();
    private final List<String> storiesLast = new ArrayList<>();
    private final List<String> playersLast = new ArrayList<>();
    private final List<String> dialogsLast = new ArrayList<>();

    public JsonStorage(EpicRPG plugin) {
        this.plugin = plugin;
        String savesDir = plugin.getDataFolder() + File.separator + "saves";
        File dir = new File(savesDir);
        dir.mkdir();
        this.actionsDir = savesDir + File.separator + "actions";
        this.storiesDir = savesDir + File.separator + "stories";
        this.playersDir = savesDir + File.separator + "players";
        this.dialogDir = savesDir + File.separator + "dialogs";

        JsonDeserializer<?> deserializer = (json, typeOfT, context) -> {
            JsonObject jsonObject = json.getAsJsonObject();
            String clazz = jsonObject.get("class").getAsString();
            JsonElement element = jsonObject.get("properties");

            try {
                return context.deserialize(element, Class.forName(clazz));
            } catch (ClassNotFoundException cnfe) {
                try {
                    return context.deserialize(element, Class.forName(clazz.replaceAll("^(.*)\\.(.*)$", "$1\\$$2")));
                } catch (ClassNotFoundException cnfe2) {
                    cnfe2.printStackTrace();
                    throw new JsonParseException(cnfe.getCause());
                }
            }
        };

        gsonActions = new GsonBuilder().registerTypeAdapter(Action.class, new ActionAdapter(plugin))
                .registerTypeAdapter(Objective.class, new ObjectiveAdapter(plugin))
                .registerTypeAdapter(Location.class, new LocationAdapter())
                .registerTypeAdapter(ActionDataStore.class, deserializer)
                .registerTypeAdapter(ActionDataStore.class, new ActionDataStoreAdapter())
                .registerTypeHierarchyAdapter(ItemStack.class, new ItemStackAdapter())
                .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                .enableComplexMapKeySerialization()
                .setPrettyPrinting().create();

        gsonStories = new GsonBuilder().enableComplexMapKeySerialization()
                .registerTypeAdapter(Reward.class, deserializer)
                .registerTypeAdapter(Reward.class, new RewardAdapter())
                .registerTypeAdapter(Requirement.class, deserializer)
                .registerTypeAdapter(Requirement.class, new RequirementAdapter())
                .registerTypeAdapter(Location.class, new LocationAdapter())
                .registerTypeHierarchyAdapter(ItemStack.class, new ItemStackAdapter())
                .excludeFieldsWithModifiers(Modifier.TRANSIENT).setPrettyPrinting().create();

        gsonPlayers = new GsonBuilder().enableComplexMapKeySerialization()
                .excludeFieldsWithModifiers(Modifier.TRANSIENT).setPrettyPrinting().create();

        gsonDialog = new GsonBuilder().enableComplexMapKeySerialization()
                .excludeFieldsWithModifiers(Modifier.TRANSIENT).setPrettyPrinting().create();

    }

    public void loadActions() {
        File dir = new File(actionsDir);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File file : directoryListing) {
                if (!file.getName().endsWith(".json")) continue;
                try {
                    JsonReader reader = new JsonReader(new FileReader(file.getPath()));

                    ActiveAction action = gsonActions.fromJson(reader, ActiveAction.class);
                    actionsLast.add(action.getUniqueId().toString());

                    plugin.getActionManager().addActiveAction(action);
                    reader.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void saveActions() {
        List<String> added = new ArrayList<>();

        File dir = new File(actionsDir);
        dir.mkdir();

        // Save to file
        for (ActiveAction action : plugin.getActionManager().getActiveActions()) {

            File file = new File(dir + File.separator + action.getUniqueId() + ".json");
            added.add(action.getUniqueId().toString());

            try (Writer writer = new FileWriter(file.getPath())) {
                gsonActions.toJson(action, writer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (String string : actionsLast) {
            if (!added.contains(string)) {
                File file = new File(dir + File.separator + string + ".json");
                if (!file.delete()) {
                    System.out.println("Delete failed.");
                }
            }
        }
        actionsLast.clear();
        actionsLast.addAll(added);
    }

    public void loadStories() {
        File dir = new File(storiesDir);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File file : directoryListing) {
                if (!file.getName().endsWith(".json")) continue;
                try {
                    storiesLast.add(file.getName().replace(".json", ""));
                    JsonReader reader = new JsonReader(new FileReader(file.getPath()));

                    Story story = gsonStories.fromJson(reader, Story.class);

                    plugin.getStoryManager().addStory(story);
                    reader.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void saveStories() {
        List<String> added = new ArrayList<>();

        File dir = new File(storiesDir);
        dir.mkdir();

        // Save to file
        for (Story story : plugin.getStoryManager().getStories()) {

            added.add(story.getUniqueId().toString());

            File file = new File(dir + File.separator + story.getUniqueId() + ".json");

            try (Writer writer = new FileWriter(file.getPath())) {
                gsonStories.toJson(story, writer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (String string : storiesLast) {
            if (!added.contains(string)) {
                File file = new File(dir + File.separator + string + ".json");
                if (!file.delete()) {
                    System.out.println("Delete failed.");
                }
            }
        }
        storiesLast.clear();
        storiesLast.addAll(added);
    }

    public void loadDialogs() {
        File dir = new File(dialogDir);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File file : directoryListing) {
                if (!file.getName().endsWith(".json")) continue;
                try {
                    dialogsLast.add(file.getName().replace(".json", ""));
                    JsonReader reader = new JsonReader(new FileReader(file.getPath()));

                    Dialog dialog = gsonDialog.fromJson(reader, Dialog.class);

                    plugin.getDialogManager().addDialog(dialog);
                    reader.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void saveDialogs() {
        List<String> added = new ArrayList<>();

        File dir = new File(dialogDir);
        dir.mkdir();

        // Save to file
        for (Dialog dialog : plugin.getDialogManager().getDialogs()) {

            added.add(String.valueOf(dialog.getCitizenId()));

            File file = new File(dir + File.separator + dialog.getCitizenId() + ".json");

            try (Writer writer = new FileWriter(file.getPath())) {
                gsonDialog.toJson(dialog, writer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (String string : dialogsLast) {
            if (!added.contains(string)) {
                File file = new File(dir + File.separator + string + ".json");
                if (!file.delete()) {
                    System.out.println("Delete failed.");
                }
            }
        }
        dialogsLast.clear();
        dialogsLast.addAll(added);
    }

    public void loadPlayers() {
        File dir = new File(playersDir);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File file : directoryListing) {
                if (!file.getName().endsWith(".json")) continue;
                try {
                    playersLast.add(file.getName().replace(".json", ""));
                    JsonReader reader = new JsonReader(new FileReader(file.getPath()));

                    StoryPlayer player = gsonPlayers.fromJson(reader, StoryPlayer.class);

                    plugin.getContendentManager().addPlayer(player);
                    reader.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void savePlayers() {
        List<String> added = new ArrayList<>();

        File dir = new File(playersDir);
        dir.mkdir();

        // Save to file
        for (StoryPlayer player : plugin.getContendentManager().getPlayers()) {

            added.add(player.getUniqueId().toString());

            File file = new File(dir + File.separator + player.getUniqueId() + ".json");

            try (Writer writer = new FileWriter(file.getPath())) {
                gsonPlayers.toJson(player, writer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (String string : playersLast) {
            if (!added.contains(string)) {
                File file = new File(dir + File.separator + string + ".json");
                if (!file.delete()) {
                    System.out.println("Delete failed.");
                }
            }
        }
        playersLast.clear();
        playersLast.addAll(added);
    }
}
