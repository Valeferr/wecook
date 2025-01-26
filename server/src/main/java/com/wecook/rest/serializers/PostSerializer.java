package com.wecook.rest.serializers;

import com.google.gson.*;
import com.wecook.model.Post;
import com.wecook.rest.utils.CustomGson;

import java.lang.reflect.Type;

public class PostSerializer implements JsonSerializer<Post> {

    @Override
    public JsonElement serialize(Post post, Type type, JsonSerializationContext jsonSerializationContext) {
        CustomGson customGson = CustomGson.getInstance();
        JsonObject jsonObject = customGson.getGson().toJsonTree(post).getAsJsonObject();

        jsonObject.remove("recipe");
        jsonObject.addProperty("recipe", post.getRecipe().getId());

        return jsonObject;
    }
}
