package com.craftaro.epicrpg.storage.json.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.craftaro.epicrpg.story.quest.requirement.Requirement;

import java.lang.reflect.Type;

public class RequirementAdapter implements JsonSerializer<Requirement> {
    @Override
    public JsonElement serialize(Requirement requirement, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("class", new JsonPrimitive(requirement.getClass().getCanonicalName()));
        result.add("properties", context.serialize(requirement, requirement.getClass()));

        return result;
    }
}
