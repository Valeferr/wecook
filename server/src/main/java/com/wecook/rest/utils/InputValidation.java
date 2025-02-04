package com.wecook.rest.utils;

import java.io.IOException;

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
    public static boolean isImageValid(byte[] image) throws IOException {
        String mimeType = SecurityUtils.getMimeType(image);
        if (mimeType == null || !mimeType.startsWith("data:image/")) {
            return false;
        }

        return true;
    }
}
