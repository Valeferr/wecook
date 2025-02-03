package com.wecook.rest.serializers;

import com.google.gson.*;
import com.wecook.model.Post;
import com.wecook.model.StandardUser;

import java.lang.reflect.Type;
import java.util.Base64;

public class PostSerializer implements JsonSerializer<Post> {

    @Override
    public JsonElement serialize(Post post, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", post.getId());

        StandardUser standardUser = post.getStandardUser();
        jsonObject.addProperty("username", standardUser.getUsername());
        //TODO: Remuovi quando hai la foto
        if (standardUser.getProfilePicture() != null) {
            String profilePicture = Base64.getEncoder().encodeToString(standardUser.getProfilePicture());
            jsonObject.addProperty("profilePicture", profilePicture);
        }

        if (post.getRecipe() != null) {
            jsonObject.addProperty("recipeId", post.getRecipe().getId());
            jsonObject.addProperty("description", post.getRecipe().getDescription());
        }

        byte[] postPicture = post.getPostPicture();
        String postPictureEncoded = Base64.getEncoder().encodeToString(postPicture);
        jsonObject.addProperty("postPicture", postPictureEncoded);

        jsonObject.addProperty("saved", false);
        jsonObject.addProperty("likes", post.getLikes().size());

        return jsonObject;
    }
}
