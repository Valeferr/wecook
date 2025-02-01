package com.wecook.rest.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.wecook.model.RecipeIngredient;
import com.wecook.rest.utils.CustomGson;

import java.lang.reflect.Type;

public class RecipeIngredientSerializer implements JsonSerializer<RecipeIngredient> {
    @Override
    public JsonElement serialize(RecipeIngredient recipeIngredient, Type type, JsonSerializationContext jsonSerializationContext) {
        CustomGson customGson = CustomGson.getInstance();
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", recipeIngredient.getId());
        jsonObject.addProperty("quantity", recipeIngredient.getQuantity());
        jsonObject.addProperty("measurementUnit", String.valueOf(recipeIngredient.getMeasurementUnit()));
        jsonObject.addProperty("step", recipeIngredient.getStep().getId());
        jsonObject.addProperty("ingredient", recipeIngredient.getIngredient().getId());

        return jsonObject;
    }
}
