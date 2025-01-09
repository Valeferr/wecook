package com.wecook.rest.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.glassfish.grizzly.http.server.Request;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class RequestParser {
    public static <E> E parseJsonRequest(Request request, Class<E> type) throws JsonSyntaxException {
        String jsonBody;
        try (Scanner scanner = new Scanner(request.getInputStream(), StandardCharsets.UTF_8)) {
            jsonBody = scanner.useDelimiter("\\A").next();
        }

        Gson gson = new Gson();
        return gson.fromJson(jsonBody, type);
    }
}
