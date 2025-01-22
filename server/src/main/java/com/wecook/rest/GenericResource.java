package com.wecook.rest;

import com.google.gson.Gson;
import com.wecook.rest.utils.CustomGson;

public abstract class GenericResource {
    protected static final Gson gson = CustomGson.getInstance().getGson();
}
