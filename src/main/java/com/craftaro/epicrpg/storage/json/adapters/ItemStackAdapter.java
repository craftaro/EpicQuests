package com.craftaro.epicrpg.storage.json.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Base64;

public class ItemStackAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {
    @Override
    public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        try (BukkitObjectInputStream stream = new BukkitObjectInputStream(
                new ByteArrayInputStream(Base64.getDecoder().decode(jsonObject.get("base64").getAsString())))) {
            return (ItemStack) stream.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public JsonElement serialize(ItemStack itemStack, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream(); BukkitObjectOutputStream bukkitStream = new BukkitObjectOutputStream(stream)) {

            bukkitStream.writeObject(itemStack);

            json.addProperty("base64", Base64.getEncoder().encodeToString(stream.toByteArray()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;
    }
}
