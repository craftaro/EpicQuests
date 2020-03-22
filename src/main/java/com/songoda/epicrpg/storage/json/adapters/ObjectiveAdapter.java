package com.songoda.epicrpg.storage.json.adapters;

import com.google.gson.*;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.story.Story;
import com.songoda.epicrpg.story.quest.Objective;
import com.songoda.epicrpg.story.quest.Quest;

import java.lang.reflect.Type;
import java.util.UUID;

public class ObjectiveAdapter implements JsonSerializer<Objective>, JsonDeserializer<Objective> {

    private final EpicRPG plugin;

    public ObjectiveAdapter(EpicRPG plugin) {
        this.plugin = plugin;
    }

    @Override
    public Objective deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        for (Story story : plugin.getStoryManager().getStories())
            for (Quest quest : story.getQuests())
                for (Objective objective : quest.getObjectives())
                    if (objective.getUniqueId().equals(UUID.fromString(jsonObject.get("uuid").getAsString())))
                        return objective;
        return null;
    }

    @Override
    public JsonElement serialize(Objective objective, Type type, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("uuid", objective.getUniqueId().toString());
        return json;
    }
}