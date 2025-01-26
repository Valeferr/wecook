package com.wecook.rest.serializers;

import com.google.gson.*;
import com.wecook.model.Post;
import com.wecook.rest.utils.CustomGson;

import java.lang.reflect.Type;
import java.util.Base64;

public class PostSerializer implements JsonSerializer<Post> {

    @Override
    public JsonElement serialize(Post post, Type type, JsonSerializationContext jsonSerializationContext) {
        CustomGson customGson = CustomGson.getInstance();
        JsonObject jsonObject = customGson.getGson().toJsonTree(post).getAsJsonObject();

        jsonObject.remove("recipe");
        jsonObject.addProperty("recipe", post.getRecipe().getId());

        byte[] postPicture = post.getPostPicture();
        String postPictureEncoded = Base64.getEncoder().encodeToString(postPicture);
        jsonObject.remove("postPicture");
        jsonObject.addProperty("postPicture", postPictureEncoded);

        return jsonObject;
    }
}
