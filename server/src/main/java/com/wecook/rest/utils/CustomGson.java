package com.wecook.rest.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wecook.model.*;
import com.wecook.rest.serializers.*;

import java.time.LocalDate;

public class CustomGson {
    private final Gson gson;

    private static CustomGson instance = null;

    private CustomGson() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                .registerTypeAdapter(StandardUser.class, new StandardUserSerializer())
                .registerTypeAdapter(Post.class, new PostSerializer())
                .registerTypeAdapter(Recipe.class, new RecipeSerializer())
                .registerTypeAdapter(Step.class, new StepSerializer())
                .registerTypeAdapter(RecipeIngredient.class, new RecipeIngredientSerializer())
                .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls()
                .create();
    }

    public static CustomGson getInstance() {
        if (instance == null) {
            instance = new CustomGson();
        }
        return instance;
    }

    public Gson getGson() {
        return gson;
    }
}
