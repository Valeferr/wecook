package com.wecook.rest.serializers;

import com.google.gson.*;
import com.wecook.model.StandardUser;
import com.wecook.model.enums.FoodCategories;
import com.wecook.rest.utils.RequestParser;

import java.lang.reflect.Type;
import java.util.Set;

public class StandardUserSerializer implements JsonSerializer<StandardUser> {

    @Override
    public JsonElement serialize(StandardUser standardUser, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();

        JsonArray jsonArray = new JsonArray();

        if (standardUser.getAllergies() != null){
            for (StandardUser.Allergy allergy : standardUser.getAllergies())  {
                jsonArray.add(String.valueOf(allergy));
            }
        }
        jsonObject.add("allergies", jsonArray);

        FoodCategories foodPreference = standardUser.getFoodPreference();
        if (foodPreference != null) {
            jsonObject.addProperty("foodPreference", foodPreference.toString());
        } else {
            jsonObject.add("foodPreference", JsonNull.INSTANCE);
        }

        jsonObject.addProperty("favoriteDish", standardUser.getFavoriteDish());

        jsonObject.addProperty("id", standardUser.getId());
        jsonObject.addProperty("username", standardUser.getUsername());
        jsonObject.addProperty("email", standardUser.getEmail());
        jsonObject.addProperty("role", standardUser.getRole().toString());

        jsonObject.addProperty("posts", standardUser.getPosts().size());
        jsonObject.addProperty("followers", standardUser.getFollowers().size());
        jsonObject.addProperty("following", standardUser.getFollowing().size());

        jsonObject.add("picture", JsonNull.INSTANCE);
        if (standardUser.getProfilePicture() != null) {
            String profilePicture = RequestParser.byteArrayToBase64(standardUser.getProfilePicture());
            jsonObject.addProperty("picture", profilePicture);
        }

        return jsonObject;
    }
}
