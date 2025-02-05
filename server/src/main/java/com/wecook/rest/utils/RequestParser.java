package com.wecook.rest.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.container.ContainerRequestContext;
import org.glassfish.grizzly.http.server.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;

public class RequestParser {
    private static final Gson gson = CustomGson.getInstance().getGson();

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

    public static byte[] base64ToByteArray(String base64) {
        String[] parts = base64.split(",", 2);
        String base64Prefix = parts[0] + ",";
        String base64Data = parts[1];

        byte[] prefixBytes = base64Prefix.getBytes(StandardCharsets.UTF_8);

        byte[] imageBytes = Base64.getDecoder().decode(base64Data);

        byte[] fullByteArray = new byte[prefixBytes.length + imageBytes.length];

        System.arraycopy(prefixBytes, 0, fullByteArray, 0, prefixBytes.length);
        System.arraycopy(imageBytes, 0, fullByteArray, prefixBytes.length, imageBytes.length);

        return fullByteArray;
    }

    public static String byteArrayToBase64(byte[] fullByteArray) {
        int commaIndex = -1;
        for (int i = 0; i < fullByteArray.length; i++) {
            if (fullByteArray[i] == (byte) ',') {
                commaIndex = i;
                break;
            }
        }

        if (commaIndex == -1) {
            throw new IllegalArgumentException("Invalid base64 file!");
        }

        String base64Prefix = new String(fullByteArray, 0, commaIndex + 1, StandardCharsets.UTF_8);

        byte[] imageBytes = new byte[fullByteArray.length - (commaIndex + 1)];
        System.arraycopy(fullByteArray, commaIndex + 1, imageBytes, 0, imageBytes.length);

        String base64Encoded = Base64.getEncoder().encodeToString(imageBytes);

        return base64Prefix + base64Encoded;
    }
}
