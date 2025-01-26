package com.wecook.rest;

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

        userRequest = RequestParser.jsonRequestToClass(context, User.class);
        password = SecurityUtils.sha256(userRequest.getPassword());
        email = userRequest.getEmail().trim();

        if (password.isEmpty() || email.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        User user;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createNamedQuery(User.GET_BY_EMAIL, User.class);
            query.setParameter("email", email);
            user = query.getSingleResult();
        }

        if (!user.getPassword().equals(password)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        org.glassfish.grizzly.http.server.Session httpSession = context.getSession(true);
        httpSession.setAttribute("user", user);
        httpSession.setSessionTimeout(1440);

        return Response.ok(gson.toJson(user)).build();
    }

    @Path("/logout")
    @GET
    public Response logout(@Context Request context) {
        context.getSession().setValid(false);
        return Response.ok().build();
    }
}