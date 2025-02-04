package com.wecook.rest.serializers;

import com.google.gson.*;
import com.wecook.model.Post;
import com.wecook.model.StandardUser;
import com.wecook.model.Step;
import com.wecook.rest.utils.RequestParser;

import java.lang.reflect.Type;
import java.util.Base64;

public class PostSerializer implements JsonSerializer<Post> {

    @Override
    public JsonElement serialize(Post post, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", post.getId());

        StandardUser standardUser = post.getStandardUser();
        jsonObject.addProperty("userUsername", standardUser.getUsername());
        jsonObject.addProperty("userId", standardUser.getId());

        jsonObject.add("userPicture", JsonNull.INSTANCE);
        if (standardUser.getProfilePicture() != null) {
            String profilePicture = Base64.getEncoder().encodeToString(standardUser.getProfilePicture());
            jsonObject.addProperty("userPicture", profilePicture);
        }

        jsonObject.add("recipeId", JsonNull.INSTANCE);
        jsonObject.add("description", JsonNull.INSTANCE);
        jsonObject.add("recipeDuration", JsonNull.INSTANCE);
        jsonObject.add("recipeDifficulty", JsonNull.INSTANCE);
        jsonObject.add("recipeCategory", JsonNull.INSTANCE);
        jsonObject.add("recipeTitle", JsonNull.INSTANCE);
        if (post.getRecipe() != null) {
            jsonObject.addProperty("recipeId", post.getRecipe().getId());
            jsonObject.addProperty("description", post.getRecipe().getDescription());
            jsonObject.addProperty("recipeDuration", post.getRecipe().getSteps().stream().mapToInt(Step::getDuration).sum());
            jsonObject.addProperty("recipeDifficulty", post.getRecipe().getDifficulty().toString());
            jsonObject.addProperty("recipeCategory", post.getRecipe().getCategory().toString());
            jsonObject.addProperty("recipeTitle", post.getRecipe().getTitle());
        }

        String postPictureEncoded = RequestParser.byteArrayToBase64(post.getPostPicture());
        jsonObject.addProperty("picture", postPictureEncoded);

        jsonObject.addProperty("saved", false);
        jsonObject.addProperty("liked", false);
        jsonObject.addProperty("likes", post.getLikes().size());

        return jsonObject;
    }
}
