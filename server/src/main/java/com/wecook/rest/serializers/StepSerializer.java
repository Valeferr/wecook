package com.wecook.rest.serializers;

import com.google.gson.*;
import com.wecook.model.RecipeIngredient;
import com.wecook.model.Step;
import com.wecook.rest.utils.CustomGson;

import java.lang.reflect.Type;
import java.util.List;

public class StepSerializer implements JsonSerializer<Step> {
    @Override
    public JsonElement serialize(Step step, Type type, JsonSerializationContext jsonSerializationContext) {
        CustomGson customGson = CustomGson.getInstance();
        JsonObject jsonObject = customGson.getGson().toJsonTree(step).getAsJsonObject();
        JsonArray jsonArray = new JsonArray();

        List<RecipeIngredient> recipeIngredients = step.getIngredients();
        for(RecipeIngredient recipeIngredient : recipeIngredients) {
            JsonObject recipeIngredientJsonObject = customGson.getGson().toJsonTree(recipeIngredient).getAsJsonObject();
            recipeIngredientJsonObject.addProperty("recipeIngredientId", recipeIngredient.getId());

            jsonArray.add(recipeIngredientJsonObject);
        }
        jsonObject.remove("ingredients");

        jsonObject.addProperty("ingredients", String.valueOf(jsonArray));
        return jsonObject;
    }
}
