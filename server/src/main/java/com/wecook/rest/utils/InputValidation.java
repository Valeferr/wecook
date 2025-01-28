package com.wecook.rest.utils;

public class InputValidation {
    public static boolean isEmailValid(String email){
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }
}
