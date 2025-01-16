package com.wecook.rest.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.glassfish.grizzly.http.server.Request;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class RequestParser {
    private static Gson gson = new Gson();

    public static <E> E jsonRequestToClass(Request request, Class<E> type) throws JsonSyntaxException {
        String jsonBody;
        try (Scanner scanner = new Scanner(request.getInputStream(), StandardCharsets.UTF_8)) {
            jsonBody = scanner.useDelimiter("\\A").next();
        }

        return gson.fromJson(jsonBody, type);
    }

    public static JsonObject jsonRequestToGson(Request request){
        String jsonBody;
        try (Scanner scanner = new Scanner(request.getInputStream(), StandardCharsets.UTF_8)) {
            jsonBody = scanner.useDelimiter("\\A").next();
        }

        return JsonParser.parseString(jsonBody).getAsJsonObject();
    }
}
