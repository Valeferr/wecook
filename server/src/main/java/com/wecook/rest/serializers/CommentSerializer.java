package com.wecook.rest.serializers;

import com.google.gson.*;
import com.wecook.model.Comment;
import com.wecook.rest.utils.CustomGson;

import java.lang.reflect.Type;

public class CommentSerializer implements JsonSerializer<Comment> {
    @Override
    public JsonElement serialize(Comment comment, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        Gson gson = CustomGson.getInstance().getGson();

        jsonObject.addProperty("id", comment.getId());
        jsonObject.addProperty("text", comment.getText());
        jsonObject.addProperty("publicationDate", String.valueOf(comment.getPublicationDate()));
        jsonObject.add("user", gson.toJsonTree(comment.getStandardUser()));

        return jsonObject;
    }
}
