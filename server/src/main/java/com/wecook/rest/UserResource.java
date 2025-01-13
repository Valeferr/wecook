package com.wecook.rest;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wecook.model.HibernateUtil;
import com.wecook.model.ModeratorUser;
import com.wecook.model.StandardUser;
import com.wecook.model.User;
import com.wecook.rest.utils.RequestParser;
import com.wecook.rest.utils.SecurityUtils;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.grizzly.http.server.Request;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

@Path("/users")
public class UserResource {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context Request context) {
        User requestUser;

        try {
            requestUser = RequestParser.parseJsonRequest(context, User.class);
            String passwordHash = SecurityUtils.sha256(requestUser.getPassword());
            requestUser.setPassword(passwordHash);
        } catch (JsonSyntaxException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        User user = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            if (requestUser.getRole().equals(User.Roles.STANDARD)) {
                user = new StandardUser();
            } else if (requestUser.getRole().equals(User.Roles.MODERATOR)) {
                user = new ModeratorUser();
            }

            user.setEmail(requestUser.getEmail());
            user.setUsername(requestUser.getUsername());
            user.setPassword(requestUser.getPassword());
            user.setRole(requestUser.getRole());

            try {
                session.persist(user);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw new Exception(e.getMessage());
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        Gson gson = new Gson();
        return Response.status(Response.Status.CREATED)
                .entity(gson.toJson(user))
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@Context Request context) {
        List<User> users;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            users= session.createNamedQuery(User.GET_ALL, User.class).getResultList();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        Gson gson = new Gson();
        return Response.ok(gson.toJson(users)).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne(@Context Request context, @PathParam("id") int id) {
        User user;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            user = session.get(User.class, id);
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            Gson gson = new Gson();
            return Response.ok(gson.toJson(user)).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@Context Request context, @PathParam("id") int id) {
        User user;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            user = session.get(User.class, id);
            Transaction transaction = session.beginTransaction();
            try {
                session.remove(user);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw new Exception(e.getMessage());
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Gson gson = new Gson();
        return Response.ok(gson.toJson(user)).build();
    }
}
