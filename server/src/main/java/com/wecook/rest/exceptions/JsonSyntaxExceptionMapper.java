package com.wecook.rest.exceptions;

import com.google.gson.JsonSyntaxException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class JsonSyntaxExceptionMapper implements ExceptionMapper<JsonSyntaxException> {
    @Override
    public Response toResponse(JsonSyntaxException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("Invalid JSON syntax: Please ensure the request body is correctly formatted.")
                .build();
    }
}
