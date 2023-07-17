package com.craftaro.epicrpg.storage.json;

import com.craftaro.epicrpg.storage.json.adapters.ActionAdapter;
import com.craftaro.epicrpg.storage.json.adapters.ActionDataStoreAdapter;
import com.craftaro.epicrpg.storage.json.adapters.ItemStackAdapter;
import com.craftaro.epicrpg.storage.json.adapters.LocationAdapter;
import com.craftaro.epicrpg.storage.json.adapters.ObjectiveAdapter;
import com.craftaro.epicrpg.storage.json.adapters.RequirementAdapter;
import com.craftaro.epicrpg.storage.json.adapters.RewardAdapter;
import com.craftaro.epicrpg.story.contender.StoryPlayer;
import com.craftaro.epicrpg.story.quest.Objective;
import com.craftaro.epicrpg.story.quest.action.Action;
import com.craftaro.epicrpg.story.quest.action.ActiveAction;
import com.craftaro.epicrpg.story.quest.requirement.Requirement;
import com.craftaro.epicrpg.story.quest.reward.Reward;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.craftaro.epicrpg.EpicRPG;
import com.craftaro.epicrpg.data.ActionDataStore;
import com.craftaro.epicrpg.dialog.Dialog;
import com.craftaro.epicrpg.story.Story;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
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

        this.gsonActions = new GsonBuilder().registerTypeAdapter(Action.class, new ActionAdapter(plugin))
                .registerTypeAdapter(Objective.class, new ObjectiveAdapter(plugin))
                .registerTypeAdapter(Location.class, new LocationAdapter())
                .registerTypeAdapter(ActionDataStore.class, deserializer)
                .registerTypeAdapter(ActionDataStore.class, new ActionDataStoreAdapter())
                .registerTypeHierarchyAdapter(ItemStack.class, new ItemStackAdapter())
                .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                .enableComplexMapKeySerialization()
                .setPrettyPrinting().create();

        this.gsonStories = new GsonBuilder().enableComplexMapKeySerialization()
                .registerTypeAdapter(Reward.class, deserializer)
                .registerTypeAdapter(Reward.class, new RewardAdapter())
                .registerTypeAdapter(Requirement.class, deserializer)
                .registerTypeAdapter(Requirement.class, new RequirementAdapter())
                .registerTypeAdapter(Location.class, new LocationAdapter())
                .registerTypeHierarchyAdapter(ItemStack.class, new ItemStackAdapter())
                .excludeFieldsWithModifiers(Modifier.TRANSIENT).setPrettyPrinting().create();

        this.gsonPlayers = new GsonBuilder().enableComplexMapKeySerialization()
                .excludeFieldsWithModifiers(Modifier.TRANSIENT).setPrettyPrinting().create();

        this.gsonDialog = new GsonBuilder().enableComplexMapKeySerialization()
                .excludeFieldsWithModifiers(Modifier.TRANSIENT).setPrettyPrinting().create();

    }

    public void loadActions() {
        File dir = new File(this.actionsDir);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File file : directoryListing) {
                if (!file.getName().endsWith(".json")) {
                    continue;
                }
                try {
                    JsonReader reader = new JsonReader(new FileReader(file.getPath()));

                    ActiveAction action = this.gsonActions.fromJson(reader, ActiveAction.class);
                    this.actionsLast.add(action.getUniqueId().toString());

                    this.plugin.getActionManager().addActiveAction(action);
                    reader.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void saveActions() {
        List<String> added = new ArrayList<>();

        File dir = new File(this.actionsDir);
        dir.mkdir();

        // Save to file
        for (ActiveAction action : this.plugin.getActionManager().getActiveActions()) {

            File file = new File(dir + File.separator + action.getUniqueId() + ".json");
            added.add(action.getUniqueId().toString());

            try (Writer writer = new FileWriter(file.getPath())) {
                this.gsonActions.toJson(action, writer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (String string : this.actionsLast) {
            if (!added.contains(string)) {
                File file = new File(dir + File.separator + string + ".json");
                if (!file.delete()) {
                    System.out.println("Delete failed.");
                }
            }
        }
        this.actionsLast.clear();
        this.actionsLast.addAll(added);
    }

    public void loadStories() {
        File dir = new File(this.storiesDir);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File file : directoryListing) {
                if (!file.getName().endsWith(".json")) {
                    continue;
                }
                try {
                    this.storiesLast.add(file.getName().replace(".json", ""));
                    JsonReader reader = new JsonReader(new FileReader(file.getPath()));

                    Story story = this.gsonStories.fromJson(reader, Story.class);

                    this.plugin.getStoryManager().addStory(story);
                    reader.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void saveStories() {
        List<String> added = new ArrayList<>();

        File dir = new File(this.storiesDir);
        dir.mkdir();

        // Save to file
        for (Story story : this.plugin.getStoryManager().getStories()) {

            added.add(story.getUniqueId().toString());

            File file = new File(dir + File.separator + story.getUniqueId() + ".json");

            try (Writer writer = new FileWriter(file.getPath())) {
                this.gsonStories.toJson(story, writer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (String string : this.storiesLast) {
            if (!added.contains(string)) {
                File file = new File(dir + File.separator + string + ".json");
                if (!file.delete()) {
                    System.out.println("Delete failed.");
                }
            }
        }
        this.storiesLast.clear();
        this.storiesLast.addAll(added);
    }

    public void loadDialogs() {
        File dir = new File(this.dialogDir);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File file : directoryListing) {
                if (!file.getName().endsWith(".json")) {
                    continue;
                }
                try {
                    this.dialogsLast.add(file.getName().replace(".json", ""));
                    JsonReader reader = new JsonReader(new FileReader(file.getPath()));

                    Dialog dialog = this.gsonDialog.fromJson(reader, Dialog.class);

                    this.plugin.getDialogManager().addDialog(dialog);
                    reader.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void saveDialogs() {
        List<String> added = new ArrayList<>();

        File dir = new File(this.dialogDir);
        dir.mkdir();

        // Save to file
        for (Dialog dialog : this.plugin.getDialogManager().getDialogs()) {

            added.add(String.valueOf(dialog.getCitizenId()));

            File file = new File(dir + File.separator + dialog.getCitizenId() + ".json");

            try (Writer writer = new FileWriter(file.getPath())) {
                this.gsonDialog.toJson(dialog, writer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (String string : this.dialogsLast) {
            if (!added.contains(string)) {
                File file = new File(dir + File.separator + string + ".json");
                if (!file.delete()) {
                    System.out.println("Delete failed.");
                }
            }
        }
        this.dialogsLast.clear();
        this.dialogsLast.addAll(added);
    }

    public void loadPlayers() {
        File dir = new File(this.playersDir);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File file : directoryListing) {
                if (!file.getName().endsWith(".json")) {
                    continue;
                }
                try {
                    this.playersLast.add(file.getName().replace(".json", ""));
                    JsonReader reader = new JsonReader(new FileReader(file.getPath()));

                    StoryPlayer player = this.gsonPlayers.fromJson(reader, StoryPlayer.class);

                    this.plugin.getContendentManager().addPlayer(player);
                    reader.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void savePlayers() {
        List<String> added = new ArrayList<>();

        File dir = new File(this.playersDir);
        dir.mkdir();

        // Save to file
        for (StoryPlayer player : this.plugin.getContendentManager().getPlayers()) {

            added.add(player.getUniqueId().toString());

            File file = new File(dir + File.separator + player.getUniqueId() + ".json");

            try (Writer writer = new FileWriter(file.getPath())) {
                this.gsonPlayers.toJson(player, writer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (String string : this.playersLast) {
            if (!added.contains(string)) {
                File file = new File(dir + File.separator + string + ".json");
                if (!file.delete()) {
                    System.out.println("Delete failed.");
                }
            }
        }
        this.playersLast.clear();
        this.playersLast.addAll(added);
    }
}
