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
import org.hibernate.Transaction;

import java.util.List;
import java.util.logging.Logger;

@Path("/users")
public class UserResource {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context Request context) {
        User user;

        try {
            user = RequestParser.parseJsonRequest(context, User.class);
            String passwordHash = SecurityUtils.sha256(user.getPassword());
            user.setPassword(passwordHash);
        } catch (JsonSyntaxException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
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
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
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
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .build();
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
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            user = session.get(User.class, id);
            transaction = session.beginTransaction();
            session.remove(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                     .build();
        }

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .build();
        }

        Gson gson = new Gson();
        return Response.ok(gson.toJson(user)).build();
    }
}
