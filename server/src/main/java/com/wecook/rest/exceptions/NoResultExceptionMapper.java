package com.wecook.rest.exceptions;

import jakarta.persistence.NoResultException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class NoResultExceptionMapper implements ExceptionMapper<NoResultException> {
    @Override
    public Response toResponse(NoResultException e) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity("The requested resource was not found. Please check your input and try again.")
                .build();
    }
}
