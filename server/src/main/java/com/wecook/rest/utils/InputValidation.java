package com.wecook.rest.utils;

import java.io.IOException;
import java.util.Base64;
import java.util.Set;

import static com.wecook.rest.utils.SecurityUtils.getMimeType;

public class InputValidation {
    public static boolean isEmailValid(String email){
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }

    public static boolean isCommentValid(String text) {
        return text.matches( "^[A-Za-zÀ-Üà-ü0-9,;%°\"'-().\\s]{1,}$");
    }

    public static boolean isFavoriteDishValid(String favoriteDish) {
        return favoriteDish.matches("[A-Za-zÀ-Üà-ü\\s]{1,}$");
    }

    public static boolean isTitleValid(String title) {
        return title.matches("[A-Za-zÀ-Üà-ü\\s]{1,}$");
    }

    public static boolean isDescriptionValid(String description) {
        return description.matches("^[A-Za-zÀ-Üà-ü0-9,;%°\"'!-().\\s]{1,}$");
    }

    private static final Set<String> SUPPORTED_IMAGE_TYPES = Set.of("image/png", "image/jpeg", "image/webp", "image/bmp", "image/svg+xml");


    public static boolean isImageValid(String base64Image) throws IOException {
        if (base64Image == null || !base64Image.startsWith("data:image/")) {
            return false;
        }

        String mimeType = SecurityUtils.extractMimeType(base64Image);
        if (mimeType == null || !SUPPORTED_IMAGE_TYPES.contains(mimeType)) {
            return false;
        }

        String base64Data = base64Image.substring(base64Image.indexOf(",") + 1);
        byte[] imageBytes = Base64.getDecoder().decode(base64Data);

        String detectedMimeType = getMimeType(imageBytes);
        return detectedMimeType != null && detectedMimeType.equals(mimeType);
    }


}
