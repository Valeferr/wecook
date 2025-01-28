package com.wecook.rest.serializers;

import com.google.gson.*;
import com.wecook.model.Recipe;
import com.wecook.model.Step;
import com.wecook.rest.utils.CustomGson;

import java.lang.reflect.Type;
import java.util.Set;

public class RecipeSerializer implements JsonSerializer<Recipe> {
    @Override
    public JsonElement serialize(Recipe recipe, Type type, JsonSerializationContext jsonSerializationContext) {
        CustomGson customGson = CustomGson.getInstance();
        JsonObject jsonObject = customGson.getGson().toJsonTree(recipe).getAsJsonObject();
        JsonArray jsonArray  = new JsonArray();

        Set<Step> steps = recipe.getSteps();
        for (Step step : steps) {
            JsonObject stepJsonObject = customGson.getGson().toJsonTree(step).getAsJsonObject();
            stepJsonObject.addProperty("stepId", step.getId());
            jsonArray.add(stepJsonObject);
        }
        jsonObject.remove("steps");

        jsonObject.addProperty("steps", String.valueOf(jsonArray));

        return jsonObject;
    }
}
