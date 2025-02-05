package com.wecook.rest.serializers;

import com.google.gson.*;
import com.wecook.model.RecipeIngredient;
import com.wecook.rest.utils.CustomGson;

import java.lang.reflect.Type;

public class RecipeIngredientSerializer implements JsonSerializer<RecipeIngredient> {
    @Override
    public JsonElement serialize(RecipeIngredient recipeIngredient, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();

        Gson gson = CustomGson.getInstance().getGson();
        jsonObject.addProperty("id", recipeIngredient.getId());
        jsonObject.addProperty("quantity", recipeIngredient.getQuantity());
        jsonObject.addProperty("measurementUnit", String.valueOf(recipeIngredient.getMeasurementUnit()));
        jsonObject.addProperty("step", recipeIngredient.getStep().getId());
        jsonObject.add("ingredient", gson.toJsonTree(recipeIngredient.getIngredient()));

        return jsonObject;
    }
}
