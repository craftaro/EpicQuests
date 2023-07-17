package com.craftaro.epicrpg.storage.json.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.craftaro.epicrpg.EpicRPG;
import com.craftaro.epicrpg.story.quest.action.Action;

import java.lang.reflect.Type;

public class ActionAdapter implements JsonSerializer<Action>, JsonDeserializer<Action> {
    private final EpicRPG plugin;

    public ActionAdapter(EpicRPG plugin) {
        this.plugin = plugin;
    }

    @Override
    public Action deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        return this.plugin.getActionManager().getAction(jsonObject.get("type").getAsString());
    }

    @Override
    public JsonElement serialize(Action action, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("type", action.getType());
        return json;
    }
}
