package com.wecook.rest.serializers;

import com.google.gson.*;
import com.wecook.model.Comment;
import com.wecook.model.Like;
import com.wecook.model.Post;
import com.wecook.rest.CommentResource;
import com.wecook.rest.utils.CustomGson;

import java.lang.reflect.Type;
import java.util.Base64;
import java.util.Set;

public class PostSerializer implements JsonSerializer<Post> {

    @Override
    public JsonElement serialize(Post post, Type type, JsonSerializationContext jsonSerializationContext) {
        CustomGson customGson = CustomGson.getInstance();
        JsonObject jsonObject = customGson.getGson().toJsonTree(post).getAsJsonObject();

        Set<Comment> comments = post.getComments();
        JsonArray commentJsonArray = new JsonArray();
        for (Comment comment : comments) {
            JsonObject commentJsonObject = customGson.getGson().toJsonTree(comment).getAsJsonObject();
            commentJsonObject.addProperty("commentId", comment.getId());
            commentJsonArray.add(commentJsonObject);
        }
        Set<Like> likes = post.getLikes();
        JsonArray likeJsonArray = new JsonArray();
        for (Like like : likes) {
            JsonObject likeJsonObject = customGson.getGson().toJsonTree(like).getAsJsonObject();
            likeJsonObject.addProperty("likeId", like.getId());
            likeJsonArray.add(likeJsonObject);
        }


        jsonObject.remove("recipe");
        jsonObject.addProperty("recipe", post.getRecipe().getId());

        byte[] postPicture = post.getPostPicture();
        String postPictureEncoded = Base64.getEncoder().encodeToString(postPicture);
        jsonObject.remove("postPicture");
        jsonObject.addProperty("postPicture", postPictureEncoded);

        jsonObject.remove("comments");
        jsonObject.addProperty("comments", Integer.valueOf(String.valueOf(commentJsonArray)));

        jsonObject.remove("likes");
        jsonObject.addProperty("likes", Integer.valueOf(String.valueOf(likeJsonArray)));

        return jsonObject;
    }
}
