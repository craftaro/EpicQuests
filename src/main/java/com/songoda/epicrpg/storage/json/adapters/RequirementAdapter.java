package com.songoda.epicrpg.storage.json.adapters;

import com.google.gson.*;
import com.songoda.epicrpg.story.quest.requirement.Requirement;
import com.songoda.epicrpg.story.quest.reward.Reward;

import java.lang.reflect.Type;

public class RequirementAdapter implements JsonSerializer<Requirement> {

    @Override
    public JsonElement serialize(Requirement requirement, Type type, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("class", new JsonPrimitive(requirement.getClass().getCanonicalName()));
        result.add("properties", context.serialize(requirement, requirement.getClass()));

        return result;
    }
}