package com.wecook.rest.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.wecook.model.Comment;

import java.lang.reflect.Type;

public class CommentSerializer implements JsonSerializer<Comment> {
    @Override
    public JsonElement serialize(Comment comment, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", comment.getId());
        jsonObject.addProperty("text", comment.getText());
        jsonObject.addProperty("publicationDate", String.valueOf(comment.getPublicationDate()));
        jsonObject.addProperty("userId", comment.getStandardUser().getId());

        return jsonObject;
    }
}
