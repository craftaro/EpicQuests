package com.craftaro.epicrpg.storage.json.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.craftaro.epicrpg.EpicRPG;
import com.craftaro.epicrpg.story.Story;
import com.craftaro.epicrpg.story.quest.Objective;
import com.craftaro.epicrpg.story.quest.Quest;

import java.lang.reflect.Type;
import java.util.UUID;

public class ObjectiveAdapter implements JsonSerializer<Objective>, JsonDeserializer<Objective> {
    private final EpicRPG plugin;

    public ObjectiveAdapter(EpicRPG plugin) {
        this.plugin = plugin;
    }

    @Override
    public Objective deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        for (Story story : this.plugin.getStoryManager().getStories()) {
            for (Quest quest : story.getQuests()) {
                for (Objective objective : quest.getObjectives()) {
                    if (objective.getUniqueId().equals(UUID.fromString(jsonObject.get("uuid").getAsString()))) {
                        return objective;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public JsonElement serialize(Objective objective, Type type, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("uuid", objective.getUniqueId().toString());
        return json;
    }
}
