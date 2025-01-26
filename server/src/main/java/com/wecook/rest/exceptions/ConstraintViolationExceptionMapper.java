package com.wecook.rest.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.hibernate.exception.ConstraintViolationException;

public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException e) {
        return Response.status(Response.Status.CONFLICT)
                .entity("A conflict occurred while processing the request. Please verify the data and try again.")
                .build();
    }
}
