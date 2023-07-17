package com.songoda.epicrpg.storage.json.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.songoda.epicrpg.data.ActionDataStore;

import java.lang.reflect.Type;

public class ActionDataStoreAdapter implements JsonSerializer<ActionDataStore> {
    @Override
    public JsonElement serialize(ActionDataStore dataStore, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("class", new JsonPrimitive(dataStore.getClass().getCanonicalName()));
        result.add("properties", context.serialize(dataStore, dataStore.getClass()));

        return result;
    }
}
