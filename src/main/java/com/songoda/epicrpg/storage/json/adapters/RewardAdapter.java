package com.songoda.epicrpg.storage.json.adapters;

import com.google.gson.*;
import com.songoda.epicrpg.data.ActionDataStore;
import com.songoda.epicrpg.story.quest.reward.Reward;

import java.lang.reflect.Type;

public class RewardAdapter implements JsonSerializer<Reward> {

    @Override
    public JsonElement serialize(Reward reward, Type type, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("class", new JsonPrimitive(reward.getClass().getCanonicalName()));
        result.add("properties", context.serialize(reward, reward.getClass()));

        return result;
    }
}