package com.wecook.rest.serializers;

import com.google.gson.*;
import com.wecook.model.Recipe;
import com.wecook.model.RecipeIngredient;
import com.wecook.model.Step;
import com.wecook.rest.utils.CustomGson;

import java.lang.reflect.Type;

public class RecipeSerializer implements JsonSerializer<Recipe> {
    @Override
    public JsonElement serialize(Recipe recipe, Type type, JsonSerializationContext jsonSerializationContext) {
        CustomGson customGson = CustomGson.getInstance();
        JsonObject jsonObject = new JsonObject();


        jsonObject.addProperty("id", recipe.getId());
        jsonObject.addProperty("title", recipe.getTitle());
        jsonObject.addProperty("description", recipe.getDescription());
        jsonObject.addProperty("difficulty", String.valueOf(recipe.getDifficulty()));
        jsonObject.addProperty("category", String.valueOf(recipe.getCategory()));
        JsonArray steps = new JsonArray();
        for (Step step : recipe.getSteps()) {
            JsonObject jsonStep = customGson.getGson().toJsonTree(step).getAsJsonObject();
            steps.add(jsonStep);
        }
        jsonObject.add("steps", steps);

        return jsonObject;
    }
}
