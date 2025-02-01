package com.wecook.rest.serializers;

import com.google.gson.*;
import com.wecook.model.Step;
import com.wecook.rest.utils.CustomGson;

import java.lang.reflect.Type;

public class StepSerializer implements JsonSerializer<Step> {
    @Override
    public JsonElement serialize(Step step, Type type, JsonSerializationContext jsonSerializationContext) {
        CustomGson customGson = CustomGson.getInstance();
        JsonObject jsonObject = new JsonObject();


        jsonObject.addProperty("id", step.getId());
        jsonObject.addProperty("description", step.getDescription());
        jsonObject.addProperty("duration", step.getDuration());
        jsonObject.addProperty("action", String.valueOf(step.getAction()));
        jsonObject.addProperty("stepIndex", step.getStepIndex());

        return jsonObject;
    }
}
