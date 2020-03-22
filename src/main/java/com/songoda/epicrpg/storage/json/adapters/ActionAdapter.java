package com.songoda.epicrpg.storage.json.adapters;

import com.google.gson.*;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.story.quest.action.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Base64;

public class ActionAdapter implements JsonSerializer<Action>, JsonDeserializer<Action> {

    private final EpicRPG plugin;

    public ActionAdapter(EpicRPG plugin) {
        this.plugin = plugin;
    }

    @Override
    public Action deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        return plugin.getActionManager().getAction(jsonObject.get("type").getAsString());
    }

    @Override
    public JsonElement serialize(Action action, Type type, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("type", action.getType());
        return json;
    }
}