package com.wecook.rest;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wecook.model.HibernateUtil;
import com.wecook.model.User;
import com.wecook.rest.utils.RequestParser;
import com.wecook.rest.utils.SecurityUtils;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.grizzly.http.server.Request;
import org.hibernate.Session;
import org.hibernate.query.Query;

@Path("/")
public class LoginResource extends GenericResource {

    @Path("/login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Context Request context) {
        User userRequest;
        String password, email;

        try {
            userRequest = RequestParser.jsonRequestToClass(context, User.class);
            password = SecurityUtils.sha256(userRequest.getPassword());
            email = userRequest.getEmail().trim();
        } catch (JsonSyntaxException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (password.isEmpty() || email.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        User user = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createNamedQuery(User.GET_BY_EMAIL, User.class);
            query.setParameter("email", email);
            user = query.getSingleResult();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (user.getPassword().equals(password)) {
            org.glassfish.grizzly.http.server.Session httpSession = context.getSession(true);
            httpSession.setAttribute("user", user);
            return Response.ok(gson.toJson(user)).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @Path("/logout")
    @GET
    public Response logout(@Context Request context) {
        org.glassfish.grizzly.http.server.Session httpSession = context.getSession();
        httpSession.setValid(false);
        return Response.ok().build();
    }

}