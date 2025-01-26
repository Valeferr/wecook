package com.wecook.rest.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wecook.rest.serializers.LocalDateSerializer;
import com.wecook.rest.serializers.PostSerializer;

import java.time.LocalDate;

public class CustomGson {
    private final Gson gson;

    private static CustomGson instance = null;

    private CustomGson() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                .registerTypeAdapter(PostSerializer.class, new PostSerializer())
                .excludeFieldsWithoutExposeAnnotation()
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
