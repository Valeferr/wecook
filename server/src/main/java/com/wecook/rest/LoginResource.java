package com.wecook.rest;

import com.google.gson.JsonObject;
import com.wecook.model.HibernateUtil;
import com.wecook.model.User;
import com.wecook.rest.utils.InputValidation;
import com.wecook.rest.utils.JwtManager;
import com.wecook.rest.utils.RequestParser;
import com.wecook.rest.utils.SecurityUtils;
import jakarta.persistence.NoResultException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.grizzly.http.server.Request;
import org.hibernate.Session;
import org.hibernate.query.Query;

@Path("/auth")
public class LoginResource extends GenericResource {
    @Path("/login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Context Request context) {
        JsonObject jsonObject;
        try {
            jsonObject = RequestParser.jsonRequestToGson(context);
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (!jsonObject.has("email") || !jsonObject.has("password")) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        String email = jsonObject.get("email").getAsString().trim();
        String password = jsonObject.get("password").getAsString();
        if (password.isEmpty() || email.isEmpty() || !InputValidation.isEmailValid(email)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        password = SecurityUtils.sha256(password);

        User user;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createNamedQuery(User.GET_BY_EMAIL, User.class);
            query.setParameter("email", email);
            user = query.getSingleResult();

        }catch (NoResultException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (!user.getPassword().equals(password)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        String token = JwtManager.getInstance().generateToken(user);

        JsonObject jsonUser = gson.toJsonTree(user).getAsJsonObject();
        jsonUser.addProperty("token", token);

        return Response.ok(jsonUser.toString()).build();
    }

    @Path("/logout")
    @POST
    public Response logout(@Context Request context) {
        String authorizationToken = context.getHeader("Authorization").replaceAll("Bearer ", "");
        JwtManager.getInstance().invalidateToken(authorizationToken);
        return Response.ok().build();
    }
}