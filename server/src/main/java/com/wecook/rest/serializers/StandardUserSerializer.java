package com.wecook.rest.serializers;

import com.google.gson.*;
import com.wecook.model.StandardUser;
import com.wecook.rest.utils.RequestParser;

import java.lang.reflect.Type;

public class StandardUserSerializer implements JsonSerializer<StandardUser> {

    @Override
    public JsonElement serialize(StandardUser standardUser, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();

        JsonArray jsonArray = new JsonArray();
        for (StandardUser.Allergy allergy : standardUser.getAllergies())  {
            jsonArray.add(String.valueOf(allergy));
        }
        jsonObject.add("allergies", jsonArray);
        jsonObject.addProperty("foodPreference", String.valueOf(standardUser.getFoodPreference()));
        jsonObject.addProperty("favoriteDish", standardUser.getFavoriteDish());
//        jsonObject.addProperty("following", standardUser.getFollowing().size());
//        jsonObject.addProperty("follower", standardUser.getFollowers().size());

        jsonObject.addProperty("id", standardUser.getId());
        jsonObject.addProperty("username", standardUser.getUsername());
        jsonObject.addProperty("email", standardUser.getEmail());

        jsonObject.add("picture", JsonNull.INSTANCE);
        if (standardUser.getProfilePicture() != null) {
            String profilePicture = RequestParser.byteArrayToBase64(standardUser.getProfilePicture());
            jsonObject.addProperty("picture", profilePicture);
        }

        return jsonObject;
    }
}
